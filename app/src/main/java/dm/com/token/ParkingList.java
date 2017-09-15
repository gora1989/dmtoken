package dm.com.token;

/**
 * Created by gora3 on 5/17/17.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParkingList extends AppCompatActivity {

    TextView name;
    TextView phone;
    TextView vehicle_no;

    ParkingModel pmodel;

    private DatabaseReference mDatabase;
    private String mUserId;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference modelCloudEndPoint;
    //CountryCodePicker ccp;
    String dataRef;
    String ERROR = "error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_listview);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = (TextView) findViewById(R.id.editTextName);
        phone = (TextView) findViewById(R.id.editTextPhone);
        vehicle_no = (TextView) findViewById(R.id.editTextVehicleNo);
        /*ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.setDefaultCountryUsingNameCode("US");
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Toast.makeText(ParkingList.this, "Updated " + ccp.getSelectedCountryName(), Toast.LENGTH_SHORT).show();
            }
        });*/

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserId = mFirebaseUser.getUid();

        modelCloudEndPoint = mDatabase.child(mUserId).child("models");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            dataRef = extras.getString("dataRef");
            if (dataRef != null) {
                setParkingModelValues(extras);
                Button b = (Button) findViewById(R.id.button1);
                b.setVisibility(View.INVISIBLE);

                name.setText(pmodel.getName());
                name.setFocusable(false);
                name.setClickable(false);

                phone.setText(pmodel.getPhone());
                phone.setFocusable(false);
                phone.setClickable(false);

                vehicle_no.setText(pmodel.getVehicle_no());
                vehicle_no.setFocusable(false);
                vehicle_no.setClickable(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (dataRef != null) {
                getMenuInflater().inflate(R.menu.parking_list, menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                gotoMainActivity();
                return true;

            case R.id.Edit_Contact:
                Button b = (Button) findViewById(R.id.button1);
                b.setVisibility(View.VISIBLE);
                name.setEnabled(true);
                name.setFocusableInTouchMode(true);
                name.setClickable(true);

                phone.setEnabled(true);
                phone.setFocusableInTouchMode(true);
                phone.setClickable(true);

                vehicle_no.setEnabled(true);
                vehicle_no.setFocusableInTouchMode(true);
                vehicle_no.setClickable(true);

                return true;
            case R.id.mark_delivered:
                try {
                    modelCloudEndPoint.child(dataRef).child("status").setValue("DELIVERED");
                    modelCloudEndPoint.child(dataRef).child("deliveryTimestamp").setValue(System.currentTimeMillis());
                    gotoMainActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        if (isValidPhone(phone.getText().toString())) {
            if (extras != null) {
                Long parking_timestamp = new Long(0);
                Long delivery_timestamp = new Long(0);
                Random ran = new Random();
                int code = 100000 + ran.nextInt(900000);
                if (dataRef != null) {
                    try {
                        pmodel.setName(name.getText().toString());
                        pmodel.setPhone(phone.getText().toString());
                        pmodel.setVehicle_no(vehicle_no.getText().toString());
                        modelCloudEndPoint.child(dataRef).setValue(pmodel);
                        gotoMainActivity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
             /*  parking_timestamp=System.currentTimeMillis();
                DatabaseReference dRef=modelCloudEndPoint.push();
                 pmodel = new ParkingModel(dRef.getKey(),name.getText().toString(), phone.getText().toString(),
                         vehicle_no.getText().toString(), Integer.toString(code),"PARKED", parking_timestamp,delivery_timestamp);
                dRef.setValue(pmodel);
                    gotoMainActivity();*/
                    SendSMS task = new SendSMS();
                    task.execute();
                }
            }
        } else {
            Toast.makeText(ParkingList.this, "Please enter a valid phone number", Toast.LENGTH_LONG).show();
        }
    }

    public void setParkingModelValues(Bundle extras) {
        pmodel = new ParkingModel();
        pmodel.setId(extras.getString("dataRef"));
        pmodel.setName(extras.getString("name"));
        pmodel.setPhone(extras.getString("phone"));
        pmodel.setVehicle_no(extras.getString("vehicle_no"));
        pmodel.setToken(extras.getString("token"));
        pmodel.setStatus(extras.getString("status"));
        pmodel.setParkingTimestamp(extras.getLong("parking_timestamp"));
        pmodel.setDeliveryTimestamp(extras.getLong("delivery_timestamp"));
    }

    public void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public boolean isValidPhone(String phone) {
        if (phone.length() == 10) {
            String expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{3,15}$";
            CharSequence inputString = phone;
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(inputString);
            return matcher.matches();
        } else {
            return false;
        }

    }

    public String sendSMS(int token) {
        //Your authentication key
        String authkey = "151930AQfvJdTb5912cce9";
        //Multiple mobiles numbers separated by comma
        String mobiles = phone.getText().toString();
        //Sender ID,While using route4 sender id should be 6 characters long.
        String senderId = "eTOKEN";
        //Your message to send, Add URL encoding here.
        String message = "Hi " + name.getText().toString() + " your car " + vehicle_no.getText().toString() +
                " has been parked with us. Please show your token number "
                + token + " to our valet for pickup.";
        //define route
        // String route = "default";
        String route = "4";

        URLConnection myURLConnection = null;
        URL myURL = null;
        BufferedReader reader = null;

        //encoding message
        String encoded_message = URLEncoder.encode(message);

        //Send SMS API
        String mainUrl = "https://control.msg91.com/api/sendhttp.php?";

        //Prepare parameter string
        StringBuilder sbPostData = new StringBuilder(mainUrl);
        sbPostData.append("authkey=" + authkey);
        sbPostData.append("&mobiles=" + mobiles);
        sbPostData.append("&message=" + encoded_message);
        sbPostData.append("&route=" + route);
        sbPostData.append("&sender=" + senderId);
        sbPostData.append("&response=json");

        //final string
        mainUrl = sbPostData.toString();
        try {
            //prepare connection
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

            //reading response
            String response = reader.readLine();
            // while ((response = reader.readLine()) != null)
            //finally close connection
            reader.close();
            return parseJSONResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public String parseJSONResponse(String response) {
        try {
            //print response
            Log.d("RESPONSE", "" + response);
            JSONObject json = new JSONObject(response);
            String message = json.getString("message");
            String type = json.getString("type");
            if (!type.equalsIgnoreCase("success")) {
                Toast.makeText(ParkingList.this, message, Toast.LENGTH_SHORT).show();
            }
            return type;
        } catch (final JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }

    private class SendSMS extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        private String resp;
        int code;
        String response = "";

        @Override
        protected String doInBackground(String... urls) {
            publishProgress("Starting...");
            Random ran = new Random();
            code = 100000 + ran.nextInt(900000);
            Log.d("generated token", "" + code);

            return sendSMS(code);
        }


        @Override
        protected void onPostExecute(String result) {
            Log.d("Result from MSG91", response + "------" + result);
            progressDialog.dismiss();
            if (result != null) {
                if (result.equalsIgnoreCase(ERROR)) {
                    Log.d("message response", response);
                } else {
                    Long delivery_timestampLong = new Long(0);
                    Long parking_timestampLong = System.currentTimeMillis();
                    DatabaseReference dRef = modelCloudEndPoint.push();
                    ParkingModel pmodel = new ParkingModel(dRef.getKey(), name.getText().toString(), phone.getText().toString(),
                            vehicle_no.getText().toString(),
                            Integer.toString(code), "PARKED", parking_timestampLong, delivery_timestampLong);
                    dRef.setValue(pmodel);
                    gotoMainActivity();
                }
            }

        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ParkingList.this,
                    "",
                    "Sending token ");
        }
    }

}