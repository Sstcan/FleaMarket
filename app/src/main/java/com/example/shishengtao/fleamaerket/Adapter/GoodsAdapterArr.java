//package com.example.shishengtao.fleamaerket.Adapter;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.shishengtao.fleamaerket.FindActivity.Goodlistitem;
//import com.example.shishengtao.fleamaerket.R;
//
//import java.util.List;
//
//public class GoodsAdapterArr extends ArrayAdapter<Goodlistitem> {
//
//    private int resourceId;
//
//    public GoodsAdapterArr(Context context, int textViewResourceId, List<Goodlistitem> objects) {
//        super(context, textViewResourceId, objects);
//        resourceId = textViewResourceId;
//    }
//
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        Goodlistitem goods=getItem(position);
//        Log.d("TestReturn","商品适配器里面的goods是========="+goods);
//        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
//
//        //接下来将goods对象和属性和视图控对应起来
//        ImageView img_goodsImg = view.findViewById(R.id.img_goodsImg);
//        TextView tv_goodsId = view.findViewById(R.id.tv_goodsId);
//        TextView tv_goodsName = view.findViewById(R.id.tv_goodsName);
//        TextView tv_goodsUnit = view.findViewById(R.id.tv_goodsUnit);
//        TextView tv_goodsPrice = view.findViewById(R.id.tv_goodsPrice);
//        TextView tv_goodsQuantity = view.findViewById(R.id.tv_goodsQuantity);
//
//        tv_goodsId.setText(goods.getGoodsID());
//        tv_goodsName.setText(goods.getGoodsName());
//        tv_goodsUnit.setText(goods.getUnit());
//        tv_goodsPrice.setText(""+goods.getPrice());
//        tv_goodsQuantity.setText(""+goods.getQuality());
//
//        // 图片的操作
//        byte[] bytes = goods.getGoodsImg();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
//        img_goodsImg.setImageBitmap(bitmap);
//
//        return view;
//
//    }
//
//
//}
