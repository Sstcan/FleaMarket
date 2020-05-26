package com.example.shishengtao.fleamaerket.FindActivity;

import com.example.shishengtao.fleamaerket.BO.GoodsItemBO;

import java.util.ArrayList;
import java.util.List;

public class ReGoodsList {

    protected int flag;
    protected String message;
    protected String token;
    private String uname;
    private int flagType;//1--摊位，2--求购

    private String uphone;
    private int sex;
    private String qq;
    private String weixin;
    private List<GoodsItemBO> goodsList;


    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getFlag() {
        return flag;
    }


    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<GoodsItemBO> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsItemBO> goodsList) {
        this.goodsList = goodsList;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public int getFlagType() {
        return flagType;
    }

    public void setFlagType(int flagType) {
        this.flagType = flagType;
    }

    @Override
    public String toString() {
        return "ReGoodsList{" +
                "flag=" + flag +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                ", uname='" + uname + '\'' +
                ", flagType=" + flagType +
                ", uphone='" + uphone + '\'' +
                ", sex=" + sex +
                ", qq='" + qq + '\'' +
                ", weixin='" + weixin + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }
}
