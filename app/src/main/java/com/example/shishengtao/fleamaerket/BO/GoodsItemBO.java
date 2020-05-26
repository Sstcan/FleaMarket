package com.example.shishengtao.fleamaerket.BO;

import java.io.Serializable;
import java.util.Arrays;

public class GoodsItemBO implements Serializable {
    private String goodsID;  //ID
    private String goodsType; //商品所属类
    private String goodsName; //商品名
    private float price;       // 价格
    private String unit;      //单位
    private float quality;   //数量
    private String userid;   //发布人ID
    private byte[] goodsImg; //商品图片

    private int sex;
    private String goodsTypeName; //所属类名（可选字段）
    private int opType;     //操作类型（发布，维护等）

    private String token;    //token
    private String qq;

    public int page;//页数
    protected int checkType;//查询方式 1---刷新  2---加载
    public int pageSize;//数据条数

    public int flagType;//1--摊位，2--求购

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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCheckType() {
        return checkType;
    }

    public void setCheckType(int checkType) {
        this.checkType = checkType;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getFlagType() {
        return flagType;
    }

    public void setFlagType(int flagType) {
        this.flagType = flagType;
    }

    @Override
    public String toString() {
        return "GoodsItemBO{" +
                "goodsID='" + goodsID + '\'' +
                ", goodsType='" + goodsType + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", price=" + price +
                ", unit='" + unit + '\'' +
                ", quality=" + quality +
                ", userid='" + userid + '\'' +
                ", goodsImg=" + Arrays.toString(goodsImg) +
                ", sex=" + sex +
                ", goodsTypeName='" + goodsTypeName + '\'' +
                ", opType=" + opType +
                ", token='" + token + '\'' +
                ", qq='" + qq + '\'' +
                ", page=" + page +
                ", checkType=" + checkType +
                ", pageSize=" + pageSize +
                ", flagType=" + flagType +
                '}';
    }
}
