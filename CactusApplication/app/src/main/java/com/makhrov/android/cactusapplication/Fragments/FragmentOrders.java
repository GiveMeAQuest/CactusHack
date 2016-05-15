package com.makhrov.android.cactusapplication.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.makhrov.android.cactusapplication.Adapters.CartProductsAdapter;
import com.makhrov.android.cactusapplication.Adapters.OrdersAdapter;
import com.makhrov.android.cactusapplication.OrderActivity;
import com.makhrov.android.cactusapplication.R;
import com.makhrov.android.cactusapplication.WEB.dto.Order;
import com.makhrov.android.cactusapplication.WEB.dto.Product;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Anton on 15.05.2016.
 */
public class FragmentOrders extends android.support.v4.app.Fragment {

    public static OrdersAdapter ordersAdapter;
    @Bind(R.id.orders_list_view)
    RecyclerView ordersView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_orders_layout,container,false);

        ordersAdapter = new OrdersAdapter(new ArrayList<Order>(),getContext());
        final RecyclerView ordersRecyclerView = (RecyclerView) rootView.findViewById(R.id.orders_list_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ordersRecyclerView.setLayoutManager(llm);
        ordersRecyclerView.setAdapter(ordersAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
    }
}
