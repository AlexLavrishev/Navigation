package ua.inmart.heltech.osm;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LANavigatorStartActivity extends AppCompatActivity implements View.OnClickListener , SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "Main Activity";
    MapView mapView;
    LAPositionThread threadPosition;
    ArrayList<OverlayItem> anotherOverlayItemArray;
    ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay;
    Button myLoc;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<GeoPoint> Tasks;
    ListView listView ;
    LAListAdapter adapter;
    List<LAListItemObject> list;
    int f = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanavigator_start);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        threadPosition = new LAPositionThread(this);
        initMap();
        anotherOverlayItemArray = new ArrayList<OverlayItem>();
        myLoc = (Button) findViewById(R.id.myLoc);
        myLoc.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        list = new ArrayList();
    }


    //  createNotification(56, R.drawable.ic_launcher, "New Message",
//      "There is a new message from Bob!");


    private List<LAListItemObject> initTaskList() {
        Random generator = new Random();
        int i = generator.nextInt(3) + 1;
        Log.i(TAG, "initTaskList: "+ i );


        list.add( new LAListItemObject("Task Name", "Task description. " + f, "10:56 ", threadPosition.lat, threadPosition.lon, i  ));
        return list;
    }


    public void setMyLocation() {
        System.out.println("Location is :" + threadPosition.lat + " " + threadPosition.lon);
        deleteAllOverlays();
        mapView.getOverlays().clear();
        mapView.getController().setZoom(18);
        GeoPoint position = new GeoPoint(threadPosition.lat, threadPosition.lon);
        mapView.getController().animateTo(position);
        anotherOverlayItemArray.add(new OverlayItem("My position", "", position));
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

    @Override
    public void onRefresh() {
        Log.i(TAG, "onRefresh: ");
        if (f == 0 ){
            adapter = new LAListAdapter(getBaseContext(),  initTaskList());
            listView.setAdapter(adapter);
            Log.i(TAG, "onRefresh: 1");
        }else{
            initTaskList();
            Log.i(TAG, "onRefresh: 0");

        }
        f++;
        adapter.notifyDataSetChanged();
        listView.refreshDrawableState();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
