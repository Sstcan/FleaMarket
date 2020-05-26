package com.example.shishengtao.fleamaerket.HomeActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shishengtao.fleamaerket.Adapter.GoodsItemAdapter;
import com.example.shishengtao.fleamaerket.BO.GoodsItemBO;
import com.example.shishengtao.fleamaerket.FindActivity.ReGoodsList;
import com.example.shishengtao.fleamaerket.R;
import com.example.shishengtao.fleamaerket.utils.HttpUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    //属性定义
    private String TAG = "TAG";
    private EditText et_content;
    private Button bt_query, bt_clear;
    private ImageView iv_back;
    private int opType = 90004;//操作类型
    private String goodsName;
    //查询列表中的属性
    RecyclerView recyclerView;
    List<GoodsItemBO> resultGoodsList = new ArrayList<GoodsItemBO>();

    private ImageView iv_no_goods;//无商品时
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        //控件初始化
        init();

        //发送请求实现搜索商品的功能
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(opType);
            }
        });
        //将EditText中的内容清空
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_content.setText("");
                bt_clear.setVisibility(View.GONE);
            }
        });
        //点击取消则finish
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }



    //控件初始化
    private void init() {
        iv_back = findViewById(R.id.search_iv_back);
        et_content = findViewById(R.id.search_et_content);
        bt_query = findViewById(R.id.search_bt_query);
        bt_clear = findViewById(R.id.search_bt_clear);
        iv_back = findViewById(R.id.search_iv_back);
        iv_no_goods = findViewById(R.id.search_iv_no_goods);
        recyclerView  =findViewById(R.id.search_tag_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void getData(int opType) {
        goodsName = et_content.getText().toString().trim();
        //判断EditText框内容是否为空
        if (goodsName.equals("") || goodsName == null){
            Toast.makeText(SearchActivity.this,"请输入想要搜索的内容！",Toast.LENGTH_SHORT).show();
            iv_no_goods.setVisibility(View.GONE);
            return;
        }else {
            bt_clear.setVisibility(View.VISIBLE);
        }

        GoodsItemBO goods = new GoodsItemBO();
        goods.setOpType(opType);
        goods.setGoodsName(goodsName);
        //将获取的对象转换成Json串
        Gson gson = new Gson();
        String searchJsonStr = gson.toJson(goods, GoodsItemBO.class);
        Log.i(TAG, "查询商品中buyJsonStr is :" + searchJsonStr);

        String IP = getResources().getString(R.string.URL_HOME);
        String url=IP+"/Proj20/search";

        HttpUtils.sendOkHttpRequest(url, searchJsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "获取数据失败了" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Log.d(TAG, "获取数据成功了");
                    Log.d(TAG, "response.code()==" + response.code());
                    final String jsonData = response.body().string();
                    Log.d(TAG, "查询商品中的response.body().string()==" + jsonData);
                    Gson gson = new Gson();
                    Log.i(TAG, "开始解析jsonData");
                    ReGoodsList responseBuy = gson.fromJson(jsonData, ReGoodsList.class);
                    Log.i(TAG, "结束解析jsonData");
                    Log.i(TAG, "结束解析responseBuy:" + responseBuy);
                    //Log.i(TAG,"查询商品的列表："+ responseBuy.getGoodList().get(0));
                    final int flag = responseBuy.getFlag();
                    Log.i(TAG, "flag==" + flag);
                    resultGoodsList = responseBuy.getGoodsList();
                    Log.i("resultGoodsList", "resultGoodsList==" + resultGoodsList.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setVisibility(View.VISIBLE);
                           // iv_networkbad.setVisibility(View.GONE);
                            iv_no_goods.setVisibility(View.GONE);
                            if (flag == 200) {
                                Log.i(TAG, "run: success");
                                //为RecyclerView的item指定其布局为线性布局
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                //绑定适配器
                                GoodsItemAdapter adapter = new GoodsItemAdapter(SearchActivity.this, resultGoodsList);
                                recyclerView.setAdapter(adapter);
                                if (resultGoodsList.size() == 0) {
                                    iv_no_goods.setVisibility(View.VISIBLE);
                                }
                            } else if (flag == 30001) {
                                Toast.makeText(SearchActivity.this, "登录信息已失效,请再次登录", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SearchActivity.this, "查询失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
