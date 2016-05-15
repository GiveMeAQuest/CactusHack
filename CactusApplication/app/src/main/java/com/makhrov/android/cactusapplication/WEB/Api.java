package com.makhrov.android.cactusapplication.WEB;

import com.makhrov.android.cactusapplication.WEB.dto.Order;
import com.makhrov.android.cactusapplication.WEB.dto.Product;
import com.makhrov.android.cactusapplication.WEB.dto.ProductsResponse;
import com.makhrov.android.cactusapplication.WEB.dto.User;
import com.makhrov.android.cactusapplication.WEB.dto.UserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Anton on 14.05.2016.
 */
public interface Api {

    @GET("products/")
    Observable<ProductsResponse> getAvailableProducts();


//    @POST("orders/")
//    Observable<Order> makeOrder(@Query("username") String userID, @Query("prepare_time") String prepareTime, @QueryMap("products") ArrayList<Product> products);

    @GET("login/{name}/")
    Observable<User> getIdAndLogIn(@Path("name") String name);

    @FormUrlEncoded
    @POST("orders/")
    Observable<Order> makeOrder(@Field("username") String username, @Field("prepare_time")String prepareTime, @Field("count") String count, @Field("id") String id);
}
