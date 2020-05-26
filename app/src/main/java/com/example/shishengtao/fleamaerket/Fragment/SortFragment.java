package com.example.shishengtao.fleamaerket.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.shishengtao.fleamaerket.R;
import com.example.shishengtao.fleamaerket.sort_Activity.SortDetailActivity;

public class SortFragment extends Fragment {

    private RelativeLayout rl_mobile;
    private RelativeLayout rl_clothes;
    private RelativeLayout rl_books;
    private RelativeLayout rl_sports_goods;
    private RelativeLayout rl_electric;
    private RelativeLayout rl_others;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

        rl_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SortDetailActivity.class);
                intent.putExtra("Commoditype","分类：数码");
                intent.putExtra("classifyType","1");
                startActivity(intent);
            }
        });


        rl_clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SortDetailActivity.class);
                intent.putExtra("Commoditype","分类：服饰");
                intent.putExtra("classifyType","2");
                startActivity(intent);
            }
        });


        rl_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SortDetailActivity.class);
                intent.putExtra("Commoditype","分类：书籍");
                intent.putExtra("classifyType","3");
                startActivity(intent);
            }
        });


        rl_sports_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SortDetailActivity.class);
                intent.putExtra("Commoditype","分类：体育用品");
                intent.putExtra("classifyType","4");
                startActivity(intent);
            }
        });


        rl_electric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SortDetailActivity.class);
                intent.putExtra("classifyType","5");
                intent.putExtra("Commoditype","分类：电器");

                startActivity(intent);
            }
        });


        rl_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SortDetailActivity.class);
                intent.putExtra("Commoditype","分类：其它");
                intent.putExtra("classifyType","6");
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sort, container, false);
    }

    //初始化控件
    public void init(){
        rl_mobile = getView().findViewById(R.id.rl_mobile);
        rl_clothes = getView().findViewById(R.id.rl_clothes);
        rl_books = getView().findViewById(R.id.rl_books);
        rl_sports_goods = getView().findViewById(R.id.rl_sports_goods);
        rl_electric = getView().findViewById(R.id.rl_electric);
        rl_others = getView().findViewById(R.id.rl_others);
    }
}
