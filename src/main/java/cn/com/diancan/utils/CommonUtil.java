package cn.com.diancan.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.DecimalFormat;
import java.util.Random;

public class CommonUtil {
    /**
     * 获取随机数
     *
     * @param max
     * @param len
     * @return
     */
    public static int[] Randoms(int max, int len) {
        Random rand = new Random();
        int[] rands = new int[len];
        boolean[] bool = new boolean[max];
        int randint = 0;

        for (int i = 0; i < len; i++) {
            do {
                randint = rand.nextInt(max);
            } while (bool[randint]);
            bool[randint] = true;
            rands[i] = randint;
        }

        return rands;
    }

    /**
     * @param a 被除数
     * @param b 除数
     * @abs 除法运算，保留小数
     */
    public static String txfloat(int a, int b) {
        DecimalFormat df = new DecimalFormat("0.00");//设置保留位数
        return df.format((float) a / b);
    }

    /**
     * 合并两个数组 concat
     *
     * @param a
     * @param b
     * @return JSONObject[]
     */
    public static JSONObject[] concat(JSONObject[] a, JSONObject[] b) {
        JSONObject[] c = new JSONObject[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    /**
     * @param num        数量
     * @param foodsArray 当前foods
     * @return array
     */
    public static JSONObject[] getFoodsArray(int num, JSONArray foodsArray) {
        JSONObject[] array = new JSONObject[num];
        /** 获取最大值 **/
        int max = foodsArray.size();
        /** 获取随机数 **/
        int[] numbers = Randoms(max, num);

        for (int i = 0; i < numbers.length; i++) {
            JSONObject foodsObj = foodsArray.getJSONObject(numbers[i]);

            array[i] = foodsObj;
        }

        return array;
    }

    public static String[] setFoodsKey() {
        String[] foodsKey = new String[21];

        foodsKey[0] = "蛋";
        foodsKey[1] = "肉片";
        foodsKey[2] = "肉丝";
        foodsKey[3] = "里脊";
        foodsKey[4] = "拌";
        foodsKey[5] = "回锅";
        foodsKey[6] = "鸡";
        foodsKey[7] = "宫保";
        foodsKey[8] = "锅巴";
        foodsKey[9] = "猪肝";
        foodsKey[10] = "土豆";
        foodsKey[11] = "青椒";
        foodsKey[12] = "蒜苔";
        foodsKey[13] = "酸菜";
        foodsKey[14] = "苦瓜";
        foodsKey[15] = "鱼香";
        foodsKey[16] = "蒜泥";
        foodsKey[17] = "豆腐";
        foodsKey[18] = "玉米";
        foodsKey[19] = "虎皮";
        foodsKey[19] = "粉";

        return foodsKey;
    }
}