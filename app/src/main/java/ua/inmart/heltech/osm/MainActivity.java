package ua.inmart.heltech.osm;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.SimpleLocationOverlay;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends Activity implements View.OnClickListener {


    final static String TAG = "heltech";
    private Button locationBtn;
    private Button deleteBtn;
    private Button task1Btn;
    private Button task2Btn;
    private Button task3Btn;
    private TextView latt;
    private TextView lont;
    private TextView degt;
    double lat;
    double lon;
    float deg;
    MapView mapView;
    private SimpleLocationOverlay mMyLocationOverlay;
    private LocationManager locationManager;
    private LocationListener listener;

    private final String USER_AGENT = "Mozilla/5.0";
    UUID tabid;
    final String TABID = "TAB_ID";

    ArrayList<OverlayItem> anotherOverlayItemArray;
    @Override  public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getDefaults(TABID, getBaseContext()) == "" || getDefaults(TABID, getBaseContext()) == null) {
            tabid = UUID.randomUUID();
            setDefaults(TABID, ""+tabid, getBaseContext());
            Log.i(TAG, "EMPTY");
        }





        mapView = (MapView)findViewById(R.id.map); //constructor
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.getController().setZoom(15); //set initial zoom-level, depends on your need
        mapView.getController().setCenter(new GeoPoint(48.3230, 38.0312)); //This point is in Enschede, Netherlands. You should select a point in your map or get it from user's location.
        mapView.setUseDataConnection(false); //keeps the mapView from loading online tiles using network connection.

        mapView.setMinZoomLevel(12);
        locationBtn = (Button) findViewById(R.id.myLocation);
        locationBtn.setOnClickListener(this);


        task1Btn = (Button) findViewById(R.id.task);
        task1Btn.setOnClickListener(this);

        task2Btn = (Button) findViewById(R.id.task2);
        task2Btn.setOnClickListener(this);

        task3Btn = (Button) findViewById(R.id.task3);
        task3Btn.setOnClickListener(this);

        deleteBtn = (Button) findViewById(R.id.delete);
        deleteBtn.setOnClickListener(this);

        latt = (TextView) findViewById(R.id.lat);
        lont = (TextView) findViewById(R.id.lon);
        degt = (TextView) findViewById(R.id.deg);
        anotherOverlayItemArray = new ArrayList<OverlayItem>();


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                t.setText("\n " + location.getLongitude() + " " + location.getLatitude());
                lat = location.getLatitude();
                lon = location.getLongitude();
                deg = location.getBearing();
                latt.setText("" + lat);
                lont.setText("" + lon);
                degt.setText("" + deg);

                Log.i(TAG, "onLocationChanged: web request: " +
                        "\nlat " + lat +
                        "\nlon " + lon +
                        "\nimei " + getDefaults(TABID, getBaseContext()) +
                        "");
                RequestQueue queue;
                queue = Volley.newRequestQueue(getBaseContext());
                String uri = "http://heltech.inmart.ua/position.php?lat="+lat+"&lon="+lon+"&id="+getDefaults(TABID, getBaseContext());
                Log.i(TAG, "onLocationChanged: " + uri);
                StringRequest myReq = new StringRequest(Request.Method.GET,
                        uri,
                        new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response) {
                                Log.i(TAG, "onResponse: SUCCESS");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                                Log.i(TAG, "onErrorResponses: " + error.toString());
                            }
                        });
                queue.add(myReq);


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000*3, 20, listener);

        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000*3, 20, listener);


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay;
        mapView.getOverlays().clear();
        GeoPoint position;
        GeoPoint pos2;
        GeoPoint pos1;
        switch (id){
            case R.id.myLocation:
                Log.i(TAG, "onClick: Renew");
                Log.i(TAG, "onClick lat : " + lat);
                Log.i(TAG, "onClick lon : " + lon);
                Log.i(TAG, "onClick deg : " + deg);
                latt.setText("" + lat);
                lont.setText("" + lon);
                degt.setText("" + deg);
                mapView.getController().setZoom(18);
                position = new GeoPoint(lat, lon);
                mapView.getController().animateTo(position);
                anotherOverlayItemArray.add(new OverlayItem("My position", "asdd", position));
                anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(this, anotherOverlayItemArray, null);
                mapView.getOverlays().add(anotherItemizedIconOverlay);
                break;
            case R.id.task:
                pos2  = new GeoPoint(48.334954, 38.021556);
                pos1  = new GeoPoint(lat, lon);
                anotherOverlayItemArray.add(new OverlayItem("My position", "asdd", pos1));
                anotherOverlayItemArray.add(new OverlayItem("Task1", "asdd", pos2));
                anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(this, anotherOverlayItemArray, null);
                mapView.getOverlays().add(anotherItemizedIconOverlay);
                zoomValueBetweenMarkers(pos1, pos2);
                break;
            case R.id.task2:
                pos2  = new GeoPoint(48.313799, 38.011626);
                pos1  = new GeoPoint(lat, lon);
                anotherOverlayItemArray.add(new OverlayItem("My position", "asdd", pos1));
                anotherOverlayItemArray.add(new OverlayItem("Task2", "asdd", pos2));
                anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(this, anotherOverlayItemArray, null);
                mapView.getOverlays().add(anotherItemizedIconOverlay);
                zoomValueBetweenMarkers(pos1, pos2);
                break;
            case R.id.task3:
                pos2  = new GeoPoint(48.292780, 38.097577);
                pos1  = new GeoPoint(lat, lon);
                anotherOverlayItemArray.add(new OverlayItem("My position", "asdd", pos1));
                anotherOverlayItemArray.add(new OverlayItem("Task3", "asdd", pos2));
                anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(this, anotherOverlayItemArray, null);
                mapView.getOverlays().add(anotherItemizedIconOverlay);
                zoomValueBetweenMarkers(pos1, pos2);
                break;
            case R.id.delete:
                Log.i(TAG, "onClick: SIZE " + anotherOverlayItemArray.size());
                for (int i = anotherOverlayItemArray.size() - 1; i >= 0; i--) {
                    anotherOverlayItemArray.remove(i);
                }
                anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(this, anotherOverlayItemArray, null);
                mapView.getOverlays().add(anotherItemizedIconOverlay);
                break;


        }
        for (int i = 0; i < anotherOverlayItemArray.size(); i++) {
            Log.i(TAG, "Item: " + i + " " + anotherOverlayItemArray.get(i).getTitle() + " " + anotherOverlayItemArray.get(i).getPoint()) ;
        }
    }


    void zoomValueBetweenMarkers(GeoPoint first, GeoPoint second){
        double x1 = first.getLatitude();
        double x2 = second.getLatitude();
        double y1 = first.getLongitude();
        double y2 = second.getLongitude();
        double x = (x2 + x1) / 2;
        double y = (y2 + y1) / 2;
        GeoPoint pos = new GeoPoint(x, y);
        x = x2 - x1;
        y = y2 - y1;
        x = Math.sqrt(x*x+y*y);
        x1 = (x * 8.1) / 0.29524656804779076  ;
        Log.i(TAG, "zoomValueBetweenMarkers: " + x);
        Log.i(TAG, "zoomValueBetweenMarkers: " + x1);
        x = Math.abs(16 - Math.sqrt(x1*x1-x*x));
        Log.i(TAG, "zoomValueBetweenMarkers: " + x );
        mapView.getController().setZoom((int) x);
        mapView.getController().animateTo(pos);
    }
    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }



}