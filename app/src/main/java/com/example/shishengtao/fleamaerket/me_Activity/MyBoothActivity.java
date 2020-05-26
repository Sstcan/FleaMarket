package com.example.shishengtao.fleamaerket.me_Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shishengtao.fleamaerket.Adapter.GoodsItemAdapter;
import com.example.shishengtao.fleamaerket.BO.GoodsItemBO;
import com.example.shishengtao.fleamaerket.FindActivity.ReGoodsList;
import com.example.shishengtao.fleamaerket.R;
import com.example.shishengtao.fleamaerket.utils.HttpUtils;
import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyBoothActivity extends AppCompatActivity {


    //属性定义
    private ImageView iv_back;
    private String TAG = "TAG";
    private SharedPreferences preferences;

    private int opType = 90004;//操作类型
    //查询列表中的属性
    private  RecyclerView recyclerView;
    List<GoodsItemBO> newResultGoodsList = new ArrayList<GoodsItemBO>();
    List<GoodsItemBO> allGoodsList = new ArrayList<GoodsItemBO>();
    private SpringView springView;//下拉刷新，上拉加载的控件
    private int pageRefresh;//刷新页数
    private int pageMore = 1;//加载更多页数
    protected int LoadORRefresh = 1;//查询方式 1---上拉加载更多  2---下拉刷新
    public int pageSize = 5;//数据条数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_my_booth);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        //初始化控件
        init();

        //获取用户token
        preferences = getSharedPreferences("data",MODE_PRIVATE);
        final String token = preferences.getString("token","");
        Log.i("mybooth", "从sp获取到的token==" + token);

        getData(token,opType);

        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));

        springView.setListener(new SpringView.OnFreshListener() {
            //刷新
            @Override
            public void onRefresh() {
                pageRefresh = 1;
                LoadORRefresh = 2;
                Log.i("mybooth", "onRefresh: page is " + pageRefresh);
                //getData();
                getData(token, opType);
                springView.onFinishFreshAndLoad();
            }

            //加载更多
            @Override
            public void onLoadmore() {
                pageMore++;
                LoadORRefresh = 1;
                Log.i("mybooth", "onRefresh: page is " + pageMore);
                /*********/
                //getData();
                getData(token, opType);
                /*********/
                springView.onFinishFreshAndLoad();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    //初始化控件
   public void init(){
        recyclerView = findViewById(R.id.mybooth_recyclerView);
       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyBoothActivity.this, LinearLayoutManager.VERTICAL, false);
       recyclerView.setLayoutManager(linearLayoutManager);
        springView = findViewById(R.id.mybooth_springView);
        iv_back = findViewById(R.id.myboothiv_back);
   }

   public void getData(String token,int optype){

        //获取userid

       preferences = getSharedPreferences("data", MODE_PRIVATE);
       String userid = preferences.getString("userid","");
       Log.i("mybooth","me_boothActivity中userid--->"+userid);

       GoodsItemBO goods = new GoodsItemBO();
       goods.setToken(token);
       goods.setUserid(userid);
       goods.setOpType(optype);
       goods.setCheckType(LoadORRefresh);

       if (LoadORRefresh == 1){//加载更多
           goods.setPage(pageMore);
       }else {
           goods.setPage(pageRefresh);
       }
       goods.setPageSize(pageSize);

       //将获取的对象转换成Json串
       Gson gson = new Gson();
       String saleshowJsonStr = gson.toJson(goods, GoodsItemBO.class);
       Log.i("mybooth", "查询商品中buyJsonStr is :" + saleshowJsonStr);

       String IP = getResources().getString(R.string.URL_HOME);
       String url=IP+"/Proj20/myboothshow";

       HttpUtils.sendOkHttpRequest(url, saleshowJsonStr, new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               Log.d("myboothResponse", "获取数据失败了" + e.toString());
           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {

               Log.d(TAG, "获取数据成功了");
               Log.d(TAG, "response.code()==" + response.code());

               final String jsonData = response.body().string();
               Log.d(TAG, "me_boothActivity中查询商品中的response.body().string()==" + jsonData);

               Gson gson = new Gson();
               Log.i(TAG, "开始解析jsonData");
               ReGoodsList responseBuy = gson.fromJson(jsonData, ReGoodsList.class);
               Log.i(TAG, "结束解析jsonData");
               Log.i(TAG, "结束解析responseBuy:" + responseBuy);
               //Log.i(TAG,"查询商品的列表："+ responseBuy.getGoodList().get(0));
               final int flag = responseBuy.getFlag();
               Log.i(TAG, "flag==" + flag);
               newResultGoodsList = responseBuy.getGoodsList();
               Log.i(TAG, "newResultGoodsList==" + newResultGoodsList);

               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       //recyclerView.setVisibility(View.VISIBLE);
                       if (flag == 200) {
                           Log.i(TAG, "run: success");
                           //如果一直上拉刷新，当newResultGoodsList返回为null，说明数据库里已经没有更多数据了
                           if (newResultGoodsList.size() <=0) {
                               Toast.makeText(MyBoothActivity.this, "没有更多内容了哦！", Toast.LENGTH_SHORT).show();
                               return;
                           }
                           //上拉加载
                           if (LoadORRefresh == 1) {
                               //将newResultGoodsList加到allGoodsList的下面显示，使其按顺序排序
                               for (int i = 0; i < newResultGoodsList.size(); i++) {
                                   boolean repeat = false;//判断加入新的List中的getGoodsID是否与旧的List中的getGoodsID是否一样一样则不重复加载
                                   for (int j = 0; j < allGoodsList.size(); j++) {
                                       if (allGoodsList.get(j).getGoodsID().equals(newResultGoodsList.get(i).getGoodsID())) {
                                           repeat = true;
                                           break;
                                       }
                                   }
                                   if (repeat == false) {
                                       allGoodsList.add(newResultGoodsList.get(i));
                                       Log.i(TAG, "me_boothActivity中allGoodsList.size() " + allGoodsList.size());
                                   }
                               }
                               GoodsItemAdapter adapter = new GoodsItemAdapter(MyBoothActivity.this, allGoodsList);
                               recyclerView.setAdapter(adapter);
                               recyclerView.scrollToPosition(adapter.getItemCount() - 1);//自动滑动到底部
                           }
                           //下拉刷新
                           if (LoadORRefresh == 2) {
                               //将allGoodsList的内容加到newResultGoodsList下面显示，使得最新的展示在第一页
                               for (int i = 0; i < allGoodsList.size(); i++) {
                                   boolean repeat = false;//判断加入新的List中的getGoodsID是否与旧的List中的getGoodsID是否一样一样则不重复加载
                                   for (int j = 0; j < newResultGoodsList.size(); j++) {
                                       if (newResultGoodsList.get(j).getGoodsID().equals(allGoodsList.get(i).getGoodsID())) {
                                           repeat = true;
                                           break;
                                       }
                                   }
                                   if (repeat == false) {
                                       newResultGoodsList.add(allGoodsList.get(i));
                                       Log.i(TAG, "me_boothActivity中allGoodsList.size() " + allGoodsList.size());
                                   }
                               }
                               allGoodsList = newResultGoodsList;
                               GoodsItemAdapter adapter = new GoodsItemAdapter(MyBoothActivity.this, allGoodsList);
                               recyclerView.setAdapter(adapter);
                               //recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                           }
                       } else if (flag == 30001) {
                           Toast.makeText(MyBoothActivity.this, "登录信息已失效,请再次登录", Toast.LENGTH_SHORT).show();
                       } else {
                           Toast.makeText(MyBoothActivity.this, "查询失败！", Toast.LENGTH_SHORT).show();
                       }
                   }
               });
           }
       });


   }

}

