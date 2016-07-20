package com.example.user.simpleui;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by user on 2016/7/18.
 */
public class Utils {
    public static void writeFile(Context context, String fileName, String content)
    {
        try {
            FileOutputStream fileOutputStream = (context.openFileOutput(fileName, Context.MODE_APPEND));
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String readFile(Context context, String fileName)
    {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            byte[] buffer = new byte[2048];
            fileInputStream.read(buffer, 0 ,buffer.length);
            fileInputStream.close();
            return new String(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] urlToBytes(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, len);
            }
            return byteArrayOutputStream.toByteArray();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double[] getLatLngFromGoogleMapAPI(String address)
    {
        try {
            address = URLEncoder.encode(address,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String apiURL="http://maps.google.com/maps/api/geocode/json?address=" + address;
        //台北市羅斯福路四段一號
        byte[] data = Utils.urlToBytes(apiURL);

        if(data == null)
            return null;

        String result = new String(data);
        try {
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.getString("status").equals("OK"))
            {
                JSONObject location=jsonObject.getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");
                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");
                return new double[]{lat,lng};
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
