package com.nedfon.nedfon.bean;

/**
 * Created by Administrator on 2018/2/5 0005.
 */

public class DbDean {
    public String phone;
    public String pwd;
    public String token;
    public long time;

    public DbDean(){
    }

    public DbDean(String phones,String pwds,String tokens,long times){
        phone = phones;
        pwd = pwds;
        token = tokens;
        time = times;
    }
}
