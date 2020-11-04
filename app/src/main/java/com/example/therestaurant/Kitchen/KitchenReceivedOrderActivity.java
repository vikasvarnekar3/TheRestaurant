package com.example.therestaurant.Kitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.therestaurant.Admin.AdminNavigationActivity;
import com.example.therestaurant.Admin.AdminRemoveProductActivity;
import com.example.therestaurant.Model.ReceivedOrders;
import com.example.therestaurant.R;
import com.example.therestaurant.ViewHolder.ReceivedOrdersViewHolder;
import com.example.therestaurant.Waiter.Member;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class KitchenReceivedOrderActivity extends AppCompatActivity
{
    private DatabaseReference Productref;
    private RecyclerView myProductsList;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_received_order);

        Productref = FirebaseDatabase.getInstance().getReference().child("kitchen orders");
        myProductsList = findViewById(R.id.kitchen_products_order);
        myProductsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        myProductsList.setLayoutManager(layoutManager);

        toolbar = findViewById(R.id.kitchen_toolbar_order);
        toolbar.setTitle("Received Orders");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<ReceivedOrders> options =
                new FirebaseRecyclerOptions.Builder<ReceivedOrders>()
                        .setQuery(Productref, ReceivedOrders.class)
                        .build();
        FirebaseRecyclerAdapter<ReceivedOrders, ReceivedOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<ReceivedOrders, ReceivedOrdersViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull ReceivedOrdersViewHolder holder, int position, @NonNull final ReceivedOrders model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtquantity.setText("Quantity = " + model.getQuantity());
                        holder.txttableName.setText(model.getTable());
                        holder.txtwaiterName.setText(model.getWaiterName());

                        holder.itemView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(KitchenReceivedOrderActivity.this);
                                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner,null);
                                mBuilder.setTitle("Select time for order: ");
                                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.kitchen_spinner);
                                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(KitchenReceivedOrderActivity.this,
                                        android.R.layout.simple_spinner_item,
                                        getResources().getStringArray(R.array.timeList));
                                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinner.setAdapter(adapter1);

                                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose a time:"))
                                        {
                                            final HashMap<String,Object> orderMap = new HashMap<>();
                                            orderMap.put("Order Time",mSpinner.getSelectedItem().toString());

                                            Productref.child(model.getTime())
                                                    .updateChildren(orderMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>()
                                            {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(KitchenReceivedOrderActivity.this, "Time for Product is added Successfully...", Toast.LENGTH_SHORT).show();
                                                    }

                                                    else
                                                    {
                                                        Toast.makeText(KitchenReceivedOrderActivity.this, "Error while giving time from kitchen...", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            /*Toast.makeText(KitchenReceivedOrderActivity.this, mSpinner.getSelectedItem().toString(),
                                                    Toast.LENGTH_SHORT)
                                            .show();*/
                                            dialog.dismiss();
                                        }
                                    }
                                });

                                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                         dialog.dismiss();
                                    }
                                });
                                mBuilder.setView(mView);
                                AlertDialog dialog = mBuilder.create();
                                dialog.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ReceivedOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receivedorders,parent,false);
                        ReceivedOrdersViewHolder holder = new ReceivedOrdersViewHolder(view);
                        return holder;
                    }
                };
        myProductsList.setAdapter(adapter);
        adapter.startListening();
    }
}
