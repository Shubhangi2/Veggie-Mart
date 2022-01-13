package com.example.veggiproject;

import android.content.Intent;
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

public class home_adapter_class extends FirebaseRecyclerAdapter<home_model_class, home_adapter_class.myViewHolder> {
    public home_adapter_class(@NonNull FirebaseRecyclerOptions<home_model_class> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull home_model_class model) {
        holder.name.setText(model.getName());
        holder.price.setText(model.getPrice());
        holder.quantity.setText(model.getQuantity());
        Glide.with(holder.image.getContext()).load(model.getImage()).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), product_details_activity.class);
                intent.putExtra("vid", model.getVid());
                v.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_adapter_layout,parent, false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView name, price, quantity;
        ImageView image;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.home_adp_name);
            price = (TextView) itemView.findViewById(R.id.home_adp_price);
            quantity = (TextView) itemView.findViewById(R.id.home_adp_quantity);
            image = (ImageView) itemView.findViewById(R.id.home_adp_img);
        }
    }
}
