package com.aleksandarbroker.insurance;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aleksandarbroker.insurance.functions.CustomDatePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.aleksandarbroker.insurance.functions.CustomDatePicker.getMonth;
import static com.aleksandarbroker.insurance.functions.CustomDatePicker.setRange;
import static com.aleksandarbroker.insurance.functions.Functions.disableSroll;
import static com.aleksandarbroker.insurance.functions.Functions.getIndex;
import static com.aleksandarbroker.insurance.functions.Functions.requestFocus;
import static com.aleksandarbroker.insurance.functions.Functions.requestFocus2;

public class SecondActivity extends AppCompatActivity {

    Functions Functions = new Functions(this);
    CustomDatePicker dpSettings = new CustomDatePicker(this);
    private String activity;
    private View group;
    private View partner;
    private CheckBox green_card;
    private EditText brand;
    private TextView brand_error;
    private EditText model;
    private TextView model_error;
    private EditText reg_number;
    private TextView reg_number_error;
    private EditText rama_num;
    private TextView rama_num_error;
    private EditText policy_date;
    private TextView policy_date_error;
    private EditText seats;
    private TextView seats_error;
    private ArrayAdapter<CharSequence> seats_array;
    private ArrayAdapter<CharSequence> seats_keys;
    private EditText insurer;
    private TextView insurer_error;
    private ArrayAdapter<CharSequence> insurer_array;
    private ArrayAdapter<CharSequence> insurer_keys;
    private EditText insurance;
    private TextView insurance_error;
    private ArrayAdapter<CharSequence> insurance_array;
    private ArrayAdapter<CharSequence> insurance_keys;
    private EditText def_payment;
    private TextView def_payment_error;
    private EditText fname;
    private TextView fname_error;
    private EditText sname;
    private TextView sname_error;
    private EditText tname;
    private TextView tname_error;
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

                            if (brand.getText().hashCode() == Hashcode) {

                                if (brand.getText().toString().length() > 0) {

                                    brand_error.setText("");

                                }

                            }

                            if (model.getText().hashCode() == Hashcode) {

                                if (model.getText().toString().length() > 0) {

                                    model_error.setText("");

                                }

                            }

                            if (reg_number.getText().hashCode() == Hashcode) {

                                if (reg_number.getText().toString().length() > 0) {

                                    reg_number_error.setText("");

                                }

                            }

                            if (rama_num.getText().hashCode() == Hashcode) {

                                if (rama_num.getText().toString().length() > 0) {

                                    rama_num_error.setText("");

                                }

                            }

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

                        }
                    });

                }

            }
        }

    };
    private Button send;
    private DatePicker datePicker;
    private List<String> items;
    private ArrayList<NVP> postdata = new ArrayList<NVP>(1);
    private String userAgent;
    private ArrayAdapter<CharSequence> def_payment_keys;
    private ArrayAdapter<CharSequence> def_payment_array;

    private String vat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        getWindow().setBackgroundDrawableResource(R.drawable.bg_eco);

        Intent i = getIntent();
        //activity = i.getStringExtra("activity");
        postdata = (ArrayList<NVP>) i.getSerializableExtra("data");
        vat = i.getStringExtra("vat");

        findViewsById();
        setListeners();

        disableSroll(group, R.id.scroll_view);

        DatePicker(policy_date, policy_date_error, "Дата на полицата", -1, -365);

        Spinner(R.array.seats_array, seats, seats_error, R.string.seats_hint);
        Spinner(R.array.insurer_array, insurer, insurer_error, R.string.insurer_hint);
        Spinner(R.array.insurance_array, insurance, insurance_error, R.string.insurance_hint);
        Spinner(R.array.def_payment_array, def_payment, def_payment_error, R.string.def_payment_hint);

        send.setOnClickListener(new View.OnClickListener() {

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

        if (green_card.isChecked()) {

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

        brand.setText("тест");
        model.setText("тест");
        reg_number.setText("1234567");
        rama_num.setText("12345678901234567");
        seats.setText("5+1");
        policy_date.setText("12/15/2016");
        insurer.setText("Generali");
        insurance.setText("Авто Каско");
        def_payment.setText("1 вноска");
        fname.setText("test");
        sname.setText("test");
        tname.setText("test");

    }

    public String dataStringReverse(String data){

        String newData;

        if("".equals(data)){

            newData = "";

        }else{

            newData = ""+data.charAt(6)+data.charAt(7)+data.charAt(8)+data.charAt(9)+"-"+data.charAt(0)+data.charAt(1)+"-"+data.charAt(3)+data.charAt(4);

        }

        return newData;

    }

    public void findViewsById() {

        group = (View) findViewById(R.id.group);
        partner = (View) findViewById(R.id.partner);
        green_card = (CheckBox) findViewById(R.id.green_card);

        send = (Button) findViewById(R.id.send);

        brand = (EditText) findViewById(R.id.brand);
        brand_error = (TextView) findViewById(R.id.brand_error);

        model = (EditText) findViewById(R.id.model);
        model_error = (TextView) findViewById(R.id.model_error);

        reg_number = (EditText) findViewById(R.id.reg_number);
        reg_number_error = (TextView) findViewById(R.id.reg_number_error);

        rama_num = (EditText) findViewById(R.id.rama_num);
        rama_num_error = (TextView) findViewById(R.id.rama_num_error);

        fname = (EditText) findViewById(R.id.gc_fname);
        fname_error = (TextView) findViewById(R.id.gc_fname_error);

        sname = (EditText) findViewById(R.id.gc_sname);
        sname_error = (TextView) findViewById(R.id.gc_sname_error);

        tname = (EditText) findViewById(R.id.gc_tname);
        tname_error = (TextView) findViewById(R.id.gc_tname_error);

        policy_date = (EditText) findViewById(R.id.policy_date);
        policy_date_error = (TextView) findViewById(R.id.policy_date_error);

        seats = (EditText) findViewById(R.id.seats);
        seats_error = (TextView) findViewById(R.id.seats_error);
        seats_keys = ArrayAdapter.createFromResource(this, R.array.seats_keys, MODE_PRIVATE);
        seats_array = ArrayAdapter.createFromResource(this, R.array.seats_array, MODE_PRIVATE);

        insurer = (EditText) findViewById(R.id.insurer);
        insurer_error = (TextView) findViewById(R.id.insurer_error);
        insurer_keys = ArrayAdapter.createFromResource(this, R.array.insurer_keys, MODE_PRIVATE);
        insurer_array = ArrayAdapter.createFromResource(this, R.array.insurer_array, MODE_PRIVATE);

        insurance = (EditText) findViewById(R.id.insurance);
        insurance_error = (TextView) findViewById(R.id.insurance_error);
        insurance_keys = ArrayAdapter.createFromResource(this, R.array.insurance_keys, MODE_PRIVATE);
        insurance_array = ArrayAdapter.createFromResource(this, R.array.insurance_array, MODE_PRIVATE);

        def_payment = (EditText) findViewById(R.id.def_payment);
        def_payment_error = (TextView) findViewById(R.id.def_payment_error);
        def_payment_keys = ArrayAdapter.createFromResource(this, R.array.def_payment_keys, MODE_PRIVATE);
        def_payment_array = ArrayAdapter.createFromResource(this, R.array.def_payment_array, MODE_PRIVATE);

        userAgent = new WebView(this).getSettings().getUserAgentString();

    }

    public void setListeners() {

        brand.setOnFocusChangeListener(focusChanged);
        model.setOnFocusChangeListener(focusChanged);
        reg_number.setOnFocusChangeListener(focusChanged);
        rama_num.setOnFocusChangeListener(focusChanged);
        fname.setOnFocusChangeListener(focusChanged);
        sname.setOnFocusChangeListener(focusChanged);
        tname.setOnFocusChangeListener(focusChanged);

    }

    private void Spinner(int array, final EditText field, final TextView field_error, int setTitle) {

        String[] newArray = getResources().getStringArray(array);
        List<String> myResArrayList = Arrays.asList(newArray);
        items = new ArrayList<String>(myResArrayList);

        final CustomDialog.Builder dialog = new CustomDialog.Builder(this);
        dialog.setTitle(setTitle);
        dialog.addListView();

        final ListView listView = dialog.getListView();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        listView.setAdapter(adapter);

        items.remove(0);
        adapter.notifyDataSetChanged();

        field.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                dialog.show();

            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = (String) parent.getItemAtPosition(position);

                field.setText(item);
                field_error.setText("");

                dialog.dismiss();

            }
        });

    }

    public void DatePicker(final EditText id, final TextView field_error, String setTitle, int min, int max) {

        final CustomDialog.Builder dialog = new CustomDialog.Builder(this);
        dialog.setTitle(setTitle);
        dialog.addDatePicker();
        dialog.disableKeyboard();

        datePicker = dialog.getDatePicker();

        dpSettings.prepareDialog(datePicker);

        final Calendar today = Calendar.getInstance();
        final Calendar selectedDate = Calendar.getInstance();

        setRange(datePicker, min, max);

        datePicker.init(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Locale locale = new Locale("bg_BG");

                        SimpleDateFormat dayFormat = new SimpleDateFormat("E", locale);

                        Date getSelected = new Date(year, monthOfYear, dayOfMonth - 1);

                        selectedDate.setTime(getSelected);

                        String dayOfWeek = dayFormat.format(selectedDate.getTime());

                        dialog.setTitle(dayOfWeek + " , " + getMonth(monthOfYear) + " " + dayOfMonth + " , " + year);

                    }
                }
        );

        id.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                dialog.show();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }

        });

        dialog.addButton(this, "Done", new View.OnClickListener() {

            public void onClick(View v) {

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                Date getSelected = new Date(year - 1900, month, day);

                selectedDate.setTime(getSelected);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

                String formatedDate = dateFormatter.format(selectedDate.getTime());

                id.setText(formatedDate);

                dialog.dismiss();

                field_error.setText("");

            }
        });

    }

    public void prepareDataForPost() {

        postdata.set(12, new NVP("Type", brand.getText().toString()));
        postdata.set(13, new NVP("Model", model.getText().toString()));
        postdata.set(14, new NVP("RegNo", reg_number.getText().toString()));
        postdata.set(15, new NVP("RamaNo", rama_num.getText().toString()));
        postdata.set(16, new NVP("Places", seats_keys.getItem(getIndex(seats_array, seats.getText().toString())).toString()));
        postdata.set(17, new NVP("Date", dataStringReverse(policy_date.getText().toString())));
        postdata.set(18, new NVP("Insurer", insurer_keys.getItem(getIndex(insurer_array, insurer.getText().toString())).toString()));
        postdata.set(19, new NVP("PolicyType", insurance_keys.getItem(getIndex(insurance_array, insurance.getText().toString())).toString()));
        postdata.set(20, new NVP("InstallmentPlan", def_payment_keys.getItem(getIndex(def_payment_array, def_payment.getText().toString())).toString()));
        if (green_card.isChecked()) {

            postdata.set(21, new NVP("GreenCard", "true"));
            postdata.set(22, new NVP("FirstNameLat", fname.getText().toString()));
            postdata.set(23, new NVP("MidNameLat", sname.getText().toString()));
            postdata.set(24, new NVP("LastNameLat", tname.getText().toString()));

        } else {

            postdata.set(21, new NVP("GreenCar", "false"));

        }

        if("true".equals(vat)){

            postdata.add(new NVP("VAT", "1"));

        }

    }

    public void fillUserData() {

        EditText[] fields = {brand, model, reg_number, rama_num, seats, policy_date, insurer, insurance, def_payment, fname, sname, tname,};

        if ("true".equals(postdata.get(21).getValue())) {

            partner.setVisibility(View.VISIBLE);
            green_card.setChecked(true);

        }

        for (int count = 0; count < 12; count++) {

            if (count > 8) {

                fields[count].setText(postdata.get(count + 13).getValue());

            } else {

                fields[count].setText(postdata.get(count + 12).getValue());

            }

        }


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

                        Intent i = new Intent(SecondActivity.this, ThirdActivity.class);
                        //i.putExtra("activity", "second");
                        i.putExtra("data", postdata);

                        JSONObject myObject = new JSONObject(result);
                        String token = myObject.getString("token");

                        i.putExtra("token", token);

                        startActivity(i);
                        finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    public boolean Check(String result) throws JSONException {

        JSONObject myObject = new JSONObject(result);

        boolean success = myObject.getBoolean("success");

        String[] fieldName = {"Type", "Model", "RegNo", "RamaNo", "Places", "Date", "Insurer", "PolicyType", "InstallmentPlan", "FirstNameLat", "MidNameLat", "LastNameLat"};
        EditText[] fields = {brand, model, reg_number, rama_num, seats, policy_date, insurer, insurance, def_payment, fname, sname, tname,};
        TextView[] errors = {brand_error, model_error, reg_number_error, rama_num_error, seats_error, policy_date_error, insurer_error, insurance_error, def_payment_error, fname_error, sname_error, tname_error,};

        int number;

        if (green_card.isChecked()) {

            number = 12;

        } else {

            number = 9;

        }

        if (!success) {

            String field = myObject.getString("field");
            String message = myObject.getString("message");

            for (int count = 0; count < number; count++) {

                if (fieldName[count].equals(field)) {

                    errors[count].setText(message);
                    if (count > 3 && count < 9) {

                        requestFocus2(fields[count]);

                    } else {

                        requestFocus(fields[count]);

                    }

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
        dialog.setMessage("За да използвате приложението ви трябва интернет!");
        dialog.addButton(this, "Ок", new View.OnClickListener() {

            public void onClick(View v) {

                dialog.dismiss();
                finish();

            }
        });

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.green_card:

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

        Intent i = new Intent(SecondActivity.this, MainActivity.class);
        //i.putExtra("activity", "second");
        i.putExtra("data", postdata);
        startActivity(i);
        finish();

    }

}
