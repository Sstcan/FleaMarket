package com.example.shishengtao.fleamaerket.BO;

public class SaleResponseBo {

    private int flag;
    private String message;
    private int collectFlag;//2--已经收藏

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

    public int getCollectFlag() {
        return collectFlag;
    }

    public void setCollectFlag(int collectFlag) {
        this.collectFlag = collectFlag;
    }

    @Override
    public String toString() {
        return "SaleResponseBo{" +
                "flag=" + flag +
                ", message='" + message + '\'' +
                ", collectFlag=" + collectFlag +
                '}';
    }
}
