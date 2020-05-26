package com.example.shishengtao.fleamaerket.FindActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.shishengtao.fleamaerket.R;

public abstract class Dialogchoosephoto extends Dialog implements View.OnClickListener{

    private Activity activity;
    private RelativeLayout btnPickBySelect, btnPickByTake;
    private CustomHelper customHelper;

    public Dialogchoosephoto(Activity activity) {
        super(activity, R.style.ActionSheetDialogStyle);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_dialogtakephoto_activity);

        btnPickBySelect = (RelativeLayout) findViewById(R.id.btnPickBySelect);
        btnPickByTake = (RelativeLayout) findViewById(R.id.btnPickByTake);

        btnPickBySelect.setOnClickListener(this);
        btnPickByTake.setOnClickListener(this);

        setViewLocation();
        setCanceledOnTouchOutside(true);//外部点击取消
    }

    //设置对话框的位置
    protected  void setViewLocation(){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;

        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;
        lp.y = height;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnPickBySelect:
                btnPickBySelect();
                this.cancel();
                break;
            case R.id.btnPickByTake:
                btnPickByTake();
                this.cancel();
                break;

        }

    }
    protected abstract void btnPickBySelect();
    protected abstract void btnPickByTake ();
}
