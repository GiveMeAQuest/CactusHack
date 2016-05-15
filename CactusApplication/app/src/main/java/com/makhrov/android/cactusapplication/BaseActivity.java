package com.makhrov.android.cactusapplication;

import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.makhrov.android.cactusapplication.WEB.Api;
import com.makhrov.android.cactusapplication.WEB.ApiService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anton on 14.05.2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public Api provideApi(Gson gson) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.55.33.11:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(Api.class);
    }

    public ApiService provideService(Api vidmeApi) {
        return new ApiService(vidmeApi);
    }
}
