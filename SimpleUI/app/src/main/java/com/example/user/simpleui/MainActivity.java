package com.example.user.simpleui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CODE_DRINK_MENU_ACTIVITY =0;
    TextView textView;
    EditText editText;
    RadioGroup radioGroup;
    ListView listView;
    Spinner spinner;
    String selectedTea = "black tea";

    String menuResults ="";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    List<Order> orders = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        radioGroup =(RadioGroup) findViewById(R.id.radioGroup);
        listView = (ListView)findViewById(R.id.listView);
        spinner = (Spinner)findViewById(R.id.spinner);

        sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        editText.setText(sharedPreferences.getString("editText", ""));
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String text = editText.getText().toString();
                editor.putString("editText", text);
                editor.commit();

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    submit(v);
                    return true;
                }

                return false;
            }
        });
       radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup group, int checkedId) {
               RadioButton radioButton = (RadioButton) radioGroup.findViewById(checkedId);
               selectedTea = radioButton.getText().toString();
           }
       });
/*
     String history = Utils.readFile(this, "history");
     String[] datas = history.split("\n");
     for(String data: datas)
     {
         Order order = Order.newInstanceWithData(data);
         if(order != null)
            orders.add(order);
     }
*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = (Order)parent.getAdapter().getItem(position);
                goToDetail(order);
            }
        });
     setupListView();
     setupSpinner();
/*
        ParseObject parseObject = new ParseObject("Test");
        parseObject.put("foo","spr");
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    Toast.makeText(MainActivity.this, "上傳成功", Toast.LENGTH_LONG);
            }
        });
        //
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Test");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null)
                {
                    for(ParseObject object : objects)
                    {
                        Toast.makeText(MainActivity.this, object.getString("foo"), Toast.LENGTH_LONG).show();
                    }
                }
                    //Toast.makeText(MainActivity.this,"下載成功",Toast.LENGTH_LONG);
            }
        });

*/
        Log.d("debug", "MainActivity On Create");
    }

    public void setupListView()
    {
       // String[] data = new String[]{"black tea","green tea", "1","2","3","4","5"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        FindCallback<Order> callback = new FindCallback<Order>() {
            @Override
            public void done(List<Order> objects, ParseException e) {
                if (e == null) {
                    orders = objects;
                    OrderAdapter adapter = new OrderAdapter(MainActivity.this, orders);
                    listView.setAdapter(adapter);
                }
            }
        };


        if(networkInfo == null || networkInfo.isConnected())
        {
            Order.getQuery().fromLocalDatastore().findInBackground(callback);
        }
        else
        {
            Order.getOrderFromRemote(callback);
        }

     /*   Order.getOrderFromRemote(new FindCallback<Order>() {
            @Override
            public void done(List<Order> objects, ParseException e) {
                orders = objects;
                OrderAdapter adapter = new OrderAdapter(MainActivity.this,orders);
                listView.setAdapter(adapter);
            }
        });
        */
        OrderAdapter adapter = new OrderAdapter(this, orders);

        listView.setAdapter(adapter);

    }
    public void setupSpinner()
    {
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("StoreInfo");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                List<String> storeInfos = new ArrayList<String>();
                for(ParseObject object : objects)
                {
                   String storeInfo =  object.getString("name") + "," + object.getString("address");
                           storeInfos.add(storeInfo);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item,storeInfos);
                spinner.setAdapter(adapter);
            }
        });
        String[] data = getResources().getStringArray(R.array.storeInfos);
       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,data);
       // spinner.setAdapter(adapter);

    }
    public void submit(View view)
    {
        String text = editText.getText().toString();
        //text = text + "   性別:" + selectedSex;
        textView.setText(text);
        Order order = new Order();
        order.setNote(text);
        order.setMenuResults(menuResults);
        order.setStoreInfo((String) spinner.getSelectedItem());

        //order.saveInBackground();
        order.pinInBackground("Order");
        //order.saveEventually();
        order.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                setupListView();
            }
        });
        orders.add(order);

        //
        Utils.writeFile(this, "history", order.toData() + "\n");
        //setupListView();

        editText.setText("");
        //add.
        menuResults = "";
    }
public void goToMenu(View view)
{
    Intent intent = new Intent();
    intent.setClass(this, DrinkMenuActivity.class);
    //startActivity(intent);
    startActivityForResult(intent, REQUEST_CODE_DRINK_MENU_ACTIVITY);
}
    public void goToDetail(Order order)
    {
        Intent intent = new Intent();
        intent.setClass(this, OrderDetailActivity.class);
        intent.putExtra("note", order.getNote());
        intent.putExtra("storeInfo", order.getStoreInfo());
        intent.putExtra("menuResults",order.getMenuResults());
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_DRINK_MENU_ACTIVITY)
        {
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(this, "完成菜單",Toast.LENGTH_SHORT).show();
                //
                // textView.setText(data.getStringExtra("result"));
                menuResults =(data.getStringExtra("result"));
            }
        }
    }


    @Override
    protected void onStart(){
        super.onStart();
        Log.d("debug", "MainActivity On Create");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("debug", "MainActivity On Resume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("debug", "MainActivity On Pause");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d("debug", "MainActivity On Stop");
    }
    protected void onDestroy(){
        super.onDestroy();
        Log.d("debug", "MainActivity OnDestroy");
    }
    protected void onRestart(){
        super.onRestart();
        Log.d("debug", "MainActivity OnRestart");
    }
}
