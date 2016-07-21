package com.example.user.simpleui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.logging.Handler;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        String note = intent.getStringExtra("note");
        String menuResults = intent.getStringExtra("menuResults");
        String storeInfo = intent.getStringExtra("storeInfo");

        TextView noteTextView = (TextView)findViewById(R.id.noteTextView);
        TextView menuResultsTextView = (TextView)findViewById(R.id.menuResultsTextView);
        TextView storeInfoTextView = (TextView)findViewById(R.id.storeInfoTextView);
        ImageView staticMapImageView = (ImageView)findViewById(R.id.googleMapImageView);
        noteTextView.setText(note);
        storeInfoTextView.setText(storeInfo);

        List<String> menuResultList = Order.getMenuResultList(menuResults);

        String text = "";
        if(menuResultList != null)
        {
            for(String menuResult : menuResultList)
            {
                text += menuResult + "\n";
            }
        }
             menuResultsTextView.setText(text);

              /* new Thread(new Runnable(){
            @Override
        public void run(){
                Handler
            }
        }).run();*/
          new GeoCodingTask(staticMapImageView).execute("台北市羅斯福路四段一號");
    }

    public static class GeoCodingTask extends AsyncTask<String, Void, Bitmap>{

        WeakReference<ImageView> imageViewWeakReference;

        @Override
        protected Bitmap doInBackground(String... params) {
            String address = params[0];
                double[] latlng = Utils.getLatLngFromGoogleMapAPI(address);
            return Utils.getStatisticMap(latlng);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            super.onPostExecute(bitmap);
            if(imageViewWeakReference.get() != null && bitmap != null)
            {
                ImageView imageView = imageViewWeakReference.get();
                imageView.setImageBitmap(bitmap);
            }
        }
        public GeoCodingTask(ImageView imageView)
        {
           this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }
    }
}
