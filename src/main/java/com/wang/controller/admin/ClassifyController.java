package com.wang.controller.admin;

import com.wang.entity.Classify;
import com.wang.run.StartupRunner;
import com.wang.service.ClassifyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 文章分类控制器
 */
@RestController
@RequestMapping("/admin/classify")
public class ClassifyController {

    @Resource
    private ClassifyService classifyService;

    @Resource
    private StartupRunner startupRunner;

    /**
     * 分页查询文章分类
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public Map<String, Object> list(@RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("errorNo", 0);
        resultMap.put("data", classifyService.list(page - 1, pageSize));
        resultMap.put("total", classifyService.getCount());
        return resultMap;
    }

    /**
     * 根据id查询文章分类
     * @param classifyId
     * @return
     */
    @RequestMapping("/findById")
    public Map<String, Object> findById(Integer classifyId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("errorNo", 0);
        resultMap.put("data", classifyService.findByClassifyId(classifyId));
        return resultMap;
    }

    /**
     * 添加或修改文章类别
     * @param classify
     * @return
     */
    @RequestMapping("/save")
    public Map<String, Object> save(Classify classify) {
        Map<String, Object> resultMap = new HashMap<>();
        classifyService.save(classify);
        resultMap.put("errorNo", 0);
        resultMap.put("data", 1);
        startupRunner.loadData();
        return resultMap;
    }

    /**
     * 批量删除文章类别
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Map<String, Object> delete(@RequestParam(value = "classifyId") String ids) {
        Map<String, Object> resultMap = new HashMap<>();
        String[] idsArray = ids.split(",");
        for (String s : idsArray) {
            classifyService.delete(Integer.parseInt(s));
        }
        resultMap.put("errorNo", 0);
        resultMap.put("data", 1);
        startupRunner.loadData();
        return resultMap;
    }
}
