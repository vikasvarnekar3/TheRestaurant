package com.example.therestaurant.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.therestaurant.Interface.ItemClickListner;
import com.example.therestaurant.R;

public class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtTableName, txtTotalPrice;
    public Button ProductList;
    private ItemClickListner itemClickListner;

    public OrdersViewHolder(@NonNull View itemView)
    {
        super(itemView);

        txtTableName = itemView.findViewById(R.id.waiter_cart_table_name);
        txtTotalPrice = itemView.findViewById(R.id.waiter_cart_total_price);
        ProductList = itemView.findViewById(R.id.waiter_cart_product_list);
    }

    @Override
    public void onClick(View v)
    {
        itemClickListner.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;
    }
}
