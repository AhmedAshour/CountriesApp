package me.ahmedashour.countriesapp.datamanager;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import me.ahmedashour.countriesapp.model.Country;
import me.ahmedashour.countriesapp.network.CountriesAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountriesDataManager {
    String TAG = CountriesDataManager.class.getSimpleName();
    MutableLiveData<ArrayList<Country>> countriesList;

    public CountriesDataManager() {
        countriesList = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Country>> getCountriesFromApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://restcountries.eu/rest/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CountriesAPI countriesAPI = retrofit.create(CountriesAPI.class);
        Call<ArrayList<Country>> call = countriesAPI.getCountriesNames();
        call.enqueue(new Callback<ArrayList<Country>>() {
            @Override
            public void onResponse(Call<ArrayList<Country>> call, Response<ArrayList<Country>> response) {
                Log.d(TAG, "Success");
                countriesList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Country>> call, Throwable t) {
                Log.d(TAG, "FAILURE:" + t.toString());
            }
        });

        return countriesList;
    }
}
