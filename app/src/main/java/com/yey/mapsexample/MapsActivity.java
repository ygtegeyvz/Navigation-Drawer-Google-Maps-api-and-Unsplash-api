package com.yey.mapsexample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.yey.mapsexample.Model.PhotoModel;
import com.yey.mapsexample.api.Client;
import com.yey.mapsexample.api.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    List<PhotoModel> photoList;
    List<PhotoModel> NotNull = new ArrayList<>();
    List<Marker> MarkerList = new ArrayList<>();
    List<Polyline> PolyList = new ArrayList<>();
    //    ListView listView;
    String apikey = "5ed9fd3600e00d564a031c199d48ef5e1ee8f701fec29cca3bec6a49cd7f3dec";
    //  String photoid = "f9lF0bn0UnQ";
    //   private ImageView downloadedImg;
    private GoogleMap mMap;
    double latitude;
    double longitude;
    String title;
    String count = "20";
    private Marker myMarker;
    Polyline poly;
    Polyline currentPoly;
    Marker currentMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Random_Al(apikey, count);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);

        // Add a marker in Sydney and move the camera
        //  LatLng lon = new LatLng(latitude, longitude);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(lon));
    }

    private void Polyline_Ekle(LatLng firstCity, LatLng secondCity) {
        poly = mMap.addPolyline(new PolylineOptions()
                .add(firstCity).add(secondCity)
                .width(5)
                .color(Color.GRAY)
                .visible(true));
        PolyList.add(poly);
    }

    int mod = 0;

    @Override
    public boolean onMarkerClick(Marker marker) {
        mod++;
        if (mod % 2 == 1) {//Bug var bir taneyi almıyor çöz.
            for (int i = 0; i < PolyList.size(); i++) {
                if (marker.equals(MarkerList.get(i))) {
                    if (mod != 1) {
                        currentPoly.setColor(Color.GRAY);
                    }
                    PolyList.get(i).setColor(Color.RED);
                    currentMarker = MarkerList.get(i);
                    currentPoly = PolyList.get(i);
                }
            }
        }
        if (mod % 2 == 0) {
            // if (!marker.equals(currentMarker))
            for (int i = 0; i < PolyList.size(); i++) {
                PolyList.get(i).setColor(Color.GRAY);
            }
            for (int i = 0; i <PolyList.size() ; i++) {
                if (marker.equals(MarkerList.get(i))) {
                    currentPoly=PolyList.get(i);
                    currentPoly.setColor(Color.RED);
                }
            }
        }

        LatLng Sehir=marker.getPosition();
       // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Sehir,3.0f));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(Sehir));
        return true;
    }

    private void MarkerEkle(String title, double latitude, double longitude) {

        myMarker = mMap.addMarker(new MarkerOptions()
                .title(title)
            //    .icon(BitmapDescriptorFactory.fromResource(R.drawable.a))
                .draggable(true)
                .position(new LatLng(latitude, longitude))
        );

        MarkerList.add(myMarker);
    }

    private void Random_Al(String apikey, String count) {
        Retrofit retrofit = Client.getClient();
        Service api = retrofit.create(Service.class);
        Call<List<PhotoModel>> call = api.Random_Al(apikey, count);
        call.enqueue(new Callback<List<PhotoModel>>() {
            @Override
            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {

                photoList = response.body();
                for (int i = 0; i < 20; i++) {//location kontrol et
                    if (photoList.get(i).getLocationModel() != null) {
                        latitude = photoList.get(i).getLocationModel().getPosition().getLatitude();
                        longitude = photoList.get(i).getLocationModel().getPosition().getLongitude();
                        title = photoList.get(i).getLocationModel().getTitle();
                        NotNull.add(photoList.get(i));
                        MarkerEkle(title, latitude, longitude);
                    }
                }
                for (int i = 0; i < NotNull.size(); i++) {//location kontrol et
                    if (NotNull.get(i).getLocationModel() != null) {
                        {
                            if (i + 1 != NotNull.size())
                                if (NotNull.get(i + 1).getLocationModel() != null) {
                                    LatLng sehir = new LatLng(NotNull.get(i).getLocationModel().getPosition().getLatitude(), longitude = NotNull.get(i).getLocationModel().getPosition().getLongitude());
                                    LatLng sehir2 = new LatLng(NotNull.get(i + 1).getLocationModel().getPosition().getLatitude(), longitude = NotNull.get(i + 1).getLocationModel().getPosition().getLongitude());
                                    Polyline_Ekle(sehir, sehir2);
                                }
                        }
                    }
                }

                //             ImageView imgView = (ImageView) findViewById(R.id.imageView);
                //             Picasso.with(MainActivity.this)
                //                   .load(photoList.getUrlModel().getSmall())
                //                    .into(imgView)
            }

            @Override
            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }







/*
    private void Fotolari_Al(String photoid, String apikey) {
        Retrofit retrofit = Client.getClient();


        Service api = retrofit.create(Service.class);
        Call<PhotoModel> call = api.Fotolari_Al(photoid, apikey);

        call.enqueue(new Callback<PhotoModel>()
        {
            @Override
            public void onResponse(Call<PhotoModel> call, Response<PhotoModel> response) {

                photoList = response.body();
                latitude = photoList.getLocationModel().getPosition().getLatitude();
                longitude = photoList.getLocationModel().getPosition().getLongitude();
                title = photoList.getLocationModel().getTitle();
                MarkerEkle(title, latitude, longitude);

                //             ImageView imgView = (ImageView) findViewById(R.id.imageView);
                //             Picasso.with(MainActivity.this)
                //                   .load(photoList.getUrlModel().getSmall())
                //                    .into(imgView)
            }

            @Override
            public void onFailure(Call<PhotoModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

*/
}
