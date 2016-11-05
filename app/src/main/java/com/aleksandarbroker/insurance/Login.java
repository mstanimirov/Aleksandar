package com.aleksandarbroker.insurance;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aleksandarbroker.insurance.functions.NVP;

import java.util.ArrayList;

public class Login extends AppCompatActivity implements TextWatcher {

    private ArrayList<NVP> data;
    private EditText n1;
    private EditText n2;
    private EditText n3;
    private EditText n4;
    private Button cancel;
    private String pass;
    private TextView enter;
    private int retries;
    private int icount;

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        getWindow().setBackgroundDrawableResource(R.drawable.bg_eco);

        n1 = (EditText) findViewById(R.id.n1);
        n2 = (EditText) findViewById(R.id.n2);
        n3 = (EditText) findViewById(R.id.n3);
        n4 = (EditText) findViewById(R.id.n4);
        enter = (TextView) findViewById(R.id.enter);

        n1.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        n2.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        n3.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        n4.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

        n1.addTextChangedListener(this);
        n2.addTextChangedListener(this);
        n3.addTextChangedListener(this);
        n4.addTextChangedListener(this);

        cancel = (Button) findViewById(R.id.cancel);

        cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                //UpdateStep();

                Intent i = getIntent();
                data = (ArrayList<NVP>) i.getSerializableExtra("date");


                i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("pass", "");
                i.putExtra("data", data);
                startActivity(i);
                Login.this.finish();
            }
        });
        //RecreateUserData();
        //DeleteDataBase();
        CreateDataBaseAndTable();
        CheckForExistData();

    }

    public void UpdateStep() {

        data.add(new NVP("FirstName", ""));
        data.add(new NVP("MidName", ""));
        data.add(new NVP("LastName", ""));
        data.add(new NVP("EGN", ""));
        data.add(new NVP("MobileNumber", ""));
        data.add(new NVP("Email", ""));
        data.add(new NVP("LegalEntity", ""));
        data.add(new NVP("Bulstat", ""));
        data.add(new NVP("FirmName", ""));
        data.add(new NVP("FirmAddress", ""));
        data.add(new NVP("City", ""));
        data.add(new NVP("Address", ""));
        data.add(new NVP("Type", ""));
        data.add(new NVP("Model", ""));
        data.add(new NVP("RegNo", ""));
        data.add(new NVP("RamaNo", ""));
        data.add(new NVP("Date", ""));
        data.add(new NVP("Places", ""));
        data.add(new NVP("Insurer", ""));
        data.add(new NVP("PolicyType", ""));
        data.add(new NVP("GreenCard", ""));
        data.add(new NVP("FirstNameLat", ""));
        data.add(new NVP("MidNameLat", ""));
        data.add(new NVP("LastNameLat", ""));

    }

    public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

    }

    public void onTextChanged(CharSequence s, int i, int i1, int i2) {

    }

    public void afterTextChanged(Editable s) {

        int Hashcode = s.hashCode();
        boolean showAlert = false;

        if (n1.getText().hashCode() == Hashcode) {
            if (s.toString().length() == 1 && ValidatePasword()) {
                ValidatePasword();
                showAlert = true;
            } else {
                ValidatePasword();
            }

            if (s.toString().length() == 0) {
                n1.requestFocus();
            }

        }

        if (n2.getText().hashCode() == Hashcode) {
            if (s.toString().length() == 1 && ValidatePasword()) {
                ValidatePasword();
                showAlert = true;
            } else {
                ValidatePasword();
            }

            if (s.toString().length() == 0) {
                n1.requestFocus();
            }

        }

        if (n3.getText().hashCode() == Hashcode) {
            if (s.toString().length() == 1 && ValidatePasword()) {
                ValidatePasword();
                showAlert = true;
            } else {
                ValidatePasword();
            }

            if (s.toString().length() == 0) {
                n2.requestFocus();
            }

        }

        if (n4.getText().hashCode() == Hashcode) {
            if (s.toString().length() == 1 && ValidatePasword()) {
                ValidatePasword();
                showAlert = true;
            } else {
                ValidatePasword();
            }

            if (s.toString().length() == 0) {
                n3.requestFocus();
            }

        }

        if (showAlert) {

            FindFromDB();

        }

    }

    public void LoginErrorMessage() {
        ++retries;
        switch (retries) {
            case 1:
                enter.setText(getResources().getString(R.string.PIN_FIRSTERROR));
                break;
            case 2:
                enter.setText(getResources().getString(R.string.PIN_SECONDERROR));
                break;
            case 3:
                enter.setText(getResources().getString(R.string.PIN_THIRDERROR));
                DeleteUserData();
                break;
        }

        n1.setText("");
        n2.setText("");
        n3.setText("");
        n4.setText("");
        n1.requestFocus();

    }

    public void DeleteDataBase() {
        deleteDatabase(getResources().getString(R.string.DataBaseName));
        enter.setText(getResources().getString(R.string.DBDELETED));
    }

    public void CheckForExistData() {
        //if database exist
        SQLiteDatabase db = openOrCreateDatabase(getResources().getString(R.string.DataBaseName), Context.MODE_PRIVATE, null);
        //count rows
        String countrows = "SELECT count(*) FROM " + getResources().getString(R.string.DataBaseTableName);
        Cursor mcursor = db.rawQuery(countrows, null);
        mcursor.moveToFirst();
        icount = mcursor.getInt(0);

        if (icount == 0) {
            enter.setText(getResources().getString(R.string.PIN_FIRSTLOGINSTRING));
        }

    }

    public void DeleteUserData() {
        SQLiteDatabase checkDB = openOrCreateDatabase(getResources().getString(R.string.DataBaseName), Context.MODE_PRIVATE, null);
        checkDB.execSQL("DROP TABLE IF EXISTS " + getResources().getString(R.string.DataBaseTableName));
        checkDB.execSQL("CREATE TABLE IF NOT EXISTS " + getResources().getString(R.string.DataBaseTableName) + "(" + getResources().getString(R.string.DataBaseTableFields) + ");");

        icount = 0;
        showToast(getResources().getString(R.string.PIN_CLEAR));
        enter.setText(getResources().getString(R.string.PIN_FIRSTLOGINSTRING));

    }

    public void RecreateUserData() {

        SQLiteDatabase checkDB = openOrCreateDatabase(getResources().getString(R.string.DataBaseName), Context.MODE_PRIVATE, null);
        String sql = "INSERT INTO " + getResources().getString(R.string.DataBaseTableName) + "(password) VALUES('" + pass + "');";
        checkDB.execSQL(sql);
        checkDB.close();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("pass", pass);
        startActivity(i);
        finish();

    }

    // function run on Create screen
    private void CreateDataBaseAndTable() {
        try {
            SQLiteDatabase checkDB = openOrCreateDatabase(getResources().getString(R.string.DataBaseName), Context.MODE_PRIVATE, null);
            checkDB.execSQL("CREATE TABLE IF NOT EXISTS " + getResources().getString(R.string.DataBaseTableName) + "(" + getResources().getString(R.string.DataBaseTableFields) + ");");
            checkDB.close();
        } catch (SQLiteException e) {
            enter.setText(getResources().getString(R.string.NODB));
        }
    }

    //  function run after user fill 4 input boxes
    public void FindFromDB() {

        //if database exist
        SQLiteDatabase db = openOrCreateDatabase(getResources().getString(R.string.DataBaseName), Context.MODE_PRIVATE, null);

        if (icount == 0) {
            RecreateUserData();
        } else {
            String selectQuery = "SELECT * FROM " + getResources().getString(R.string.DataBaseTableName) + " WHERE password='" + pass + "'";
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("pass", pass);
                startActivity(i);
                finish();
            } else {
                LoginErrorMessage();
            }
        }
    }

    public boolean ValidatePasword() {
        pass = n1.getText().toString() + n2.getText().toString() + n3.getText().toString() + n4.getText().toString();
        boolean valid = false;
        if (pass.length() == 4) {
            valid = true;
        } else {

            if (n1.getText().toString().length() == 0) {
                n1.requestFocus();
                return valid;
            }
            if (n2.getText().toString().length() == 0) {
                n2.requestFocus();
                return valid;
            }
            if (n3.getText().toString().length() == 0) {
                n3.requestFocus();
                return valid;
            }
            if (n4.getText().toString().length() == 0) {
                n4.requestFocus();
                return valid;
            }

        }
        return valid;
    }

    public void showToast(String str) {

        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

    }

}
