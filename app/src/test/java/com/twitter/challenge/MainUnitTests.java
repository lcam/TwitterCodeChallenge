package com.twitter.challenge;

import com.twitter.challenge.model.Conditions;
import com.twitter.challenge.model.Weather;
import com.twitter.challenge.network.MainApi;
import com.twitter.challenge.network.MainNetworkClient;
import com.twitter.challenge.network.RequestContent;
import com.twitter.challenge.ui.MainViewModel;
import com.twitter.challenge.ui.Resource;
import com.twitter.challenge.util.TemperatureConverter;

import org.assertj.core.data.Offset;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.within;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MainUnitTests {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    RequestContent mockRequestContent;

    @InjectMocks
    MainViewModel viewModel;

    @Test
    public void testCelsiusToFahrenheitConversion() {
        final Offset<Float> precision = within(0.01f);

        assertThat(TemperatureConverter.celsiusToFahrenheit(-50)).isEqualTo(-58, precision);
        assertThat(TemperatureConverter.celsiusToFahrenheit(0)).isEqualTo(32, precision);
        assertThat(TemperatureConverter.celsiusToFahrenheit(10)).isEqualTo(50, precision);
        assertThat(TemperatureConverter.celsiusToFahrenheit(21.11f)).isEqualTo(70, precision);
        assertThat(TemperatureConverter.celsiusToFahrenheit(37.78f)).isEqualTo(100, precision);
        assertThat(TemperatureConverter.celsiusToFahrenheit(100)).isEqualTo(212, precision);
        assertThat(TemperatureConverter.celsiusToFahrenheit(1000)).isEqualTo(1832, precision);
    }

    @Test
    public void test_getConditions() {
        // Preparation: mock DummyService
        //MainViewModel dummyService = Mockito.mock(MainTabViewModel.DummyService.class);
        Conditions conditions = new Conditions();
        Weather weather = new Weather();
        weather.setTemp((float)5);
        conditions.setWeather(weather);
        Resource<Conditions> conditionsResource = Resource.success(conditions);
        Mockito.doReturn(Observable.just(conditionsResource)).when(mockRequestContent).queryConditions();

        // Trigger
        TestObserver<Resource<Conditions>> testObserver = viewModel.getConditions().test();

        // Validation
        testObserver.assertValues(conditionsResource);
        // clean up
        testObserver.dispose();
    }

    @Test
    public void test_getFutureConditions() {
        // Preparation: mock DummyService
        //MainViewModel dummyService = Mockito.mock(MainTabViewModel.DummyService.class);
        Conditions conditions = new Conditions();
        Weather weather = new Weather();
        weather.setTemp((float)5);
        conditions.setWeather(weather);
        List<Conditions> conditionsList = new ArrayList<>();
        conditionsList.add(conditions);
        Resource<List<Conditions>> conditionsResourceList = Resource.success(conditionsList);
        Mockito.doReturn(Observable.just(conditionsResourceList)).when(mockRequestContent).queryFutureConditions(5);

        viewModel.getFutureConditions(5)
                .test()
                .assertValues(conditionsResourceList)
                .dispose();
    }
}
