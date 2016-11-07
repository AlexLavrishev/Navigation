package ua.inmart.heltech.osm;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LANavigatorStartActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Main Activity";
    MapView mapView;
    LAPositionThread theardPosition;
    ArrayList<OverlayItem> anotherOverlayItemArray;
    ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay;
    Button myLoc;

    ArrayList<GeoPoint> Tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanavigator_start);
        theardPosition = new LAPositionThread(this);

        initMap();

        anotherOverlayItemArray = new ArrayList<OverlayItem>();


        myLoc = (Button) findViewById(R.id.myLoc);
        myLoc.setOnClickListener(this);

    }

    private void initTasks (){

    }


    private class GetTasks extends AsyncTask<String,Void,Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try
            {
                String u = params[0];
                URL url = new URL(u);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                JSONObject response = readStream(in);
                Log.i(TAG, "doInBackground: " + response.toString());
                urlConnection.disconnect();
            }
            catch(Exception e){
                Log.i(TAG, "doInBackground: false " + e.toString());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {

        }
    }

    public void setMyLocation() {
        System.out.println("Location is :" + theardPosition.lat + " " + theardPosition.lon);
        deleteAllOverlays();
        mapView.getOverlays().clear();
        mapView.getController().setZoom(18);
        GeoPoint position = new GeoPoint(theardPosition.lat, theardPosition.lon);
        mapView.getController().animateTo(position);
        anotherOverlayItemArray.add(new OverlayItem("My position", "asdd", position));
        anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(this, anotherOverlayItemArray, null);
        mapView.getOverlays().add(anotherItemizedIconOverlay);
        Log.i(TAG, "onClick: SIZE " + anotherOverlayItemArray.size());
    }

    private void initMap() {
        mapView = (MapView)findViewById(R.id.map); //constructor
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.getController().setZoom(15); //set initial zoom-level, depends on your need
        mapView.getController().setCenter(new GeoPoint(48.3230, 38.0312));
        mapView.setUseDataConnection(false); //keeps the mapView from loading online tiles using network connection.
    }
    private void deleteAllOverlays() {
        for (int i = anotherOverlayItemArray.size() - 1; i >= 0; i--) {
            anotherOverlayItemArray.remove(i);
        }
        anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(this, anotherOverlayItemArray, null);
        mapView.getOverlays().add(anotherItemizedIconOverlay);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myLoc:
                setMyLocation();
                break;
        }
    }

    private JSONObject readStream(InputStream in) {
        BufferedReader streamReader = null;
        JSONObject res = null;
        try {
            streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        try {
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            res = new JSONObject(responseStrBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
}
