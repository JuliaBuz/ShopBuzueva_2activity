package com.example.magaz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Authorization extends AppCompatActivity implements View.OnClickListener{

    Button btnvxod, btnreg;
    EditText edLog, edPass;
    DBUsers dbUsers;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        btnvxod = (Button)findViewById(R.id.btnvxod);
        btnvxod.setOnClickListener(this);

        btnreg = (Button)findViewById(R.id.btnreg);
        btnreg.setOnClickListener(this);

        edLog = (EditText) findViewById(R.id.edLog);
        edPass = (EditText) findViewById(R.id.edPass);

        dbUsers = new DBUsers(this);
        database = dbUsers.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBUsers.KEY_USERNAME, "admin");
        contentValues.put(DBUsers.KEY_PASSWORD, "1111");
        database.insert(DBUsers.TABLE_CONTACTS, null, contentValues);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnvxod:
                Cursor logCursor = database.query(DBUsers.TABLE_CONTACTS, null, null, null, null, null, null);
                boolean logged = false;
                if (logCursor.moveToFirst()) {
                    int usernameIndex = logCursor.getColumnIndex(DBUsers.KEY_USERNAME);
                    int passwordIndex = logCursor.getColumnIndex(DBUsers.KEY_PASSWORD);
                    do {
                        if (edLog.getText().toString().equals(logCursor.getString(usernameIndex)) && edPass.getText().toString().equals(logCursor.getString(passwordIndex))) {
                            if (edLog.getText().toString().equals("admin") && edPass.getText().toString().equals("1111")) {
                                logged = true;
                                Intent intent = new Intent(Authorization.this, Base.class);
                                startActivity(intent);
                                break;
                            } else {
                                logged = true;
                                Intent intent = new Intent(Authorization.this, MainActivity.class);
                                startActivity(intent);
                                break;
                            }
                        }
                    } while (logCursor.moveToNext());
                }
                logCursor.close();
                if (!logged)
                    Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnreg:
                Cursor signCursor = database.query(DBUsers.TABLE_CONTACTS, null, null, null, null, null, null);
                boolean finded = false;
                if (signCursor.moveToFirst()) {
                    int usernameIndex = signCursor.getColumnIndex(DBUsers.KEY_USERNAME);
                    do {
                        if (edLog.getText().toString().equals(signCursor.getString(usernameIndex))) {
                            Toast.makeText(this, "Введенный логин уже существует в базе", Toast.LENGTH_LONG).show();
                            finded = true;
                            break;
                        }
                    } while (signCursor.moveToNext());
                }
                if (!finded) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBUsers.KEY_USERNAME, edLog.getText().toString());
                    contentValues.put(DBUsers.KEY_PASSWORD, edPass.getText().toString());
                    database.insert(DBUsers.TABLE_CONTACTS, null, contentValues);
                    Toast.makeText(this, "Вы успешно зарегестрировались", Toast.LENGTH_LONG).show();
                }
                signCursor.close();
                break;
        }
    }
}