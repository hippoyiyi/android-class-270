package com.example.user.simpleui;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2016/7/14.
 */
@ParseClassName("Drink")
public class Drink extends ParseObject {

    //private String name;
    //private int mPrice=0;
    //private int lPrice=0;
    int imageId;


    public void setName(String name) {
        put("name", name);
    }

    public String getName() {
        String name = getString("name");
        if(name == null){
            return "";
        }
        return name;
    }

    public void setmPrice(int mPrice) {
        put("mprice", mPrice);
    }

    public int getmPrice() {
        return getInt("mPrice");
    }
    public void setlPrice(int lPrice) {
        put("lPrice", lPrice);
    }

    public int getlPrice() {
        return getInt("lPrice");
    }



    public JSONObject getJsonObject()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", getName());
            jsonObject.put("mPrice",getmPrice());
            jsonObject.put("lPrice",getlPrice());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
   public static Drink newInstanceWithData(String data)
   {
       Drink drink = new Drink();
       try {
           JSONObject jsonObject = new JSONObject(data);
           drink.setName(jsonObject.getString("name"));
           drink.setlPrice(jsonObject.getInt("lPrice"));
           drink.setmPrice(jsonObject.getInt("mPrice"));
           //drink.name = jsonObject.getString("name");
       } catch (JSONException e) {
           e.printStackTrace();
       }
       return drink;
   }
}
