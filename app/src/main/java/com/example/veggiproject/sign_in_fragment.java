package com.example.veggiproject;

import static com.example.veggiproject.registerActivity.on_reset_password_fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link sign_in_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sign_in_fragment extends Fragment {
    private TextView dont_have_account;
    private FrameLayout parentFramelayout;

    private EditText email;
    private EditText password;

    private Button sign_in;
    private ImageButton close;
    private TextView forgot_pwd;

    private String email_Pattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    private FirebaseAuth firebaseAuth;
    private ProgressDialog loading_bar;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public sign_in_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sign_in_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static sign_in_fragment newInstance(String param1, String param2) {
        sign_in_fragment fragment = new sign_in_fragment();
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
        View view = inflater.inflate(R.layout.fragment_sign_in_fragment, container, false);
        dont_have_account = view.findViewById(R.id.dont_have_account);
        parentFramelayout = getActivity().findViewById(R.id.frameLayout);

        email = view.findViewById(R.id.email_sign_in);
        password = view.findViewById(R.id.password_sign_in);

        sign_in = view.findViewById(R.id.sign_in_btn);
        close = view.findViewById(R.id.close_sign_in);

        forgot_pwd = view.findViewById(R.id.forgot_pwd_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();
        loading_bar = new ProgressDialog(getContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dont_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new sing_up_fragment());
            }
        });

        forgot_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on_reset_password_fragment = true;
                setFragment(new reset_password());
            }
        });


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_email_and_password();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_intent();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFramelayout.getId(), fragment);
        fragmentTransaction.commit();
    }
    private void checkinputs(){
        if(!TextUtils.isEmpty(email.getText().toString())){
            if(!TextUtils.isEmpty(password.getText().toString())){
                sign_in.setEnabled(true);
            }else{
                sign_in.setEnabled(false);
            }
        }else{
            sign_in.setEnabled(false);
        }
    }
    private void check_email_and_password() {
        if(email.getText().toString().matches(email_Pattern)){
            if(password.length() >= 8){
                        sign_in.setEnabled(false);

                        loading_bar.setTitle("signing into your account");
                        loading_bar.setMessage("Please wait, while signing into your account");
                        loading_bar.setCanceledOnTouchOutside(false);
                        loading_bar.show();

                        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getContext(), "Signed in successfully", Toast.LENGTH_SHORT).show();
                                            loading_bar.dismiss();

                                            DatabaseReference admin = FirebaseDatabase.getInstance().getReference().child("Admin");
                                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                            String currentUser = firebaseUser.getUid();

                                            admin.child(currentUser).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        Intent intent = new Intent(getActivity(), adminActivity.class);
                                                        startActivity(intent);
                                                        getActivity().finish();

                                                    }else{
                                                        main_intent();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });



                                        }
                                        else{
                                            sign_in.setEnabled(true);
                                            String error = task.getException().getMessage();
                                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
            }else{
                Toast.makeText(getActivity(), "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        }else{
                Toast.makeText(getActivity(), "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
    private void main_intent(){
        Intent main_intent = new Intent(getActivity(), MainActivity.class);
        startActivity(main_intent);
        getActivity().finish();
    }
}