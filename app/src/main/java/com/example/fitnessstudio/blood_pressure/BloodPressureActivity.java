package com.example.fitnessstudio.blood_pressure;

import androidx.annotation.NonNull;
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

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import com.example.fitnessstudio.R;
import com.example.fitnessstudio.heart_rate.HeartRateActivity;
import com.example.fitnessstudio.report_generation.ReportGeneration;
import com.example.fitnessstudio.session.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class BloodPressureActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private OutputAnalyzerBloodPressure analyzer;

    private final int REQUEST_CODE_CAMERA2 = 0;
    public static final int MESSAGE_UPDATE_REALTIME2 = 1;
    public static final int MESSAGE_UPDATE_FINAL2 = 2;
    public static final int MESSAGE_CAMERA_NOT_AVAILABLE2 = 3;
    private double answerStored=0.0D;
    public enum VIEW_STATE2 {
        MEASUREMENT2,
        SHOW_RESULTS2
    }

    @SuppressLint("HandlerLeak")
    private final Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_UPDATE_REALTIME2) {
                String string=msg.obj.toString();
                string=string.substring(string.indexOf(":")+2,string.lastIndexOf(' '));
                double answer=Double.parseDouble(string)*2.0;
                answerStored=answer;
                string="Blood pressure calculated: "+answer+" mmHg";
                ((TextView) findViewById(R.id.bloodPressureAnswer)).setText(string);
            }
            if (msg.what == MESSAGE_UPDATE_FINAL2) {
                setViewState(VIEW_STATE2.SHOW_RESULTS2);
            }
            if (msg.what == MESSAGE_CAMERA_NOT_AVAILABLE2) {
                Log.println(Log.WARN, "camera", msg.obj.toString());
                ((TextView) findViewById(R.id.bloodPressureAnswer)).setText(
                        R.string.camera_not_found
                );
                analyzer.stop();
            }
        }
    };
    private final CameraServiceBloodPressure cameraService = new CameraServiceBloodPressure(this, mainHandler);
    @Override
    protected void onResume() {
        super.onResume();
        analyzer = new OutputAnalyzerBloodPressure(this, findViewById(R.id.graphTextureView2), mainHandler);
        TextureView cameraTextureView = findViewById(R.id.textureView21);
        SurfaceTexture previewSurfaceTexture = cameraTextureView.getSurfaceTexture();
        boolean justShared = false;
        if ((previewSurfaceTexture != null) && !justShared) {
            Surface previewSurface = new Surface(previewSurfaceTexture);
            if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                Snackbar.make(
                        findViewById(R.id.linearLayout2),
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
        analyzer = new OutputAnalyzerBloodPressure(this, findViewById(R.id.graphTextureView2), mainHandler);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CODE_CAMERA2);
        AppCompatButton saveBloodPressure=this.findViewById(R.id.save_blood_pressure);
        saveBloodPressure.setOnClickListener(event->{
            Intent intent=new Intent(this, HeartRateActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            String pattern = "dd-MM-yyyy";
            @SuppressLint("SimpleDateFormat")
            String dateInString =new SimpleDateFormat(pattern).format(new Date());
            String userId= SessionManager.getUserId();
            HashMap<String,String> map=new HashMap<>();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String address="Users/"+userId;
            DatabaseReference usersReference = database.getReference(address).child("bloodPressureMap");
            if(answerStored!=0.0D)
                map.put(dateInString,answerStored+"");
            usersReference.setValue(map);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA2) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Snackbar.make(
                        findViewById(R.id.linearLayout2),
                        getString(R.string.cameraPermissionRequired),
                        Snackbar.LENGTH_LONG
                ).show();
            }
        }
    }
    protected void setViewState(VIEW_STATE2 state) {
        switch (state) {
            case MEASUREMENT2:
                findViewById(R.id.start_tracking_button_blood_pressure).setVisibility(View.INVISIBLE);
                break;
            case SHOW_RESULTS2:
                if(!ReportGeneration.isReport)
                    findViewById(R.id.start_tracking_button_blood_pressure).setVisibility(View.VISIBLE);
                else{
                    findViewById(R.id.start_tracking_button_blood_pressure).setVisibility(View.INVISIBLE);
                    findViewById(R.id.save_blood_pressure).setVisibility(View.VISIBLE);
                }
                break;
        }
    }
    public void onClickNewMeasurementBloodPressure(View view) {
        onClickNewMeasurementBloodPressure();
    }
    public void onClickNewMeasurementBloodPressure() {
        analyzer = new OutputAnalyzerBloodPressure(this, findViewById(R.id.graphTextureView2), mainHandler);
        char[] empty = new char[0];
        ((TextView) findViewById(R.id.bloodPressureAnswer)).setText(empty, 0, 0);
        ((TextView) findViewById(R.id.bloodPressureAnswer)).setText(R.string.blood_pressure_calculation_start);
        setViewState(VIEW_STATE2.MEASUREMENT2);
        TextureView cameraTextureView = findViewById(R.id.textureView21);
        SurfaceTexture previewSurfaceTexture = cameraTextureView.getSurfaceTexture();
        if (previewSurfaceTexture != null) {
            Surface previewSurface = new Surface(previewSurfaceTexture);
            cameraService.start(previewSurface);
            analyzer.measurePulse(cameraTextureView, cameraService);
        }
    }
}