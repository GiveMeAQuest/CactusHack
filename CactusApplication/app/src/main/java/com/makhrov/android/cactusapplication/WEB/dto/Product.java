package com.makhrov.android.cactusapplication.WEB.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Anton on 14.05.2016.
 */
@Getter
@Setter
public class Product {
    int id;
    int cost;
    String name;
    String image;
    int height;
    int width;
    int inCart;
}
