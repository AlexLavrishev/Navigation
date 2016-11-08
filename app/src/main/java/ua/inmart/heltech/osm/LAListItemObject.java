package ua.inmart.heltech.osm;

/**
 * Created by shadow on 08/11/16.
 */
public class LAListItemObject {

    String title;
    String des;
    String time;
    double lat;
    double lon;
    int i;

    public LAListItemObject(String title, String des, String time, double lat, double lon, int i ) {
        this.title = title;
        this.des = des;
        this.time = time;
        this.lat = lat;
        this.lon = lon;
        this.i = i;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;

    }
    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
