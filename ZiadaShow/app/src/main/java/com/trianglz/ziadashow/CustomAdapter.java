package com.trianglz.ziadashow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends BaseAdapter{
    private DrawerActivity activity;
    private List<SongItem> songItems;
    private static LayoutInflater inflater=null;






    public CustomAdapter(DrawerActivity activity, List<SongItem> songItems) {
        this.activity = activity;
        this.songItems = songItems;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return songItems.size();    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return songItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
}


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.single_row, null);
        TextView tv = (TextView) convertView.findViewById(R.id.title);
        TextView tv2 = (TextView) convertView.findViewById(R.id.artist);
        SongItem s = songItems.get(position);
        holder = new ViewHolder();
        holder.img = (ImageView) convertView.findViewById(R.id.imageh);
        convertView.setTag(holder);
        tv.setText(s.getTitle());
        tv2.setText(s.getArtist());

        Picasso.with(activity).load(s.getImage()).into(holder.img);


        return convertView;
    }
    static class ViewHolder {
        // TextView titleTextView;
        ImageView img;
    }
}

