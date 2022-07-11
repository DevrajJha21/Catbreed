package com.example.catbreed.ui.main;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catbreed.R;
import com.example.catbreed.databinding.ActivityMainBinding;
import com.example.catbreed.network.NetworkResponse;
import com.example.catbreed.ui.main.model.BreedsDatum;
import com.example.catbreed.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;
    private ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private CatBreedAdapter catBreedAdapter;
    private ArrayList<BreedsDatum> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        initUiComponent();
        mainViewModel.getCatBreeds();
        observeCatBreedResponse();
    }
    // initialiazation of ui component
    private void initUiComponent() {
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        catBreedAdapter = new CatBreedAdapter(list);
        recyclerView.setAdapter(catBreedAdapter);
    }
    // calling the cat breed listing api
    private void observeCatBreedResponse(){

        mainViewModel.getBreedResponseLiveData().observe(this, new Observer<NetworkResponse>() {
            @Override
            public void onChanged(NetworkResponse networkResponse) {
                switch (networkResponse.status) {
                    case LOADING:
                        //showing progess bar
                        AppUtils.showLoader(MainActivity.this);
                        break;
                    case SUCCESS:
                        //hiding progress bar on success response
                        AppUtils.hideLoader();
                        catBreedAdapter.addBreedDataList((List<BreedsDatum>) networkResponse.data);
                        break;
                    case ERROR:
                        //hiding progress bar on api error
                        AppUtils.hideLoader();
                        Toast.makeText(MainActivity.this, networkResponse.error.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }




}