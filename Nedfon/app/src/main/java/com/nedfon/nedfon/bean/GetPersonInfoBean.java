package com.nedfon.nedfon.bean;

/**
 * Created by Administrator on 2018/3/2 0002.
 * {"result":1,"data":{
 * "id":15,"nickname":null,"phone":"13458581206","sex":null,"lat":null,"lng":null,
 * "email":"3232","pswd":"123456","createTime":"2018-02-06","lastLoginTime":null,"status":1,"loginCount":null,
 * "registerIp":null,"userType":"1","lastLoginIp":null,"currentLoginTime":null,"currentLoginIp":null,"resetPwd":null,
 * "errtime":null,"errcount":null,"duty":null,"roleStrlist":null,"perminsStrlist":null,"resetKey":"524655"
 * },"msg":"获取个人信息成功"}
 */

public class GetPersonInfoBean {
    public int id;
    public String nickname;
    public String phone;
    public int sex;
    public int loginCount;
    public int status;
    public String registerIp;
}
