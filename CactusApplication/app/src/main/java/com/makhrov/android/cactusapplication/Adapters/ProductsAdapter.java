package com.makhrov.android.cactusapplication.Adapters;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makhrov.android.cactusapplication.Fragments.FragmentCart;
import com.makhrov.android.cactusapplication.MainActivity;
import com.makhrov.android.cactusapplication.OrderActivity;
import com.makhrov.android.cactusapplication.R;
import com.makhrov.android.cactusapplication.WEB.dto.Product;
import com.makhrov.android.cactusapplication.WEB.utils.BitmapWorkerTask;

import java.util.List;

/**
 * Created by Anton on 14.05.2016.
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    List<Product> products;
    private Context ctx;
    OrderActivity orderActivity;

    public ProductsAdapter(List<Product> products, Context ctx, OrderActivity orderActivity) {
        this.products = products;
        this.ctx = ctx;
        this.orderActivity = orderActivity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_item, parent, false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Product product = products.get(position);

        if(product.getName() != null){
            holder.name.setText(product.getName());
        }

        holder.price.setText(String.valueOf(product.getCost())+" ₴");

        //There is an issue with AsyncTask and RecycleView, so i had to clean out preview image first
        holder.productPreview.setImageDrawable(null);
        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(holder.productPreview,product.getImage(),holder.progressBar);
        bitmapWorkerTask.execute();

        System.out.println("" + OrderActivity.metrics.widthPixels + " hehehe" + product.getHeight()*OrderActivity.metrics.widthPixels/product.getWidth());
        holder.productPreview.setLayoutParams(new LinearLayout.LayoutParams(OrderActivity.metrics.widthPixels,630));
//        holder.productPreview.setLayoutParams(new LinearLayout.LayoutParams(OrderActivity.metrics.widthPixels,product.getHeight()*OrderActivity.metrics.widthPixels/product.getWidth()));

        //Intent for VidViewActivity
        holder.productPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(orderActivity);
                alertDialog.setTitle("Add this product to cart?" );

                LinearLayout layout = new LinearLayout(orderActivity);
                layout.setPadding(15, 15, 15, 15);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CENTER_HORIZONTAL);
                alertDialog.setView(layout);

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((FragmentCart)(orderActivity.getFragmentsInOrderActivity()[1])).productsAdapter.addItem(products.get(position));
                        ((FragmentCart)(orderActivity.getFragmentsInOrderActivity()[1])).productsAdapter.notifyDataSetChanged();
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
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView price;
        private ImageView productPreview;
        private ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.product_name_view);
            price = (TextView) itemView.findViewById(R.id.product_price_view);
            productPreview = (ImageView) itemView.findViewById(R.id.product_image_view);
            progressBar = (ProgressBar) itemView.findViewById(R.id.preview_progress_bar);
        }
    }

}
