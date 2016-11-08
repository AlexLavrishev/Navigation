package ua.inmart.heltech.osm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

/**
 * Created by shadow on 08/11/16.
 */
public class LAListAdapter extends BaseAdapter {
    private List<LAListItemObject> list;
    private LayoutInflater layoutInflater;

    public LAListAdapter(Context ctx , List<LAListItemObject> list) {
        this.list = list;
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = layoutInflater.inflate(R.layout.list_item, parent, false);
        }
        LAListItemObject item = getLAListItemObject(position);

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView des = (TextView) view.findViewById(R.id.des);
        TextView lat = (TextView) view.findViewById(R.id.lat);
        TextView lon = (TextView) view.findViewById(R.id.lon);
        TextView time = (TextView) view.findViewById(R.id.time);
        ImageView state = (ImageView) view.findViewById(R.id.imageStateTask);

        title.setText(item.getTitle());
        des.setText(item.getDes());
        lat.setText(""+item.getLat());
        lon.setText("" + item.getLon());
        time.setText(item.getTime());

        switch (item.getI()){
            case 1:
                state.setImageResource(R.drawable.inwork);
                break;
            case 2:
                state.setImageResource(R.drawable.mypos);
                break;
            case 3:
                state.setImageResource(R.drawable.task);
                break;
        }



        return view;
    }

    public LAListItemObject getLAListItemObject(int position) {
        return (LAListItemObject) getItem(position);
    }
}
