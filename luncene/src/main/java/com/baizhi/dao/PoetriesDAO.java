package com.baizhi.dao;

import com.baizhi.entity.Poetries;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/11/8.
 */
@Mapper
public interface PoetriesDAO {
    List<Poetries> findAll();
    List<Poetries> findByName(@Param(value = "name") String name);

}
