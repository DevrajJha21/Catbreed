package com.example.catbreed.api;

import com.example.catbreed.ui.main.model.BreedsDatum;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ApiService {
    @GET("v1/breeds")
     Single<List<BreedsDatum>> getCatBreeds();

}
