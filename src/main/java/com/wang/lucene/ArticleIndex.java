package com.wang.lucene;

import com.wang.constant.Constant;
import com.wang.entity.Article;
import com.wang.util.DateUtil;
import com.wang.util.StringUtil;
import com.wang.util.WriterAndDirManager;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 文章索引类
 */
@Component
public class ArticleIndex {

    private Directory directory;


    @Value("lucenePath")
    private String lucenePath;  // 索引目录

    /**
     * 获取Writer实例
     * @return
     * @throws Exception
     */
    private IndexWriter getWriter() throws Exception{
        directory = FSDirectory.open(Paths.get(lucenePath));
        SmartChineseAnalyzer chineseAnalyzer = new SmartChineseAnalyzer();
        IndexWriterConfig writerConfig = new IndexWriterConfig(chineseAnalyzer);
        IndexWriter writer = new IndexWriter(directory, writerConfig);
        return writer;
    }

    /**
     * 添加文章索引
     * @param article
     * @throws Exception
     */
    public void addIndex(Article article) throws Exception {
        IndexWriter writer = getWriter();
        Document document = new Document();
        document.add(new StringField("id", String.valueOf(article.getArticleId()), Field.Store.YES));
        document.add(new StringField("publishDate", DateUtil.formatDate(article.getPublishDate(), Constant.DATE_FORMAT), Field.Store.YES));
        document.add(new TextField("title", article.getTitle(), Field.Store.YES));
        document.add(new TextField("content", article.getContent(), Field.Store.YES));

        writer.addDocument(document);
        writer.close();
    }

    /**
     * 根据articleId删除指定索引
     * @param articleId
     * @throws Exception
     */
    public void deleteIndex(String articleId) throws Exception {
        IndexWriter writer = getWriter();
        writer.deleteDocuments(new Term("id", articleId));
        writer.forceMergeDeletes();
        writer.commit();
        writer.close();
    }

    /**
     * 更新文章索引
     * @param article
     * @throws Exception
     */
    public void updateIndex(Article article) throws Exception {
        IndexWriter writer = getWriter();
        Document document = new Document();
        document.add(new StringField("id", String.valueOf(article.getArticleId()), Field.Store.YES));
        document.add(new StringField("imageName", String.valueOf(article.getImageName()), Field.Store.YES));
        document.add(new StringField("author", String.valueOf(article.getAuthor()), Field.Store.YES));
        document.add(new StringField("click", String.valueOf(article.getClick()), Field.Store.YES));
        document.add(new StringField("commentNum", String.valueOf(article.getCommentNum()), Field.Store.YES));
        document.add(new StringField("isOriginal", String.valueOf(article.getIsOriginal()), Field.Store.YES));
        document.add(new StringField("isTop", String.valueOf(article.getIsTop()), Field.Store.YES));
        document.add(new StringField("classifyId", String.valueOf(article.getClassify().getClassifyId()), Field.Store.YES));
        document.add(new StringField("classifyName", String.valueOf(article.getClassify().getClassifyName()), Field.Store.YES));
        document.add(new StringField("publishDate", DateUtil.formatDate(article.getPublishDate(), Constant.DATE_FORMAT), Field.Store.YES));
        document.add(new TextField("title", article.getTitle(), Field.Store.YES));
        document.add(new TextField("content", article.getContent(), Field.Store.YES));

        writer.updateDocument(new Term("id", String.valueOf(article.getArticleId())), document);
        writer.close();

    }

    public List<Article> searchArticle(String q) throws Exception {
        directory = FSDirectory.open(Paths.get(lucenePath));
        WriterAndDirManager manager = new WriterAndDirManager();
        IndexWriter writer = null;
        writer = manager.getIndexWriter(directory);
        writer.commit();

        IndexReader reader = manager.getIndexReader(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer(true);

        QueryParser queryParser = new QueryParser("title", analyzer);
        Query query = queryParser.parse(q);

        QueryParser queryParser1 = new QueryParser("content", analyzer);
        Query query1 = queryParser1.parse(q);

        booleanQuery.add(query, BooleanClause.Occur.SHOULD);
        booleanQuery.add(query1, BooleanClause.Occur.SHOULD);

        TopDocs topDocs = searcher.search(booleanQuery.build(), 100);
        QueryScorer scorer = new QueryScorer(query);
        SimpleSpanFragmenter simpleSpanFragmenter = new SimpleSpanFragmenter(scorer);
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color = 'red'>", "</font></b>");
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(simpleSpanFragmenter);

        List<Article> articleList = new ArrayList<Article>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            Article article = new Article();
            article.setArticleId(Integer.parseInt(doc.get("id")));
            article.setPublishDate(DateUtil.formatString(doc.get("publishDate"), Constant.DATE_FORMAT));
            String title = doc.get("title");
            String content = doc.get("content");
            if (title != null) {
                TokenStream tokenStream = analyzer.tokenStream("title", new StringReader(content));
                String HTitle = highlighter.getBestFragment(tokenStream, title);
                if (StringUtil.isNotEmpty(HTitle)) {
                    article.setTitle(title);
                } else {
                    article.setTitle(HTitle);
                }
            }

            if (content != null) {
                TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
                String hContent = highlighter.getBestFragment(tokenStream, content);
                if (StringUtil.isEmpty(hContent)) {
                    if (content.length() <= 100) {
                        article.setContent(content);
                    } else {
                        article.setContent(content.substring(0, 100));
                    }
                } else {
                    article.setContent(hContent);
                }
            }
            articleList.add(article);
        }

        writer.close();
        return articleList;
    }

}
