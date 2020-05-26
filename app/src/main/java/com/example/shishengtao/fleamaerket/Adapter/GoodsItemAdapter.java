package com.example.shishengtao.fleamaerket.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shishengtao.fleamaerket.BO.GoodsItemBO;
import com.example.shishengtao.fleamaerket.FindActivity.GoodsDetailActivity;
import com.example.shishengtao.fleamaerket.R;

import java.util.List;

public class GoodsItemAdapter extends RecyclerView.Adapter<GoodsItemAdapter.ViewHolder> {


    private List<GoodsItemBO> mygoodsList;
    private Context myContext;

    //获取布局中的实例，  这里的view是指RecyclerView的子项的最外层布局
    public static class ViewHolder extends RecyclerView.ViewHolder{

        View goodsView;
        ImageView goods_img;
        TextView goodsID;
        TextView goodsName;
        TextView goods_price;
        TextView goods_quality;
        TextView goods_unit;
        TextView goods_qq;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            goodsView = itemView;
            goods_img = itemView.findViewById(R.id.img_goodsImg);
            goodsID = itemView.findViewById(R.id.tv_goodsId);
            goodsName = itemView.findViewById(R.id.tv_goodsName);
            goods_unit = itemView.findViewById(R.id.tv_goodsUnit);
            goods_price = itemView.findViewById(R.id.tv_goodsPrice);
            goods_quality = itemView.findViewById(R.id.tv_goodsQuantity);
        }
    }

    //构造函数
    //这个方法用于把眼展示的数据源传进来，并赋值给一份全局变量mgoodsList,后续的操作都基于这个数据源
    public GoodsItemAdapter(Context context, List<GoodsItemBO> gooList) {

        mygoodsList = gooList;
        myContext = context;
    }


    //由于继承自RecyclerView.Adapter，需要重写下面的三个方法

    //此方法用于创建ViewHolder的实例,将find_tanweishow_goods_item布局加载进来，
    //并把加载出来的布局传入到构造函数中
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.find_tanweishow_goods_item,viewGroup,false);
        final ViewHolder holder = new ViewHolder(view);
        Log.d("adapter","执行Adapter");
        // //RecyclerView的点击事件，点击后进入商品详情
        //...........
        holder.goodsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                GoodsItemBO goods = mygoodsList.get(position);

                Intent intent = new Intent(view.getContext(),GoodsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("goods",goods);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    //用于对ecyclerView 的子项进行赋值,会在每个子项被滚到屏幕内的时候执行
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        //通过position参数得到当前的子项实例
        GoodsItemBO goods = mygoodsList.get(i);

        viewHolder.goodsID.setText(goods.getGoodsID());
        viewHolder.goodsName.setText(goods.getGoodsName());
        viewHolder.goods_price.setText(""+goods.getPrice());
        viewHolder.goods_quality.setText(""+goods.getQuality());
        viewHolder.goods_unit.setText(goods.getUnit());

        // 图片的操作
        byte[] bytes = goods.getGoodsImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
        viewHolder.goods_img.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return mygoodsList.size();
    }

}
