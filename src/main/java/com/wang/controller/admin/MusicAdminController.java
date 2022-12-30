package com.wang.controller.admin;

import com.wang.entity.Music;
import com.wang.run.StartupRunner;
import com.wang.service.MusicService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 音乐控制器
 */
@RestController
@RequestMapping("/admin/music")
public class MusicAdminController {

    @Resource
    private MusicService musicService;

    @Resource
    private StartupRunner startupRunner;

    /**
     * 分页查询音乐
     *
     * @param music
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public Map<String, Object> list(Music music, @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("errorNo", 0);
        resultMap.put("data", musicService.list(music, page - 1, pageSize));
        resultMap.put("total", musicService.getCount(music));
        return resultMap;
    }

    /**
     * 根据musicId查询音乐
     *
     * @param musicId
     * @return
     */
    @RequestMapping("/findById")
    public Map<String, Object> findById(Integer musicId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("errorNo", 0);
        resultMap.put("data", musicService.findByMusicId(musicId));
        return resultMap;
    }

    /**
     * 添加或修改音乐
     *
     * @param music
     * @return
     */
    public Map<String, Object> save(Music music) {
        Map<String, Object> resultMap = new HashMap<>();
        musicService.save(music);
        resultMap.put("errorNo", 0);
        resultMap.put("data", 1);
        startupRunner.loadData();
        return resultMap;
    }

    /**
     * 批量删除友情链接
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Map<String, Object> delete(@RequestParam(value = "musicId") String ids) {
        Map<String, Object> resultMap = new HashMap<>();
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            musicService.delete(Integer.parseInt(idsStr[i]));
        }
        resultMap.put("errorNo", 0);
        resultMap.put("data", 1);
        startupRunner.loadData();
        return resultMap;
    }
}
