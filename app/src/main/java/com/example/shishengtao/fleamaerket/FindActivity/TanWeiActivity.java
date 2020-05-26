package com.example.shishengtao.fleamaerket.FindActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shishengtao.fleamaerket.BO.SaleResponseBo;
import com.example.shishengtao.fleamaerket.BO.SaleBO;
import com.example.shishengtao.fleamaerket.R;
import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import org.angmarch.views.NiceSpinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TanWeiActivity extends TakePhotoActivity implements  View.OnClickListener {

    //商品属性
    protected ImageView GoodimageView,backImg;
    private byte[] goodsimages;
    protected EditText Goodsname,Goodsprice,Goodssort,tell,Goodsdescrible,Goodsquantity;
    protected String goodsname,goodssort,goodsdescrible,goodstell,goodsquantity;
    protected Float goodsprice;
    protected Button submit;

    protected String GoodType;
    protected int goodsType;//商品所属类
    protected int GoodType_int;
    protected int opType=90003;

    protected String sp_uname,sp_token,sp_userid;
    protected SharedPreferences sp;

    protected ArrayList<TImage> images;
    protected CustomHelper customHelper;//拍照或是从手机相册选择




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_tanwei_activity);

        //对customHelper进行实例化
        View contentView = LayoutInflater.from(this).inflate(R.layout.find_tanwei_activity, null);
        setContentView(contentView);
        customHelper = CustomHelper.of(contentView);

        //下拉选择
        NiceSpinner niceSpinner =findViewById(R.id.Stall_nice_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList( "数码", "服饰","书籍","体育用品", "电器","其它"));
        niceSpinner.attachDataSource(dataset);
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GoodType_int=position;
                Log.i("Test","GoodType_int is======="+GoodType_int);
                Log.i("Test","Position is======="+position);

                GoodType=String .valueOf(GoodType_int+1);
                Log.i("Test","GoodType is======="+GoodType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        //把图片转化为字节数组
//        Resources res = getResources();
//        Bitmap bmp = BitmapFactory.decodeResource(res,R.drawable.aimage);
//        goodsimages = Bitmap2Bytes(bmp);


        //对商品的信息实例化
        GoodimageView = findViewById(R.id.btn_click_showbottomdialog);
        backImg = findViewById(R.id.sale_back);
        Goodsname = findViewById(R.id.goods_name);
        Goodsprice = findViewById(R.id.goods_price);
      // Goodssort=findViewById(R.id.Stall_nice_spinner);
        Goodsquantity = findViewById(R.id.goods_quantity);
        tell = findViewById(R.id.tell);
        Goodsdescrible = findViewById(R.id.good_describe);
        submit = findViewById(R.id.askbuy_submit);


        GoodimageView.setOnClickListener(this);
        submit.setOnClickListener(this);
        backImg.setOnClickListener(this);

        //从getSharedPreferences里面获得 uname 和token
        sp=getSharedPreferences("data",Context.MODE_PRIVATE);
        sp_uname=sp.getString("uname","");
        sp_token=sp.getString("token","");
        sp_userid=sp.getString("userid","");

        Log.i("Test","从sp里面读取的内容"+"uname is====="+sp_uname+"   token is====="+sp_token);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sale_back:
                finish();
                break;
            case R.id.btn_click_showbottomdialog:

                new Dialogchoosephoto(TanWeiActivity.this) {

                    @Override
                    protected void btnPickBySelect() {

                        customHelper.onClick("selectphoto", getTakePhoto());
                    }

                    @Override
                    protected void btnPickByTake() {
                        customHelper.onClick("takephoto", getTakePhoto());
                    }
                }.show();
                break;
            case R.id.askbuy_submit:

                goodsname=Goodsname.getText().toString();
                goodsprice= Float.valueOf(Goodsprice.getText().toString());
               // goodssort=Goodssort.getText().toString();
                goodstell=tell.getText().toString();
                goodsquantity = Goodsquantity.getText().toString();

                //图片
                GoodimageView.setDrawingCacheEnabled(true);
               // Bitmap bitmap = ((BitmapDrawable) GoodimageView.getDrawable()).getBitmap();
               // Bitmap bitmap = GoodimageView.getDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(GoodimageView.getDrawingCache());
                goodsimages = Bitmap2Bytes(bitmap);
                GoodimageView.setDrawingCacheEnabled(false);



                Log.i("Test","商品名称是======"+goodsname);


                issue(opType,sp_uname,sp_token,sp_userid,goodsimages,goodsname,goodsprice,GoodType,goodsquantity,goodstell);

        }

    }

   // bitmp转bytes
    private byte[] Bitmap2Bytes(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//ByteArrayOutputStream字节数组输出流
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);//quality 图像压缩率，0-100。 0 压缩100%，100意味着不压缩；//Bitmap.CompressFormat.PNG图像压缩的格式
        return baos.toByteArray();
    }

    //byte[] b = getIntent().getByteArrayExtra("bitmap");
    //Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

    //TakePhotoActivity的接口
    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        images=result.getImages();
        showImg(images);
    }

    private void showImg(ArrayList<TImage> images) {

        for (int i = 0, j = images.size(); i < j - 1; i += 2) {
//            View view = LayoutInflater.from(this).inflate(R.layout.image_show, null);
//            ImageView imageView1 = (ImageView) view.findViewById(R.id.imgShow1);
//            ImageView imageView2 = (ImageView) view.findViewById(R.id.imgShow2);
//            Glide.with(this).load(new File(images.get(i).getCompressPath())).into(imageView1);
//            Glide.with(this).load(new File(images.get(i + 1).getCompressPath())).into(imageView2);
//            linearLayout.addView(view);
        }
        //处理一张图片
        if (images.size() % 2 == 1) {
//            View view = LayoutInflater.from(this).inflate(R.layout.image_show, null);
//            ImageView imageView1 = (ImageView) view.findViewById(R.id.imgShow1);
            Glide.with(this).load(new File(images.get(images.size() - 1).getCompressPath())).into(GoodimageView);
//            linearLayout.addView(view);
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    //发送请求
    protected void issue(int optype,String uname,String Token,String userID,byte [] image,String goodsNme, float goodsPrice ,String goodsType,String goodsqauntity,String Tell/*,String goodsDescrible*/) {

        SaleBO goods= new SaleBO();

        String uuid = UUID.randomUUID().toString();

        goods.setOpType(optype);
        goods.setGoodsID(uuid);
        goods.setGoodsImg(image);
        goods.setGoodsName(goodsNme);
        goods.setPrice(goodsPrice);
        goods.setGoodsType(goodsType);
        goods.setUname(uname);
        goods.setToken(Token);
        goods.setUserid(userID);
        goods.setUnit(Tell);


        //默认参数
        goods.setSex(1);
        goods.setQq("1980856430");
        goods.setWeixin("1234557");
        goods.setQuality(Float.parseFloat(goodsquantity));

        goods.setUphone("189283665443");



        //把Goods对象转化为Json串
        Gson gson=new Gson();
        String userJsonStr=gson.toJson(goods,SaleBO.class);
        Log.i("Test","jsonStr is :"+userJsonStr);

        //String url="http://118.89.217.225:8080/Proj20/sale";
        String IP = getResources().getString(R.string.URL_HOME);
        String url = IP + "/Proj20/sale";


        sendRequst(url,userJsonStr);
    }

    protected void sendRequst(String url, String userJsonStr) {
        Log.i("Test1","userJsonStr is : "+userJsonStr);

        OkHttpClient client=new OkHttpClient();
        // MediaType JSON = MediaType.parse("application/json; charset=utf-8");

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

                Gson gson=new Gson();
                SaleResponseBo saleResponseBo =gson.fromJson(s,SaleResponseBo.class);
                int responseFlage= saleResponseBo.getFlag();

                if (responseFlage==200){
                    Log.i("test", "responFlag is========"+responseFlage);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TanWeiActivity.this,"发布商品成功",Toast.LENGTH_LONG).show();
                            TanWeiActivity.this.finish();
                        }
                    });
                }else if(responseFlage==40001){

                   Toast.makeText(TanWeiActivity.this,"图片过大",Toast.LENGTH_LONG).show();
                }


            }
        });
    }

}
