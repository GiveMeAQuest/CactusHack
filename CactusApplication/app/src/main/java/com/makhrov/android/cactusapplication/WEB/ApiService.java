package com.makhrov.android.cactusapplication.WEB;

import com.makhrov.android.cactusapplication.WEB.dto.Order;
import com.makhrov.android.cactusapplication.WEB.dto.Product;
import com.makhrov.android.cactusapplication.WEB.dto.ProductsResponse;
import com.makhrov.android.cactusapplication.WEB.dto.User;
import com.makhrov.android.cactusapplication.WEB.dto.UserResponse;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Anton on 14.05.2016.
 */
public class ApiService {

    private Api api;

    public ApiService(Api api) {
        this.api = api;
    }

    public Observable<ProductsResponse> getAvailableProducts() {
        return addSchedulers(api.getAvailableProducts());
    }

    public Observable<User> getIdAndLogIn(String name){
        return addSchedulers(api.getIdAndLogIn(name));
    }

//    public Observable<Order> makeOrder(String username, String prepareTime, ArrayList<Product> products){
//        return addSchedulers(api.makeOrder(username,prepareTime,products));
//    }

    public Observable<Order> makeOrder(String username, String prepareTime, String counts, String ids) {
        return addSchedulers(api.makeOrder(username,prepareTime, counts, ids));
    }

    private <T> Observable<T> addSchedulers(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
