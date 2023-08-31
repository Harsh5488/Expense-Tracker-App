package com.example.roomexample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Expense> arr;

    public ExpenseAdapter(Context context, ArrayList<Expense> arr) {
        this.context = context;
        this.arr = arr;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.expensecard,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense model = arr.get(position);
        int index = position;

        holder.rootLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.delete);
                builder.setTitle("Delete this Log?");
                builder.setMessage("Are you sure to delete this Expense Log?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Expense model = new Expense(
                                arr.get(index).getId(),
                                arr.get(index).getTitle(),
                                arr.get(index).getPrice(),
                                arr.get(index).getDateTime()
                        );
                        DatabaseHelper db = DatabaseHelper.getDB(context);
                        db.expenseDAO().deleteTx(model);
                        arr.remove(index);
                        notifyItemRemoved(index);

                        ((MainActivity) context).setTotalSpend();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.view_expense);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                EditText editTitle = dialog.findViewById(R.id.editTitle);
                EditText editPrice = dialog.findViewById(R.id.editPrice);
                Button btnEdit = dialog.findViewById(R.id.btnEdit);
                Button btnDiscard = dialog.findViewById(R.id.btnDiscard);

                editTitle.setText(arr.get(index).getTitle());
                editPrice.setText(arr.get(index).getPrice());

                btnDiscard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Expense model = new Expense(editTitle.getText().toString(),editPrice.getText().toString(),arr.get(index).getDateTime());
                        model.setId(arr.get(index).getId());
                        DatabaseHelper databaseHelper = DatabaseHelper.getDB(context);
                        databaseHelper.expenseDAO().updateTx(model);
                        arr.set(index,model);
                        notifyItemChanged(index);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        holder.textTitle.setText(model.getTitle());
        holder.textPrice.setText("Rs. "+model.getPrice());
        holder.textDateTime.setText(model.getDateTime());
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView rootLayout;
        TextView textTitle,textPrice, textDateTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.rootLayout);
            textTitle = itemView.findViewById(R.id.textTitle);
            textPrice = itemView.findViewById(R.id.textPrice);
            textDateTime = itemView.findViewById(R.id.textDateTime);
        }
    }
}
