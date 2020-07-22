package cn.com.diancan.mapper;

import cn.com.diancan.entity.Params;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ParamsMapper {
    List<Params> finds();

    Integer updateParams(@Param("peopleNum") Integer peopleNum,@Param("meatNum") Integer meatNum,@Param("cheapNum") Integer cheapNum,@Param("soupNum") Integer soupNum);
}
