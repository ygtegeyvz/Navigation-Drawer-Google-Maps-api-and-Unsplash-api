package com.yey.deneme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v4.view.ViewPager;
import java.util.List;

import com.squareup.picasso.Picasso;
import com.yey.deneme.Model.PhotoModel;

/**
 * Created by ege on 13.12.2017.
 */

public class photo_adapter extends PagerAdapter {
    Activity activity;
    List<PhotoModel> photo;
    LayoutInflater layoutInflater;

    public photo_adapter(Activity activity, List<PhotoModel> photo) {
        this.activity = activity;
        this.photo = photo;
    }

    @Override
    public int getCount() {
        return photo.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.viewpager_item, container, false);

        final ImageView image;
        image = (ImageView) itemView.findViewById(R.id.imgBas);
        DisplayMetrics dis = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height = dis.heightPixels;
        int width = dis.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, show_Photo.class);
                i.putExtra("link",  photo.get(position).getUrlModel().getSmall());
                activity.startActivity(i);
            }
        });

        try {
            Picasso.with(activity.getApplicationContext())
                    .load(photo.get(position).getUrlModel().getSmall())
                    .into(image);
        } catch (Exception e) {

        }

        container.addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
