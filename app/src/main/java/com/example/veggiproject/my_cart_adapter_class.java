package com.example.veggiproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class my_cart_adapter_class extends FirebaseRecyclerAdapter<cart_item_model_class, my_cart_adapter_class.myViewHolder> {

    public my_cart_adapter_class(@NonNull FirebaseRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull cart_item_model_class model) {
        holder.name.setText(model.getName());
        holder.quantity.setText(model.getQuantity());
        holder.price.setText(model.getPrice());
        Glide.with(holder.image.getContext()).load(model.getImage()).into(holder.image);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item, parent, false);
        return new myViewHolder(view);
    }


    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView quantity, price, name;
        ImageView image;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.cart_item_name);
            quantity = (TextView) itemView.findViewById(R.id.cart_item_quantity);
            price = (TextView) itemView.findViewById(R.id.cart_item_price);
            image = (ImageView) itemView.findViewById(R.id.cart_item_img);

        }
    }
}
