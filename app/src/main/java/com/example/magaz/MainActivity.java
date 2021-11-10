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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    float itog = 0;
    boolean clik = false;
    Button btnBD, dboform;
    DBHelper dbHelper;
    TextView pricetxt;
    SQLiteDatabase database;
    ContentValues contentValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnBD = (Button) findViewById(R.id.btnBD);
        btnBD.setOnClickListener(this);
        pricetxt = (TextView) findViewById(R.id.pricetxt);
        dboform = (Button) findViewById(R.id.dboform);
        dboform.setOnClickListener(this);

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

                Button btnsumma = new Button(this);
                btnsumma.setOnClickListener(new View.OnClickListener(){@Override
                public void onClick(View view) {
                                              View dobav = (View) view.getParent();
                                              ViewGroup outputDB = (ViewGroup) dobav.getParent();
                                              outputDB.removeView(dobav);
                                              outputDB.invalidate();
                                              float itogplus = Float.parseFloat(OutPutPrice.getText().toString());
                                              itog = itog+itogplus;
                                              pricetxt.setText("" + itog);
                                              clik = true;
                    UpdateTable();
                }
                });
                params.weight = 1.0f;
                btnsumma.setTextSize(13);
                btnsumma.setLayoutParams(params);
                btnsumma.setText("В корзину");
                btnsumma.setId(cursor.getInt(idIndex));
                dbOuyPutRow.addView(btnsumma);
                dbOutPut.addView(dbOuyPutRow);

        } while (cursor.moveToNext());
        }
        cursor.close();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnBD:
                Intent intent = new Intent(this, Base.class);
                startActivity(intent);
                break;
            case R.id.dboform:
                if (clik == false) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Ваша корзина пуста", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Итого : " + pricetxt.getText() + " руб.", Toast.LENGTH_SHORT);
                    toast.show();
                    itog = 0;
                    clik = false;
                    pricetxt.setText("0");
                }
                break;
            default:
                break;
        }
    }
}
