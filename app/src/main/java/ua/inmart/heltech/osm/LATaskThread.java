package ua.inmart.heltech.osm;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by shadow on 09/11/16.
 */
public class LATaskThread  extends Thread {
    private static final String TAG = "Task";
    int i = 0;
    Context ctx;
    JSONArray taskJsonArr;
    LADialogFragment dialog;
    public LATaskThread(Context context) {
        super("LATaskThread");
        System.out.println("Создан LATaskThread  " + this);
        ctx = context;
        start(); // Запускаем поток
        dialog = new LADialogFragment();
    }

    public void run() {
        try {
            while(true){
                i++;
                Thread.sleep(10000);
                try
                {
                    String u = "http://heltech.inmart.ua/task.php";
                    URL url = new URL(u);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    JSONObject response = readStream(in);

                    String res = response.optString("response");
                    Log.i(TAG, "run: " + res);
                    if ( res.equals("true")){
                        String id = response.optString("id");

                        Log.i(TAG, "true run: " + response.toString());
                        taskJsonArr = response.getJSONArray("tasks");
                        Activity activity = (Activity) ctx;
                        if (!dialog.isAdded()){
                            dialog = LADialogFragment.newInstance(id, taskJsonArr.length());
                            FragmentManager fm = activity.getFragmentManager();
                            dialog.show(fm, "");
                        }else{
                            Log.i(TAG, "run: Activity is already added");
                        }

                    }else{
                        taskJsonArr = null;
                        Log.i(TAG, "false run: " + response.toString());

                    }

                }
                catch(Exception e){
                    Log.i(TAG, "doInBackground: false " + e.toString());
                }

            }
        } catch (InterruptedException e) {
            System.out.println("LATaskThread  прерван");
        }
        System.out.println("LATaskThread  завершён");
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
