package com.example.shishengtao.fleamaerket.BO;

public class UserBo_login {
    private String uname;
    private String upassword;
    private int opType;


    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }

    @Override
    public String toString() {
        return "UserBo_login{" +
                "uname='" + uname + '\'' +
                ", upassword='" + upassword + '\'' +
                '}';
    }
}
