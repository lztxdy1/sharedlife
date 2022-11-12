package com.wang.service;

import com.wang.entity.Music;

import java.util.List;

/**
 * 音乐Service接口
 * @author laptop
 */
public interface MusicService {

    /**
     * 分页查询music
     * @param music
     * @param page
     * @param pageSize
     * @return
     */
    public List<Music> list(Music music, Integer page, Integer pageSize);

    /**
     * 获取总记录数
     * @param music
     * @return
     */
    public Long getCount(Music music);

    /**
     * 根据musicId查询music
     * @param musicId
     * @return
     */
    public Music findByMusicId(Integer musicId);

    /**
     * 添加或修改music
     * @param music
     */
    public void save(Music music);

    /**
     * 根据id删除music
     * @param musicId
     */
    public void delete(Integer musicId);
}
