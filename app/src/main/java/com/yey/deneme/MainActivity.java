package com.yey.deneme;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;
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
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
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
    String created_at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        Random_Al(apikey, count);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //    fab.setOnClickListener(new View.OnClickListener() {
        //  @Override
        //   public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                  .setAction("Action", null).show();
        // }
        //  });
//appbarmainden sildim buraları
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //       ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        //             this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //    drawer.addDrawerListener(toggle);
        //  toggle.syncState();

        //  NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                    place_photo(i);
                    place_Created_at(i);
                    if (NotNull.get(i).getLocationModel().getTitle() != null) {
                        place_title(i);
                    }
                    if (NotNull.get(i).getDescription() != null) {
                        place_description(i);
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
                    place_photo(i);
                    place_Created_at(i);
                    if (NotNull.get(i).getLocationModel().getTitle() != null) {
                        place_title(i);
                    }
                    if (NotNull.get(i).getDescription() != null) {
                        place_description(i);
                    }

                }
            }
        }

        LatLng Sehir = marker.getPosition();
        mMap.animateCamera(CameraUpdateFactory.newLatLng(Sehir));

        return true;
    }

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
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
