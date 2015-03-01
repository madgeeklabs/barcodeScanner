package com.madgeeklabs.barcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.madgeeklabs.barcodescanner.api.HealthFridgeApi;
import com.madgeeklabs.barcodescanner.common.Credentials;
import com.madgeeklabs.barcodescanner.models.Product;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends Activity implements Callback<Response> {

    private static final String TAG = MainActivity.class.getName();
    private HealthFridgeApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "SCANNER ONLINE!");

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Credentials.SERVER)
                .build();

        api = adapter.create(HealthFridgeApi.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.setPackage("com.google.zxing.client.android");
        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        String contents = "empty";
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.d(TAG, "contents:" + contents);

                api.registerNumber(new Product(contents), MainActivity.this);
                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 300);
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void success(Response response, Response response2) {
        Log.d(TAG, "SUCCESS PROGRESSING BARCODE");
    }

    @Override
    public void failure(RetrofitError error) {
        Log.d(TAG, "" + error.getMessage());
    }
}
