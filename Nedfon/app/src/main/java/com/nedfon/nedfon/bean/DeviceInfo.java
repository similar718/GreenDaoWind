package com.nedfon.nedfon.bean;

/**
 * Created by Administrator on 2018/1/24.
 */
public class DeviceInfo {
    public int id;
    public String sn; //设备sn码
    public String deviceid;
    public String apikey;
    public String terminal; //设备名称
    public String clienttime; //创建时间
    public String areaid;
    public String areaname;
    public String location; //坐落地址
    public float longitude; //纬度
    public float latitude; //经度
    public String addTime; //设备添加时间
    public int commStatus; //通讯状态 0 不在线 1在线
    public String commTime; //通讯时间
    public int status; //错误状态码
    public int status1; //错误状态码
    public int status2; //错误状态码
    public String updatetime; //状态更新时间
    public int ionsflag; //负离子开关状态 0关闭 1打开
    public int workmodel; //工作模式 0自动 1手动
    public int workgear; //工作档位
    public float outtmp; //室外温度
    public float intmp; //室内温度
    public float outpm25; //室外pm25值
    public float inpm25; //室内pm25值
    public int settime; //定时器是否启用 0 未启用 1 启用
    public String opentime;
    public String closetime;
    public int windstatus; //风机状态
    public int windgear; //风机工作岗位
    public int washtimeremaining; //清洗剩余时间
    public float intmpalarmdata; //温度报警阀值
    public float inpm25alarmdata; //PM2.5报警阀值
    public float insweet; //室内湿度
    public float outsweet; //室外湿度
    public float sweetalarmdata; //湿度报警阀值
    public int changeOrPushModel; //排风 送风模式
    public int userid; //所属用户ID
    public String username; //用户名称
    public String userphone; //用户号码
    public String useremail; //用户Email
    public String ip1;
    public int socket1;
    public String ip2;
    public int socket2;
    public String deviceVersion;
}
