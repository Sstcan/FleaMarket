package com.example.shishengtao.fleamaerket.me_Activity;

import android.util.Log;

import com.example.shishengtao.fleamaerket.BO.UserBo_login;
import com.example.shishengtao.fleamaerket.meActivity_class.Constants;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FindUser {
    int optype=90006;
    protected void register(String token, String password ) {

        UserBo_login userbao=new UserBo_login();

        Constants constants=new Constants();
        userbao.setOpType(constants.OP_LOGIN);

      /*  userbao.setUname(name);
        userbao.setUpassword(password);*/


        Gson gson=new Gson();
        String userJsonStr=gson.toJson(userbao,UserBo_login.class);
        Log.i("Test","jsonStr is :"+userJsonStr);

        String url="http://118.89.217.225:8080/Proj20/user";

        sendRequst(url,userJsonStr);
    }

    protected void sendRequst(String url, String userJsonStr) {
        Log.i("Test1","userJsonStr is : "+userJsonStr);

        OkHttpClient client=new OkHttpClient();

        RequestBody requestBody = new  FormBody.Builder()
                .add( "reqJson",userJsonStr )
                .build();
        Request request=new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Test1","获取数据失败了"+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d("Test1","获取数据成功了");
                Log.d("Test1","response.code()=="+response.code());

                final String s = response.body().string();
                Log.d("Test1","response.body().string()=="+s);

               /* Gson gson=new Gson();
                loginBo=gson.fromJson(s,LoginBo.class);*/

            }
        });
    }
}
