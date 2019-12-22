package com.twitter.challenge.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twitter.challenge.R;
import com.twitter.challenge.model.Conditions;
import com.twitter.challenge.util.StandardDev;
import com.twitter.challenge.util.TemperatureConverter;
import com.twitter.challenge.viewmodels.ViewModelProviderFactory;

import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends DaggerAppCompatActivity {

    private static final String TAG = "MainActivity";

    private MainViewModel viewModel;
    Unbinder unbinder;

    @BindView(R.id.temperature)
    TextView temperatureView;

    @BindView(R.id.windspeed)
    TextView windspeedView;

    @BindView(R.id.cloud)
    ImageView cloudView;

    @BindView(R.id.standard_dev)
    TextView standardDev;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    MainRecyclerAdapter adapter;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    private CompositeDisposable disposables = new CompositeDisposable();
    private boolean buttonClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(MainViewModel.class);

        iniRecyclerView();
        subscribeObservers();

        if(savedInstanceState != null)
            buttonClicked = savedInstanceState.getBoolean("displayFuture");

        if(buttonClicked) //was the button already clicked during previous screen change?
            subscribeObserversFuture();

        //final TextView temperatureView = (TextView) findViewById(R.id.temperature);
        //temperatureView.setText(getString(R.string.temperature, 34f, TemperatureConverter.celsiusToFahrenheit(34)));
    }

    public void displayFuture(View view) {
        if(!buttonClicked) { //only need button to react once
            subscribeObserversFuture();
            buttonClicked = true;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("displayFuture", buttonClicked);
    }

    private void iniRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void subscribeObservers() {
        Log.d(TAG, "subscribeObservers: starting");

        disposables.add(viewModel.getConditions().subscribe(
                conditionsResource -> observeConditions(conditionsResource),
                throwable -> Log.e(TAG, "subscribeObservers: onError", throwable)
        ));
    }

    private void subscribeObserversFuture() {
        Log.d(TAG, "subscribeObserversFuture: starting");

        disposables.add(viewModel.getFutureConditions(5).subscribe(
                conditionsResource -> observeFutureConditions(conditionsResource),
                throwable -> Log.e(TAG, "subscribeObservers: onError", throwable)
        ));
    }

    private void observeConditions(Resource<Conditions> conditionsResource) {
        Log.d(TAG, "observeConditions: starting");

        if(conditionsResource != null) {
            switch (conditionsResource.status) {
                case SUCCESS:{
                    Log.d(TAG, "observeConditions: got weather conditions");
                    float tempCelsius = conditionsResource.data.getWeather().getTemp();
                    float tempFahrenheit = TemperatureConverter.celsiusToFahrenheit(tempCelsius);
                    double windSpeed = conditionsResource.data.getWind().getSpeed();
                    int cloudiness = conditionsResource.data.getClouds().getCloudiness();
                    temperatureView.setText(getString(R.string.temperature, tempCelsius, tempFahrenheit));
                    windspeedView.setText(getString(R.string.windspeed, windSpeed));
                    if(cloudiness > 50)
                        cloudView.setImageResource(R.drawable.cloud_icon);
                    break;
                }
                case ERROR:{
                    Log.e(TAG, "observeConditions: ERROR! " + conditionsResource.message);
                    Toast.makeText(this, conditionsResource.message, Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
    }

    private void observeFutureConditions(Resource<List<Conditions>> conditionsResource) {
        Log.d(TAG, "observeFutureConditions: starting");

        if(conditionsResource != null) {
            switch (conditionsResource.status) {
                case SUCCESS:{
                    Log.d(TAG, "observeFutureConditions: got weather conditions");
                    double standard_deviation = StandardDev.calculate(conditionsResource.data);
                    double standard_deviation_f = TemperatureConverter.celsiusToFahrenheit((float)standard_deviation);
                    standardDev.setText(getString(R.string.standard_dev, standard_deviation, standard_deviation_f));
                    adapter.setConditionsList(conditionsResource.data);
                    break;
                }
                case ERROR:{
                    Log.e(TAG, "observeFutureConditions: ERROR! " + conditionsResource.message);
                    Toast.makeText(this, conditionsResource.message, Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        disposables.clear();
    }
}
