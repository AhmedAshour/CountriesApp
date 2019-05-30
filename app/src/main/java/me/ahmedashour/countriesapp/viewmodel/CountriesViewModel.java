package me.ahmedashour.countriesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import me.ahmedashour.countriesapp.datamanager.CountriesDataManager;
import me.ahmedashour.countriesapp.model.Country;

public class CountriesViewModel extends ViewModel {

    CountriesDataManager countriesDataManager;

    public CountriesViewModel() {
        countriesDataManager = new CountriesDataManager();
    }

    public LiveData<ArrayList<Country>> getCountries() {
        return countriesDataManager.getCountriesFromApi();

    }
}
