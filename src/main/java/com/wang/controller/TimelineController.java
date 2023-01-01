package com.wang.controller;

import com.wang.entity.Timeline;
import com.wang.service.TimelineService;
import com.wang.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * 时光轴控制器
 */
@RestController
@RequestMapping("/timeline")
public class TimelineController {

    @Resource
    private TimelineService timelineService;

    /**
     * 查询时光轴
     *
     * @return
     */
    @RequestMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> resultMap = new HashMap<>();
        List<Timeline> maps = timelineService.list(null, 0, 100);
        if (CollectionUtils.isNotEmpty(maps)) {
            Map<String, Object> jsonMap = new LinkedHashMap<>();
            for (Timeline map : maps) {
                Map<String, Object> yearMap = (Map<String, Object>) jsonMap.get(map.getYear());
                if (null == yearMap) {
                    yearMap = new LinkedHashMap<>();
                    yearMap.put("year", map.getYear());
                }
                Map<String, Object> monthMap = (Map<String, Object>) yearMap.get("month");
                if (null == monthMap) {
                    monthMap = new LinkedHashMap<>();
                }
                List<Map<String, Object>> monthList = (List<Map<String, Object>>) monthMap.get(map.getMonth());
                if (null == monthList) {
                    monthList = new ArrayList<>();
                }
                monthMap.remove(map.getMonth());
                Map<String, Object> obj = new HashMap<>();
                obj.put("create_time", DateUtil.formatDate(map.getPublishDate(), "MM月dd日 hh:mm"));
                obj.put("content", map.getContent());
                monthList.add(obj);
                monthMap.put(map.getMonth(), monthList);
                yearMap.remove("month");
                yearMap.put("month", monthMap);
                jsonMap.remove(map.getYear());
                jsonMap.put(map.getYear(), yearMap);
            }
            List<Map<String, Object>> dates = new ArrayList<>();
            for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                dates.add((Map<String, Object>) entry.getValue());
            }
            resultMap.put("dates", dates);
            resultMap.put("result", 1);
        } else {
            resultMap.put("data", "暂无时光轴数据！！！");
        }
        return resultMap;
    }
}
