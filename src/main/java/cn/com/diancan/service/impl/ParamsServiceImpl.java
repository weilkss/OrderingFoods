package cn.com.diancan.service.impl;

import cn.com.diancan.entity.Params;
import cn.com.diancan.mapper.ParamsMapper;
import cn.com.diancan.service.ParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ParamsService")
public class ParamsServiceImpl implements ParamsService {
    @Autowired
    private ParamsMapper paramsMapper;

    @Override
    /**
     * 获取params
     */
    public List<Params> finds(){
        return paramsMapper.finds();
    }
    /**
     * 更新params
     * @param peopleNum
     * @param meatNum
     * @param cheapNum
     * @param soupNum
     * @return
     */
    public Integer updateParams(Integer peopleNum,Integer meatNum,Integer cheapNum,Integer soupNum){
        return  paramsMapper.updateParams(peopleNum,meatNum,cheapNum,soupNum);
    }
}
