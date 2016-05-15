package com.makhrov.android.cactusapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.makhrov.android.cactusapplication.Fragments.FragmentCart;
import com.makhrov.android.cactusapplication.Fragments.FragmentOrders;
import com.makhrov.android.cactusapplication.Fragments.FragmentProducts;
import com.makhrov.android.cactusapplication.WEB.Api;
import com.makhrov.android.cactusapplication.WEB.ApiService;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Anton on 14.05.2016.
 */
public class OrderActivity extends BaseActivity {


    public static DisplayMetrics metrics = new DisplayMetrics();
    public static OrderActivity self;
    public static String username;
    public static String userID;

    ApiService apiService;
    Api api;
//
//    @Bind(R.id.my_recycler_view)
//    protected RecyclerView recyclerView;

    @Bind(R.id.product_tabs_pager)
    public ViewPager pager;

    public static SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        self = this;
//        api = provideApi(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
//                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//                .create());

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(sectionsPagerAdapter);
        pager.setOffscreenPageLimit(2);

        username = getIntent().getExtras().get("username").toString();
        userID = getIntent().getExtras().getString("userid").toString();

//        apiService = provideService(api);
//
//        apiService.getAvailableProducts().subscribe(new Action1<ProductsResponse>() {
//            @Override
//            public void call(ProductsResponse productsResponse) {
//                ProductsAdapter productsAdapter = new ProductsAdapter(productsResponse.getProducts(),getApplicationContext(),self);
//                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
//                llm.setOrientation(LinearLayoutManager.VERTICAL);
//                recyclerView.setLayoutManager(llm);
//                recyclerView.setAdapter(productsAdapter);
//            }
//        });
    }

    static class SectionsPagerAdapter extends FragmentPagerAdapter {


        public static Fragment[] fragments = {
                new FragmentProducts(),
                new FragmentCart(),
                new FragmentOrders()
        };

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        public static Fragment[] getFragments(){
            return fragments;
        }
    }

    public Fragment[] getFragmentsInOrderActivity(){
        return sectionsPagerAdapter.getFragments();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Quit?" );

        LinearLayout layout = new LinearLayout(this);
        layout.setPadding(15, 15, 15, 15);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        alertDialog.setView(layout);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        alertDialog.show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        TimePickerDialog tpd = new TimePickerDialog(this, myCallBack, 14, 6, true);
        return tpd;
    }

    TimePickerDialog.OnTimeSetListener myCallBack = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            FragmentCart.makeOrder(hourOfDay,minute);
        }
    };
}
