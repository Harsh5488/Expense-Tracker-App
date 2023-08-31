package com.example.roomexample;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Expense")
public class Expense {
    public static final String TABLE_NAME = "Expense";
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "Title")
    private String title;

    @ColumnInfo(name = "Price")
    private String price;

    @ColumnInfo(name = "DateTime")
    private String dateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Ignore
    public Expense(String title, String price) {
        this.title = title;
        this.price = price;
    }


    public Expense(String title, String price, String dateTime) {
        this.title = title;
        this.price = price;
        this.dateTime = dateTime;
    }

    @Ignore
    public Expense(int id, String title, String price, String dateTime) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.dateTime = dateTime;
    }
}
