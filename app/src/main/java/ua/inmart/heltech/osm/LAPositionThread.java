package ua.inmart.heltech.osm;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Created by shadow on 07/11/16.
 */
public class LAPositionThread extends Thread{

    private static final String TAG = "LAPositionThread";
    private LocationManager locationManager;
    private LocationListener listener;
    double lat;
    double lon;
    int i = 0 ;
    Context ctx;
    public LAPositionThread(Context context) {
        // Создаём новый второй поток
        super("LAPositionThread");
        System.out.println("Создан LAPositionThread  " + this);
        ctx = context;
        locationManager = (LocationManager) ctx.getSystemService(ctx.LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                 Log.i(TAG,"\n " + location.getLongitude() + " " + location.getLatitude());
                lat = location.getLatitude();
                lon = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

        };

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000*3, 20, listener);

        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000*3, 20, listener);

        start(); // Запускаем поток
    }

    public void run() {
        try {
            while(true){
                i++;
                System.out.println("LAPositionThread Position :" + i + " " + lat + " " + lon);
                Thread.sleep(1000);
                if (i == 10 ){
                    createNotification(50, R.drawable.mypos, "New Message", "There is a new task from Petrovich", ctx);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("LAPositionThread  прерван");
        }
        System.out.println("LAPositionThread  завершён");
    }
    private void createNotification(int nId, int iconRes, String title, String body ,  Context ctx) {
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                ctx).setSmallIcon(iconRes)
                .setContentTitle(title)
                .setContentText(body);

        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(nId, mBuilder.build());
    }

}
//        theardPosition.lat
//        theardPosition.lon

//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//            }
//        }, 5000);