package com.example.veggiproject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
//import android.widget.ImageButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link sing_up_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sing_up_fragment extends Fragment {
    private TextView already_have_account;
    private FrameLayout parentFramelayout;

    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirm_password;
    private ImageButton close;
    private Button sign_up;
    private FirebaseAuth firebaseAuth;
    private String email_Pattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    private FirebaseFirestore firebaseFirestore;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public sing_up_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sing_up_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static sing_up_fragment newInstance(String param1, String param2) {
        sing_up_fragment fragment = new sing_up_fragment();
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
        View view = inflater.inflate(R.layout.fragment_sign_up_fragment, container, false);
        parentFramelayout = getActivity().findViewById(R.id.frameLayout);
        already_have_account = view.findViewById(R.id.already_have_account);

        name = view.findViewById(R.id.name_sign_up);
        email = view.findViewById(R.id.email_sign_up);
        password = view.findViewById(R.id.password_sign_up);
        confirm_password = view.findViewById(R.id.confirm_pwd_sign_up);

        close = view.findViewById(R.id.close_sign_up);
        sign_up = view.findViewById(R.id.sign_up_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        already_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new sign_in_fragment());
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
                check_inputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                check_inputs();
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
                check_inputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                check_inputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_email_and_password();
            }
        });
    }


    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFramelayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void check_inputs(){
        if(!TextUtils.isEmpty(name.getText())){
            if(!TextUtils.isEmpty(email.getText())){
                if(!TextUtils.isEmpty(password.getText()) && password.length() >= 8){
                    if(!TextUtils.isEmpty(confirm_password.getText())){
                        sign_up.setEnabled(true);
                    }else{
                        sign_up.setEnabled(false);
                    }
                }else{
                    sign_up.setEnabled(false);
                }
            }else{
                sign_up.setEnabled(false);
            }
        }else{
            sign_up.setEnabled(false);
        }
    }

    private void check_email_and_password(){
        Drawable customErrorIcon = getResources().getDrawable(R.mipmap.e1);
        customErrorIcon.setBounds(0,0,customErrorIcon.getIntrinsicWidth(), customErrorIcon.getIntrinsicHeight());

        if(email.getText().toString().matches(email_Pattern)){
            if(password.getText().toString().equals(confirm_password.getText().toString())){

                sign_up.setEnabled(false);

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map<Object, String> userData = new HashMap<>();
                                    userData.put("Name", name.getText().toString());

                                    firebaseFirestore.collection("users")
                                            .add(userData)
                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if(task.isSuccessful()){
                                                       main_intent();
                                                    }else {
                                                        sign_up.setEnabled(true);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getActivity(),error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }else{
                                    sign_up.setEnabled(true);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(),error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }else{
                confirm_password.setError("Password doesn't match",customErrorIcon);
            }
        }else{
                email.setError("Invalid email",customErrorIcon);
        }
    }

    private void main_intent() {
        Intent main_intent = new Intent(getActivity(), MainActivity.class);
        startActivity(main_intent);
        getActivity().finish();
    }


    }