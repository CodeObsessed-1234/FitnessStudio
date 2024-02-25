package com.example.fitnessstudio.blood_pressure;

import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.TextureView;
import com.example.fitnessstudio.R;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
public class OutputAnalyzerBloodPressure {
    private final BloodPressureActivity heartRateActivity;
    private final ChartDrawerBloodPressure chartDrawer;
    private MeasureStoreBloodPressure store;
    private final int measurementInterval = 45;
    private final int measurementLength = 15000;
    private final int clipLength = 3500;
    private int detectedValleys = 0;
    private int ticksPassed = 0;
    private final CopyOnWriteArrayList<Long> valleys = new CopyOnWriteArrayList<>();
    private CountDownTimer timer;
    private final Handler mainHandler;
    OutputAnalyzerBloodPressure(BloodPressureActivity activity, TextureView graphTextureView, Handler mainHandler) {
        this.heartRateActivity = activity;
        this.chartDrawer = new ChartDrawerBloodPressure(graphTextureView);
        this.mainHandler = mainHandler;
    }
    private boolean detectValley() {
        final int valleyDetectionWindowSize = 13;
        CopyOnWriteArrayList<MeasurementBloodPressure<Integer>> subList = store.getLastStdValues(valleyDetectionWindowSize);
        if (subList.size() < valleyDetectionWindowSize) {
            return false;
        } else {
            Integer referenceValue = subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f)).measurement;
            for (MeasurementBloodPressure<Integer> measurement : subList) {
                if (measurement.measurement < referenceValue) return false;
            }
            return (!subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f)).measurement.equals(
                    subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f) - 1).measurement));
        }
    }
    void measurePulse(TextureView textureView, CameraServiceBloodPressure cameraService) {
        store = new MeasureStoreBloodPressure();
        detectedValleys = 0;
        timer = new CountDownTimer(measurementLength, measurementInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (clipLength > (++ticksPassed * measurementInterval)) return;
                Thread thread = new Thread(() -> {
                    Bitmap currentBitmap = textureView.getBitmap();
                    int pixelCount = textureView.getWidth() * textureView.getHeight();
                    int measurement = 0;
                    int[] pixels = new int[pixelCount];
                    assert currentBitmap != null;
                    currentBitmap.getPixels(pixels, 0, textureView.getWidth(), 0, 0, textureView.getWidth(), textureView.getHeight());
                    for (int pixelIndex = 0; pixelIndex < pixelCount; pixelIndex++) {
                        measurement += (pixels[pixelIndex] >> 16) & 0xff;
                    }
                    store.add(measurement);
                    if (detectValley()) {
                        detectedValleys = detectedValleys + 1;
                        valleys.add(store.getLastTimestamp().getTime());
                        String currentValue = String.format(
                                Locale.getDefault(),
                                heartRateActivity.getResources().getQuantityString(R.plurals.measurement_output_template_blood_pressure, detectedValleys),
                                (valleys.size() == 1)
                                        ? (60f * (detectedValleys) / (Math.max(1, (measurementLength - millisUntilFinished - clipLength) / 1000f)))
                                        : (60f * (detectedValleys - 1) / (Math.max(1, (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f))),
                                detectedValleys,
                                1f * (measurementLength - millisUntilFinished - clipLength) / 1000f);

                        sendMessage(BloodPressureActivity.MESSAGE_UPDATE_REALTIME2, currentValue);
                    }
                    Thread chartDrawerThread = new Thread(() -> chartDrawer.draw(store.getStdValues()));
                    chartDrawerThread.start();
                });
                thread.start();
            }
            @Override
            public void onFinish() {
                CopyOnWriteArrayList<MeasurementBloodPressure<Float>> stdValues = store.getStdValues();
                if (valleys.size() == 0) {
                    mainHandler.sendMessage(Message.obtain(
                            mainHandler,
                            BloodPressureActivity.MESSAGE_CAMERA_NOT_AVAILABLE2,
                            "No valleys detected - there may be an issue when accessing the camera."));
                    return;
                }
                String currentValue = String.format(
                        Locale.getDefault(),
                        heartRateActivity.getResources().getQuantityString(R.plurals.measurement_output_template_blood_pressure, detectedValleys - 1),
                        60f * (detectedValleys - 1) / (Math.max(1, (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f)),
                        detectedValleys - 1,
                        1f * (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f);
                sendMessage(BloodPressureActivity.MESSAGE_UPDATE_REALTIME2, currentValue);
                StringBuilder returnValueSb = new StringBuilder();
                returnValueSb.append(currentValue);
                returnValueSb.append(heartRateActivity.getString(R.string.row_separator));
                returnValueSb.append(heartRateActivity.getString(R.string.raw_values));
                returnValueSb.append(heartRateActivity.getString(R.string.row_separator));
                for (int stdValueIdx = 0; stdValueIdx < stdValues.size(); stdValueIdx++) {
                    MeasurementBloodPressure<Float> value = stdValues.get(stdValueIdx);
                    String timeStampString =
                            new SimpleDateFormat(
                                    heartRateActivity.getString(R.string.dateFormatGranular),
                                    Locale.getDefault()
                            ).format(value.timestamp);
                    returnValueSb.append(timeStampString);
                    returnValueSb.append(heartRateActivity.getString(R.string.separator));
                    returnValueSb.append(value.measurement);
                    returnValueSb.append(heartRateActivity.getString(R.string.row_separator));
                }
                returnValueSb.append(heartRateActivity.getString(R.string.output_detected_peaks_header));
                returnValueSb.append(heartRateActivity.getString(R.string.row_separator));
                for (long tick : valleys) {
                    returnValueSb.append(tick);
                    returnValueSb.append(heartRateActivity.getString(R.string.row_separator));
                }
                sendMessage(BloodPressureActivity.MESSAGE_UPDATE_FINAL2, returnValueSb.toString());
                cameraService.stop();
            }
        };
        heartRateActivity.setViewState(BloodPressureActivity.VIEW_STATE2.MEASUREMENT2);
        timer.start();
    }
    void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }
    void sendMessage(int what, Object message) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = message;
        mainHandler.sendMessage(msg);
    }
}