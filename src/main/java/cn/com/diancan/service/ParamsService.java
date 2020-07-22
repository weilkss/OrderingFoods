package cn.com.diancan.service;

import cn.com.diancan.entity.Params;

import java.util.List;

public interface ParamsService {
    List<Params> finds();

    Integer updateParams(Integer peopleNum,Integer meatNum,Integer cheapNum,Integer soupNum);
}
