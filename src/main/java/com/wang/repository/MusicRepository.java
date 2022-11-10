package com.wang.repository;

import com.wang.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * yyRepository接口
 * @author laptop
 */
public interface MusicRepository extends JpaRepository<Music, Integer>, JpaSpecificationExecutor<Music> {


}
