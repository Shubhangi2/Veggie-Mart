package com.example.veggiproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrdersFragment extends Fragment {
    private RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        recyclerView = view.findViewById(R.id.myOrder_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference myOrder = FirebaseDatabase.getInstance().getReference().child("My Orders");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = firebaseUser.getUid();

        FirebaseRecyclerOptions<myOrder_model> options =
                new FirebaseRecyclerOptions.Builder<myOrder_model>()
                        .setQuery(myOrder.child(currentUser), myOrder_model.class)
                        .build();

        FirebaseRecyclerAdapter<myOrder_model, order_myViewHolder> adapter = new FirebaseRecyclerAdapter<myOrder_model, order_myViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull order_myViewHolder holder, int position, @NonNull myOrder_model model) {
                holder.date.setText(model.getDate());
                holder.price.setText(model.getPrice());
                holder.quantity.setText(model.getQuantity());
                int oneProductPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                String t_amount = String.valueOf(oneProductPrice);
                holder.totalAmount.setText(t_amount);
                Glide.with(holder.orderImage.getContext()).load(model.getImage()).into(holder.orderImage);


                if((model.getStatus()).equals("shipped")){
                    holder.shipping_status.setText("Product is delivered");
                }
                if(model.getStatus().equals("not shipped")){
                    holder.shipping_status.setText("product will delivered soon");
                }

            }

            @NonNull
            @Override
            public order_myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_single_item_layout, parent, false);
                return new order_myViewHolder(view);
            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}