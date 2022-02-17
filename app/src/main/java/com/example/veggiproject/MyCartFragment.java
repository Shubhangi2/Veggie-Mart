package com.example.veggiproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCartFragment extends Fragment {
    private RecyclerView recyclerView;
    private Button nxt_button;
    private TextView total_amount_txt;
    private RecyclerView.LayoutManager layoutManager;
    private int overallPrice = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyCartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyCartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCartFragment newInstance(String param1, String param2) {
        MyCartFragment fragment = new MyCartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);
        recyclerView = view.findViewById(R.id.my_cart_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        nxt_button = view.findViewById(R.id.cart_next_button);
        total_amount_txt = view.findViewById(R.id.total_amount_txt);

        total_amount_txt.setText(String.valueOf(overallPrice));
        Toast.makeText(getContext(), "total" + overallPrice, Toast.LENGTH_SHORT).show();


        nxt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), confirm_final_order.class);
                intent.putExtra("Total Price", String.valueOf(overallPrice));
                startActivity(intent);
            }
        });



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference dbquery = FirebaseDatabase.getInstance().getReference().child("cartList");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = firebaseUser.getUid();

        FirebaseRecyclerOptions<cart_item_model_class> options =
                new FirebaseRecyclerOptions.Builder<cart_item_model_class>()
                        .setQuery(dbquery.child("User view").child(currentUser).child("products"), cart_item_model_class.class)
                        .build();


        FirebaseRecyclerAdapter<cart_item_model_class, cart_myViewHolder>  adapter = new FirebaseRecyclerAdapter<cart_item_model_class, cart_myViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull cart_myViewHolder holder, int position, @NonNull cart_item_model_class model) {
                holder.name.setText(model.getName());
                holder.quantity.setText(model.getQuantity());
                holder.price.setText(model.getPrice());

                int oneProductPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                overallPrice = overallPrice + oneProductPrice;

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
            public cart_myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item, parent, false);
                return new cart_myViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}