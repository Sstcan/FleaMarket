package com.example.shishengtao.fleamaerket.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.util.Printer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shishengtao.fleamaerket.MainActivity;
import com.example.shishengtao.fleamaerket.R;
import com.example.shishengtao.fleamaerket.me_Activity.LoginActivity;
import com.example.shishengtao.fleamaerket.me_Activity.MyAskbuyActivity;
import com.example.shishengtao.fleamaerket.me_Activity.MyBoothActivity;
import com.example.shishengtao.fleamaerket.me_Activity.MycollectActivity;
import com.example.shishengtao.fleamaerket.me_Activity.PersonalInforActivity;
import com.example.shishengtao.fleamaerket.me_Activity.SetActivity;

import java.io.ByteArrayInputStream;

import static android.text.TextUtils.isEmpty;


public class MeFragment extends Fragment {

    private ImageView unloginimage,loginimag;
    private TextView username;
    private String sp_uname,sp_token;
    private SharedPreferences sp;
    private RelativeLayout rl_mybooth,rl_myaskbuy,rl_colloct,rl_set;
    private RelativeLayout rl_login,rl_unlogin;
    private String extra;




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //初始化
        init();

        sp=getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        sp_uname=sp.getString("uname","");
        Log.i("mefragment","从sp里面读取的内容"+"uname is====="+sp_uname);

//        String imagestr =  sp.getString("image","");
//        byte[] imgeBytes = Base64.decode(imagestr.getBytes(),Base64.DEFAULT);
//
//        ByteArrayInputStream bais = new ByteArrayInputStream(imgeBytes);


        //VISIBLE:0  意思是可见的
        //INVISIBLE:4 意思是不可见的，但还占着原来的空间
        //GONE:8  意思是不可见的，不占用原来的布局空间
        rl_login.setVisibility(View.GONE);
        rl_unlogin.setVisibility(View.GONE);

        if (isEmpty(extra)){
            rl_unlogin.setVisibility(View.VISIBLE);
        }else {
            rl_login.setVisibility(View.VISIBLE);
            username.setText(sp_uname);
        }

        //当进入APP后，如果之前没有退出登录状态，则一直显示你的用户名
        boolean login = sp.getBoolean("login",false);
        if(login == true){
            rl_login.setVisibility(View.VISIBLE);
            username.setText(sp_uname);
          //  loginimag.setImageDrawable(Drawable.createFromStream(bais,"image"));
            rl_unlogin.setVisibility(View.GONE);
        }else{
            rl_login.setVisibility(View.GONE);
            rl_unlogin.setVisibility(View.VISIBLE);
           // loginimag.setImageResource(R.drawable.me_my);
        }

        unloginimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        loginimag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonalInforActivity.class);
                startActivity(intent);
            }
        });

        rl_mybooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ttt","点击了booth");
                Intent intent = new Intent(getActivity(),MyBoothActivity.class);
                startActivity(intent);
            }
        });

        rl_myaskbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ttt","点击了askbuy");
                Intent intent = new Intent(getActivity(),MyAskbuyActivity.class);
                startActivity(intent);
            }
        });

        rl_colloct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MycollectActivity.class);
                startActivity(intent);
            }
        });

        rl_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SetActivity.class);
                startActivity(intent);

            }
        });

    }

    //初始化
    private void init() {

        rl_login = getActivity().findViewById(R.id.my_RLlogin);
        rl_unlogin = getActivity().findViewById(R.id.my_RLunlogin);
        unloginimage=getActivity().findViewById(R.id.my_ivunlogin);
        loginimag = getActivity().findViewById(R.id.my_ivlogin);
        username=getActivity().findViewById(R.id.my_tvloginname);


        rl_mybooth = getActivity().findViewById(R.id.MyRela_booth);
        rl_myaskbuy = getActivity().findViewById( R.id.MyRela_buy);
        rl_colloct = getActivity().findViewById(R.id.MyRela_colloct);
        rl_set = getActivity().findViewById(R.id.MyRela_set);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        extra=((MainActivity) context).getextra();//通过强转成宿主activity，就可以获取到MainActivity传递过来的数据
        Log.d("mefragment","mefragment中的extra是======="+extra);

    }

}
