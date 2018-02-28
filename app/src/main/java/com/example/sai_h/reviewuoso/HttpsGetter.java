package com.example.sai_h.reviewuoso;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by sai_h on 27-02-2018.
 */

public class HttpsGetter extends AsyncTask<String,Void,String>{
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

    @Override
    protected String doInBackground(String... urls) {
        String html = null;
        Log.e("Entry","Entered");
        URL url = null;
        try {
            url = new URL(urls[0]);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            html = convertToString(in);
        } catch (MalformedURLException e) {
            Log.i("MExeception","err");
        }
        catch(ProtocolException p){
            Log.i("PExeception","err");
        }
        catch(IOException i){
            Log.i("IOExeception","err");
        }
        return html;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
