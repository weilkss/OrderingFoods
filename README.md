# Springboot DingTalk 自定义机器人点餐系统


### 在linux上部署springboot：

```shell script
##查看端口
netstat -tunlp|grep 7456

##杀死进程
kill -9 {PID}

##永久启动服务
nohup java -jar diancan-0.0.1-SNAPSHOT.jar system.log 2>&1&
```

### 更新功能 新增数据到mysql
- 新增自定义菜单

>如果没有某个菜或者大家都讨厌某个菜就可以取消勾选，怎么下次点菜就不会出现

- 新增点餐记忆功能

>下次点餐会按照上次点菜进行点餐，比如上次点餐人数5个人，这次也就5个人，如果要点6个人的则手动加一下，下次又自动点6个人的

- 优化菜单

>去掉点餐重复菜品