package com.cyber.events;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class InternetConnection {
    private Context context;
    private String url;

    public InternetConnection(Context context, String url)
    {
        this.context = context;
        this.url = url;
    }

    public boolean isConnected(){
        ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null) return false;

        NetworkInfo info = cm.getActiveNetworkInfo();

        if(info != null && info.isConnected())
        {
            if (info.isAvailable())
                return true;
        }
        return false;
    }

    JSONObject getData(String profile, double lat, double lon, double radius, String lastUpdate, String authCode){
        if(isConnected()){
            try{
                URL fullUrl = new URL(this.url+"index.php?profile="+ URLEncoder.encode(profile,"UTF-8")
                            + "&lon="+URLEncoder.encode(String.valueOf(lon),"UTF-8")
                            + "&lat="+URLEncoder.encode(String.valueOf(lat),"UTF-8")
                            + "&radius="+URLEncoder.encode(String.valueOf(radius),"UTF-8")
                            + "&lastupdate="+URLEncoder.encode(lastUpdate,"UTF-8")
                            + "&ac="+URLEncoder.encode(authCode,"UTF-8"));

                HttpURLConnection conn = (HttpURLConnection) fullUrl.openConnection();
                conn.setConnectTimeout(30000);
                conn.connect();
                // handle the response
                int status = conn.getResponseCode();

                // If response is not success
                if (status == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = r.readLine();
                    if(line.charAt(0) == '(' || line.charAt(line.length()-1) == ')')
                        line = line.substring(1, line.length()-1);
                    conn.disconnect();
                    return new JSONObject(line);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public JSONObject getNearest(String profile, double lat, double lon, String authCode){
        if(isConnected()){
            try{
                URL fullUrl = new URL(this.url+"getnearest.php?profile="+ URLEncoder.encode(profile,"UTF-8")
                        + "&lon="+URLEncoder.encode(String.valueOf(lon),"UTF-8")
                        + "&lat="+URLEncoder.encode(String.valueOf(lat),"UTF-8")
                        + "&ac="+URLEncoder.encode(authCode,"UTF-8"));

                HttpURLConnection conn = (HttpURLConnection) fullUrl.openConnection();
                conn.setConnectTimeout(30000);
                conn.connect();
                // handle the response
                int status = conn.getResponseCode();

                // If response is not success
                if (status == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = r.readLine();
                    if(line.charAt(0) == '(' || line.charAt(line.length()-1) == ')')
                        line = line.substring(1, line.length()-1);
                    conn.disconnect();
                    return new JSONObject(line);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    JSONObject getDataForPlaces(String[] lonlat, String color, String authCode, String lastUpdate){
        if(isConnected()){
            try{
                String lonlats = "";
                for (String lonlatItem : lonlat) {
                    lonlats = lonlats.concat("&lonlat[]="+URLEncoder.encode(lonlatItem,"UTF-8"));
                }

                if(lonlats.length()>0){
                    lonlats = lonlats.substring(1);
                }
                URL fullUrl = new URL(this.url+"getsome.php?"
                        + lonlats
                        +"&color=" + URLEncoder.encode(color,"UTF-8")
                        +"&ac=" + URLEncoder.encode(authCode,"UTF-8")
                        + "&lastupdate=" + URLEncoder.encode(lastUpdate,"UTF-8"));


                HttpURLConnection conn = (HttpURLConnection) fullUrl.openConnection();
                conn.setConnectTimeout(30000);
                conn.connect();
                // handle the response
                int status = conn.getResponseCode();

                // If response is not success
                if (status == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = r.readLine();
                    if(line.charAt(0) == '(' || line.charAt(line.length()-1) == ')')
                        line = line.substring(1, line.length()-1);
                    conn.disconnect();
                    return new JSONObject(line);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    JSONObject searchPlace(String placeName, String authCode){
        if(isConnected()){
            try{
                URL fullUrl = new URL(this.url + "search.php?search_text="+ URLEncoder.encode(placeName,"UTF-8")
                        + "&ac="+URLEncoder.encode(authCode,"UTF-8"));

                HttpURLConnection conn = (HttpURLConnection) fullUrl.openConnection();
                conn.setConnectTimeout(30000);
                conn.connect();
                // handle the response
                int status = conn.getResponseCode();

                // If response is not success
                if (status == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = r.readLine();
                    if(line.charAt(0) == '(' || line.charAt(line.length()-1) == ')')
                        line = line.substring(1, line.length()-1);
                    conn.disconnect();
                    return new JSONObject(line);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getEvents() {
        if(isConnected()){
            try{
                URL fullUrl = new URL(this.url + "/getEvents");

                HttpURLConnection conn = (HttpURLConnection) fullUrl.openConnection();
                conn.setConnectTimeout(30000);
                conn.connect();
                // handle the response
                int status = conn.getResponseCode();

                // If response is not success
                if (status == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = r.readLine();
                    if(line.charAt(0) == '(' || line.charAt(line.length()-1) == ')')
                        line = line.substring(1, line.length()-1);
                    conn.disconnect();
                    return line;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
