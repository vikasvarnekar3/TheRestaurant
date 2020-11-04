package com.example.therestaurant.Waiter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.therestaurant.Admin.AdminAddProductActivity;
import com.example.therestaurant.Admin.AdminNavigationActivity;
import com.example.therestaurant.Model.Products;
import com.example.therestaurant.Prevalent.PrevalentWaiter;
import com.example.therestaurant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WaiterProductDetailsActivity extends AppCompatActivity
{
    Toolbar toolbar;
    private Button addToCartButton;
    private ElegantNumberButton numberButton;
    private CircleImageView productImage;
    private TextView productPrice,productName;
    private String productId = "" , Name , Phone , status = "unpaid" ;
    private DatabaseReference  WaiterRef;
    TextView selected;
    private Spinner spinner;
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_product_details);

        toolbar = findViewById(R.id.waiter_toolbar_product_details);
        toolbar.setTitle("Product Details");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productId = getIntent().getStringExtra("pid");

        WaiterRef = FirebaseDatabase.getInstance().getReference().child("Waiter");

        selected = findViewById(R.id.waiter_select_table);
        spinner = findViewById(R.id.waiter_spinner);

        addToCartButton =(Button) findViewById(R.id.waiter_pd_add_to_cart_button);
        numberButton = (ElegantNumberButton) findViewById(R.id.waiter_numberBtn);
        productImage =(CircleImageView) findViewById(R.id.waiter_product_image_details);
        productName =(TextView) findViewById(R.id.waiter_product_name_details);
        productPrice =(TextView) findViewById(R.id.waiter_product_price_details);
        member = new Member();

        List<String> Categories = new ArrayList<>();
        Categories.add(0,"Choose Table");
        Categories.add("Table 1");
        Categories.add("Table 2");
        Categories.add("Table 3");
        Categories.add("Table 4");
        Categories.add("Table 5");
        Categories.add("Table 6");
        Categories.add("Table 7");
        Categories.add("Table 8");
        Categories.add("Table 9");
        Categories.add("Table 10");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(parent.getItemAtPosition(position).equals("Choose Table"))
                {

                }
                else
                {
                    selected.setText(parent.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        getProductDetails(productId);

        addToCartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                member.setSpinner(spinner.getSelectedItem().toString());
                addingToCartList();
            }
        });

        WaiterRef.child(PrevalentWaiter.CurrentOnlineUser.getPhone_no())
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {
                            Name = dataSnapshot.child("name").getValue().toString();
                            Phone = dataSnapshot.child("phone_no").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                    }
                });
    }

    private void addingToCartList()
    {
        final String saveCurrentTime,saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        if (member.getSpinner() == "Choose Table")
        {
            selected.setError("Please select table");
            return;
        }

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        final HashMap<String,Object> orderMap = new HashMap<>();
        orderMap.put("waiterName",Name);
        orderMap.put("waiterPhone",Phone);

        orderRef.child(PrevalentWaiter.CurrentOnlineUser.getPhone_no())
                .updateChildren(orderMap)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(WaiterProductDetailsActivity.this, "Waiter Information Added Successfully.", Toast.LENGTH_SHORT).show();

                            final HashMap<String,Object> cartMap = new HashMap<>();
                            cartMap.put("pid",productId);
                            cartMap.put("pname",productName.getText().toString());
                            cartMap.put("price",productPrice.getText().toString());
                            cartMap.put("date",saveCurrentDate);
                            cartMap.put("time",saveCurrentTime);
                            cartMap.put("quantity",numberButton.getNumber());
                            cartMap.put("status",status);
                            cartMap.put("table",member.getSpinner());

                            cartListRef.child(PrevalentWaiter.CurrentOnlineUser.getPhone_no())
                                    .child("Tables").child(member.getSpinner()).child(productId)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(WaiterProductDetailsActivity.this, "Order Added Successfully.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(WaiterProductDetailsActivity.this, WaiterNavigationActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });

        final DatabaseReference kitchenorder = FirebaseDatabase.getInstance().getReference().child("kitchen orders");

        final HashMap<String,Object> kitchenMap = new HashMap<>();
        kitchenMap.put("waiterName",Name);
        kitchenMap.put("pid",productId);
        kitchenMap.put("pname",productName.getText().toString());
        kitchenMap.put("price",productPrice.getText().toString());
        kitchenMap.put("quantity",numberButton.getNumber());
        kitchenMap.put("time",saveCurrentTime);
        kitchenMap.put("table",member.getSpinner());

        kitchenorder.child(saveCurrentTime).updateChildren(kitchenMap)
        .addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(WaiterProductDetailsActivity.this, "Product order is Successfully gone in kitchen...", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Toast.makeText(WaiterProductDetailsActivity.this, "Error while transferring product to kitchen: ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getProductDetails(String productId)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText("Rs." + products.getPrice());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() ==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
