# 钉钉自定义机器人，开发一个自动点餐的系统

> 首先，我是一个前端页面仔，Java 是自学的，所以有什么代码入不了各位大佬的眼，希望指出，那可能就是我将来加薪的重要的一分，万分感谢！🙏。其次，每天中午我们老大叫我们点菜，当你在处理 bug 有头绪的时候突然就给你打断了，最关键的是你也不知道吃什么，你说气不气，一气之下，写了这个点餐系统，然后自动点餐发送到群里，如果有不满意的还可以重新点一份～

## 功能

1.工作日期间每天上午 11 点准时点餐机器人发送到钉钉群

2.如果系统点餐不满意可以去点餐首页在手动点餐一次，可以选择就餐人数，荤菜数等，且手动点餐后会记录此次点餐的状态比如：人数、荤菜数等，下次系统点餐会按照此次配置进行点餐

3.如果有同事不喜欢吃的菜或者餐馆下架的菜品，可以在菜单列表去取消勾选，那么不管是系统点餐还是手动点餐都不会在出现在点餐的列表上

4.优化不出现同名菜品，比如`仔姜肉丝`和`鱼香肉丝`都是肉丝，那么系统就会处理只会出现一个肉丝

|               钉钉消息界面               |                   点餐首页                   |                 菜单列表                  |
| :--------------------------------------: | :------------------------------------------: | :---------------------------------------: |
| ![](http://file.weilkss.cn/IMG_1324.png) | ![](http://file.weilkss.cn/dingtalkhome.png) | ![](http://file.weilkss.cn/diacan-03.gif) |

## 添加机器人

[官方文档](https://ding-doc.dingtalk.com/doc#/serverapi2/qf2nxq) 写得很清楚，我这里就不写添加步骤了，需要注意的是安全设置，这里一般选择关键字就行，是比较省心的做法～

## 新建项目 SpringBoot 整合 mybatis(XML 配置文件)

新建 `SpringBoot` 我这里就不操作了,直接来看下 `pom.xml` 的配置

### 配置 web 核心依赖

主要是有部分页面，点餐和菜单页面

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### mysql 数据库驱动 和 mybatis

```xml
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 配置本地 dingtalk 依赖插件

这个是钉钉自定义机器人的依赖包，[下载地址](https://ding-doc.dingtalk.com/doc#/faquestions/vzbp02/8DMhu)，首先在根目录下建一个 `libs` 的文件夹，把下载好的 `SDK` 放到 `libs` 下，在 `pom.xml` 做一下配置

```xml
<dependency>
  <groupId>com.dingtalk.open</groupId>
  <artifactId>taobao-sdk-java</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <scope>system</scope>
  <systemPath>${project.basedir}/libs/dingtalk-sdk-java/taobao-sdk-java-auto.jar</systemPath>
</dependency>
```

要在 `<build>` 里面加入 `<configuration>` 配置,不然的话打包发布到服务器找不到本地依赖插件的

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
      <configuration>
        <fork>true</fork>
        <includeSystemScope>true</includeSystemScope>
      </configuration>
    </plugin>
  </plugins>
</build>
```

## 数据库

`foods` 表结构

![](http://file.weilkss.cn/截屏2020-07-2414.53.53.png)

`params` 表结构

![](http://file.weilkss.cn/截屏2020-07-2414.54.09.png)

## WebController web 页面控制器

```java
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
     * 返回foods列表
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

```

## ApiController

> 这里主要是更新状态和 foods 的状态的接口

数据库的数据使用 `mapper` 注入,在 `resources` 中新建 `mapper` 文件夹,在里面是 `*Mapper.xml` 文件,所以要在 `application.properties` 配置

```xml
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.type-aliases-package=cn.com.diancan.entity
mybatis.configuration.map-underscore-to-camel-case=true
```

要在入口注入 `@MapperScan("cn.com.diancan.mapper")`

在 `mapper` 中

```java
@Mapper
public interface FoodsMapper {
    List<Foods> findFoods();

    Integer updateFoods(@Param("fid") Integer fid,@Param("show") Integer show);

    List<Foods> findFoodsByType(@Param("type") Integer type);
}
```

`service` 业务处理层

```java
public interface FoodsService {
    List<Foods> findFoods();

    Integer updateFoods(Integer fid,Integer show);

    List<Foods> findFoodsByType(Integer type);
}
```

impl

```java

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
```

看看 `FoodsMapper.xml`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="cn.com.diancan.mapper.FoodsMapper">

    <select id="findFoods" resultType="Foods">SELECT * FROM foods</select>

    <update id="updateFoods" parameterType="Foods">
        UPDATE foods SET `show`=#{show} WHERE fid=#{fid}
    </update>

    <select id="findFoodsByType" resultType="Foods">SELECT * FROM foods WHERE `type`=#{type} AND `show`=1</select>
</mapper>
```

下面代码 api 是为了记录手动点餐后记录本次的状态

```java
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
```

以下代码是更新当前菜名是否选中，取消勾选下次点菜就不会出现

```java
@ResponseBody
@PostMapping(value = "/api/updateFoodsByFid", produces = "application/json;charset=UTF-8")
public Result updateFoodsByFid(@RequestBody JSONObject jsonObject) {
    Integer fid = Integer.parseInt(jsonObject.get("fid").toString());
    Integer show = Integer.parseInt(jsonObject.get("show").toString());
    foodsService.updateFoods(fid, show);
    return Result.success();
}
```

## DingTalkController

实例化 `dingTalkClient` 和 `request`

```java
private DingTalkClient dingTalkClient = new DefaultDingTalkClient(WebConstants.SERVER_URL);
private OapiRobotSendRequest request = new OapiRobotSendRequest();
```

我这里用到了 `text` 和 `Markdown` 的消息类型

```java
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
```

```java
/**
  * sendMarkdown
  */
public void sendMarkdown() throws ApiException {
    OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
    markdown.setTitle(WebConstants.SEND_TEXT_TITLE);

    String text = buffer.toString();
    markdown.setText(text);

    request.setMsgtype("markdown");
    request.setMarkdown(markdown);

    dingTalkClient.execute(request);
}
```

核心逻辑，通过获取到的 `foods` 然后根据 `array` 长度获取不重复的随机数，然后在正则匹配是否有同名菜，如果有的化就重新获取一个菜，直到不同名,最后在循环最终获取的数组，在通过 `StringBuffer` 来拼接字符串

### 获取不重复指定长度的随机数组

```java
/**
  * 获取随机数
  *
  * @param max
  * @param len
  * @return rands
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
```

### 判断是否有同名菜

把重复的菜名放到一个数组里，然后在处理这个数组把重复的菜名替换掉

```java
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
```

最后：这块优化是后面写的，是因为之前一直发现出现同名菜，然后同事说这不智能，然后我才抽空闲时间做了这块优化，就目前来看，是没有问题的！自己很少写文章，对项目的解析和总结也就这样子，如果有什么问题欢迎 [![](https://img.shields.io/github/issues/weilkss/OrderingFoods)](https://github.com/weilkss/OrderingFoods/issues) 如果你觉得对你有帮助欢迎 [![](https://img.shields.io/github/stars/weilkss/OrderingFoods?style=social)](https://github.com/weilkss/OrderingFoods)
