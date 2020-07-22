package cn.com.diancan.controller;

import cn.com.diancan.entity.Foods;
import cn.com.diancan.entity.Params;
import cn.com.diancan.service.FoodsService;
import cn.com.diancan.service.ParamsService;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/diancan")
public class WebController {
    @Autowired
    private ParamsService paramsService;

    @RequestMapping()
    public String getIndex(ModelMap map) {
        /**
         * 获取服务器时间
         */
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        map.addAttribute("curTime", dateFormat.format(date));
        /**
         * 获取params
         */
        List<Params> paramsObject = paramsService.finds();
        map.addAttribute("params", paramsObject.get(0));

        return "index";
    }

    /**
     *
     */
    @Autowired
    private FoodsService foodsService;

    @RequestMapping("/foods")
    public String getFoodsHtml(ModelMap map){
        List<Foods> list = foodsService.findFoods();

        JSONArray maxFoods = new JSONArray();
        JSONArray meatFoods = new JSONArray();
        JSONArray cheapFoods = new JSONArray();
        JSONArray soupFoods = new JSONArray();

        for(int i = 0;i<list.size();i++){
            Integer type = list.get(i).getType();

            if(type == 1){
                maxFoods.add(list.get(i));
            }else if(type == 2){
                meatFoods.add(list.get(i));
            }else if(type ==3){
                cheapFoods.add(list.get(i));
            }else{
                soupFoods.add(list.get(i));
            }
        }

        map.addAttribute("maxFoods",maxFoods);
        map.addAttribute("meatFoods",meatFoods);
        map.addAttribute("cheapFoods",cheapFoods);
        map.addAttribute("soupFoods",soupFoods);
        return "foods";
    }
}
