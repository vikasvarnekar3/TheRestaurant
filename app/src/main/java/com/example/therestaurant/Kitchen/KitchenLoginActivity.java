package com.example.therestaurant.Kitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.therestaurant.Admin.AdminLoginActivity;
import com.example.therestaurant.Admin.AdminNavigationActivity;
import com.example.therestaurant.Admin.AdminRegistrationActivity;
import com.example.therestaurant.Model.Admin;
import com.example.therestaurant.Model.Kitchen;
import com.example.therestaurant.Prevalent.PrevalentAdmin;
import com.example.therestaurant.Prevalent.PrevalentKitchen;
import com.example.therestaurant.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class KitchenLoginActivity extends AppCompatActivity {

    EditText mphone,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    private TextView forgotPassword;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_login);

        mphone = findViewById(R.id.kitchen_login_phone);
        mPassword = findViewById(R.id.kitchen_login_password);
        progressBar = findViewById(R.id.kitchen_progressBar_login);
        mLoginBtn = findViewById(R.id.kitchen_login);
        forgotPassword = (TextView) findViewById(R.id.kitchen_forgot_password);
        mCreateBtn = findViewById(R.id.kitchen_Register_here);

        mLoginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String phone = mphone.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(phone))
                {
                    mphone.setError("Phone id is required");
                    return;
                }
                if (TextUtils.isEmpty(password))
                {
                    mPassword.setError("Password is required");
                    return;
                }
                if (password.length() < 8)
                {
                    mPassword.setError("Password must be  greater than or equal to 8 characters.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();
                RootRef.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.child("Kitchen").child(phone).exists())
                        {
                            Kitchen userData =dataSnapshot.child("Kitchen").child(phone).getValue(Kitchen.class);

                            if(userData.getPhone_no().equals(phone))
                            {
                                if(userData.getPassword().equals(password))
                                {
                                    Toast.makeText(KitchenLoginActivity.this, "Welcome , Logged in successfully" , Toast.LENGTH_SHORT).show();

                                    Intent createintent =new Intent(KitchenLoginActivity.this, KitchenNavigationActivity.class);
                                    PrevalentKitchen.CurrentOnlineUser = userData ;
                                    startActivity(createintent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(KitchenLoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(KitchenLoginActivity.this, "Account with this  " + phone + "number do not exist", Toast.LENGTH_SHORT).show();
                            Toast.makeText(KitchenLoginActivity.this, "You need to create a new account.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });
            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent createintent =new Intent(KitchenLoginActivity.this, KitchenRegistrationActivity.class);
                startActivity(createintent);
                finish();
            }
        });
    }
}
