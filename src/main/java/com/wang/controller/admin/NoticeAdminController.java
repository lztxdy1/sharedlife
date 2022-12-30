package com.wang.controller.admin;

import com.wang.entity.Notice;
import com.wang.service.NoticeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 公告控制器
 */
@RestController
@RequestMapping("/admin/notice")
public class NoticeAdminController {

    @Resource
    private NoticeService noticeService;

    /**
     * 分页查询公告
     *
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public Map<String, Object> list(@RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("errorNo", 0);
        resultMap.put("data", noticeService.list(page - 1, pageSize));
        resultMap.put("total", noticeService.getCount());
        return resultMap;
    }

    /**
     * 根据noticeId查询公告
     *
     * @param noticeId
     * @return
     */
    @RequestMapping("/findById")
    public Map<String, Object> findById(Integer noticeId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("errorNo", 0);
        resultMap.put("data", noticeService.findByNoticeId(noticeId));
        return resultMap;
    }

    /**
     * 添加或修改公告
     *
     * @param notice
     * @return
     */
    @RequestMapping("/save")
    public Map<String, Object> save(Notice notice) {
        Map<String, Object> resultMap = new HashMap<>();
        notice.setPublishDate(new Date());
        noticeService.save(notice);
        resultMap.put("errorNo", 0);
        resultMap.put("data", 1);
        return resultMap;
    }

    /**
     * 批量删除公告
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Map<String, Object> delete(@RequestParam(value = "noticeId") String ids) {
        Map<String, Object> resultMap = new HashMap<>();
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            noticeService.delete(Integer.parseInt(idsStr[i]));
        }
        resultMap.put("errorNo", 0);
        resultMap.put("data", 1);
        return resultMap;
    }
}
