package com.example.veggiproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class cart_myViewHolder extends RecyclerView.ViewHolder {
    TextView quantity, price, name;
    ImageView vegImage, delImage ;

    public cart_myViewHolder(@NonNull View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.cart_item_name);
        quantity = (TextView) itemView.findViewById(R.id.cart_item_quantity);
        price = (TextView) itemView.findViewById(R.id.cart_item_price);
        vegImage = (ImageView) itemView.findViewById(R.id.cart_item_img);
        delImage = (ImageView) itemView.findViewById(R.id.cart_item_delete);

    }
}
