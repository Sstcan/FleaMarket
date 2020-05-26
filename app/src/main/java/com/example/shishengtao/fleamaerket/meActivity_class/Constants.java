package com.example.shishengtao.fleamaerket.meActivity_class;

public class Constants {
    public static int REQUEST_JSON_STRING_IS_NULL = 30000;
    public static int REQUEST_JSON_TOKEN_INVALID = 30001;
    public static int TOKEN_LAST_TIME = 3000*60; //3000分钟

    // 登录时
    public static int LOGIN_SUCCESS = 200;
    public static int LOGIN_FAILED_USERNAME_IS_NULL = 20001;
    public static int LOGIN_FAILED_PASSWORD_IS_NULL = 20001;
    public static int LOGIN_FAILED_PASSWORD_WRONG = 20002;
    public static int LOGIN_FAILED_DB = 20003;

    // 注册时
    public static int REGISTER_USER_SUCCESS = 200;
    public static int REGISTER_USER_FAILED_DB = 10000;
    public static int REGISTER_USER_FAILED_IMG_BIG = 10001;
    public static int REGISTER_USER_FAILED_USER_EXIST = 10002;
    public static int REGISTER_FAILED_USERNAME_IS_NULL = 10003;
    public static int REGISTER_FAILED_PASSWORD_IS_NULL = 10004;

    // 对用户进行CRUD操作 ，查询和修改
    public static int USER_QUERY_SUCCESS = 200;
    public static int USER_UPDATE_SUCCESS = 200;

    public static int USER_FAILED_QUERY = 10005;
    public static int USER_FAILED_UPDATE = 10006;
    public static int USER_FAILED_NOT_QUERY_OR_UPDATE = 10007;
    public static int USER_FAILED_IMG_BIG = 10008;
    public static int USER_TOKEN_ERROR =  13000;

    // 发布商品
    public static String GOODS_OK = "200";
    public static int SALE_GOODS_SUCCESS = 200;
    public static int SALE_GOODS_FAILED_IMG_BIG = 40001;
    public static int SALE_GOODS_FAILED_DB = 40002;
    public static int SALE_GOODS_FAILED_PARAMS = 40003;

    // 发布商品
    public static int BUY_GOODS_SUCCESS = 200;
    public static int BUY_GOODS_FAILED_IMG_BIG = 40001;
    public static int BUY_GOODS_FAILED_DB = 40002;

    // 防止图片过大
    public static long BIGEST_SIZE = 401024; //18K

    // 操作类型
    public static int OP_REGISTER = 90001;
    public static int OP_LOGIN = 90002;

    public static int OP_PUBLISH_GOODS = 90003;	//发布商品
    public static int OP_QUERY_GOODS = 90004;   //查询商品
    public static int OP_UPDATE_GOODS = 90005;  //更新商品

    public static int OP_QUERY_USER = 90006; //查询用户
    public static int OP_UPDATE_USER = 90007;//更新用户

    public static int OP_ERRROR_CODE = 99999; //全局错误
    public static int JSON_PARASE_ERROR = 88888; //json 数据有问题

}
