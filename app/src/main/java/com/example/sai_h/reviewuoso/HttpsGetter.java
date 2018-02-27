package com.example.sai_h.reviewuoso;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sai_h on 27-02-2018.
 */

public class HttpsGetter {
    public String connCall(String s){
        String html = null;
        try {
            URL url = new URL(s);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            html = convertToString(in);
        }
        catch(Exception e){
            Log.e("connCall","Conn establishmentErr");
        }
        return html;
    }
    public String convertToString(InputStream in){
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader b = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = b.readLine()) != null) {
                sb.append(line + "\n");
            }
            b.close();
        }
        catch(Exception e){
            Log.i("Convstr","String conversion error");
        }
        return sb.toString();
    }
}
