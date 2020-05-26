package com.example.shishengtao.fleamaerket.me_Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shishengtao.fleamaerket.BO.RegisterBo;
import com.example.shishengtao.fleamaerket.BO.UserBO;
import com.example.shishengtao.fleamaerket.FindActivity.TanWeiActivity;
import com.example.shishengtao.fleamaerket.R;
import com.example.shishengtao.fleamaerket.meActivity_class.Constants;
import com.example.shishengtao.fleamaerket.utils.HttpUtils;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_username, et_password, surepasw;
    private Button btn_register;
    private ImageView register_back, register_image;
    private byte[] images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      // this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        init();
        //把图片转化为字节数组
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.me_register_chen);
        images = Bitmap2Bytes(bmp);

        btn_register.setOnClickListener(this);
        register_back.setOnClickListener(this);
    }

    //初始化
    private void init() {

        et_username = findViewById(R.id.ETregister_user);
        et_password = findViewById(R.id.ETregister_password);
        surepasw = findViewById(R.id.ETsure_password);

        btn_register = findViewById(R.id.sure_register);
        register_back = findViewById(R.id.register_back);
        register_image = findViewById(R.id.register_photo);
    }

    // bitmp转bytes
    private byte[] Bitmap2Bytes(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//ByteArrayOutputStream字节数组输出流
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_register:

                String username = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String surepassword=surepasw.getText().toString().trim();

                Log.i("register", "uname is :" + username);
                Log.i("register", "upassword is :" + password);

                if (username.equals(" ") | password.equals(" ")) {
                    Toast.makeText(RegisterActivity.this, "请输入需要注册的用户名或密码",
                            Toast.LENGTH_LONG).show();
                } else if(!surepassword.equals(password)) {
                    Toast.makeText(RegisterActivity.this, "两次密码不一致，请重新输入",
                            Toast.LENGTH_LONG).show();
                }else {
                    //发送请求注册用户
                    register(username, password, images);
                }
                break;
            case R.id.register_back:
                finish();
                break;
        }

    }

    //开始注册
    protected void register(String name, String password ,byte[] images) {

        UserBO userbao=new UserBO();
        Constants constants=new Constants();
        String uuid = UUID.randomUUID().toString();
        String phone="13868881230";
        int sex=1;

        userbao.setOpType(constants.OP_REGISTER);
        userbao.setUid(uuid);
        userbao.setUname(name);
        userbao.setUpassword(password);
        userbao.setSex(sex);
        userbao.setUimage(images);
        userbao.setUphone(phone);

        Gson gson=new Gson();
        String userJsonStr=gson.toJson(userbao,UserBO.class);
        Log.i("register","开始注册时的jsonStr is :"+userJsonStr);

        String IP=getResources().getString(R.string.URL_HOME);
        String url=IP+"/Proj20/register";
        sendRequst(url,userJsonStr);
    }

    //发送注册请求
    protected void sendRequst(String url, String userJsonStr) {
        Log.i("register","发送注册请求时的userJsonStr is : "+userJsonStr);

        HttpUtils.sendOkHttpRequest(url, userJsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("register","获取数据失败了"+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d("register","获取数据成功了");
                Log.d("register","response.code()=="+response.code());

                final String s = response.body().string();
                Log.d("register","response.body().string()=="+s);

                Gson gson=new Gson();
                RegisterBo  registerBo=gson.fromJson(s,RegisterBo.class);
                int responFlag=registerBo.getFlag();

                if (responFlag==200){
                    Log.i("register", "register responFlag is========"+responFlag);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"注册成功，请登录！",Toast.LENGTH_LONG).show();
                            RegisterActivity.this.finish();
                        }
                    });

                }else if (responFlag==10002){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"用户已经存在，请重新注册!",Toast.LENGTH_LONG).show();
                        }
                    });

                }else if(responFlag==10003){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"用户名不能为空，请重新注册!",Toast.LENGTH_LONG).show();
                        }
                    });
                }else if(responFlag==10004){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"密码不能为空，请重新注册",Toast.LENGTH_LONG).show();

                        }
                    });
                }
                Log.d("register","response的User=="+registerBo.toString());
            }
        });

    }
}
