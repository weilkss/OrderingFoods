package cn.com.diancan.service.impl;

import cn.com.diancan.entity.Foods;
import cn.com.diancan.mapper.FoodsMapper;
import cn.com.diancan.service.FoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("FoodsService")
public class FoodsServiceImpl implements FoodsService {
    @Autowired
    private FoodsMapper foodsMapper;

    @Override
    /**
     * 获取所有的foods
     */
    public List<Foods> findFoods() {
        return foodsMapper.findFoods();
    }

    /**
     * 更改food的状态
     *
     * @param fid  foodId
     * @param show 是否显示
     * @return Integer
     */
    public Integer updateFoods(Integer fid, Integer show) {
        return foodsMapper.updateFoods(fid, show);
    }

    /**
     * 根据type获取foods列表
     *
     * @param type
     * @return
     */
    public List<Foods> findFoodsByType(Integer type) {
        return foodsMapper.findFoodsByType(type);
    }
}
