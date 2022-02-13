package com.example.veggiproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.ProtectionDomain;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddItemActivity extends AppCompatActivity {
    String name, price, quantity, desc;
    private ImageView input_image;
    private TextView input_name, input_price, input_quantity, input_desc;
    private Button addItem_button;
    private ProgressDialog loadingBar;
    private Uri imageURI;
    private String productRandomKey, downloadImageUri;
    private String savecurrentDate, savecurrrentTime;
    private StorageReference ProductImagesRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        loadingBar = new ProgressDialog(this);

        input_image = findViewById(R.id.admin_image_additem);
        input_name = findViewById(R.id.admin_addItem_name);
        input_price = findViewById(R.id.admin_addItem_price);
        input_quantity = findViewById(R.id.admin_addItem_quantity);
        input_desc = findViewById(R.id.admin_addItem_descritpion);
        addItem_button = findViewById(R.id.admin_addItem_btn);
        
        addItem_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateItems();
            }
        });

        ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                    input_image.setImageURI(result);
            }
        });


        input_image.setOnClickListener(View -> launcher.launch("image/*"));
        
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageURI = data.getData();
        input_image.setImageURI(imageURI);
    }

    public void validateItems(){
        name = input_name.getText().toString();
        price = input_price.getText().toString();
        quantity = input_quantity.getText().toString();
        desc = input_desc.getText().toString();

        if(input_image == null){
            Toast.makeText(getApplicationContext(), "Image is mandatory", Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(name)){
            Toast.makeText(getApplicationContext(), "Name is mandatory", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(price)){
            Toast.makeText(getApplicationContext(), "price is mandatory", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(quantity)){
            Toast.makeText(getApplicationContext(), "quantity is mandatory", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(desc)){
            Toast.makeText(getApplicationContext(), "description is mandaotry", Toast.LENGTH_SHORT).show();
        }else{
            storeProductInformation();
        }
    }

    public void storeProductInformation(){
        loadingBar.setTitle("Uploading data");
        loadingBar.setMessage("Dear admin, Please while we are uploading the data to database");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy ");
        savecurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        savecurrrentTime = currentTime.format(calendar.getTime());

        productRandomKey = savecurrentDate + savecurrrentTime;

        StorageReference filePath = ProductImagesRef.child(imageURI.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageURI);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(getApplicationContext(), "Error:" + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Image stored successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();

                        }
                        downloadImageUri = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){

                                downloadImageUri = task.getResult().toString();
                                Toast.makeText(getApplicationContext(), "getting product image uri successfully", Toast.LENGTH_SHORT).show();
                                addProductItems();
                            }
                    }
                });
            }
        });
    }

    public void addProductItems(){
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("vid",productRandomKey);
        productMap.put("date", savecurrentDate);
        productMap.put("time", savecurrrentTime);
        productMap.put("image", downloadImageUri);
        productMap.put("name", name);
        productMap.put("price", price);
        productMap.put("quantity", quantity);
        productMap.put("description", desc);

        DatabaseReference vegetables = FirebaseDatabase.getInstance().getReference().child("Vegetables");
        
        vegetables.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(), "Items added successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(), "Items are not getting added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

 
    }


