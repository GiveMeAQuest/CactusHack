package com.makhrov.android.cactusapplication.WEB.dto;

import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Anton on 15.05.2016.
 */
@Getter @Setter
public class Order {
    String id;
    @SerializedName("order_time")
    String orderTime;
    @SerializedName("prepare_time")
    String prepareTime;
    @SerializedName("access_token")
    String accessToken;
    ArrayList<Product> products;
}
