package com.makhrov.android.cactusapplication.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makhrov.android.cactusapplication.Adapters.ProductsAdapter;
import com.makhrov.android.cactusapplication.OrderActivity;
import com.makhrov.android.cactusapplication.R;
import com.makhrov.android.cactusapplication.WEB.Api;
import com.makhrov.android.cactusapplication.WEB.ApiService;
import com.makhrov.android.cactusapplication.WEB.dto.ProductsResponse;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Action1;

/**
 * Created by Anton on 14.05.2016.
 */
public class FragmentProducts extends android.support.v4.app.Fragment {

    @Bind(R.id.products_recycler_view)
    RecyclerView recyclerView;

    ProductsAdapter productsAdapter;

    ApiService apiService;
    Api api;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.products_layout, container, false);

        api = provideApi(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create());

        apiService = provideService(api);

        apiService.getAvailableProducts()
                .subscribe(new Action1<ProductsResponse>() {
                    @Override
                    public void call(ProductsResponse productsResponse) {
                        productsAdapter = new ProductsAdapter(productsResponse.getProducts(),getContext(), OrderActivity.self);
//                        productsAdapter.addAll(productsResponse.getProducts());
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(llm);
                        recyclerView.setAdapter(productsAdapter);
                    }
                });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

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
