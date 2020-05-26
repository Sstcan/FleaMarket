package com.example.shishengtao.fleamaerket.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shishengtao.fleamaerket.Adapter.GoodsItemAdapter;
import com.example.shishengtao.fleamaerket.BO.GoodsItemBO;
import com.example.shishengtao.fleamaerket.FindActivity.AskBuyActivity;
import com.example.shishengtao.fleamaerket.FindActivity.ReGoodsList;
import com.example.shishengtao.fleamaerket.FindActivity.TanWeiActivity;
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
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FindFragment extends Fragment implements View.OnClickListener {

    protected Button bt_qiugou;
    protected Button bt_tanwei;
    final protected int opType = 90004;
    protected SharedPreferences sp;
    protected String sp_uname, sp_token;//从SP中获取的用户名和用户的token

    protected ImageView addIamge; //商品的图片，这里用的是一张固定的图片
    protected String Type = "AskBuy";//用来点击切换摊位和求购按钮的TAG
    protected int BoothORAskbuy = 2;//加载商品显示界面的时候使用；1--摊位 2--求购

    protected RecyclerView recyclerView;//用来加载显示商品的布局
    protected SpringView springView;//下拉刷新，上拉加载的控件

    List<GoodsItemBO> newResultGoodsList = new ArrayList<GoodsItemBO>();
    List<GoodsItemBO> saleGoodsList = new ArrayList<GoodsItemBO>();
    List<GoodsItemBO> sale_allGoodsList = new ArrayList<GoodsItemBO>();
    List<GoodsItemBO> buyGoodsList = new ArrayList<GoodsItemBO>();
    List<GoodsItemBO> buy_allGoodsList = new ArrayList<GoodsItemBO>();

    protected int pageRefresh;//刷新页数
    protected int pageLoadMore = 1;//加载更多的页数
    protected int LoadMoreORRefesh = 1;//查询的方式  1---上拉加载更多  2---下拉刷新
    public int pageSize = 5;//数据条数
    //  protected ListView listView;//用来加载显示商品的布局
    protected LinearLayoutManager layoutManager;
    // protected GoodsAdapter adapter;
   // protected List<Goodlistitem> resultGoodlist = new ArrayList<Goodlistitem>();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        sp = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        sp_token = sp.getString("token", "");
        sp_uname = sp.getString("name", "");

        bt_tanwei = getActivity().findViewById(R.id.btn_tanwei);
        bt_qiugou = getActivity().findViewById(R.id.btn_qiugou);
        addIamge = getActivity().findViewById(R.id.find_add);

        //listView=getActivity().findViewById(R.id.lv_showGoods);
        recyclerView = getActivity().findViewById(R.id.recyclerView);
        springView = getActivity().findViewById(R.id.springView);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Log.i("Find", "已经初始化");

        //设置顶部求购按钮默认初始状态，白色
        bt_qiugou.setEnabled(false);

        sendRequst(opType);
        bt_tanwei.setOnClickListener(this);
        bt_qiugou.setOnClickListener(this);
        addIamge.setOnClickListener(this);

        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));

        springView.setListener(new SpringView.OnFreshListener() {

            //下拉刷新
            @Override
            public void onRefresh() {

                pageRefresh = 1;
                LoadMoreORRefesh = 2;
                Log.i("find", "findfragment中onRefresh: page is " + pageRefresh + "");
                sendRequst(opType);
                springView.onFinishFreshAndLoad();

            }

            //上拉加载更多
            @Override
            public void onLoadmore() {
                pageLoadMore++;
                LoadMoreORRefesh = 1;
                Log.i("find", "findfragment中onRefresh: page is " + pageLoadMore);
                sendRequst(opType);
                springView.onFinishFreshAndLoad();
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find, container, false);


    }


    @Override
    public void onClick(View v) {

        bt_qiugou.setEnabled(true);
        bt_tanwei.setEnabled(true);

        switch (v.getId()) {
            case R.id.btn_tanwei:
                // show(sp_token,opType);
                BoothORAskbuy = 1;
                sendRequst(opType);
                Type = "Stall";
                bt_tanwei.setEnabled(false);
                break;
            case R.id.btn_qiugou:

                // show(sp_token,Constants.OP_QUERY_GOODS);
                BoothORAskbuy = 2;
                sendRequst(opType);
                Type = "AskBuy";
                bt_qiugou.setEnabled(false);
                // requstBtn.getType("AskBuy");
                break;
            case R.id.find_add:

                if (Type.equals("Stall")) {
                    Intent intent = new Intent(getActivity(), TanWeiActivity.class);
                    startActivity(intent);
                    bt_tanwei.setEnabled(false);
                    Log.i("Find", "跳转到TanWeiActivity");

                } else if (Type.equals("AskBuy")) {
                    Intent intent = new Intent(getActivity(), AskBuyActivity.class);
                    startActivity(intent);
                    bt_qiugou.setEnabled(false);
                    Log.i("Find", "跳转到AskBuyActivity");

                }


        }
    }

    private void sendRequst(int opType) {

        GoodsItemBO goods = new GoodsItemBO();
        goods.setOpType(opType);
        goods.setCheckType(LoadMoreORRefesh);//
        if (LoadMoreORRefesh == 1) {
            goods.setPage(pageLoadMore);
        } else {
            goods.setPage(pageRefresh);
        }

        goods.setPageSize(pageSize);
        goods.setFlagType(BoothORAskbuy);
        Gson gson = new Gson();
        String userJsonStr = gson.toJson(goods, GoodsItemBO.class);
        Log.i("find", "find_fragment中查询商品中userJsonStr is :" + userJsonStr);

        String IP = getResources().getString(R.string.URL_HOME);
        String url = IP + "/Proj20/findshow";

        HttpUtils.sendOkHttpRequest(url, userJsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("findReturn", "获取数据失败了" + e.toString());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("findReturn", "获取数据成功了");
                Log.d("findReturn", "response.code()==" + response.code());

                final String jsonData = response.body().string();
                Log.d("findReturn", "response.body().string()==" + jsonData);

                Gson gson = new Gson();
                ReGoodsList reGoodsList = gson.fromJson(jsonData,ReGoodsList.class);

                Log.i("findReturn", "结束解析findshowResponse:" + reGoodsList);
                final int flag = reGoodsList.getFlag();
                Log.i("findReturn", "flag==" + flag);

                final int boothORaskbuy = reGoodsList.getFlagType();
                Log.i("findReturn", "find_fragment中flagType---> " + boothORaskbuy);

                newResultGoodsList = reGoodsList.getGoodsList();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setVisibility(View.VISIBLE);
                        if (flag == 200) {
                            Log.i("finfReturn", "run: success");
                            if (newResultGoodsList.size() < 0) {
                                Toast.makeText(getActivity(), "没有更多内容了哦！", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            //摊位列表的显示
                            if (boothORaskbuy == 1) {
                                saleGoodsList = newResultGoodsList;

                                //上拉加载
                                if (LoadMoreORRefesh == 1) {
                                    //将newResultGoodsList加到sale_allGoodsList的下面显示，使其按顺序排序
                                    for (int i = 0; i < newResultGoodsList.size(); i++) {
                                        boolean repeat = false;//判断加入新的List中的getGoodsID是否与旧的List中的getGoodsID是否一样一样则不重复加载
                                        for (int j = 0; j < sale_allGoodsList.size(); j++) {
                                            if (sale_allGoodsList.get(j).getGoodsID().equals(newResultGoodsList.get(i).getGoodsID())) {
                                                repeat = true;
                                                break;
                                            }
                                        }

                                        if (repeat == false) {
                                            sale_allGoodsList.add(newResultGoodsList.get(i));
                                            Log.i("findReturn", "home_fragment中allGoodsList.size() " + sale_allGoodsList.size());

                                        }

                                    }

                                    GoodsItemAdapter adapter = new GoodsItemAdapter(getContext(), sale_allGoodsList);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setScrollingTouchSlop(adapter.getItemCount() - 1);////自动滑动到底部


                                }

                                //下拉刷新
                                if (LoadMoreORRefesh == 2) {
                                    //将sale_allGoodsList加到newResultGoodsList的下面显示，使其按顺序排序
                                    for (int i = 0; i < sale_allGoodsList.size(); i++) {
                                        boolean repeat = false;//判断加入新的List中的getGoodsID是否与旧的List中的getGoodsID是否一样一样则不重复加载
                                        for (int j = 0; j < newResultGoodsList.size(); j++) {
                                            if (newResultGoodsList.get(j).getGoodsID().equals(sale_allGoodsList.get(i).getGoodsID())) {
                                                repeat = true;
                                                break;
                                            }
                                        }

                                        if (repeat == false) {
                                            sale_allGoodsList.add(newResultGoodsList.get(i));
                                            Log.i("findReturn", "home_fragment中allGoodsList.size() " + sale_allGoodsList.size());

                                        }

                                    }

                                    sale_allGoodsList = newResultGoodsList;
                                    GoodsItemAdapter adapter = new GoodsItemAdapter(getContext(), sale_allGoodsList);
                                    recyclerView.setAdapter(adapter);
                                    //  recyclerView.setScrollingTouchSlop(adapter.getItemCount() - 1);////自动滑动到底部


                                }
                            }

                            //求购列表的显示
                            if (boothORaskbuy == 2) {
                                buyGoodsList = newResultGoodsList;

                                //上拉加载
                                if (LoadMoreORRefesh == 1) {
                                    //将newResultGoodsList加到buy_allGoodsList的下面显示，使其按顺序排序
                                    for (int i = 0; i < newResultGoodsList.size(); i++) {
                                        boolean repeat = false;//判断加入新的List中的getGoodsID是否与旧的List中的getGoodsID是否一样一样则不重复加载
                                        for (int j = 0; j < buy_allGoodsList.size(); j++) {
                                            if (buy_allGoodsList.get(j).getGoodsID().equals(newResultGoodsList.get(i).getGoodsID())) {
                                                repeat = true;
                                                break;
                                            }
                                        }
                                        if (repeat == false) {
                                            buy_allGoodsList.add(newResultGoodsList.get(i));
                                            Log.i("finRenturn", "home_fragment中allGoodsList.size() " + buy_allGoodsList.size());
                                        }
                                    }
                                    GoodsItemAdapter adapter = new GoodsItemAdapter(getContext(), buy_allGoodsList);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);//自动滑动到底部

                                }
                                //下拉刷新
                                if (LoadMoreORRefesh == 2) {

                                    //将buy_allGoodsList的内容加到newResultGoodsList下面显示，使得最新的展示在第一页
                                    for (int i = 0; i < buy_allGoodsList.size(); i++) {
                                        boolean repeat = false;//判断加入新的List中的getGoodsID是否与旧的List中的getGoodsID是否一样一样则不重复加载
                                        for (int j = 0; j < newResultGoodsList.size(); j++) {
                                            if (newResultGoodsList.get(j).getGoodsID().equals(buy_allGoodsList.get(i).getGoodsID())) {
                                                repeat = true;
                                                break;
                                            }
                                        }
                                        if (repeat == false) {
                                            newResultGoodsList.add(buy_allGoodsList.get(i));
                                            Log.i("findReturn", "home_fragment中allGoodsList.size() " + buy_allGoodsList.size());
                                        }
                                    }
                                    buy_allGoodsList = newResultGoodsList;
                                    GoodsItemAdapter adapter = new GoodsItemAdapter(getContext(), buy_allGoodsList);
                                    recyclerView.setAdapter(adapter);
                                }

                            }


                        }else if (flag == 30001){
                            Toast.makeText(getActivity(), "登录信息已失效,请再次登录", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getActivity(), "查询失败！", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });


    }

//    protected void show(String token, int optype) {
//
//        AskbuyBo askbuyBo = new AskbuyBo();
//        askbuyBo.setToken(token);
//        askbuyBo.setOpType(optype);
//        Gson gson = new Gson();
//        String goodsJsonStr = gson.toJson(askbuyBo, AskbuyBo.class);
//        Log.i("TestReturn", "jsonStr is :" + goodsJsonStr);
//
//        //   String url="http://118.89.217.225:8080/Proj20/register";
//        String url = "http://192.168.1.7:8081/Proj20/buy";
//
//        sendRequst(url, goodsJsonStr);
//    }
//
//    protected void sendRequst(String url, String userJsonStr) {
//        Log.i("myLog", "userJsonStr is : " + userJsonStr);
//
//
//        OkHttpClient client = new OkHttpClient();
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//
//        RequestBody requestBody = new FormBody.Builder()
//                .add("reqJson", userJsonStr)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("TestReturn", "获取数据失败了" + e.toString());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                Log.d("TestReturn", "获取数据成功了");
//                Log.d("TestReturn", "response.code()==" + response.code());
//
//                final String s = response.body().string();
//                Log.d("TestReturn", "response.body().string()==" + s);
//
//                Gson gson = new Gson();
//                ReGoodsList goodsList = gson.fromJson(s, ReGoodsList.class);
//                Log.d("TestReturn", "goodsList is=========" + goodsList);
//
//                resultGoodlist = goodsList.getGoodsList();//后台返回的商品信息中还有 flag message，让它等于resultGoodlist，就把flag message去掉了
//                Log.d("TestReturn", "resultGoodlist is=========" + resultGoodlist);
//
//                int resposeFlag = goodsList.getFlag();
//                Log.d("TestReturn", "resposeFlag=goodsList.getFlag()======" + goodsList.getFlag());
//                if (resposeFlag == 200) {
//                    Log.d("TestReturn", "开始执行了列表显示");
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.d("TestReturn", "开始GoodsAdapterArr");
//                            GoodsAdapterArr goodsAdapterArr = new GoodsAdapterArr(getContext(), R.layout.find_tanweishow_goods_item, resultGoodlist);
//                            //  listView.setAdapter(goodsAdapterArr);
//                            Log.d("TestReturn", "结束GoodsAdapterArr");
//
//                        }
//                    });
//
//                }
//            }
//        });
//    }
}
