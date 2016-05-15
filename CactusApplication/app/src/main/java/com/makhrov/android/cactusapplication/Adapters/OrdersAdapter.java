package com.makhrov.android.cactusapplication.Adapters;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makhrov.android.cactusapplication.Fragments.FragmentCart;
import com.makhrov.android.cactusapplication.OrderActivity;
import com.makhrov.android.cactusapplication.R;
import com.makhrov.android.cactusapplication.WEB.dto.Order;
import com.makhrov.android.cactusapplication.WEB.dto.Product;
import com.makhrov.android.cactusapplication.WEB.utils.BitmapWorkerTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Anton on 15.05.2016.
 */
public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder>{

    public static List<Order> orders;
    Context ctx;


    public OrdersAdapter(List<Order> orders, Context ctx) {
        this.orders = orders;
        this.ctx = ctx;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Order order = orders.get(position);

        holder.accessToken.setText("AccessToken: " + order.getAccessToken());
        holder.orderTime.setText("OrderTime: " + order.getOrderTime());
        holder.prepareTime.setText("PrepareTime:" + order.getPrepareTime());

        ArrayList<String> items = new ArrayList<>();
        for(Product p : order.getProducts()){
            items.add(p.getName());
        }
        ArrayAdapter<String> productsAdapter = new ArrayAdapter<String>(ctx,R.layout.products_list_list_item,R.id.products_in_order_item,items);
        holder.productsListView.setAdapter(productsAdapter);
        holder.removeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    orders.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, orders.size());
                } catch (Exception e){
                    return;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView accessToken;
        private TextView orderTime;
        private TextView prepareTime;
        private ListView productsListView;
        private ImageButton removeImageButton;

        public ViewHolder(View itemView) {
            super(itemView);
            accessToken = (TextView) itemView.findViewById(R.id.access_token_view);
            orderTime = (TextView) itemView.findViewById(R.id.order_time_view);
            prepareTime = (TextView) itemView.findViewById(R.id.prepare_time_view);
            productsListView = (ListView) itemView.findViewById(R.id.products_list_view);
            removeImageButton = (ImageButton) itemView.findViewById(R.id.delete_order_hint);
        }
    }

}
