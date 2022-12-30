package com.wang.controller.admin;

import com.wang.constant.Constant;
import com.wang.entity.Timeline;
import com.wang.run.StartupRunner;
import com.wang.service.TimelineService;
import com.wang.util.DateUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.management.ObjectName;
import java.util.HashMap;
import java.util.Map;

/**
 * 时光轴控制器
 */
@RestController
@RequestMapping("/admin/timeline")
public class TimelineAdminController {

    @Resource
    private TimelineService timelineService;

    @Resource
    private StartupRunner startupRunner;

    /**
     * 分页查询时光轴
     *
     * @param timeline
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public Map<String, Object> list(Timeline timeline, @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "pageSize") Integer pageSize) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("errorNo", 0);
        resultMap.put("data", timelineService.list(timeline, page - 1, pageSize));
        resultMap.put("total", timelineService.getCount(timeline));
        return resultMap;
    }

    /**
     * 根据timelineId查询时光轴
     *
     * @param timelineId
     * @return
     */
    @RequestMapping("/findById")
    public Map<String, Object> findById(Integer timelineId) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> tempMap = new HashMap<>();
        Timeline timeline = timelineService.findByTimelineId(timelineId);
        tempMap.put("timelineId", timeline.getTimelineId());
        tempMap.put("year", timeline.getYear());
        tempMap.put("month", timeline.getMonth());
        tempMap.put("timeDate", DateUtil.formatDate(timeline.getPublishDate(), Constant.DATE_FORMAT));
        tempMap.put("content", timeline.getContent());
        tempMap.put("errorNo", 0);
        resultMap.put("data", tempMap);
        return resultMap;
    }


    /**
     * 添加或修改时光轴
     *
     * @param timeline
     * @param timeDate
     * @return
     */
    @RequestMapping("/save")
    public Map<String, Object> save(Timeline timeline, String timeDate) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            timeline.setPublishDate(DateUtil.formatString(timeDate, Constant.DATE_FORMAT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        timelineService.save(timeline);
        resultMap.put("errorNo", 0);
        resultMap.put("data", 1);
        startupRunner.loadData();
        return resultMap;
    }

    /**
     * 批量删除时光轴
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Map<String, Object> delete(@RequestParam(value = "timelineId") String ids) {
        Map<String, Object> resultMap = new HashMap<>();
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            timelineService.delete(Integer.parseInt(idsStr[i]));
        }
        resultMap.put("errorNo", 0);
        resultMap.put("data", 1);
        startupRunner.loadData();
        return resultMap;
    }
}
