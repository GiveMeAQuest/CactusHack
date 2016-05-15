package com.makhrov.android.cactusapplication.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DialogTitle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makhrov.android.cactusapplication.Adapters.CartProductsAdapter;
import com.makhrov.android.cactusapplication.Adapters.ProductsAdapter;
import com.makhrov.android.cactusapplication.OrderActivity;
import com.makhrov.android.cactusapplication.R;
import com.makhrov.android.cactusapplication.WEB.Api;
import com.makhrov.android.cactusapplication.WEB.ApiService;
import com.makhrov.android.cactusapplication.WEB.dto.Order;
import com.makhrov.android.cactusapplication.WEB.dto.Product;
import com.makhrov.android.cactusapplication.WEB.dto.ProductsResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Action1;

/**
 * Created by Anton on 14.05.2016.
 */
public class FragmentCart extends android.support.v4.app.Fragment {


    public static CartProductsAdapter productsAdapter;



    public static TextView totalTextView;
    static ApiService apiService;
    static Api api;

    @OnClick(R.id.order_button)
    public void onOrderClick(){
        getActivity().showDialog(0);
    }

    public static void makeOrder(int myHour, int myMinute){
        String counts = "";
        String ids = "";
        for(Product p : productsAdapter.products){
            ids += (String.valueOf(p.getId())) + " ";
            counts += (String.valueOf(p.getInCart())) + " ";
        }
        System.out.println("something");
        apiService.makeOrder(OrderActivity.username,myHour + ":" + myMinute,counts,ids)
                .subscribe(new Action1<Order>() {
                    @Override
                    public void call(Order order) {
                        Toast.makeText(OrderActivity.self,"Your order approved",Toast.LENGTH_SHORT).show();
                        FragmentOrders.ordersAdapter.orders.add(order);
                        FragmentOrders.ordersAdapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        return;
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cart_layout, container, false);

        api = provideApi(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create());

        apiService = provideService(api);

        totalTextView = (TextView) rootView.findViewById(R.id.total_price_text_view);
        productsAdapter = new CartProductsAdapter(new ArrayList<Product>(), OrderActivity.self);
        RecyclerView cartRecyclerView = (RecyclerView) rootView.findViewById(R.id.cart_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        cartRecyclerView.setLayoutManager(llm);
        cartRecyclerView.setAdapter(productsAdapter);

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
