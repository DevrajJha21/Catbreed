package com.example.catbreed.ui.main;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.catbreed.api.RxSingleSchedulers;
import com.example.catbreed.network.NetworkResponse;
import com.example.catbreed.repo.Repository;
import com.example.catbreed.ui.main.model.BreedsDatum;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private Repository repository;

    RxSingleSchedulers rxSingleSchedulers;
    private CompositeDisposable disposable = new CompositeDisposable();
private MutableLiveData<Boolean> isSorted=new MutableLiveData();
    @Inject
    public MainViewModel(Repository repository, RxSingleSchedulers rxSingleSchedulers) {
        this.repository = repository;
        this.rxSingleSchedulers = rxSingleSchedulers;
    }

    private MutableLiveData<NetworkResponse> breedResponseLiveData = new MutableLiveData<>();

    public MutableLiveData<NetworkResponse> getBreedResponseLiveData() {
        return breedResponseLiveData;
    }

    public void getCatBreeds() {
        disposable.add(repository.getCatBreeds()
                .doOnEvent((newsList, throwable) -> onLoading())
                .compose(rxSingleSchedulers.applySchedulers())
                .subscribe(this::onSuccess,
                        this::onError));
    }


    private void onSuccess(List<BreedsDatum> newsList) {
        NetworkResponse.success(newsList);
        breedResponseLiveData.postValue(NetworkResponse.success(newsList));
    }

    private void onError(Throwable error) {
        breedResponseLiveData.postValue(NetworkResponse.error(error));
    }

    private void onLoading() {
        breedResponseLiveData.postValue(NetworkResponse.loading());
    }


}
