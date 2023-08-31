package com.example.roomexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button btnAdd, btnFilter;
    private Toolbar toolbar;
    private RecyclerView rvExpense;
    ArrayList<Expense> arr;
    private TextView textTotalSpend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAddExpense);
        btnFilter = findViewById(R.id.btnFilter);
        toolbar = findViewById(R.id.toolbar);
        rvExpense = findViewById(R.id.rvExpense);
        textTotalSpend = findViewById(R.id.textTotalSpend);

        setSupportActionBar(toolbar);

        DatabaseHelper databaseHelper = DatabaseHelper.getDB(this);
        arr = (ArrayList<Expense>) databaseHelper.expenseDAO().getAllExpense();
        ExpenseAdapter adapter = new ExpenseAdapter(this, arr);
        rvExpense.setLayoutManager(new LinearLayoutManager(this));
        rvExpense.setAdapter(adapter);

        setTotalSpend();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_expense);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                EditText editTitle = dialog.findViewById(R.id.editTitle);
                EditText editPrice = dialog.findViewById(R.id.editPrice);
                Button btnSave = dialog.findViewById(R.id.btnSave);
                Button btnDiscard = dialog.findViewById(R.id.btnDiscard);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editTitle.getText().toString().isEmpty()) {
                            editTitle.requestFocus();
                            editTitle.setError("Enter Title!");
                        }
                        else if (editPrice.getText().toString().isEmpty()) {
                            editPrice.requestFocus();
                            editPrice.setError("Enter Price!");
                        }
                        else {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy\nhh:mm:ss");
                            String dateTime = sdf.format(new Date());

                            Expense model = new Expense(editTitle.getText().toString(), editPrice.getText().toString(), dateTime);
                            databaseHelper.expenseDAO().addTx(model);

                            arr.add(model);
                            adapter.notifyItemInserted(arr.size() - 1);
                            rvExpense.scrollToPosition(arr.size() - 1);
                            setTotalSpend();
                            dialog.dismiss();
                        }
                    }
                });
                btnDiscard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Feature Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    public void setTotalSpend(){
        int sum=0;
        for(Expense e: arr){
            sum+=Integer.parseInt(e.getPrice());
        }
        textTotalSpend.setText("Rs. "+sum+"/-");
    }
}