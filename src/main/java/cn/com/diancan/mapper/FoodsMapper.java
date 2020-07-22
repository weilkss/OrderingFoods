package cn.com.diancan.mapper;

import cn.com.diancan.entity.Foods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FoodsMapper {
    List<Foods> findFoods();

    Integer updateFoods(@Param("fid") Integer fid,@Param("show") Integer show);

    List<Foods> findFoodsByType(@Param("type") Integer type);
}
