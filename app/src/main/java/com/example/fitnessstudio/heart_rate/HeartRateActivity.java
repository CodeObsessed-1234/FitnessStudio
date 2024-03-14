package com.example.fitnessstudio.heart_rate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.report_generation.ReportGeneration;
import com.example.fitnessstudio.session.SessionManager;
import com.example.fitnessstudio.user_interface.UserInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class HeartRateActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private OutputAnalyzer analyzer;

    private final int REQUEST_CODE_CAMERA = 0;
    public static final int MESSAGE_UPDATE_REALTIME = 1;
    public static final int MESSAGE_UPDATE_FINAL = 2;
    public static final int MESSAGE_CAMERA_NOT_AVAILABLE = 3;
    private double answerStored=0.0D;
    public enum VIEW_STATE {
        MEASUREMENT,
        SHOW_RESULTS
    }

    @SuppressLint("HandlerLeak")
    private final Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what ==  MESSAGE_UPDATE_REALTIME) {
                String string=msg.obj.toString();
                string=string.substring(string.indexOf(":")+2,string.lastIndexOf(' '));
                answerStored=Double.parseDouble(string);
                ((TextView) findViewById(R.id.textView)).setText(msg.obj.toString());
            }
            if (msg.what == MESSAGE_UPDATE_FINAL) {
                setViewState(VIEW_STATE.SHOW_RESULTS);
            }
            if (msg.what == MESSAGE_CAMERA_NOT_AVAILABLE) {
                Log.println(Log.WARN, "camera", msg.obj.toString());
                ((TextView) findViewById(R.id.textView)).setText(
                        R.string.camera_not_found
                );
                analyzer.stop();
            }
        }
    };
    private final CameraService cameraService = new CameraService(this, mainHandler);
    @Override
    protected void onResume() {
        super.onResume();
        analyzer = new OutputAnalyzer(this, findViewById(R.id.graphTextureView), mainHandler);
        TextureView cameraTextureView = findViewById(R.id.textureView2);
        SurfaceTexture previewSurfaceTexture = cameraTextureView.getSurfaceTexture();
        boolean justShared = false;
        if ((previewSurfaceTexture != null) && !justShared) {
            Surface previewSurface = new Surface(previewSurfaceTexture);
            if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                Snackbar.make(
                        findViewById(R.id.linearLayout),
                        getString(R.string.noFlashWarning),
                        Snackbar.LENGTH_LONG
                ).show();
            }
            cameraService.start(previewSurface);
            analyzer.measurePulse(cameraTextureView, cameraService);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        cameraService.stop();
        if (analyzer != null) analyzer.stop();
        analyzer = new OutputAnalyzer(this, findViewById(R.id.graphTextureView), mainHandler);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CODE_CAMERA);
        AppCompatButton saveHeartRate=this.findViewById(R.id.save_heart_rate);
        saveHeartRate.setOnClickListener(event->{
            String pattern = "dd-MM-yyyy";
            @SuppressLint("SimpleDateFormat")
            String dateInString =new SimpleDateFormat(pattern).format(new Date());
            String userId= SessionManager.getUserId();
            HashMap<String,String> map=new HashMap<>();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String address="Users/"+userId;
            DatabaseReference usersReference = database.getReference(address).child("heartRateMap");
            if(answerStored!=0.0D)
                map.put(dateInString,answerStored+"");
            usersReference.setValue(map);
            Intent intent=new Intent(this,UserInterface.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Snackbar.make(
                        findViewById(R.id.linearLayout),
                        getString(R.string.cameraPermissionRequired),
                        Snackbar.LENGTH_LONG
                ).show();
            }
        }
    }
    public void setViewState(VIEW_STATE state) {
        switch (state) {
            case MEASUREMENT:
                findViewById(R.id.start_tracking_button_heart_rate).setVisibility(View.INVISIBLE);
                break;
            case SHOW_RESULTS:
                if(!ReportGeneration.isReport)
                    findViewById(R.id.start_tracking_button_heart_rate).setVisibility(View.VISIBLE);
                else{
                    findViewById(R.id.start_tracking_button_heart_rate).setVisibility(View.INVISIBLE);
                    findViewById(R.id.save_heart_rate).setVisibility(View.VISIBLE);
                }
                break;
        }
    }
    public void onClickNewMeasurement(View view) {
        onClickNewMeasurement();
    }
    public void onClickNewMeasurement() {
        analyzer = new OutputAnalyzer(this, findViewById(R.id.graphTextureView), mainHandler);
        char[] empty = new char[0];
        ((TextView) findViewById(R.id.textView)).setText(empty, 0, 0);
        ((TextView) findViewById(R.id.textView)).setText(R.string.heart_rate_calculation_start);
        setViewState(VIEW_STATE.MEASUREMENT);
        TextureView cameraTextureView = findViewById(R.id.textureView2);
        SurfaceTexture previewSurfaceTexture = cameraTextureView.getSurfaceTexture();
        if (previewSurfaceTexture != null) {
            Surface previewSurface = new Surface(previewSurfaceTexture);
            cameraService.start(previewSurface);
            analyzer.measurePulse(cameraTextureView, cameraService);
        }
    }
}