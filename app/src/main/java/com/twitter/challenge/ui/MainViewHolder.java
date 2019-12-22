package com.twitter.challenge.ui;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twitter.challenge.R;
import com.twitter.challenge.model.Conditions;
import com.twitter.challenge.util.TemperatureConverter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.future_temperature)
    TextView futureTemperatureView;
    @BindView(R.id.future_windspeed)
    TextView futureWindSpeedView;
    @BindView(R.id.future_cloud)
    ImageView futureCloudView;

    public MainViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Conditions conditions) {
        Context context = itemView.getContext();
        float tempCelsius = conditions.getWeather().getTemp();
        float tempFahrenheit = TemperatureConverter.celsiusToFahrenheit(tempCelsius);
        double windSpeed = conditions.getWind().getSpeed();
        int cloudiness = conditions.getClouds().getCloudiness();

        futureTemperatureView.setText(context.getString(R.string.future_temperature, tempCelsius, tempFahrenheit));
        futureWindSpeedView.setText(context.getString(R.string.future_windspeed, windSpeed));
        if(cloudiness > 50)
            futureCloudView.setImageResource(R.drawable.cloud_icon);
    }
}
