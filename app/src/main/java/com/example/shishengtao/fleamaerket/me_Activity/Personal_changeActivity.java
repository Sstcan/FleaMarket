package com.example.shishengtao.fleamaerket.me_Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shishengtao.fleamaerket.BO.User;
import com.example.shishengtao.fleamaerket.FindActivity.CustomHelper;
import com.example.shishengtao.fleamaerket.FindActivity.Dialogchoosephoto;
import com.example.shishengtao.fleamaerket.FindActivity.ReGoodsList;
import com.example.shishengtao.fleamaerket.MainActivity;
import com.example.shishengtao.fleamaerket.R;
import com.example.shishengtao.fleamaerket.meActivity_class.Constants;
import com.example.shishengtao.fleamaerket.utils.HttpUtils;
import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Personal_changeActivity extends TakePhotoActivity implements View.OnClickListener {

    private RelativeLayout rl_icon;
    private ImageView iv_back,iv_image;
    private String TAG = "TAG";
    private EditText et_nickname_show, et_qq_show, et_wichat_show, et_phone_show, et_sex_show;

    private Button button;//确定修改
    private SharedPreferences preferences;
    private int opType = Constants.OP_UPDATE_USER;
    private int sex;
    private byte[] bts_uimages;

    protected ArrayList<TImage> images;
    protected CustomHelper customHelper;//拍照或是从手机相册选择


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_personal_change);

        //对customHelper进行实例化
        View contentView = LayoutInflater.from(this).inflate(R.layout.me_activity_personal_change, null);
        setContentView(contentView);
        customHelper = CustomHelper.of(contentView);


        init();

//        //把图片转化为字节数组
//        Resources res = getResources();
//        Bitmap bmp = BitmapFactory.decodeResource(res,R.drawable.aimage);
//        bts_uimages = Bitmap2Bytes(bmp);

        //获取该用户的信息并显示其中的信息
        preferences = getSharedPreferences("data", MODE_PRIVATE);
        //昵称
        String uname = preferences.getString("uname", "");

        if (uname != null) {
            et_nickname_show.setText(uname);
        } else {
            return;
        }

        boolean login = preferences.getBoolean("login", false);
        Log.i("sp", "me_fragment中login--->" + login);

        if (login) {

            SharedPreferences preferences = getSharedPreferences("userinfo", MODE_PRIVATE);
            //QQ号
            String qq = preferences.getString("qq", "");

//            String imagestr =  preferences.getString("image","");
//            byte[] imgeBytes = Base64.decode(imagestr.getBytes(),Base64.DEFAULT);
//            ByteArrayInputStream bais = new ByteArrayInputStream(imgeBytes);
//            iv_image.setImageDrawable(Drawable.createFromStream(bais,"image"));

            Log.d("sp", "sp里面的QQ号=====" + qq);
            if (qq == null || qq.equals("")) {
                et_qq_show.setText("未填写");
            } else {
                et_qq_show.setText(qq);
            }
            //微信号
            String wichat = preferences.getString("weixin", "");
            Log.d("sp", "sp里面的微信号=====" + wichat);
            if (wichat == null || wichat.equals("")) {
                et_wichat_show.setText("未填写");
            } else {
                et_wichat_show.setText(wichat);
            }

            //电话
            String phone = preferences.getString("phone", "");
            Log.d("sp", "sp里面的电话号=====" + phone);
            if (phone == null || phone.equals("")) {
                et_phone_show.setText("未填写");
            } else {
                et_phone_show.setText(phone);
            }
        }


        iv_back.setOnClickListener(this);
        button.setOnClickListener(this);
        iv_image.setOnClickListener(this);

    }

    private void init() {

        rl_icon = findViewById(R.id.change_rl_icon);
        iv_back = findViewById(R.id.change_iv_back);
        iv_image = findViewById(R.id.change_icon_image);

        et_nickname_show = findViewById(R.id.change_et_name);
        et_qq_show = findViewById(R.id.change_et_qq_show);
        et_wichat_show = findViewById(R.id.change_et_wichat_show);
        et_phone_show = findViewById(R.id.change_et_phone_show);
        button = findViewById(R.id.change_button);

    }

    // bitmp转bytes
    private byte[] Bitmap2Bytes(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//ByteArrayOutputStream字节数组输出流
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);//quality 图像压缩率，0-100。 0 压缩100%，100意味着不压缩；//Bitmap.CompressFormat.PNG图像压缩的格式
        return baos.toByteArray();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_iv_back:
                finish();
                break;
            case R.id.change_icon_image:
                Log.i("TAG","点击修改信息里面的拍照========");
                new Dialogchoosephoto(Personal_changeActivity.this){

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

            case R.id.change_button:
                Log.i(TAG,"点击了确认修改的按钮===========");



                getData(opType);
                break;

            default:
                break;
        }


    }


    private void getData(int opType) {


        //图片
        iv_image.setDrawingCacheEnabled(true);
        // Bitmap bitmap = ((BitmapDrawable) GoodimageView.getDrawable()).getBitmap();
        // Bitmap bitmap = GoodimageView.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(iv_image.getDrawingCache());
        bts_uimages = Bitmap2Bytes(bitmap);
        iv_image.setDrawingCacheEnabled(false);

        String nickName = et_nickname_show.getText().toString().trim();//昵称
        String qqNumber = et_qq_show.getText().toString().trim();//QQ号
        String wichatNumber = et_wichat_show.getText().toString().trim();//微信号
        String phoneNumber = et_phone_show.getText().toString().trim();//电话号码

        //获取已登录用户的token
        String token = preferences.getString("token","");
        Log.i(TAG,"personal_change_activity中的token--->"+token);
        String userid = preferences.getString("userid","");
        Log.i(TAG,"personal_change_activity中的userid--->"+userid);

        User user = new User();
        user.setToken(token);
        user.setUid(userid);
        user.setOpType(opType);
        user.setUname(nickName);
        user.setQq(qqNumber);
        user.setWeixin(wichatNumber);
        user.setSex(sex);
        user.setUimage(bts_uimages);
        user.setUphone(phoneNumber);
      //  user.setUimage(bts_uimages);

        //将获取的对象转换成Json串
        Gson gson = new Gson();
        String userJsonStr = gson.toJson(user, User.class);
        Log.i("TAG", "personal_change_activity中的userJsonStr is :" + userJsonStr);
        String IP = getResources().getString(R.string.URL_HOME);
        String url=IP+"/Proj20/updateuser";

        HttpUtils.sendOkHttpRequest(url, userJsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "获取数据失败了" + e.toString());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Log.d(TAG, "获取数据成功了");
                    Log.d(TAG, "personal_change_activity中的response.code()==" + response.code());

                    final String jsonData = response.body().string();
                    Log.d(TAG, "personal_change_activity中的response.body().string()==" + jsonData);

                    Gson gson = new Gson();
                    Log.i(TAG, "开始解析jsonData");
                    ReGoodsList responseBuy = gson.fromJson(jsonData, ReGoodsList.class);
                    Log.i(TAG, "结束解析jsonData");
                    Log.i(TAG, "结束解析responseBuy:" + responseBuy);
                    //Log.i(TAG,"查询商品的列表："+ responseBuy.getGoodList().get(0));
                    final int flag = responseBuy.getFlag();
                    Log.i(TAG, "flag==" + flag);

                    final String qq = responseBuy.getQq();
                    final String weixin = responseBuy.getWeixin();
                    final String phone = responseBuy.getUphone();
                    final int sex = responseBuy.getSex();
                    final String uname = responseBuy.getUname();




                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (flag == 200) {
                                Log.i(TAG, "run: success");

                                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                                editor.putString("qq",qq);
                                editor.putString("weixin",weixin);
                                editor.putString("phone",phone);
                                editor.putInt("sex",sex);
                               // editor.putString("pictureUrl",pictureUrl);
                                editor.putString("uname",uname);
                                editor.commit();

                                Toast.makeText(Personal_changeActivity.this, "您的信息已修改成功！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Personal_changeActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }  else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Personal_changeActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });


    }

    //takephoto 接口
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
            Glide.with(this).load(new File(images.get(images.size() - 1).getCompressPath())).into(iv_image);
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
}
