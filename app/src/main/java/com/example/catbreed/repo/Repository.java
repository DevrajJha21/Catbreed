package com.example.catbreed.repo;

import com.example.catbreed.api.ApiService;
import com.example.catbreed.ui.main.model.BreedsDatum;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class Repository {
    public ApiService apiService;

    @Inject
    public Repository(ApiService apiService) {
        this.apiService = apiService;
    }
    public Single<List<BreedsDatum>> getCatBreeds(){
        return apiService.getCatBreeds();
    }
}
