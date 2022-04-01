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

public class fav_adapterClass extends FirebaseRecyclerAdapter<fav_model_class, fav_adapterClass.myViewHolder> {

    public fav_adapterClass(@NonNull FirebaseRecyclerOptions<fav_model_class> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull fav_model_class model) {
      holder.name.setText(model.getName());
      holder.price.setText(model.getPrice());
      holder.quantity.setText(model.getQuantity());
      Glide.with(holder.image.getContext()).load(model.getImage()).into(holder.image);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_single_item, parent, false);
       return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, price, quantity;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.favourite_vegName);
            price = (TextView) itemView.findViewById(R.id.favourite_vegPrice);
            quantity = (TextView) itemView.findViewById(R.id.favourite_veg_quantity);
            image = (ImageView) itemView.findViewById(R.id.favourite_imageView);
        }
    }
}
