package com.yey.deneme;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.ViewPager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.yey.deneme.Model.PhotoModel;
import com.yey.deneme.api.Client;
import com.yey.deneme.api.Service;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    List<PhotoModel> NotNull = new ArrayList<>();
    List<Marker> MarkerList = new ArrayList<>();
    List<Polyline> PolyList = new ArrayList<>();
    String apikey = "5ed9fd3600e00d564a031c199d48ef5e1ee8f701fec29cca3bec6a49cd7f3dec";
    private GoogleMap mMap;
    double latitude;
    double longitude;
    String title;
    String count = "20";
    private Marker myMarker;
    Polyline poly;
    ImageView imgAna;
    TextView txtIcerik,txtBaslik,txtTarih,txtTitle;
    Button btnPrev,btnNext;
    Service services;
    List<PhotoModel> photoList;
    ViewPager viewPager;
    ProgressDialog mDialog;
    int pagerIndex = 0;
photo_adapter adapter;
    private Polyline currentPoly;
    private Marker currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(slideOffset == 1f){
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                }
                super.onDrawerSlide(drawerView,slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if(newState == DrawerLayout.STATE_DRAGGING){
                    Log.d("Dragging","State changed");
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                super.onDrawerClosed(drawerView);
            }



        };
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);



        imgAna = (ImageView) findViewById(R.id.imgBas);
        txtIcerik = (TextView) findViewById(R.id.txtIcerik);
        txtBaslik = (TextView) findViewById(R.id.txtBaslik);
        txtTarih = (TextView) findViewById(R.id.txtTarih);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        viewPager = findViewById(R.id.viewPager);
        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnNext = (Button) findViewById(R.id.btnNext);
        mDialog = new ProgressDialog(MainActivity.this);

        services = Client.getClient().create(Service.class);


        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pagerIndex > 0){
                    pagerIndex--;
                    viewPager.setCurrentItem(pagerIndex);
                    txtIcerik.setText(photoList.get(pagerIndex).getDescription());
                    txtTarih.setText(photoList.get(pagerIndex).getCreated_at().substring(0,10));
                    txtTitle.setText(photoList.get(pagerIndex).getLocationModel().getTitle());
                    Double lat = Double.parseDouble(String.valueOf(photoList.get(pagerIndex).getLocationModel().getPosition().getLatitude()));
                    Double lng = Double.parseDouble(String.valueOf(photoList.get(pagerIndex).getLocationModel().getPosition().getLongitude()));
                    updateMaps(new LatLng(lat,lng));
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pagerIndex < photoList.size()-1){
                    pagerIndex++;
                    viewPager.setCurrentItem(pagerIndex);
                    txtIcerik.setText(photoList.get(pagerIndex).getDescription());
                    txtTarih.setText(photoList.get(pagerIndex).getCreated_at().substring(0,10));
                    txtTitle.setText(photoList.get(pagerIndex).getLocationModel().getTitle());
                    Double lat = Double.parseDouble(String.valueOf(photoList.get(pagerIndex).getLocationModel().getPosition().getLatitude()));
                    Double lng = Double.parseDouble(String.valueOf(photoList.get(pagerIndex).getLocationModel().getPosition().getLongitude()));
                    updateMaps(new LatLng(lat,lng));
                }
            }
        });
        Random_Al(apikey, count);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                pagerIndex = position;
                txtIcerik.setText(photoList.get(pagerIndex).getDescription());
                txtTarih.setText(photoList.get(pagerIndex).getCreated_at().substring(0,10));
                txtTitle.setText(photoList.get(pagerIndex).getLocationModel().getTitle());
                Double lat = Double.parseDouble(String.valueOf(photoList.get(pagerIndex).getLocationModel().getPosition().getLatitude()));
                Double lng = Double.parseDouble(String.valueOf(photoList.get(pagerIndex).getLocationModel().getPosition().getLongitude()));
                updateMaps(new LatLng(lat,lng));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void updateImageView(String markerTag){
        String split = markerTag.substring(1,markerTag.length());
        int clickedID = Integer.parseInt(split);

        pagerIndex = clickedID;
        viewPager.setCurrentItem(pagerIndex);
        txtIcerik.setText(photoList.get(pagerIndex).getDescription());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(!drawer.isDrawerOpen(GravityCompat.START)){
            drawer.openDrawer(GravityCompat.START);
        }

    }

    public void updateMaps(LatLng position){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(5f).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
/*
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
*/
    int mod = 0;

   /*
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
            //        place_photo(i);
            //        place_Created_at(i);
                    if (NotNull.get(i).getLocationModel().getTitle() != null) {
              //          place_title(i);
                    }
                    if (NotNull.get(i).getDescription() != null) {
               //         place_description(i);
                    }
                }
            }
        }
        if (mod % 2 == 0) {
            for (int i = 0; i < PolyList.size(); i++) {
                PolyList.get(i).setColor(Color.GRAY);
            }
            for (int i = 0; i < PolyList.size(); i++) {
                if (marker.equals(MarkerList.get(i))) {
                    currentPoly = PolyList.get(i);
                    currentPoly.setColor(Color.RED);
                 //   place_photo(i);
                  //  place_Created_at(i);
                    if (NotNull.get(i).getLocationModel().getTitle() != null) {
                 //       place_title(i);
                    }
                    if (NotNull.get(i).getDescription() != null) {
                    //    place_description(i);
                    }

                }
            }
        }

        LatLng Sehir = marker.getPosition();
        mMap.animateCamera(CameraUpdateFactory.newLatLng(Sehir));

        return true;
    }
/*
    public void place_Created_at(int i) {
        TextView textView = (TextView) findViewById(R.id.tarih);
        created_at = NotNull.get(i).getCreated_at();
        created_at = created_at.substring(0, 10);
        textView.setText(created_at);
    }

    public void place_title(int i) {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(NotNull.get(i).getLocationModel().getTitle());
    }

    public void place_photo(int positionMarker) {
        ImageView imgView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(MainActivity.this)
                .load(NotNull.get(positionMarker).getUrlModel().getSmall())
                .into(imgView);
    }

    public void place_description(int i) {
      //  TextView textView = (TextView) findViewById(R.id.description);
     //   textView.setText(NotNull.get(i).getDescription());
    }
*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                updateImageView(marker.getId());
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
                            //        place_photo(i);
                            //        place_Created_at(i);
                            if (NotNull.get(i).getLocationModel().getTitle() != null) {
                                //          place_title(i);
                            }
                            if (NotNull.get(i).getDescription() != null) {
                                //         place_description(i);
                            }
                        }
                    }
                }
                if (mod % 2 == 0) {
                    for (int i = 0; i < PolyList.size(); i++) {
                        PolyList.get(i).setColor(Color.GRAY);
                    }
                    for (int i = 0; i < PolyList.size(); i++) {
                        if (marker.equals(MarkerList.get(i))) {
                            currentPoly = PolyList.get(i);
                            currentPoly.setColor(Color.RED);
                            //   place_photo(i);
                            //  place_Created_at(i);
                            if (NotNull.get(i).getLocationModel().getTitle() != null) {
                                //       place_title(i);
                            }
                            if (NotNull.get(i).getDescription() != null) {
                                //    place_description(i);
                            }

                        }
                    }
                }

                LatLng Sehir = marker.getPosition();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(Sehir));

                return true;
             //   return false;
            }
        });
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
    private void Random_Al(final String apikey, String count) {

        Retrofit retrofit = Client.getClient();
        Service api = retrofit.create(Service.class);
        Call<List<PhotoModel>> call = api.Random_Al(apikey, count);
        call.enqueue(new Callback<List<PhotoModel>>() {
            @Override
            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {
                if(response.body() == null){
                    mDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Veri alınamadı.", Toast.LENGTH_SHORT).show();
                    return;
                }
                photoList = response.body();
                if(photoList.size() <= 0){
                    mDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Bir hatadan dolayı resim bilgisi alınamadı.", Toast.LENGTH_SHORT).show();
                    return;
                }


                for (int i = 0; i < photoList.size() ; i++) {
                    if(photoList.get(i).getLocationModel() == null){
                        photoList.remove(i);
                        i--;
                    }else if(photoList.get(i).getLocationModel().getPosition() == null){
                        photoList.remove(i);
                        i--;
                    }
                    else if(photoList.get(i).getLocationModel().getTitle()==null)
                    {
                        photoList.remove(i);
                        i--;
                    }
                }

                for (int i = 0; i < photoList.size(); i++) {//location kontrol et
                    if (photoList.get(i).getLocationModel() != null) {
                        latitude = photoList.get(i).getLocationModel().getPosition().getLatitude();
                        longitude = photoList.get(i).getLocationModel().getPosition().getLongitude();
                        title = photoList.get(i).getLocationModel().getTitle();
                        //    created_at=photoList.get(i).getCreated_at();
                        NotNull.add(photoList.get(i));
                        MarkerEkle(title, latitude, longitude);
                        //    ImageView imgView = (ImageView) findViewById(R.id.imageView);
                        //       Picasso.with(MainActivity.this)
                        //             .load(photoList.get(i).getUrlModel().getSmall())
                        //           .into(imgView);

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

                adapter = new photo_adapter(MainActivity.this,photoList);
                viewPager.setAdapter(adapter);

                txtIcerik.setText(photoList.get(pagerIndex).getDescription());
                txtTarih.setText(photoList.get(pagerIndex).getCreated_at().substring(0,10));
                txtTitle.setText(photoList.get(pagerIndex).getLocationModel().getTitle());
            }

            @Override
            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Polyline_Ekle(LatLng firstCity, LatLng secondCity) {
        poly = mMap.addPolyline(new PolylineOptions()
                .add(firstCity).add(secondCity)
                .width(5)
                .color(Color.GRAY)
                .visible(true));
        PolyList.add(poly);
    }
}
