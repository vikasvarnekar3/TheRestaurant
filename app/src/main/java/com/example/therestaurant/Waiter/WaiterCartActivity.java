package com.example.therestaurant.Waiter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.therestaurant.Model.Orders;
import com.example.therestaurant.Prevalent.PrevalentWaiter;
import com.example.therestaurant.R;
import com.example.therestaurant.ViewHolder.OrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WaiterCartActivity extends AppCompatActivity
{
    private DatabaseReference ordersRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_cart);

        toolbar = findViewById(R.id.waiter_toolbar_cart);
        toolbar.setTitle("Orders");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(PrevalentWaiter.CurrentOnlineUser.getPhone_no()).child("Tables");
        recyclerView = findViewById(R.id.waiter_cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //ProductList = (Button) findViewById(R.id.waiter_cart_product_list);
        //txtTotalAmount =(TextView) findViewById(R.id.waiter_cart_total_price);
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

        FirebaseRecyclerOptions<Orders> options
                = new FirebaseRecyclerOptions.Builder<Orders>()
                        .setQuery(ordersRef, Orders.class)
                        .build();

        FirebaseRecyclerAdapter<Orders, OrdersViewHolder> adapter
                = new FirebaseRecyclerAdapter<Orders, OrdersViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull OrdersViewHolder holder, final int position, @NonNull final Orders model)
            {
                holder.txtTableName.setText("Table No.: " + model.getTable());
               // holder.txtTotalPrice.setText("Price = Rs.");
                holder.ProductList.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String table = getRef(position).getKey();
                        Intent intent = new Intent(WaiterCartActivity.this, WaiterShowProductActivity.class);
                        intent.putExtra("table",table);
                        startActivity(intent);
                    }
                });



                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(WaiterCartActivity.this);
                        builder.setTitle("Can we go to confirm final order:");

                        builder.setItems(options, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int i)
                            {
                                if ( i == 0 )
                                {
                                    Intent intent = new Intent(WaiterCartActivity.this, WaiterConfirmFinalOrderActivity.class);
                                    intent.putExtra("table",model.getTable());
                                    startActivity(intent);
                                }
                                if ( i == 1 )
                                {
                                    Intent intent = new Intent(WaiterCartActivity.this, WaiterCartActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                OrdersViewHolder holder = new OrdersViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
