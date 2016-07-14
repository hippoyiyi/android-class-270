package com.example.user.simpleui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

public class DrinkMenuActivity extends AppCompatActivity {


    TextView totalTextView;
    ListView drinkMenuListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);
        Log.d("Debug", "DrinkmenuActivity OnCreate");
        totalTextView = (TextView)findViewById(R.id.totalTextView);
        drinkMenuListView = (ListView)findViewById(R.id.drinkMenulistView);
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.d("debug", "DrinkMenuMctivity OnCreate");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("debug", "DrinkMenuActivity OnResume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("debug", "DrinkMenuActivity OnPause");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d("debug", "DrinkMenuActivity OnStop");
    }
    protected void onDestroy(){
        super.onDestroy();
        Log.d("debug", "DrinkMenuActivity OnDestroy");
    }
    protected void onRestart(){
        super.onRestart();
        Log.d("debug", "DrinkMenuActivity OnRestart");
    }
}
