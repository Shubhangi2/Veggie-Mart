package com.example.veggiproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class order_myViewHolder extends RecyclerView.ViewHolder {
    TextView shipping_status, date, price, quantity, totalAmount;
    ImageView orderImage;
    public order_myViewHolder(@NonNull View itemView) {
        super(itemView);
        shipping_status = (TextView) itemView.findViewById(R.id.shipping_status);
        date = (TextView) itemView.findViewById(R.id.myOrder_date);
        price = (TextView) itemView.findViewById(R.id.order_item_price);
        quantity = (TextView) itemView.findViewById(R.id.order_quantity);
        totalAmount = (TextView) itemView.findViewById(R.id.total_amount_ofOrder);
        orderImage = (ImageView) itemView.findViewById(R.id.myOrder_img);
    }
}
