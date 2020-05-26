package com.example.shishengtao.fleamaerket.FindActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shishengtao.fleamaerket.BO.AskbuyBo;
import com.example.shishengtao.fleamaerket.BO.SaleBO;
import com.example.shishengtao.fleamaerket.BO.SaleResponseBo;
import com.example.shishengtao.fleamaerket.R;
import com.example.shishengtao.fleamaerket.utils.HttpUtils;
import com.google.gson.Gson;

import org.angmarch.views.NiceSpinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class AskBuyActivity extends AppCompatActivity implements View.OnClickListener  {

    //商品属性
    protected ImageView goodsimageView,backImg;
    private byte[] goodsimages;
    protected EditText et_goodsname,et_goodsprice,et_goodsquantity,et_tell,et_gooodsscrible;
    protected String goodsname,goodsquantity,goodstell,goodsscrible;
    protected Float goodsprice;
    protected Button btn_submit;


    final protected int opType=90003;
    protected SharedPreferences sp;
    protected String sp_uname,sp_token,sp_userid;

    protected int GoodType_int;
    protected  String GoodType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_askbuy_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        NiceSpinner niceSpinner=findViewById(R.id.ASKB_nice_spinner);
        List<String> dataset=new LinkedList<>(Arrays.asList( "数码", "服饰","书籍","体育用品", "电器","其它"));
        niceSpinner.attachDataSource(dataset);
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                GoodType_int=i;
                Log.i("askbuy","GoodType_int is======="+GoodType_int);
                Log.i("askbuy","Position is======="+i);

                GoodType=String .valueOf(GoodType_int+1);
                Log.i("askbuy","GoodType is======="+GoodType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //初始化
        init();

        sp=getSharedPreferences("data",MODE_PRIVATE);
        sp_token=sp.getString("token","");
        sp_uname = sp.getString("uname","");
        sp_userid = sp.getString("userid","");
        Log.i("askbuy","从sp里面读取的内容"+"uname is====="+sp_uname+"   token is====="+sp_token);

        //把图片转化为字节数组
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res,R.drawable.find_askbuyimg);
        goodsimages = Bitmap2Bytes(bmp);

        btn_submit.setOnClickListener(this);

    }

    //初始化
    private void init() {
        goodsimageView = findViewById(R.id.ASKB_img_askbuy);
        et_goodsname = findViewById(R.id.ASKB_goods_name);
        et_goodsprice = findViewById(R.id.ASKB_goods_price);
        et_goodsquantity = findViewById(R.id.ASKB_goods_quantity);
        et_tell = findViewById(R.id.ASKB_tell);
        et_gooodsscrible = findViewById(R.id.ASKB_good_describe);
        backImg = findViewById(R.id.ASKB_sale_back);
        btn_submit = findViewById(R.id.ASKB_askbuy_submit);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ASKB_sale_back:
                finish();
                break;
            case R.id.ASKB_askbuy_submit:
                goodsname = et_goodsname.getText().toString();
                goodsprice = Float.valueOf(et_goodsprice.getText().toString());
                goodsquantity = et_goodsquantity.getText().toString();
                goodstell = et_tell.getText().toString();
                issue(opType,sp_uname,sp_token,sp_userid,goodsimages,goodsname,goodsprice,GoodType,goodsquantity,goodstell);
                break;
        }

    }

    // bitmp转bytes
    private byte[] Bitmap2Bytes(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//ByteArrayOutputStream字节数组输出流
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);//quality 图像压缩率，0-100。 0 压缩100%，100意味着不压缩；//Bitmap.CompressFormat.PNG图像压缩的格式
        return baos.toByteArray();
    }

    //发布购买商品
    protected void issue(int optype,String uname,String Token,String userID,byte [] image,String goodsNme, float goodsPrice ,String goodsType,String goodsqauntity,String Tell/*,String goodsDescrible*/){
        AskbuyBo goods = new AskbuyBo();

        goods.setOpType(optype);
        goods.setUname(uname);
        goods.setUserid(userID);
        goods.setToken(Token);
        goods.setGoodsImg(image);
        goods.setGoodsName(goodsNme);
        goods.setPrice(goodsPrice);
        goods.setGoodsType(goodsType);
        goods.setQuality(Float.parseFloat(goodsqauntity));
        goods.setUnit(Tell);

        //默认参数
        goods.setSex(1);
        goods.setQq("1980856430");
        goods.setWeixin("1234557");
        goods.setQuality(Float.parseFloat(goodsquantity));

        //把Goods对象转化为Json串
        Gson gson=new Gson();
        String userJsonStr=gson.toJson(goods,AskbuyBo.class);
        Log.i("askbuy","jsonStr is :"+userJsonStr);

        //String url="http://118.89.217.225:8080/Proj20/sale";
        String IP = getResources().getString(R.string.URL_HOME);
        String url = IP + "/Proj20/buy";

        sendRequst(url,userJsonStr);

    }

    private void sendRequst(String url, String userJsonStr) {
        Log.i("askbuyreturn","userJsonStr is : "+userJsonStr);
        HttpUtils.sendOkHttpRequest(url, userJsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("askbuyreturn","获取数据失败了"+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d("askbuyreturn","获取数据成功了");
                Log.d("askbuyreturn","response.code()=="+response.code());

                final String s = response.body().string();
                Log.d("askbuyreturn","response.body().string()=="+s);

                Gson gson=new Gson();
                SaleResponseBo saleResponseBo =gson.fromJson(s,SaleResponseBo.class);
                final int responseFlage= saleResponseBo.getFlag();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseFlage==200){
                            Log.i("askbuyreturn", "responFlag is========"+responseFlage);
                            Toast.makeText(AskBuyActivity.this,"发布商品成功",Toast.LENGTH_LONG).show();
                            finish();
                        }else if(responseFlage==40001){

                            Toast.makeText(AskBuyActivity.this,"图片过大",Toast.LENGTH_LONG).show();
                            return;
                        }else if(responseFlage == 30001){
                            Toast.makeText(AskBuyActivity.this,"登录失效，请重新登录",Toast.LENGTH_LONG).show();
                            return;
                        }else if (responseFlage == 40003) {
                            Toast.makeText(AskBuyActivity.this,"登录失效，请重新登录",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });



            }
        });
    }
    /*protected void askbuy(String token,int optype) {

        AskbuyBo askbuyBo=new AskbuyBo();
        askbuyBo.setToken(token);
        askbuyBo.setOpType(optype);

        Gson gson=new Gson();
        String goodsJsonStr=gson.toJson(askbuyBo,AskbuyBo.class);
        Log.i("Test2", "jsonStr is :" + goodsJsonStr);

        //   String url="http://118.89.217.225:8080/Proj20/register";
        String url="http://118.89.217.225:8080/Proj20/buy";

        sendRequst(url,goodsJsonStr);
    }

    protected void sendRequst(String url, String userJsonStr) {
        Log.i("myLog","userJsonStr is : "+userJsonStr);

        OkHttpClient client=new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody requestBody = new  FormBody.Builder()
                .add( "reqJson",userJsonStr )
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody )
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.d("Test2","获取数据失败了"+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d("Test2","获取数据成功了");
                Log.d("Test2","response.code()=="+response.code());

                 String s = response.body().string();
                Log.d("Test2","response.body().string()=="+s);

               *//* Gson gson=new Gson();
                Type type=new TypeToken<List<Goodlistitem>>(){ }.getType();
                List<Goodlistitem> goodsLists=gson.fromJson(s,type);

                String goodsId=goodsLists.get(0).getGoodsID();
                String goodsname = goodsLists.get(0).getGoodsName();*//*

                Gson gson=new Gson();
                ReGoodsList goodsList=gson.fromJson(s,ReGoodsList.class);

                String Goods=goodsList.toString();


                Log.i("Test2","查询的商品信息========="+Goods);


               *//* Log.i("Test","商品ID is ======="+goodsId+"商品名称 is=========="+goodsname);
                Log.i("Test","Message is ======="+message);*//*

 *//*     runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(AskBuyActivity.this,FindFragment.class);

                    }
                });*//*



            }
        });
    }
*/

}
