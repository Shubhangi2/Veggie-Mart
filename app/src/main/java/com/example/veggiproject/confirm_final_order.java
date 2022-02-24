
package com.example.veggiproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Map;

public class confirm_final_order extends AppCompatActivity {
    private String final_amount;
    private Button confirm;
    private EditText name, phone, address, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        name = findViewById(R.id.confirm_user_name);
        phone = findViewById(R.id.confirm_user_phone);
        address = findViewById(R.id.confirm_user_address);
        city = findViewById(R.id.confirm_city_name);
        confirm = findViewById(R.id.confirm_btn);

        final_amount = getIntent().getStringExtra("Total Price");
        Toast.makeText(getApplicationContext(), "total amount: "+final_amount, Toast.LENGTH_SHORT).show();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    public void check(){
        if(TextUtils.isEmpty(name.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please provide your name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(phone.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please provide your phone number", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(address.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please provide your address", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(city.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please provide your city name", Toast.LENGTH_SHORT).show();
        }else{
            confirmUserOrder();
        }
    }

    public void confirmUserOrder(){
        final String saveCurrentDate, saveCurrentTime;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calendar.getTime());

        DatabaseReference order = FirebaseDatabase.getInstance().getReference().child("Orders");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = firebaseUser.getUid();
        String currentUserEmail = firebaseUser.getEmail();
//        String order_id = "orderId " + saveCurrentDate + saveCurrentTime;

        String arr[] = currentUserEmail.split("@");
        String order_id = arr[0];

        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("totalAmount", final_amount);
        orderMap.put("name", name.getText().toString());
        orderMap.put("phone", phone.getText().toString());
        orderMap.put("address", address.getText().toString());
        orderMap.put("city", city.getText().toString());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("order_id", order_id);

//        order.child(currentUser).child(order_id).updateChildren(orderMap)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("cartList")
//                                    .child("User view")
//                                    .child(currentUser)
//                                    .removeValue()
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if(task.isSuccessful()){
//                                                Intent intent = new Intent(confirm_final_order.this, MainActivity.class);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                startActivity(intent);
//                                                finish();
//                                            }
//                                        }
//                                    });
//                        }
//                    }
//                });

        DatabaseReference mycartItem = FirebaseDatabase.getInstance().getReference().child("cartList");

        mycartItem.child("User view").child(currentUser).child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map map = (Map)snapshot.getValue();

                DatabaseReference order = FirebaseDatabase.getInstance().getReference().child("Orders");

                order.child(currentUser).child(order_id).child("contact details").updateChildren(orderMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    order.child(currentUser).child(order_id).child("total product details")
                                            .updateChildren(map)
                                            .addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                        if(task.isSuccessful()){

                                                            DatabaseReference myOrder = FirebaseDatabase.getInstance().getReference().child("My Orders");
                                                            myOrder.child(currentUser).updateChildren(map)
                                                                    .addOnCompleteListener(new OnCompleteListener() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task task) {
                                                                            FirebaseDatabase.getInstance().getReference()
                                                                                    .child("cartList")
                                                                                    .child("User view")
                                                                                    .child(currentUser)
                                                                                    .removeValue()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if(task.isSuccessful()){
                                                                                                Toast.makeText(getApplicationContext(), "Your order received successfully", Toast.LENGTH_SHORT).show();
                                                                                                Intent intent = new Intent(confirm_final_order.this,MainActivity.class);
                                                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                                startActivity(intent);
                                                                                                finish();
                                                                                            }
                                                                                        }
                                                                                    });
                                                                        }
                                                                    });


                                                        }
                                                }
                                            });
                                }else{
                                    Toast.makeText(getApplicationContext(), "Data is not stored", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}