package com.example.veggiproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class my_cart_adapter_class extends FirebaseRecyclerAdapter<cart_item_model_class, my_cart_adapter_class.myViewHolder> {

    public my_cart_adapter_class(@NonNull FirebaseRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull cart_item_model_class model) {
        holder.name.setText(model.getName());
        holder.quantity.setText(model.getQuantity());
        holder.price.setText(model.getPrice());
        Glide.with(holder.vegImage.getContext()).load(model.getImage()).into(holder.vegImage);
        
        holder.delImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "You clicked on delete button", Toast.LENGTH_SHORT).show();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String currentUser = firebaseUser.getUid();
                DatabaseReference cartList = FirebaseDatabase.getInstance().getReference().child("cartList");
                cartList.child("User view").child(currentUser).child("products").child(model.getVid()).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(v.getContext(), "One item deleted", Toast.LENGTH_SHORT).show();
                                    
                                }else{
                                    Toast.makeText(v.getContext(), "Incomplete", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                
         
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item, parent, false);
        return new myViewHolder(view);
    }


    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView quantity, price, name;
        ImageView vegImage, delImage ;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.cart_item_name);
            quantity = (TextView) itemView.findViewById(R.id.cart_item_quantity);
            price = (TextView) itemView.findViewById(R.id.cart_item_price);
            vegImage = (ImageView) itemView.findViewById(R.id.cart_item_img);
            delImage = (ImageView) itemView.findViewById(R.id.cart_item_delete);

        }
    }
}
