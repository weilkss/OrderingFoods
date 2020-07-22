package cn.com.diancan.service;

import cn.com.diancan.entity.Foods;

import java.util.List;

public interface FoodsService {
    List<Foods> findFoods();

    Integer updateFoods(Integer fid,Integer show);

    List<Foods> findFoodsByType(Integer type);
}
