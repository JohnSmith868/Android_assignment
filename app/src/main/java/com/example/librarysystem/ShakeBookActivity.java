package com.example.librarysystem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ShakeBookActivity extends AppCompatActivity implements SensorEventListener {
    TextView tv_shakeBook;
    SensorManager sensorManager;
    Sensor accelSensor;
    float shakeIdentifyCompare = 5f;
    float xIndex;
    float yIndex;
    float zIndex;
    float xIndexBefore;
    float yIndexBefore;
    float zIndexBefore;
    float xCompare;
    float yCompare;
    float zCompare;
    boolean initSensor = true;
    boolean isAccelSensorAvail;
    LoadingDialog loadingDialog;
    ImageView image;
    Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_book);
        tv_shakeBook = findViewById(R.id.tv_shake_book);
        image = findViewById(R.id.imageView);

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        loadingDialog = new LoadingDialog(this);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelSensorAvail = true;
        }else{
            tv_shakeBook.setText("sensor not available");
            isAccelSensorAvail = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isAccelSensorAvail){
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isAccelSensorAvail){
            sensorManager.registerListener(this,accelSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSensorChanged(SensorEvent event) {
        xIndex = event.values[0];
        yIndex = event.values[1];
        zIndex = event.values[2];

        if(!initSensor){
            xCompare = Math.abs(xIndexBefore-xIndex);
            yCompare = Math.abs(yIndexBefore-yIndex);
            zCompare = Math.abs(zIndexBefore-zIndex);

            boolean isShaking = (xCompare>shakeIdentifyCompare&&yCompare>shakeIdentifyCompare) || (xCompare>shakeIdentifyCompare&&zCompare>shakeIdentifyCompare) ||(yCompare>shakeIdentifyCompare&&zCompare>shakeIdentifyCompare);
            if(isShaking){
                tv_shakeBook.setText("shaking");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));

                    loadingDialog.startloadingDialog();
                    randomBook();
                    sensorManager.unregisterListener(this);

                }else {
                    vibrator.vibrate(500);

                    loadingDialog.startloadingDialog();
                    randomBook();
                    sensorManager.unregisterListener(this);

                }

            }else {

            }

        }

        xIndexBefore = xIndex;
        yIndexBefore = yIndex;
        zIndexBefore = zIndex;
        initSensor = false;

    }

    private void randomBook() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.backend_url) + "/randomBook",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject!=null){
                                loadingDialog.dialogDismiss();
                                sensorManager.unregisterListener(ShakeBookActivity.this);
                                Intent intent = new Intent(ShakeBookActivity.this, BookDetailActivity.class);
                                intent.putExtra("title",jsonObject.getString("bookname"));
                                intent.putExtra("author",jsonObject.getString("author"));
                                intent.putExtra("bookid",jsonObject.getInt("bookid"));
                                intent.putExtra("isbn",jsonObject.getString("ISBN"));
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ShakeBookActivity.this,"something error", Toast.LENGTH_SHORT).show();
                            loadingDialog.dialogDismiss();

                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShakeBookActivity.this,"something error", Toast.LENGTH_SHORT).show();
                        loadingDialog.dialogDismiss();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
