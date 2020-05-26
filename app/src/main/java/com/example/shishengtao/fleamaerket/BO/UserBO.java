package com.example.shishengtao.fleamaerket.BO;

import java.util.Arrays;

/**
 * Created by chenmin on 2018/10/23.
 */

public class UserBO {

        private String uid;
        private String uname;
        private String upassword;

        private byte[] uimage;
        private String uphone;
        private int sex;
        private String qq;
        private String weixin;

        private String token; // 查询或更新用户时，需要用到token
        private int opType;//操作类型

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public byte[] getUimage() {
            return uimage;
        }

        public void setUimage(byte[] uimage) {
            this.uimage = uimage;
        }

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

        public int getOpType() {
            return opType;
        }

        public void setOpType(int opType) {
            this.opType = opType;
        }

    @Override
    public String toString() {
        return "UserBO{" +
                "uid='" + uid + '\'' +
                ", uname='" + uname + '\'' +
                ", upassword='" + upassword + '\'' +
                ", uimage=" + Arrays.toString(uimage) +
                ", uphone='" + uphone + '\'' +
                ", sex=" + sex +
                ", qq='" + qq + '\'' +
                ", weixin='" + weixin + '\'' +
                ", token='" + token + '\'' +
                ", opType=" + opType +
                '}';
    }

/*
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(uname);
            dest.writeString(upassword);

    }

    public static final Parcelable.Creator<UserBO> CREATOR=new Creator<UserBO>() {
        @Override
        public UserBO createFromParcel(Parcel source) {
            UserBO userBO = new UserBO();
            source.readString();
            source.readString();

            return userBO;
        }

        @Override
        public UserBO[] newArray(int size) {
            return new UserBO[0];
        }
    };*/
}
