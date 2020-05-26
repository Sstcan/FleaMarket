package com.example.shishengtao.fleamaerket.FindActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shishengtao.fleamaerket.BO.CollectBO;
import com.example.shishengtao.fleamaerket.BO.GoodsItemBO;
import com.example.shishengtao.fleamaerket.BO.SaleBO;
import com.example.shishengtao.fleamaerket.BO.SaleResponseBo;
import com.example.shishengtao.fleamaerket.R;
import com.example.shishengtao.fleamaerket.utils.HttpUtils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GoodsDetailActivity extends AppCompatActivity {

    protected String TAG = "TAG";
    protected ImageView iv_back;//返回
    protected ImageView iv_image;
    protected TextView tv_goodsId, tv_goodsName, tv_goodsPrice, tv_goodsQuantity,tv_goodstell;
    protected ImageView iv_collect;
    protected int collectFlag = 2;//收藏标志  0---取消收藏，1---添加收藏, 2--查询
    protected int state = 0;     //记录当前状态
    protected int lastState = 0;//记录上次状态
    protected String token,userid,goodsID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_goods_detail_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        init();

        //接收从GoodsItemAdapter中传递过来的消息
        Intent intent = getIntent();
        //从intent中获取bundle
        Bundle bundle = intent.getExtras();
        GoodsItemBO goods = (GoodsItemBO) bundle.getSerializable("goods");

        SharedPreferences.Editor editor = getSharedPreferences("collect",MODE_PRIVATE).edit();
        editor.putString("goodsID",goods.getGoodsID());
        editor.commit();

        if(goods == null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(GoodsDetailActivity.this, "暂时没有数据哦！", Toast.LENGTH_SHORT).show();

                }
            });
        }

        //显示商品的对应信息
        showDetail(goods);

        CollectBO collectBO = new CollectBO();
        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        token = sp.getString("token","");
        Log.i("detail","GoodsItemActivity中token--->"+token);

        userid = sp.getString("userid","");
        Log.i("detail", "GoodsDetailActivity中userid--->"+userid);

        SharedPreferences sp1 = getSharedPreferences("collect",MODE_PRIVATE);
        goodsID = sp1.getString("goodsID","");
        Log.i("detail", "GoodsDetailActivity中userid--->"+goodsID);

        collectBO.setCollectFlag(collectFlag);
        collectBO.setGoodsID(goodsID);
        collectBO.setToken(token);
        collectBO.setUserid(userid);

        //发送OkHttp请求
        isCollectGoods(collectBO);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lastState == 0) {//未收藏
                    collectFlag = 1;//进入商品详情前，未收藏，然后点击则去做收藏
                }
                if(lastState == 1){//已收藏
                    collectFlag = 0;//进入详情前，已收藏，然后点金则去做取消收藏
                }
                CollectBO collectBO = new CollectBO();
                collectBO.setCollectFlag(collectFlag);
                collectBO.setToken(token);
                collectBO.setUserid(userid);
                collectBO.setGoodsID(goodsID);
                collectGoods(collectBO);
            }


        });

    }



    //初始化
    private void init() {
        iv_back = findViewById(R.id.detail_back);
        iv_image = findViewById(R.id.detail_goodsimage);
        tv_goodsId  = findViewById(R.id.detail_goods_id);
        tv_goodsName = findViewById(R.id.detail_goods_name);
        tv_goodsPrice = findViewById(R.id.detail_goods_price);
        tv_goodsQuantity = findViewById(R.id.detail_goods_quantity);
        tv_goodstell = findViewById(R.id.detail_tell);

        iv_collect = findViewById(R.id.detail_colloct);
    }

    //显示商品的对应信息
    private void showDetail(GoodsItemBO goods) {
      //  iv_image.setImageResource(R.drawable.aimage);

        byte[] bytes = goods.getGoodsImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
        iv_image.setImageBitmap(bitmap);

        tv_goodsId.setText(goods.getGoodsID());
        tv_goodsName.setText(goods.getGoodsName());
        tv_goodsPrice.setText(String.valueOf(goods.getPrice()));
        tv_goodsQuantity.setText(String.valueOf(goods.getQuality()));
        tv_goodstell.setText(goods.getUnit());
        Log.d("111","phone is======"+goods.getUnit());

    }

    //通过后台返回来的消息，判断该商品是否被收藏，设置相应的收藏图片
    private void isCollectGoods(CollectBO collectBO) {
        Gson gson = new Gson();
        String collectJsonStr = gson.toJson(collectBO, CollectBO.class);
        Log.i("detail", "GoodsItemActivity中需要传到后台的collectJsonStr is :" + collectJsonStr);
        String IP = getResources().getString(R.string.URL_HOME);
        String url=IP+"/Proj20/collect";
        HttpUtils.sendOkHttpRequest(url, collectJsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "获取数据失败了" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful()){
                    Log.d("detailResponse", "获取数据成功了");
                    Log.d("detailResponse", "response.code()==" + response.code());
                    final String collectJsonStr = response.body().string();
                    Log.d("detailResponse", "GoodsItemActivity中collectJsonStr--->" + collectJsonStr);


                    Gson gson = new Gson();
                    SaleResponseBo saleResponseBo = gson.fromJson(collectJsonStr,SaleResponseBo.class);
                    final int flag = saleResponseBo.getFlag();
                    collectFlag = saleResponseBo.getCollectFlag();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (flag == 40001){//商品已被收藏
                                iv_collect.setImageResource(R.drawable.detail_collect);
                                state = 1;
                                lastState = state;
                            }else {//未被收藏
                                iv_collect.setImageResource(R.drawable.detail_uncollect);
                                state = 0;
                                lastState = state;
                            }
                        }
                    });

                }
            }
        });



    }
    //点击收藏图片，收藏商品，取消收藏
    private void collectGoods(CollectBO collectBO) {

        Gson gson = new Gson();
        String collectJsonStr = gson.toJson(collectBO, CollectBO.class);
        Log.i("detail", "GoodsItemActivity中需要传到后台的collectJsonStr is :" + collectJsonStr);
        String IP = getResources().getString(R.string.URL_HOME);
        String url=IP+"/Proj20/collect";

        HttpUtils.sendOkHttpRequest(url, collectJsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "获取数据失败了" + e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoodsDetailActivity.this, "目前网络不佳！", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()){
                    Log.d("detailResponse", "获取数据成功了");
                    Log.d("detailResponse", "response.code()==" + response.code());
                    final String collectJsonStr = response.body().string();

                    Log.d("detailResponse", "GoodsItemActivity中collectJsonStr--->" + collectJsonStr);

                    Gson gson = new Gson();
                    SaleResponseBo saleResponseBo = gson.fromJson(collectJsonStr,SaleResponseBo.class);
                    final int flag = saleResponseBo.getFlag();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (flag == 200){
                                if(collectFlag == 1){
                                    Toast.makeText(GoodsDetailActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
                                    state = 1;
                                    iv_collect.setImageResource(R.drawable.detail_collect);
                                }else{
                                    Toast.makeText(GoodsDetailActivity.this, "取消收藏成功！", Toast.LENGTH_SHORT).show();
                                    state = 0;
                                    iv_collect.setImageResource(R.drawable.detail_uncollect);
                                }
                                lastState = state;
                            }
                            if (flag == 201){
                                Toast.makeText(GoodsDetailActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

    }
}
