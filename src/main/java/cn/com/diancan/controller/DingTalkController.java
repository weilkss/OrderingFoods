package cn.com.diancan.controller;

import cn.com.diancan.constants.WebConstants;
import cn.com.diancan.encapsulation.Result;
import cn.com.diancan.entity.Params;
import cn.com.diancan.service.FoodsService;
import cn.com.diancan.service.ParamsService;
import cn.com.diancan.utils.CommonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/diancan")
public class DingTalkController {
    @Autowired
    private FoodsService foodsService;
    @Autowired
    private ParamsService paramsService;

    private CommonUtil commonUtil = new CommonUtil();

    private DingTalkClient dingTalkClient = new DefaultDingTalkClient(WebConstants.SERVER_URL);
    private OapiRobotSendRequest request = new OapiRobotSendRequest();

    @ResponseBody
    @PostMapping(value = "/send", produces = "application/json;charset=UTF-8")
    public Result send(@RequestBody JSONObject jsonObject) throws ApiException {

        Integer peopleNum = Integer.parseInt(jsonObject.get("peopleNum").toString());
        Integer meatNum = Integer.parseInt(jsonObject.get("meatNum").toString());
        Integer cheapNum = Integer.parseInt(jsonObject.get("cheapNum").toString());
        Integer soupNum = Integer.parseInt(jsonObject.get("soupNum").toString());

        Integer result = paramsService.updateParams(peopleNum, meatNum, cheapNum, soupNum);

        sendMarkdown();

        return Result.success(result);
    }

    /**
     * @func reportCurrentTimeTask 任务调度 每天上午11点⏰
     */
    @Scheduled(cron = "0 0 11 * * ?")
    public void reportCurrentTimeTask() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);

        try {
            if (week != 1) { /** 判断是星期天就不提醒了😯 **/
                sendText();
                sendMarkdown();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * sendText
     */
    public void sendText() throws ApiException {
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(WebConstants.SEND_TEXT_CONTENT);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setIsAtAll(true);
        request.setAt(at);
        request.setMsgtype("text");
        request.setText(text);
        dingTalkClient.execute(request);
    }

    /**
     * sendMarkdown
     */
    public void sendMarkdown() throws ApiException {
        List<Params> paramsList = paramsService.finds();
        StringBuffer buffer = new StringBuffer();
        int count = 0;
        int priceTotal = 0;

        JSONArray maxFoods = JSONArray.parseArray(JSON.toJSONString(foodsService.findFoodsByType(1)));
        JSONArray meatFoods = JSONArray.parseArray(JSON.toJSONString(foodsService.findFoodsByType(2)));
        JSONArray cheapFoods = JSONArray.parseArray(JSON.toJSONString(foodsService.findFoodsByType(3)));
        JSONArray soupFoods = JSONArray.parseArray(JSON.toJSONString(foodsService.findFoodsByType(4)));

        int peopleNum = paramsList.get(0).getPeopleNum();
        int meatNum = paramsList.get(0).getMeatNum();
        int cheapNum = paramsList.get(0).getCheapNum();
        int soupNum = paramsList.get(0).getSoupNum();

        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(WebConstants.SEND_TEXT_TITLE);
        buffer.append("# 今日营养搭配:\n");

        if (paramsList.get(0).getMaxNum() == 1) {
            JSONObject[] maxFoodsArray = commonUtil.getFoodsArray(1, maxFoods);
            System.out.println(maxFoodsArray);

            count++;
            priceTotal += Integer.parseInt(maxFoodsArray[0].getString("price"));

            buffer.append("### " + count + "、" + maxFoodsArray[0].getString("name") + "\n");
        }

        JSONObject[] meatFoodsArray = commonUtil.getFoodsArray(meatNum, meatFoods);
        JSONObject[] cheapFoodsArray = commonUtil.getFoodsArray(cheapNum, cheapFoods);
        JSONObject[] soupFoodsArray = commonUtil.getFoodsArray(soupNum, soupFoods);

        JSONObject[] foodsChoiceArray = commonUtil.concat(commonUtil.concat(meatFoodsArray, cheapFoodsArray), soupFoodsArray);


        String[] foodsKey = commonUtil.setFoodsKey();
        String[] selectFoodsKey = new String[foodsChoiceArray.length];

        for (int i = 0; i < foodsChoiceArray.length; i++) {
            for (int k = 0; k < foodsKey.length; k++) {
                String pattern = ".*" + foodsKey[k] + ".*";

                boolean isMatch = Pattern.matches(pattern, foodsChoiceArray[i].getString("name"));

                if (isMatch) {
                    selectFoodsKey[i] = foodsKey[k];
                    break;
                }
            }
        }

        int countKey = 0;
        for (int j = 0; j < selectFoodsKey.length; j++) {
            for (int k = j + 1; k < selectFoodsKey.length; k++) {
                if (selectFoodsKey[j] == selectFoodsKey[k] & selectFoodsKey[j] != "null" & selectFoodsKey[k] != "null") {
                    countKey++;
                }
            }

            if (countKey == 1) {
                boolean isMatch = false;
                if (foodsChoiceArray[j].getString("type") == "2") {
                    do {
                        foodsChoiceArray[j] = commonUtil.getFoodsArray(1, meatFoods)[0];
                        String pattern = ".*" + selectFoodsKey[j] + ".*";
                        String name = foodsChoiceArray[j].getString("name");
                        isMatch = Pattern.matches(pattern, name);

                    } while (isMatch);

                } else if (foodsChoiceArray[j].getString("type") == "3") {
                    do {
                        foodsChoiceArray[j] = commonUtil.getFoodsArray(1, cheapFoods)[0];
                        String pattern = ".*" + selectFoodsKey[j] + ".*";
                        String name = foodsChoiceArray[j].getString("name");
                        isMatch = Pattern.matches(pattern, name);

                    } while (isMatch);
                }

                countKey = 0;
            }
        }

        for (int i = 0; i < foodsChoiceArray.length; i++) {
            count++;
            priceTotal += new Double(foodsChoiceArray[i].getString("price")).intValue();

            buffer.append("### " + count + "、" + foodsChoiceArray[i].getString("name") + "\n");
        }

        int total = peopleNum + priceTotal;

        buffer.append("\n");
        buffer.append("> ###### 就餐人数：" + peopleNum + "\n");
        buffer.append("> ###### 米饭（1元/人）：" + peopleNum + "元\n");
        buffer.append("> ###### 总价：" + total + "元\n");
        buffer.append("> ###### 人均：" + commonUtil.txfloat(total, peopleNum) + "元\n");

        for (int i = 0; i < peopleNum; i++) {
            if (i == peopleNum - 1) {
                buffer.append("👨🏻‍💻\n");
            } else if (i == 0 || i == 1) {
                buffer.append("👩🏻‍💻‍‍");
            } else {
                buffer.append("👨🏻‍💻");
            }
        }

        buffer.append("### [不满意，重新获取【新版本】](http://baixiaosh.cn/diancan)\n>");

        String text = buffer.toString();
        markdown.setText(text);

        request.setMsgtype("markdown");
        request.setMarkdown(markdown);

        dingTalkClient.execute(request);
    }
}
