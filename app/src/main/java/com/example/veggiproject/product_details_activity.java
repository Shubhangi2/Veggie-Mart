package com.example.veggiproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

public class product_details_activity extends AppCompatActivity {
    private ImageView plus, minus;
    private TextView elegant_num;
    int total_quantity=1;
    private String vegetable_id = "";

    private Button addToCartButton;

    private ImageView vimage;
    private TextView vname, vprice, getImage_product_detail;
    private FirebaseAuth firebaseAuth;
    private ImageView black_fav, red_fav;
    private String quantity, image;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        vegetable_id = getIntent().getStringExtra("vid");
        plus = findViewById(R.id.elegant_plus_img);
        minus = findViewById(R.id.elegant_minus_img);
        elegant_num = findViewById(R.id.elegant_number);

        vname = findViewById(R.id.product_detail_name);
        vprice = findViewById(R.id.product_detail_price);
        vimage = findViewById(R.id.product_detail_img);
        getImage_product_detail = findViewById(R.id.getImage_product_detail);
        firebaseAuth = FirebaseAuth.getInstance();

        addToCartButton = findViewById(R.id.product_detail_add_to_cart_btn);

        black_fav = findViewById(R.id.black_favourite);
        red_fav = findViewById(R.id.red_favourite);

        black_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                black_fav.setVisibility(View.GONE);
                red_fav.setVisibility(View.VISIBLE);
                add_to_favourites();
            }
        });

        red_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                black_fav.setVisibility(View.VISIBLE);
                red_fav.setVisibility(View.GONE);
                remove_from_favorites();
            }
        });


        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adding_to_cartlist();
            }
        });


        get_product_detail(vegetable_id); //called a method to show the details
        set_default_favourite_icon(vegetable_id);

        //clicklistener for elegant number button
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(total_quantity< 20){
                    total_quantity++;
                    elegant_num.setText(String.valueOf(total_quantity));
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(total_quantity>0){
                    total_quantity--;
                    elegant_num.setText(String.valueOf(total_quantity));
                }
            }
        });

    }

    void get_product_detail(String name){
        DatabaseReference vegetable_ref = FirebaseDatabase.getInstance().getReference().child("Vegetables");
       vegetable_ref.child(vegetable_id).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()){
//                   Toast.makeText(getApplicationContext(), "yes snapshot exist", Toast.LENGTH_SHORT).show();
                   home_model_class model_instance = snapshot.getValue(home_model_class.class);
                   vname.setText(model_instance.getName());
                   vprice.setText(model_instance.getPrice());
                   Glide.with(vimage.getContext()).load(model_instance.getImage()).into(vimage);
                   getImage_product_detail.setText(model_instance.getImage());
                  quantity = model_instance.getQuantity();
                  image = model_instance.getImage();
               }
               else{
                   Toast.makeText(getApplicationContext(), "Data not fetched successfully", Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });


    }

    private void adding_to_cartlist(){

        String save_current_date, save_current_time;
        Calendar cal_for_date = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        save_current_date = currentDate.format(cal_for_date.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        save_current_time = currentTime.format(cal_for_date.getTime());
//        Toast.makeText(this, save_current_time+"  "+save_current_date, Toast.LENGTH_SHORT).show();

        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("cartList");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("name",vname.getText().toString());
        cartMap.put("price", vprice.getText().toString());
        cartMap.put("date", save_current_date);
        cartMap.put("time",save_current_time);
        cartMap.put("quantity",elegant_num.getText().toString());
        cartMap.put("vid", vegetable_id);
        cartMap.put("status", "not shipped");
        cartMap.put("image",getImage_product_detail.getText().toString());

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentuser = firebaseUser.getUid();
        Toast.makeText(getApplicationContext(), currentuser, Toast.LENGTH_SHORT).show();


        cartListRef.child("User view").child(currentuser).child("products").child(vegetable_id)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            cartListRef.child("Admin view").child(currentuser).child("Products").child(vegetable_id)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(), "successful task", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(getApplicationContext(), "failed 2nd task", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(getApplicationContext(), "failed first task", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    
    
    private void add_to_favourites(){
//        Toast.makeText(this, "Data added to favourites", Toast.LENGTH_SHORT).show();

        DatabaseReference favourites = FirebaseDatabase.getInstance().getReference().child("favourites");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = firebaseUser.getUid();


        HashMap<String, Object> favMap = new HashMap<>();
        favMap.put("name", vname.getText().toString());
        favMap.put("price", vprice.getText().toString());
        favMap.put("quantity", quantity);
        favMap.put("vid", vegetable_id);
        favMap.put("image", image);

        favourites.child(currentUser).child(vegetable_id)
                .updateChildren(favMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(product_details_activity.this, "favourites add database", Toast.LENGTH_SHORT).show();
                        }else{
                            String error = task.getException().toString();
                            Toast.makeText(product_details_activity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    
    
    private void remove_from_favorites(){
//        Toast.makeText(this, "Data removed from favourtes", Toast.LENGTH_SHORT).show();

        DatabaseReference rem_favourite = FirebaseDatabase.getInstance().getReference().child("favourites");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = firebaseUser.getUid();

        rem_favourite.child(currentUser).child(vegetable_id)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(product_details_activity.this, "removed from favourites", Toast.LENGTH_SHORT).show();
                        }else{
                            String error = task.getException().toString();
                            Toast.makeText(product_details_activity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        
    }

    private void set_default_favourite_icon(String veg_id){
        DatabaseReference checkFav = FirebaseDatabase.getInstance().getReference().child("favourites");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = firebaseUser.getUid();

        checkFav.child(currentUser).child(vegetable_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            red_fav.setVisibility(View.VISIBLE);
                        }else{
                            black_fav.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}