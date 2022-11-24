package com.example.fypchatbotapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import androidx.annotation.Nullable;

import com.example.fypchatbotapp.model.FoodOrder;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String db_name = "db_order";
    private static final String tb_name = "tb_orderTable";
    private static final int db_version = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context,db_name, null,db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ tb_name+
                "(name TEXT, price TEXT,quantity TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+tb_name);
        onCreate(db);
    }

    public List<FoodOrder> getCarts()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"name","price","quantity"};
        qb.setTables(tb_name);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);
        List<FoodOrder> result = new ArrayList<>();
        if (c.moveToFirst())
        {
            do {
                result.add(new FoodOrder(c.getString(c.getColumnIndex("name")),
                        c.getString(c.getColumnIndex("price")),
                        c.getString(c.getColumnIndex("quantity"))
                ));
            }while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(FoodOrder cart)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO "+tb_name+ "(name, price, quantity) VALUES('%s','%s','%s','%s');",
                cart.getName(),cart.getQuantity(),cart.getPrice());
        db.execSQL(query);
    }

    public void clearCart()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM "+tb_name);
        db.execSQL(query);
    }
}
