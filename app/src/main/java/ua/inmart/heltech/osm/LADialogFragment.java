package ua.inmart.heltech.osm;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

/**
 * Created by shadow on 09/11/16.
 */
public class LADialogFragment  extends DialogFragment implements DialogInterface.OnClickListener {
    final String LOG_TAG = "myLogs";

    public static LADialogFragment newInstance(String id , int count) {

        LADialogFragment frag = new LADialogFragment();

        Bundle args = new Bundle();

        Log.i("asd", "newInstance: " + id );

        args.putInt("count", count);
        args.putString("id", id);

        frag.setArguments(args);

        return frag;

    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String id = getArguments().getString("id", "");
        int count = getArguments().getInt("count", 0);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("Новое задание.").setPositiveButton("Принять", this)
                .setNegativeButton("Отменить", this)
                .setMessage(id +"\nВсего новых заданий " + count);
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        Log.i(LOG_TAG, "onClick: " + which);
        String id = getArguments().getString("id", "");
        switch (which){
            case -1:
                Log.i(LOG_TAG, "onClick: OK " + id);
                break;
            case -2:
                Log.i(LOG_TAG, "onClick: Cancel");

                break;
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog: onCancel");
    }
}
