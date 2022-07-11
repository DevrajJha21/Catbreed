package com.example.catbreed;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;

import com.example.catbreed.api.RxSingleSchedulers;
import com.example.catbreed.network.NetworkResponse;
import com.example.catbreed.network.Status;
import com.example.catbreed.repo.Repository;
import com.example.catbreed.ui.main.MainViewModel;
import com.example.catbreed.ui.main.model.BreedsDatum;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

@ExtendWith(InstantExecutorExtension.class)
public class MainViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

// Mocking object
    @Mock
    Repository apiClient;
    private MainViewModel viewModel;
    @Mock
    Observer<NetworkResponse> observer;
    @Mock
    LifecycleOwner lifecycleOwner;
    Lifecycle lifecycle;

    @BeforeClass
    public static void setUpRxSchedulers() {
        Scheduler immediate = new Scheduler() {
            @Override
            public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Scheduler.Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }

        };

        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);
    }

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        lifecycle = new LifecycleRegistry(lifecycleOwner);
        viewModel = new MainViewModel(apiClient, RxSingleSchedulers.TEST_SCHEDULER);
        viewModel.getBreedResponseLiveData().observeForever(observer);
    }

    @Test
    public void testNull() {
        when(apiClient.getCatBreeds()).thenReturn(null);
        assertNotNull(viewModel.getBreedResponseLiveData());
        assertTrue(viewModel.getBreedResponseLiveData().hasObservers());
    }

    public static final String ASSET_BASE_PATH = "../app/src/main/assets/";

    String getJson(String path) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(ASSET_BASE_PATH + path)));
            String line  = br.readLine();
            while (line != null) {
                sb.append(line);
                    line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return sb.toString();
    }
// making a local catbreed response list from android assests
    private List<BreedsDatum> getList() {
        String sampleResponse = getJson("bridresponse.json");
        ArrayList<BreedsDatum> logs = new Gson().fromJson(sampleResponse, new TypeToken<List<BreedsDatum>>() {
        }.getType());

        return logs;
    }

    @Test
    public void testApiFetchDataSuccess() {
        // Mock API response
        when(apiClient.getCatBreeds()).thenReturn(Single.just(getList()));
        viewModel.getCatBreeds();
        assertEquals(viewModel.getBreedResponseLiveData().getValue().status, Status.SUCCESS);
        List<BreedsDatum> expected = ((List<BreedsDatum>) viewModel.getBreedResponseLiveData().getValue().data);
        assertEquals(expected.size(), getList().size());

    }



    @After
    public void tearDown() throws Exception {
        apiClient = null;
        viewModel = null;
    }
}
