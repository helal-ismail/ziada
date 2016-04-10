package com.trianglz.ziadashow.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trianglz.ziadashow.R;

public class DetailsActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String title=getIntent().getStringExtra("title");
        TextView ts=(TextView)findViewById(R.id.titl);
        ts.setText(title);

        String artist=getIntent().getStringExtra("artist");
        TextView ta=(TextView)findViewById(R.id.artis);
        ta.setText(artist);

        ImageView img=(ImageView)findViewById(R.id.imagio);
        String ima=getIntent().getStringExtra("image");
        Picasso.with(this).load(ima).into(img);

        String list=getIntent().getStringExtra("playlist");
        TextView li=(TextView)findViewById(R.id.listaa);
        li.setText(list);
        li.setClickable(true);


    }
}

