package com.example.shishengtao.fleamaerket.me_Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shishengtao.fleamaerket.BO.LoginBo;
import com.example.shishengtao.fleamaerket.BO.UserBo_login;
import com.example.shishengtao.fleamaerket.MainActivity;
import com.example.shishengtao.fleamaerket.R;
import com.example.shishengtao.fleamaerket.meActivity_class.Constants;
import com.example.shishengtao.fleamaerket.utils.HttpUtils;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private TextView forgetpsw;
    private Button login;
    private EditText et_username, et_password;
    private ImageView login_back;
    private String login_uname, login_paswd;
    private boolean boolean_login;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
       /* //设置光标在文字最后端闪烁
        EditText et = (EditText) findViewById(R.id.ET_login);
        et.setSelection(et.getText().toString().length());*/
        init();//初始化

        register.setOnClickListener(this);
        forgetpsw.setOnClickListener(this);
        login.setOnClickListener(this);
        login_back.setOnClickListener(this);
    }

    //初始化
    private void init() {
        register = (TextView) findViewById(R.id.Texview_login_register);
        Log.d("login", "=========register已经实例化========");
        forgetpsw = (TextView) findViewById(R.id.Texview_forgetPsw);
        Log.d("login", "=========forgetpsw已经实例化========");
        login = findViewById(R.id.bt_login);

        et_username = findViewById(R.id.ETlogin_uername);
        et_password = findViewById(R.id.ETlogin_password);

        login_back = findViewById(R.id.login_back);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Texview_login_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.Texview_forgetPsw:
                Intent intent_foegetpsw = new Intent(LoginActivity.this, FindbackPswdActivity.class);
                startActivity(intent_foegetpsw);
                break;
            case R.id.bt_login:

                login_uname = et_username.getText().toString();
                login_paswd = et_password.getText().toString();

                if (login_uname.equals(" ") | login_paswd.equals(" ")) {
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空！",
                            Toast.LENGTH_LONG).show();
                } else {
                    login(login_uname, login_paswd);
                }
                break;
            case R.id.login_back:
                finish();
                break;

        }

    }

    //开始登陆
    protected void login(String name, String password /*,byte image*/) {

        UserBo_login userbao = new UserBo_login();

        String username = name;

        Constants constants = new Constants();
        userbao.setOpType(constants.OP_LOGIN);
        userbao.setUname(name);
        userbao.setUpassword(password);


        Gson gson = new Gson();
        String userJsonStr = gson.toJson(userbao, UserBo_login.class);
        Log.i("login", "登录时的jsonStr is :" + userJsonStr);

        // String url="http://118.89.217.225:8080/Proj20/login";
        ///String url = "http://192.168.1.7:8081/Proj20/login";
        String IP = getResources().getString(R.string.URL_HOME);
        String url=IP+"/Proj20/login";
        sendRequst(url, userJsonStr, username);
    }

    //发送登陆请求
    protected void sendRequst(String url, String userJsonStr, final String l_uname) {
        Log.i("login", "发送请求时的userJsonStr is : " + userJsonStr);

        HttpUtils.sendOkHttpRequest(url, userJsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("login", "获取数据失败了" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d("login", "获取数据成功了");
                Log.d("login", "login response.code()==" + response.code());
                final String s = response.body().string();
                Log.d("login", "response.body().string()==" + s);

                //解析后台传来的数据
                Gson gson = new Gson();
                LoginBo loginResponseBo = gson.fromJson(s, LoginBo.class);
                Log.d("login", "response.body().string()==" + s);

                int responseFlage = loginResponseBo.getFlag();
                String userid=loginResponseBo.getUserid();

                if (responseFlage == 200) {
                    Log.i("login", "responFlag is========" + responseFlage);
                    boolean_login=true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        }
                    });

//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    BitmapFactory.decodeResource(getResources(),R.drawable.me_my).compress(Bitmap.CompressFormat.PNG, 50,baos);
//                    String imageString = new String(Base64.encode(baos.toByteArray(),Base64.DEFAULT));


                    /*SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();*/
                    //SharedPreferences.Editor.clear()方法是把之前commit后保存的所有信息全部进行清空。
                    editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString("uname", l_uname);
                    editor.putString("token", loginResponseBo.getToken());
                    editor.putString("userid",userid);
                    editor.putBoolean("login",boolean_login);
                 //   editor.putString("image",imageString);

                    editor.commit();

                    Log.i("login", "登陆成功后的用户名" + l_uname);
                    Log.i("login", "登陆成功后的userid" + userid);

                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("extra","登陆成功");
                    startActivity(intent);
                    finish();

                } else if (responseFlage == 20001) {
                    boolean_login=false;
                    editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putBoolean("login",boolean_login);
                    editor.commit();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "请输入用户名或密码！", Toast.LENGTH_LONG).show();
                        }
                    });
                }else if(responseFlage==20002){
                    boolean_login=false;
                    editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putBoolean("login",boolean_login);
                    editor.commit();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "密码错误，请重新输入！", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }
}
