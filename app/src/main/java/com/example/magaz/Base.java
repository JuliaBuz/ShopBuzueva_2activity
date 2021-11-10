package com.example.magaz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class Base extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnMagaz, btnClear;
    EditText edName, edstoim;
    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        btnMagaz = (Button) findViewById(R.id.btnMagaz);
        btnMagaz.setOnClickListener(this);

        edName = (EditText) findViewById(R.id.edName);
        edstoim = (EditText) findViewById(R.id.edstoim);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        UpdateTable();

    }
    public  void UpdateTable(){
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            TableLayout dbOutPut = findViewById(R.id.dbOutPut);
            dbOutPut.removeAllViews();
            do {
                TableRow dbOuyPutRow = new TableRow( this);
                dbOuyPutRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);


                TextView OutPutName = new TextView(this);
                params.weight = 3.0f;
                OutPutName.setLayoutParams(params);
                OutPutName.setText(cursor.getString(nameIndex));
                dbOuyPutRow.addView(OutPutName);

                TextView OutPutPrice = new TextView(this);
                params.weight = 3.0f;
                OutPutPrice.setLayoutParams(params);
                OutPutPrice.setText(cursor.getString(priceIndex));
                dbOuyPutRow.addView(OutPutPrice);


                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight = 1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("Удалить запись");
                deleteBtn.setId(cursor.getInt(idIndex));
                dbOuyPutRow.addView(deleteBtn);
                dbOutPut.addView(dbOuyPutRow);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnMagaz:
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            UpdateTable();
            break;
            case R.id.btnAdd:
                String name = edName.getText().toString();
                String price = edstoim.getText().toString();
                contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_PRICE, price);
                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                edName.setText(null);
                edstoim.setText(null);
                UpdateTable();
                break;
            case R.id.btnClear:
                database.delete(DBHelper.TABLE_CONTACTS, null, null);
                TableLayout dbOutPut = findViewById(R.id.dbOutPut);
                dbOutPut.removeAllViews();
                edName.setText(null);
                edstoim.setText(null);
                UpdateTable();
                break;

            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();
                database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID+ " = ?", new String[]{String.valueOf(v.getId())});
                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                if (cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_NAME);
                    int regIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRICE);
                    int realID = 1;
                    do {
                        if (cursorUpdater.getInt(idIndex)>realID){
                            contentValues.put(DBHelper.KEY_ID, realID);
                            contentValues.put(DBHelper.KEY_NAME, cursorUpdater.getString(nameIndex));
                            contentValues.put(DBHelper.KEY_PRICE, cursorUpdater.getString(regIndex));
                            database.replace(DBHelper.TABLE_CONTACTS, null, contentValues);
                        }
                        realID++;
                    }while (cursorUpdater.moveToNext());
                    if (cursorUpdater.moveToLast()&& v.getId()!=realID){
                        database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();
                }
                break;
        }
    }
}