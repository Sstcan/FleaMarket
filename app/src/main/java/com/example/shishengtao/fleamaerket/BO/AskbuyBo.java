package com.example.shishengtao.fleamaerket.BO;

import java.util.Arrays;

public class AskbuyBo {
    private int opType;     //操作类型（发布，维护等）
    private String goodsID;  //ID
    private String goodsType; //商品所属类
    private String goodsName; //商品名
    private float price;       // 价格

    private String unit;      //单位
    private float quality;   //数量
    private String userid;   //发布人ID
    private byte[] goodsImg; //商品图片
    private String uname;
    private String uphone;
    private String qq;
    private String weixin;
    private String token;    //token
    //private String goodsTypeName; //所属类名（可选字段）
    private int sex;

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getQuality() {
        return quality;
    }

    public void setQuality(float quality) {
        this.quality = quality;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public byte[] getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(byte[] goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "AskbuyBo{" +
                "opType=" + opType +
                ", goodsID='" + goodsID + '\'' +
                ", goodsType='" + goodsType + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", price=" + price +
                ", unit='" + unit + '\'' +
                ", quality=" + quality +
                ", userid='" + userid + '\'' +
                ", goodsImg=" + Arrays.toString(goodsImg) +
                ", uname='" + uname + '\'' +
                ", uphone='" + uphone + '\'' +
                ", qq='" + qq + '\'' +
                ", weixin='" + weixin + '\'' +
                ", token='" + token + '\'' +
                ", sex=" + sex +
                '}';
    }
}
