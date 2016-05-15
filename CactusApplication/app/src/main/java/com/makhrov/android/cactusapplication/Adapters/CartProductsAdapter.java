package com.makhrov.android.cactusapplication.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makhrov.android.cactusapplication.Fragments.FragmentCart;
import com.makhrov.android.cactusapplication.OrderActivity;
import com.makhrov.android.cactusapplication.R;
import com.makhrov.android.cactusapplication.WEB.dto.Product;
import com.makhrov.android.cactusapplication.WEB.utils.BitmapWorkerTask;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;

/**
 * Created by Anton on 14.05.2016.
 */
public class CartProductsAdapter extends RecyclerView.Adapter<CartProductsAdapter.ViewHolder> {
    public ArrayList<Product> products;
    OrderActivity orderActivity;
    static int nowPrice = 0;

    public CartProductsAdapter(ArrayList<Product> products, OrderActivity orderActivity) {
        this.products = products;
        this.orderActivity = orderActivity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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

        holder.countView.setText("x" + String.valueOf(product.getInCart()));


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, products.size());
                calculatePrice();
            }
        });
//        holder.productPreview.setLayoutParams(new LinearLayout.LayoutParams(OrderActivity.metrics.widthPixels,product.getHeight()*OrderActivity.metrics.widthPixels/product.getWidth()));

        //Intent for VidViewActivity
//        holder.productPreview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(orderActivity);
//                alertDialog.setTitle("Add this product to cart?" );
//
//                LinearLayout layout = new LinearLayout(orderActivity);
//                layout.setPadding(15, 15, 15, 15);
//                layout.setOrientation(LinearLayout.VERTICAL);
//                layout.setGravity(Gravity.CENTER_HORIZONTAL);
//                alertDialog.setView(layout);
//
//                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        android.os.Process.killProcess(android.os.Process.myPid());
//                    }
//                });
//
//                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        return;
//                    }
//                });
//
//                alertDialog.show();
//            }
//        });
//        super.bindViewHolder(holder, position);
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
        private TextView countView;
        private Button deleteButton;


        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.product_name_view);
            price = (TextView) itemView.findViewById(R.id.product_price_view);
            productPreview = (ImageView) itemView.findViewById(R.id.product_image_view);
            progressBar = (ProgressBar) itemView.findViewById(R.id.preview_progress_bar);
            countView = (TextView) itemView.findViewById(R.id.product_count_view);
            deleteButton = (Button) itemView.findViewById(R.id.delete_product_button);
        }
    }

    public void addItem(Product product) {
        int position = 0;
        nowPrice = nowPrice + product.getCost();
        int index = products.indexOf(product);
        if(index == -1){
            product.setInCart(1);
            products.add(product);
        } else {
            products.get(index).setInCart(products.get(index).getInCart() + 1);
        }
        notifyItemInserted(position);
        calculatePrice();
    }

    public void calculatePrice(){
        nowPrice = 0;
        for (Product p : products){
            nowPrice += p.getCost()*p.getInCart();
        }
        ((FragmentCart)orderActivity.getFragmentsInOrderActivity()[1]).totalTextView.setText(nowPrice + " ₴");
    }
}
