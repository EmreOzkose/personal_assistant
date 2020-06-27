package com.example.personalassistant;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkManager {

    String TAG_DEV = "DEVELOPER";

    private static final Pattern IP_ADDRESS
            = Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))");
    private String ip_address;
    private int port;
    private boolean connection_available;
    private Activity activity;

    public interface ResponseDataCallbacks{
        void onResponseData(String response_data);
        void onResponseDataFail(Throwable error);
    }

    NetworkManager(String ip_address, int port, Activity activity){
        this.ip_address = ip_address;
        this.port = port;
        this.connection_available = false;
        this.activity = activity;
    }

    boolean checkInternetConnection(Context context){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }

        return connected;
    }

    public void send_google_cal_request(final SpeechManager speechManager){
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());

        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.POST, getPostUrl_assistant(),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Navigation Response: ", response);

                        JSONArray event_list = null;
                        try {
                            event_list = new JSONArray(response);

                            for (int i=0; i<event_list.length(); i++) {
                                Log.d("DEV", String.valueOf(event_list.get(i)));
                                JSONObject event = (JSONObject) event_list.get(i);
                                String name = event.getString("name");
                                String start_time = event.getString("start_time");
                                Log.d("DEV", name);
                                Log.d("DEV", start_time);

                                ArrayList<String> info_list = new ArrayList<>();
                                info_list.add(speechManager.translate_from_en_to_tr(start_time));
                                info_list.add(name);

                                for (String str : info_list){
                                    speechManager.say_sentence(str);
                                    TimeUnit.SECONDS.sleep(3);
                                }

                                speechManager.say_sentence("Programınız bu kadar "+speechManager.getCall_name()+". Kolaylıklar dilerim...");
                            }

                        } catch (JSONException | InterruptedException e) {
                            e.printStackTrace();
                            speechManager.say_sentence("Server'da bir sıkıntı çıktı");
                        }

                    }},
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }}
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("from_address", from_address);

                return params;
            }
        };

        queue.add(postRequest);
    }

    // === * === getter setters

    String getIpAddress(){
        return this.ip_address;
    }

    public void setIpAddress(String ip_address){
        this.ip_address = ip_address;
    }

    int getPort(){
        return this.port;
    }

    public void setPort(int port){
        this.port = port;
    }

    Pattern getIP_ADDRESS(){
        return this.IP_ADDRESS;
    }

    boolean isConnection_available() {
        return connection_available;
    }

    void setConnection_available(boolean connection_available) {
        this.connection_available = connection_available;
    }

    public String getPostUrl_assistant(){
        return "http://" + getIpAddress() + ":" + getPort() + "/personal_assistan";
    }
}
