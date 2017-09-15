package dm.com.token;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;
    private DatabaseReference modelCloudEndPoint;

    ArrayList<ParkingModel> parkingModels;
    ListView listView;
    private static CustomParkingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parkingModels = new ArrayList<ParkingModel>();

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser == null) {
            loadLogInView();
        } else {
            mUserId = mFirebaseUser.getUid();
            modelCloudEndPoint = mDatabase.child(mUserId).child("models");
            adapter = new CustomParkingListAdapter(parkingModels, getApplicationContext());
            // Set up ListView
            final ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(adapter);

            final Button button = (Button) findViewById(R.id.addButton);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Bundle dataBundle = new Bundle();
                    dataBundle.putInt("id", 0);
                    Intent intent = new Intent(getApplicationContext(), ParkingList.class);
                    intent.putExtras(dataBundle);
                    startActivity(intent);
                }
            });

            // Use Firebase to populate the list.
            modelCloudEndPoint.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    ParkingModel model = dataSnapshot.getValue(ParkingModel.class);
                    adapter.add(model);
                    // adapter_simple.add((String) dataSnapshot.child("name").getValue());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    //adapter.remove((String) dataSnapshot.child("title").getValue());

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(final AdapterView<?> parent, View view, final int position, final long id) {
                    Bundle dataBundle = new Bundle();
                    ParkingModel model = (ParkingModel) parent.getItemAtPosition(position);
                    if (model.getStatus().equalsIgnoreCase("DELIVERED")) {
                        listView.getChildAt(position).setEnabled(false);
                    }
                    dataBundle.putString("dataRef", model.getId());
                    dataBundle.putString("phone", model.getPhone());
                    dataBundle.putString("name", model.getName());
                    dataBundle.putString("token", model.getToken());
                    dataBundle.putString("vehicle_no", model.getVehicle_no());
                    dataBundle.putString("status", model.getStatus());
                    dataBundle.putLong("parking_timestamp", model.getParkingTimestamp());
                    dataBundle.putLong("delivery_timestamp", model.getDeliveryTimestamp());
                    Intent intent = new Intent(getApplicationContext(), ParkingList.class);
                    intent.putExtras(dataBundle);
                    startActivity(intent);
                }
            });
        }
    }

    private void loadLogInView() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            mFirebaseAuth.signOut();
            loadLogInView();
        }

        return super.onOptionsItemSelected(item);
    }


}
