package com.example.shishengtao.fleamaerket.Adapter;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

public class SpinnerAdapter extends SimpleAdapter {

    public SpinnerAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);


    }
}
