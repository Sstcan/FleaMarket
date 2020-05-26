package com.example.shishengtao.fleamaerket.me_Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shishengtao.fleamaerket.BO.User;
import com.example.shishengtao.fleamaerket.FindActivity.ReGoodsList;
import com.example.shishengtao.fleamaerket.MainActivity;
import com.example.shishengtao.fleamaerket.R;
import com.example.shishengtao.fleamaerket.utils.HttpUtils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpdatePswdActivity extends AppCompatActivity {

    private ImageView iv_back;
    private EditText et_newpaswd,et_surepswd;
    private Button bt_sure;
    String password,surepasword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_update_pswd);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        iv_back = findViewById(R.id.uppswd_back);
        et_newpaswd = findViewById(R.id.uppswd_new);
        et_surepswd = findViewById(R.id.uppswd_sure);
        bt_sure = findViewById(R.id.uppswd_submit);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = et_newpaswd.getText().toString();
                surepasword = et_surepswd.getText().toString();
                if (!surepasword.equals(password)){
                    Toast.makeText(UpdatePswdActivity.this, "两次密码不一致，请重新输入", Toast.LENGTH_LONG).show();

                }else if(surepasword.equals("") || password.equals("")){
                    Toast.makeText(UpdatePswdActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();

                }else{
                    getData();
                }

//                Intent intent = new Intent(UpdatePswdActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });

    }

    private void getData() {
        //获取用户的userid
        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        String userid = preferences.getString("userid","");
        Log.i("TAG","me_passwordChangeActivity中userid--->"+userid);

        User user = new User();
        user.setUpassword(password);
        user.setUid(userid);

        //将获取的对象转换成Json串
        Gson gson = new Gson();
        String passwordJsonStr = gson.toJson(user, User.class);
        Log.i("TAG", "me_passwordChangeActivity中的passwordJsonStr is :" + passwordJsonStr);

        String IP = getResources().getString(R.string.URL_HOME);
        String url=IP+"/Proj20/updatePswd";

        HttpUtils.sendOkHttpRequest(url, passwordJsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "获取数据失败了" + e.toString());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Log.d("TAG", "获取数据成功了");
                    Log.d("TAG", "me_passwordChangeActivity中的response.code()==" + response.code());

                    final String jsonData = response.body().string();
                    Log.d("TAG", "me_passwordChangeActivity中的response.body().string()==" + jsonData);

                    Gson gson = new Gson();
                    Log.i("TAG", "开始解析jsonData");
                    ReGoodsList responseBuy = gson.fromJson(jsonData, ReGoodsList.class);
                    Log.i("TAG", "结束解析jsonData");
                    Log.i("TAG", "结束解析responseBuy:" + responseBuy);
                    final int flag = responseBuy.getFlag();
                    Log.i("TAG", "flag==" + flag);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (flag == 200) {
                                Log.i("TAG", "run: success");
                                Toast.makeText(UpdatePswdActivity.this, "您的密码已修改成功！请重新登录", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UpdatePswdActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }  else {
                                Toast.makeText(UpdatePswdActivity.this, "密码修改失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
