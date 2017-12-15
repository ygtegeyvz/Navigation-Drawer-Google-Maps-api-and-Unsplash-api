package com.yey.deneme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

/**
 * Created by ege on 13.12.2017.
 */

public class show_Photo extends AppCompatActivity{
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        image = (ImageView) findViewById(R.id.imgMain);


        Intent i = getIntent();

        String URL = i.getStringExtra("link");

        RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.imageLayout);

        try{
            Picasso.with(getApplicationContext())
                    .load(URL)
                    .into(image);
        }catch (Exception e){

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
