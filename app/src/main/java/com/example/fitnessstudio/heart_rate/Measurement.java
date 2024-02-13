package com.example.fitnessstudio.heart_rate;
import java.util.Date;
public class Measurement<T> {
    final Date timestamp;
    final T measurement;

    Measurement(Date timestamp, T measurement) {
        this.timestamp = timestamp;
        this.measurement = measurement;
    }
}
