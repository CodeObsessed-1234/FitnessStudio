package com.example.fitnessstudio.blood_pressure;

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class MeasureStoreBloodPressure {
    private final CopyOnWriteArrayList<MeasurementBloodPressure<Integer>> measurements = new CopyOnWriteArrayList<>();
    private int minimum = 2147483647;
    private int maximum = -2147483648;

    @SuppressWarnings("FieldCanBeLocal")
    private final int rollingAverageSize = 4;

    void add(int measurement) {
        MeasurementBloodPressure<Integer> measurementWithDate = new MeasurementBloodPressure<>(new Date(), measurement);
        measurements.add(measurementWithDate);
        if (measurement < minimum) minimum = measurement;
        if (measurement > maximum) maximum = measurement;
    }

    CopyOnWriteArrayList<MeasurementBloodPressure<Float>> getStdValues() {
        CopyOnWriteArrayList<MeasurementBloodPressure<Float>> stdValues = new CopyOnWriteArrayList<>();
        for (int i = 0; i < measurements.size(); i++) {
            int sum = 0;
            for (int rollingAverageCounter = 0; rollingAverageCounter < rollingAverageSize; rollingAverageCounter++) {
                sum += measurements.get(Math.max(0, i - rollingAverageCounter)).measurement;
            }
            MeasurementBloodPressure<Float> stdValue =
                    new MeasurementBloodPressure<>(
                            measurements.get(i).timestamp,
                            ((float)sum / rollingAverageSize - minimum ) / (maximum - minimum));
            stdValues.add(stdValue);
        }

        return stdValues;
    }

    @SuppressWarnings("SameParameterValue") // this parameter can be set at OutputAnalyzer
    CopyOnWriteArrayList<MeasurementBloodPressure<Integer>> getLastStdValues(int count) {
        if (count < measurements.size()) {
            return  new CopyOnWriteArrayList<>(measurements.subList(measurements.size() - 1 - count, measurements.size() - 1));
        } else {
            return measurements;
        }
    }

    Date getLastTimestamp() {
        return measurements.get(measurements.size() - 1).timestamp;
    }
}