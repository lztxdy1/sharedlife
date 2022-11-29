package com.wang.controller.admin;

import com.wang.entity.Blogger;
import com.wang.run.StartupRunner;
import com.wang.service.BloggerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 博主控制器
 */
@RestController
@RequestMapping("/admin/blogger")
public class BloggerController {

    @Resource
    private BloggerService bloggerService;

    @Resource
    private StartupRunner startupRunner;

    /**
     * 查找博主
     * @return
     */
    @RequestMapping("/find")
    public Map<String, Object> find() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("errorNo", 0);
        resultMap.put("data", bloggerService.find());
        return resultMap;
    }

    /**
     * 添加和修改博主
     * @param blogger
     * @return
     */
    @RequestMapping("/save")
    public Map<String, Object> save(Blogger blogger) {
        Map<String, Object> resultMap = new HashMap<>();
        bloggerService.save(blogger);
        resultMap.put("errorNo", 0);
        resultMap.put("data", 1);
        startupRunner.loadData();
        return resultMap;
    }
}
