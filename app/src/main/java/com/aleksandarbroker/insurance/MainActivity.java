package com.aleksandarbroker.insurance;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aleksandarbroker.insurance.functions.CustomDialog;
import com.aleksandarbroker.insurance.functions.Functions;
import com.aleksandarbroker.insurance.functions.NVP;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

import static com.aleksandarbroker.insurance.functions.Functions.disableSroll;
import static com.aleksandarbroker.insurance.functions.Functions.requestFocus;

public class MainActivity extends AppCompatActivity {

    Functions Functions = new Functions(this);
    String userAgent;
    private String activity;
    private View group;
    private View partner;
    private CheckBox uface;
    private CheckBox vat;
    private EditText fname;
    private TextView fname_error;
    private EditText sname;
    private TextView sname_error;
    private EditText tname;
    private TextView tname_error;
    private EditText egn;
    private TextView egn_error;
    private EditText mobile_number;
    private TextView mobile_number_error;
    private EditText email;
    private TextView email_error;
    private EditText bulstat;
    private TextView bulstat_error;
    private EditText firm_name;
    private TextView firm_name_error;
    private EditText firm_address;
    private TextView firm_address_error;
    private EditText city;
    private TextView city_error;
    private EditText address;
    private TextView address_error;
    View.OnFocusChangeListener focusChanged = new View.OnFocusChangeListener() {

        public void onFocusChange(View v, boolean hasFocus) {

            if (v instanceof EditText) {

                int edittextlenght = ((EditText) v).getText().length();

                if (hasFocus) {

                    if (edittextlenght == 0) {

                        ((EditText) v).setHint(null);
                        //((EditText) v).setBackgroundResource(R.drawable.input_required);

                    } else {

                    }

                    ((EditText) v).addTextChangedListener(new TextWatcher() {

                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        public void afterTextChanged(Editable s) {

                            String c = s.toString();

                            int Hashcode = s.hashCode();

                            if (fname.getText().hashCode() == Hashcode) {

                                if (fname.getText().toString().length() > 0) {

                                    fname_error.setText("");

                                }

                            }
                            if (sname.getText().hashCode() == Hashcode) {

                                if (sname.getText().toString().length() > 0) {

                                    sname_error.setText("");

                                }

                            }
                            if (tname.getText().hashCode() == Hashcode) {

                                if (tname.getText().toString().length() > 0) {

                                    tname_error.setText("");

                                }

                            }

                            if (egn.getText().hashCode() == Hashcode) {

                                if (egn.getText().toString().length() > 0) {

                                    egn_error.setText("");

                                }

                            }

                            if (mobile_number.getText().hashCode() == Hashcode) {

                                if (mobile_number.getText().toString().length() > 0) {

                                    mobile_number_error.setText("");

                                }

                            }

                            if (email.getText().hashCode() == Hashcode) {

                                if (email.getText().toString().length() > 0) {

                                    email_error.setText("");

                                }

                            }

                            if (bulstat.getText().hashCode() == Hashcode) {

                                if (bulstat.getText().toString().length() > 0) {

                                    bulstat_error.setText("");

                                }

                            }

                            if (firm_name.getText().hashCode() == Hashcode) {

                                if (firm_name.getText().toString().length() > 0) {

                                    firm_name_error.setText("");

                                }

                            }

                            if (firm_address.getText().hashCode() == Hashcode) {

                                if (firm_address.getText().toString().length() > 0) {

                                    firm_address_error.setText("");

                                }

                            }

                            if (city.getText().hashCode() == Hashcode) {

                                if (city.getText().toString().length() > 0) {

                                    city_error.setText("");

                                }

                            }

                            if (address.getText().hashCode() == Hashcode) {

                                if (address.getText().toString().length() > 0) {

                                    address_error.setText("");

                                }

                            }

                        }
                    });

                }

            }
        }

    };
    private ArrayList<NVP> postdata = new ArrayList<NVP>(1);
    private Button step2;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Locale locale = new Locale("bg_BG");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);

        getWindow().setBackgroundDrawableResource(R.drawable.bg_eco);

        Intent i = getIntent();
        //activity = i.getStringExtra("activity");
        postdata = (ArrayList<NVP>) i.getSerializableExtra("data");

        findViewsById();
        setListenters();

        disableSroll(group, R.id.scroll_view);

        step2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                new SendHttpPost().execute("http://insurance.aleksandarbroker.com/form.php");
                                //new SendHttpPost().execute("http://marti.websyle.com/insurance/form.php");

                            }
                        }, 0);
                    }
                });

            }

        });

        if (uface.isChecked()) {

            partner.setVisibility(View.VISIBLE);

        } else {

            partner.setVisibility(View.GONE);

        }

        if (postdata.get(0).getValue() != null) {

            fillUserData();
            //fillTestData();

        }

    }

    public void fillTestData(){

        fname.setText("тест");
        sname.setText("тест");
        tname.setText("тест");
        egn.setText("9907076429");
        mobile_number.setText("0896337736");
        email.setText("test@abv.bg");
        bulstat.setText("123456789");
        firm_name.setText("тесттест");
        firm_address.setText("тесттест");
        city.setText("тесттест");
        address.setText("тесттест");

    }

    public void findViewsById() {

        group = (View) findViewById(R.id.group);
        partner = (View) findViewById(R.id.partner);
        uface = (CheckBox) findViewById(R.id.uface);
        vat = (CheckBox) findViewById(R.id.dds);

        fname = (EditText) findViewById(R.id.fname);
        fname_error = (TextView) findViewById(R.id.fname_error);

        sname = (EditText) findViewById(R.id.sname);
        sname_error = (TextView) findViewById(R.id.sname_error);

        tname = (EditText) findViewById(R.id.tname);
        tname_error = (TextView) findViewById(R.id.tname_error);

        egn = (EditText) findViewById(R.id.egn);
        egn_error = (TextView) findViewById(R.id.egn_error);

        mobile_number = (EditText) findViewById(R.id.mobile_number);
        mobile_number_error = (TextView) findViewById(R.id.mobile_number_error);

        email = (EditText) findViewById(R.id.email);
        email_error = (TextView) findViewById(R.id.email_error);

        bulstat = (EditText) findViewById(R.id.bulstat);
        bulstat_error = (TextView) findViewById(R.id.bulstat_error);

        firm_name = (EditText) findViewById(R.id.firm_name);
        firm_name_error = (TextView) findViewById(R.id.firm_name_error);

        firm_address = (EditText) findViewById(R.id.firm_address);
        firm_address_error = (TextView) findViewById(R.id.firm_address_error);

        city = (EditText) findViewById(R.id.city);
        city_error = (TextView) findViewById(R.id.city_error);

        address = (EditText) findViewById(R.id.address);
        address_error = (TextView) findViewById(R.id.address_error);

        step2 = (Button) findViewById(R.id.step2);

        userAgent = new WebView(this).getSettings().getUserAgentString();

    }

    public void setListenters() {

        fname.setOnFocusChangeListener(focusChanged);
        sname.setOnFocusChangeListener(focusChanged);
        tname.setOnFocusChangeListener(focusChanged);
        egn.setOnFocusChangeListener(focusChanged);
        mobile_number.setOnFocusChangeListener(focusChanged);
        email.setOnFocusChangeListener(focusChanged);
        bulstat.setOnFocusChangeListener(focusChanged);
        firm_name.setOnFocusChangeListener(focusChanged);
        firm_address.setOnFocusChangeListener(focusChanged);
        city.setOnFocusChangeListener(focusChanged);
        address.setOnFocusChangeListener(focusChanged);

    }

    public void prepareDataForPost() {

        postdata.set(0, new NVP("FirstName", fname.getText().toString()));
        postdata.set(1, new NVP("MidName", sname.getText().toString()));
        postdata.set(2, new NVP("LastName", tname.getText().toString()));
        postdata.set(3, new NVP("EGN", egn.getText().toString()));
        postdata.set(4, new NVP("MobileNumber", mobile_number.getText().toString()));
        postdata.set(5, new NVP("Email", email.getText().toString()));
        if (uface.isChecked()) {

            postdata.set(6, new NVP("LegalEntity", "true"));
            postdata.set(7, new NVP("Bulstat", bulstat.getText().toString()));
            postdata.set(8, new NVP("CompanyName", firm_name.getText().toString()));
            postdata.set(9, new NVP("CompanyAddress", firm_address.getText().toString()));

        } else {

            postdata.set(6, new NVP("LegalEntit", ""));

        }
        postdata.set(10, new NVP("City", city.getText().toString()));
        postdata.set(11, new NVP("Address", address.getText().toString()));

    }

    public void fillUserData() {

        EditText[] fields = {fname, sname, tname, egn, mobile_number, email, bulstat, firm_name, firm_address, city, address};

        if ("true".equals(postdata.get(6).getValue())) {

            partner.setVisibility(View.VISIBLE);
            uface.setChecked(true);

        }

        for (int count = 0; count < 11; count++) {

            if (count > 5) {

                fields[count].setText(postdata.get(count + 1).getValue());

            } else {

                fields[count].setText(postdata.get(count).getValue());

            }

        }
        ;

    }

    public boolean Check(String result) throws JSONException {

        //showToast(result);

        JSONObject myObject = new JSONObject(result);

        boolean success = myObject.getBoolean("success");

        String[] fieldName = {"FirstName", "MidName", "LastName", "EGN", "MobileNumber", "Email", "Bulstat", "CompanyName", "CompanyAddress", "City", "Address"};
        EditText[] fields = {fname, sname, tname, egn, mobile_number, email, bulstat, firm_name, firm_address, city, address};
        TextView[] errors = {fname_error, sname_error, tname_error, egn_error, mobile_number_error, email_error, bulstat_error, firm_name_error, firm_address_error, city_error, address_error};

        if (!success) {

            String field = myObject.getString("field");
            String message = myObject.getString("message");

            for (int count = 0; count < 11; count++) {

                if (fieldName[count].equals(field)) {

                    errors[count].setText(message);
                    requestFocus(fields[count]);
                    return false;

                }

            }

        }

        return true;

    }

    public void NoConnection() {

        final CustomDialog.Builder dialog = new CustomDialog.Builder(this);
        dialog.show();
        dialog.setTitle("Възникна Грешка!");
        dialog.setMessage("За да използвате приложението трябва да имате връзка с интернет!");
        dialog.addButton(this, "Ок", new View.OnClickListener() {

            public void onClick(View v) {

                dialog.dismiss();
                finish();

            }
        });

    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.uface:

                if (checked) {

                    partner.setVisibility(View.VISIBLE);

                } else {

                    partner.setVisibility(View.GONE);

                }

                break;
        }
    }

    public void showToast(String str) {

        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(MainActivity.this, HomeScreen.class);
        //i.putExtra("activity", "main");
        i.putExtra("data", postdata);
        i.putExtra("from", "ma");
        startActivity(i);
        finish();

    }

    private class SendHttpPost extends AsyncTask<String, String, String> {

        public SendHttpPost() {


        }

        @Override
        protected String doInBackground(final String... params) {

            prepareDataForPost();
            boolean hasConnection = Functions.isNetworkAvailable();

            if (hasConnection) {

                HttpClient httpclient = new DefaultHttpClient();

                httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, userAgent);

                HttpPost httppost = new HttpPost(params[0]);
                //HttpPost httppost = new HttpPost("http://marti.websyle.com/testRainbowApps/form.php");

                try {

                    httppost.setEntity(new UrlEncodedFormEntity(postdata, "UTF-8"));

                    HttpResponse httpResponse = httpclient.execute(httppost);
                    String result = EntityUtils.toString(httpResponse.getEntity());
                    //loader.dismiss();
                    return result;
                    //startActivity(i);
                } catch (UnsupportedEncodingException ex) {

                } catch (IOException ex) {

                }

            } else {

                return null;

            }

            return null;

        }

        protected void onPostExecute(String result) {

            if (result == null) {

                NoConnection();

            } else {

                try {

                    boolean validate = Check(result);

                    if (validate) {

                        Intent i = new Intent(MainActivity.this, SecondActivity.class);
                        if(vat.isChecked()){

                            i.putExtra("vat", "true");

                        }else{

                            i.putExtra("vat", "false");

                        }

                        i.putExtra("data", postdata);
                        startActivity(i);
                        finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

    }

}
