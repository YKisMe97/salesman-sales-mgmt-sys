package com.example.priyanka.mapsnearbyplaces.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class sqlDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME = "FYP";
    private static final String COMPANY_TABLE = "COMPANY";
    private static final String USER_TABLE = "COMPANY_USER";
    private static final String SALESMAN_TABLE = "SALESMAN";
    private static final String PRODUCT_TABLE = "PRODUCT";
    private static final String ORDER_TABLE = "ORDER";
    private static final String EXPENSE_TABLE = "EXPENSE";
    private static final String VISIT_PLAN_TABLE = "VISIT_PLAN";

    private Context context;

    private static final String CREATE_TABLE_COMPANY = "CREATE TABLE IF NOT EXISTS " + COMPANY_TABLE
            + "(companyId VARCHAR, companyName VARCHAR, companyType VARCHAR, passcode VARCHAR)";

    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + USER_TABLE
            + "(userId VARCHAR, userCategory VARCHAR, userId VARCHAR)";

    private static final String CREATE_SALESMAN_TABLE = "CREATE TABLE IF NOT EXISTS " + SALESMAN_TABLE
            + "(salesmanId VARCHAR, userCategory VARCHAR, userId VARCHAR)";

    private static final String CREATE_PRODUCT_TABLE = "CREATE TABLE IF NOT EXISTS " + PRODUCT_TABLE
            + "(productId VARCHAR, productName VARCHAR, productQuantity INT)";

    private static final String CREATE_ORDER_TABLE = "CREATE TABLE IF NOT EXISTS " + ORDER_TABLE
            + "(orderId VARCHAR, userCategory VARCHAR, userId VARCHAR)";

    private static final String CREATE_EXPENSE_TABLE = "CREATE TABLE IF NOT EXISTS " + EXPENSE_TABLE
            + "(expenseId VARCHAR, userCategory VARCHAR, userId VARCHAR)";

    private static final String CREATE_VISIT_PLAN_TABLE = "CREATE TABLE IF NOT EXISTS " + VISIT_PLAN_TABLE
            + "(expenseId VARCHAR, userCategory VARCHAR, userId VARCHAR)";

    public sqlDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(CREATE_PRODUCT_TABLE);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String DROP_TABLE = "DROP TABLE IF EXISTS "+ COMPANY_TABLE;
        db.execSQL(DROP_TABLE);
        onCreate(db);

    }

    public Cursor getProductList(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM "+ PRODUCT_TABLE,null);
        return data;
    }
    public Cursor getSalesmanList(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM "+ SALESMAN_TABLE,null);
        return data;
    }

    public void addNewProduct(String productId, String productName, Double productPrice){
        SQLiteDatabase db = this.getWritableDatabase();
        String insert_product = "INSERT INTO PRODUCT VALUES(" + "'"+ productId + "','" + productName + "','" + productPrice + "')";
        db.execSQL(insert_product);
    }

}
