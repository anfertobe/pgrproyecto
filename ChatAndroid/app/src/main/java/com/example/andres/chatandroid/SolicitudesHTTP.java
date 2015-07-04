package com.example.andres.chatandroid;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by andres on 04/07/2015.
 */
public class SolicitudesHTTP {

    public JSONObject Post(String carne){
        StringBuilder builder = new StringBuilder();
        JSONObject json = new JSONObject();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Common.getServerUrl()+"/adicion/"+carne.trim());
        try {
            Log.i("url",Common.getServerUrl()+"/adicion/"+carne);
            HttpResponse response = client.execute(httpPost);
            Log.i("Execute","Post");
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            Log.i("Respuesta",entity.toString());
            InputStream content = entity.getContent();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            Log.i("builder",builder.toString());
            json = new JSONObject(builder.toString());
            Log.i("Json",json.toString());

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e(MainActivity.class.toString(),
                    "Post request failed" + e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(MainActivity.class.toString(),
                    "Post request failed"+e.getLocalizedMessage());
        }catch (JSONException e) {
            e.printStackTrace();
            Log.e(MainActivity.class.toString(),
                    "Post request failed"+e.getLocalizedMessage());
        }

        return json;
    }

}
