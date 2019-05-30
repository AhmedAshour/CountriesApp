package me.ahmedashour.countriesapp.network;

import java.util.ArrayList;

import me.ahmedashour.countriesapp.model.Country;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CountriesAPI {

    @GET("all?fields=name")
    Call<ArrayList<Country>> getCountriesNames();
}
