package com.example.fitnessstudio.blood_pressure;
import java.util.Date;
public class MeasurementBloodPressure<T> {
    final Date timestamp;
    final T measurement;

    MeasurementBloodPressure(Date timestamp, T measurement) {
        this.timestamp = timestamp;
        this.measurement = measurement;
    }
}
