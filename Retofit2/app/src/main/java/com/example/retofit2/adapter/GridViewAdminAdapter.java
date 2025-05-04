package com.example.retofit2.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retofit2.R;

public class GridViewAdminAdapter extends BaseAdapter {

    private final Context context;
    private final String[] titles;
    private final int[] icons;

    public GridViewAdminAdapter(Context context, String[] titles, int[] icons) {
        this.context = context;
        this.titles = titles;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_home_item, parent, false);
        }

        ImageView icon = convertView.findViewById(R.id.item_icon);
        TextView title = convertView.findViewById(R.id.item_title);

        icon.setImageResource(icons[position]);
        title.setText(titles[position]);

        return convertView;
    }
}
