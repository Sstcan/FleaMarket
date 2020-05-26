package com.example.shishengtao.fleamaerket.me_Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.shishengtao.fleamaerket.MainActivity;
import com.example.shishengtao.fleamaerket.R;

public class SetActivity extends AppCompatActivity {

    RelativeLayout rl_change,rl_unlogin,rl_uppassword;
    private SharedPreferences pref;//取
    private SharedPreferences.Editor editor;//存

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_set);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        rl_change = findViewById(R.id.set_rl_change);
        rl_unlogin = findViewById(R.id.set_rl_unlogin);
        rl_uppassword = findViewById(R.id.set_rl_password);


        pref = getSharedPreferences("data",MODE_PRIVATE);
        boolean login = pref.getBoolean("login",true);
        if(login == true){
            rl_unlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(SetActivity.this).create();
                    alertDialog.setTitle("退出？");
                    alertDialog.setMessage("您真的退出本软件吗？");

                    //添加取消按钮
                    alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "不", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }

                    });


                    //添加确定按钮
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                            editor.putBoolean("login",false);
                            editor.putString("token","");
                            editor.commit();

                            Intent intent = new Intent(SetActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();
                }
            });
        }else if(login == false){
            rl_unlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(SetActivity.this,"您未登录！",Toast.LENGTH_SHORT).show();
                }
            });
        }



        rl_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetActivity.this,Personal_changeActivity.class);
                startActivity(intent);
            }
        });


        rl_uppassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetActivity.this,UpdatePswdActivity.class);
                startActivity(intent);
            }
        });


    }


}
