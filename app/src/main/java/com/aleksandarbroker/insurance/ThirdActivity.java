package com.aleksandarbroker.insurance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aleksandarbroker.insurance.functions.AndroidMultiPartEntity;
import com.aleksandarbroker.insurance.functions.CustomDialog;
import com.aleksandarbroker.insurance.functions.Functions;
import com.aleksandarbroker.insurance.functions.NVP;
import com.android.internal.http.multipart.MultipartEntity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by Martin on 7/14/2016.
 */
public class ThirdActivity extends AppCompatActivity {

    com.aleksandarbroker.insurance.functions.Functions Functions = new Functions(this);

    String APP_PNAME;

    private ImageButton import_photo;
    private ImageButton photo;
    private Button later;
    private Bitmap selectedImage;
    private TextView rateus;
    private String activity;
    private ArrayList<NVP> postdata = new ArrayList<NVP>(1);
    private String userAgent;
    public ArrayList<String> selectedPhotos = new ArrayList<>();
    private String token;
    private TextView textView;
    private ArrayList<NVP> userdata = new ArrayList<NVP>(1);

    int serverResponseCode = 0;
    private CustomDialog.Builder loader;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.third);

        getWindow().setBackgroundDrawableResource(R.drawable.bg_eco);

        Intent i = getIntent();
        userdata = (ArrayList<NVP>) i.getSerializableExtra("data");

        token = i.getStringExtra("token");
        //userdata.add(userdata.size(), new NVP("Token", token));
        postdata.add(0, new NVP("Token", token));

        loader = new CustomDialog.Builder(this);

        import_photo = (ImageButton) findViewById(R.id.import_photo);
        photo = (ImageButton) findViewById(R.id.photo);
        later = (Button) findViewById(R.id.later);
        selectedImage = null;
        userAgent = new WebView(this).getSettings().getUserAgentString();
        textView = (TextView) findViewById(R.id.textView);

        selectedPhotos.add(0, "");

        later.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Loader();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (selectedPhotos.get(0) == "") {

                                    new SendHttpPost().execute("http://insurance.aleksandarbroker.com/ApplyLater.php");

                                } else {

                                    new SendHttpPost().execute("http://insurance.aleksandarbroker.com/upload.php");
                                    //new SendHttpPost().execute("http://marti.websyle.com/insurance/fileUpload.php");

                                    //uploadFile("http://insurance.aleksandarbroker.com/upload.php");

                                    //showToast(""+selectedPhotos.get(0));

                                }
                                //new SendHttpPost().execute("http://marti.websyle.com/insurance/form.php");

                            }
                        }, 2000);
                    }
                });

            }

        });

        setListeners();

    }

    @SuppressWarnings("deprecation")
    private class SendHttpPost extends AsyncTask<String, String, String> {

        public SendHttpPost() {


        }

        @Override
        protected String doInBackground(final String... params) {

            boolean hasConnection = Functions.isNetworkAvailable();

            String result;

            if (hasConnection) {

                HttpClient httpclient = new DefaultHttpClient();

                httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, userAgent);

                HttpPost httppost = new HttpPost(params[0]);
                //HttpPost httppost = new HttpPost("http://marti.websyle.com/testRainbowApps/form.php");

                try {

                    if (selectedPhotos.get(0) == "") {

                        httppost.setEntity(new UrlEncodedFormEntity(postdata, "UTF-8"));

                        HttpResponse httpResponse = httpclient.execute(httppost);
                        result = EntityUtils.toString(httpResponse.getEntity());

                    } else {

                        httppost.setEntity(prepareDataForPost());

                        HttpResponse httpResponse = httpclient.execute(httppost);

                        int statusCode = httpResponse.getStatusLine().getStatusCode();
                        if (statusCode == 200) {
                            // Server response
                            result = EntityUtils.toString(httpResponse.getEntity());
                        } else {
                            result = "Error occurred! Http Status Code: " + statusCode;
                        }

                    }

                    //loader.dismiss();
                    //startActivity(i);
                } catch (UnsupportedEncodingException ex) {

                    result = ex.toString();

                } catch (IOException ex) {

                    result = ex.toString();

                }

            } else {

                result = "503";

            }

            return result;

        }

        protected void onPostExecute(String result) {

            if (result == "503") {

                NoConnection();

            } else {

                //showToast("Submitted");

                try {
                    showLoader(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    @SuppressWarnings("deprecation")
    private AndroidMultiPartEntity prepareDataForPost() throws UnsupportedEncodingException {

        AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {

            @Override
            public void transferred(long num) {
                //publishProgress((int) ((num / (float) totalSize) * 100));
            }

        });

        for(int i = 0; i < selectedPhotos.size(); i++){

            entity.addPart("TalonImage[]", new FileBody(new File(selectedPhotos.get(i))));

        }

        entity.addPart("Token", new StringBody(token));

        return entity;

    }

    public void Loader(){

        loader.show();
        loader.setTitle("Изпращане");
        loader.addLoader("Моля изчакайте, в момента обработваме вашите данни");

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

    }

    public void showLoader(String result) throws JSONException {

        JSONObject myObject = new JSONObject(result);
        String message_text = myObject.getString("message");
        String title_text = "Изпращането успешно!";
        //final String redirect_url = myObject.getString("redirect_url");

        final CustomDialog.Builder dialog = new CustomDialog.Builder(this);

        dialog.setTitle(title_text);
        dialog.setMessage(message_text);
        dialog.addButton(this, "Ок", new View.OnClickListener() {

            public void onClick(View v) {

                loader.dismiss();
                dialog.dismiss();

                Intent i = new Intent(ThirdActivity.this, HomeScreen.class);
                i.putExtra("from", "ta");
                i.putExtra("data", userdata);

                startActivity(i);

                //overridePendingTransition(R.anim.popup_show, R.anim.nothing);

                finish();

            }
        });
        dialog.show();

    }

    public void NoConnection() {

        final CustomDialog.Builder dialog = new CustomDialog.Builder(this);
        dialog.show();
        dialog.setTitle("Възникна Грешка!");
        dialog.setMessage("За да използвате приложението ви трябва интернет!");
        dialog.addButton(this, "Ок", new View.OnClickListener() {

            public void onClick(View v) {

                loader.dismiss();
                dialog.dismiss();
                finish();

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case PhotoPicker.REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    if (imageReturnedIntent != null) {

                        selectedPhotos = imageReturnedIntent.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);

                        for (int i = 0; i < selectedPhotos.size(); i++) {

                            selectedImage = BitmapFactory.decodeFile(selectedPhotos.get(i));

                        }

                        later.setText("Потвърждаване");

                    }

                }
                break;

        }
    }


    public void setListeners() {

        import_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image*//*");
                startActivityForResult(photoPickerIntent, 3);*/

                PhotoPicker.builder()
                        .setPhotoCount(3)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(ThirdActivity.this, PhotoPicker.REQUEST_CODE);

            }
        });

        photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (selectedPhotos.get(0) != "") {

                    PhotoPreview.builder()
                            .setPhotos(selectedPhotos)
                            .setCurrentItem(0)
                            .setShowDeleteButton(false)
                            .start(ThirdActivity.this);

                } else {

                    showToast("Изберете снимки първо!");

                }


            }

        });

    }

    public void showToast(String str) {

        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {

            /*final CustomDialog.Builder dialog = new CustomDialog.Builder(this);

            dialog.setTitle("Изход");
            dialog.setMessage("Сигурни ли сте, че искате да излезете?");
            dialog.addButtonGroup(this, "Да", new View.OnClickListener() {

                public void onClick(View v) {

                    dialog.dismiss();
                    finish();
                    System.exit(1);

                }
            });
            dialog.addButtonGroup(this, "Не", new View.OnClickListener() {

                public void onClick(View v) {

                    dialog.dismiss();

                }
            });
            dialog.show();*/

    }

}
