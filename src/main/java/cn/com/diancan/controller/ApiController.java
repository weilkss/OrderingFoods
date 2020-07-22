package cn.com.diancan.controller;

import cn.com.diancan.encapsulation.Result;
import cn.com.diancan.service.FoodsService;
import cn.com.diancan.service.ParamsService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/diancan")
public class ApiController {
    @Autowired
    private ParamsService paramsService;

    /**
     * /api/updateParams
     *
     * @param jsonObject
     * @return
     * @abs 更新默认
     */
    @ResponseBody
    @PostMapping(value = "/api/updateParams", produces = "application/json;charset=UTF-8")
    public Result UpdateParams(@RequestBody JSONObject jsonObject) {
        Integer peopleNum = Integer.parseInt(jsonObject.get("peopleNum").toString());
        Integer meatNum = Integer.parseInt(jsonObject.get("meatNum").toString());
        Integer cheapNum = Integer.parseInt(jsonObject.get("cheapNum").toString());
        Integer soupNum = Integer.parseInt(jsonObject.get("soupNum").toString());

        Integer result = paramsService.updateParams(peopleNum, meatNum, cheapNum, soupNum);
        System.out.println(result);

        return Result.success();
    }

    @Autowired
    private FoodsService foodsService;

    /**
     * 更新foods的状态
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/api/updateFoodsByFid", produces = "application/json;charset=UTF-8")
    public Result updateFoodsByFid(@RequestBody JSONObject jsonObject) {
        Integer fid = Integer.parseInt(jsonObject.get("fid").toString());
        Integer show = Integer.parseInt(jsonObject.get("show").toString());
        foodsService.updateFoods(fid, show);
        return Result.success();
    }
}
