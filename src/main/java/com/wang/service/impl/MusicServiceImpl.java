package com.wang.service.impl;

import com.wang.entity.Music;
import com.wang.repository.MusicRepository;
import com.wang.service.MusicService;
import com.wang.util.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Music接口实现
 * @author laptop
 */
@Service("musicService")
@Transactional
public class MusicServiceImpl implements MusicService {

    @Resource
    private MusicRepository musicRepository;
    /**
     * 分页查询music
     *
     * @param music
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Music> list(Music music, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "musicId");
        Page<Music> musicPage = musicRepository.findAll((Specification<Music>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (music != null) {
                if (StringUtil.isNotEmpty(music.getName())) {
                    predicate.getExpressions().add(cb.like(root.get("name"), "%" + music.getName().trim() + "%"));
                }
                if (StringUtil.isNotEmpty(music.getArtist())) {
                    predicate.getExpressions().add(cb.like(root.get("artist"), "%" + music.getArtist().trim() + "%"));
                }
            }
            return predicate;
        }, pageable);
        return musicPage.getContent();
    }

    /**
     * 根据musicId查询music
     *
     * @param musicId
     * @return
     */
    @Override
    public Music findByMusicId(Integer musicId) {
        return musicRepository.getOne(musicId);
    }

    /**
     * 获取总记录数
     *
     * @param music
     * @return
     */
    @Override
    public Long getCount(Music music) {
        long count = musicRepository.count(new Specification<Music>() {
            @Override
            public Predicate toPredicate(Root<Music> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (music != null) {
                    if (StringUtil.isNotEmpty(music.getName())) {
                        predicate.getExpressions().add(cb.like(root.get("name"), "%" + music.getName().trim() + "%"));
                    }
                    if (StringUtil.isNotEmpty(music.getArtist())) {
                        predicate.getExpressions().add(cb.like(root.get("artist"), "%" + music.getArtist().trim() + "%"));
                    }
                }
                return predicate;
            }
        });
        return count;
    }

    /**
     * 添加或修改music
     *
     * @param music
     */
    @Override
    public void save(Music music) {
        musicRepository.save(music);
    }

    /**
     * 根据id删除music
     *
     * @param musicId
     */
    @Override
    public void delete(Integer musicId) {
        musicRepository.deleteById(musicId);
    }
}
