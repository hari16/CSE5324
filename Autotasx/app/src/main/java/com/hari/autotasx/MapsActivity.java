package com.hari.autotasx;

import android.app.PendingIntent;
import android.app.usage.ConfigurationStats;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapLongClickListener,ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Location myLocation;
    final int RQS_GooglePlayServices = 1;
    private Button mAddGeofencesButton;
    protected static final String TAG = "creating-and-monitoring-geofences";
    protected GoogleApiClient mGoogleApiClient;
    protected ArrayList<Geofence> mGeofenceList;
    private boolean mGeofencesAdded;
    private PendingIntent mGeofencePendingIntent;
    private SharedPreferences mSharedPreferences;
    private Button mRemoveGeofencesButton;
    private GeoFence geoFence = new GeoFence();
    EditText name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))

                .getMap();
        //setUpMapIfNeeded();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mAddGeofencesButton = (Button) findViewById(R.id.add_geofences_button);
        mRemoveGeofencesButton = (Button) findViewById(R.id.remove_geofences_button);
        name =  (EditText) findViewById(R.id.editText);
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMapLongClickListener(this);
        mGeofenceList = new ArrayList<Geofence>();


        mGeofencePendingIntent = null;


        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);


        mGeofencesAdded = mSharedPreferences.getBoolean(Constants.GEOFENCES_ADDED_KEY, false);
        setButtonsEnabledState();


       // populateGeofenceList();


        buildGoogleApiClient();
    }

    public void removeGeofencesButtonHandler(View view) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            // Remove geofences.
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,

                    getGeofencePendingIntent()
            ).setResultCallback(this);
        } catch (SecurityException securityException) {

            logSecurityException(securityException);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (resultCode == ConnectionResult.SUCCESS){
            Toast.makeText(getApplicationContext(),
                    "isGooglePlayServicesAvailable SUCCESS",
                    Toast.LENGTH_LONG).show();
        }else{
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
        }
       // setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(android.os.Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
  /*  private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))

                    .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
*/
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
  /*  private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
*/
    @Override
    public void onMapLongClick(LatLng point) {
        mMap.setOnMapLongClickListener(this);
        //GeoFence geoFence = new GeoFence();
        geoFence.setPoint(point);

        geoFence.setRadius(Constants.GEOFENCE_RADIUS_IN_METERS);
       Toast.makeText(this,geoFence.getPoint().toString(),Toast.LENGTH_LONG).show();
        mMap.addMarker(new MarkerOptions().position(point).title(point.toString()));

        CircleOptions circleOptions = new CircleOptions()
                .center(point)   //set center
                .radius(30)   //set radius in meters
                //.fillColor(Color.BLUE)
                .strokeColor(Color.BLACK)
                .strokeWidth(2);

        mMap.addCircle(circleOptions);
        geoFence.setName(name.getText().toString());
        populateGeofenceList();


    }


    public void onResult(Status status) {
        if (status.isSuccess()) {

            mGeofencesAdded = !mGeofencesAdded;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(Constants.GEOFENCES_ADDED_KEY, mGeofencesAdded);
            editor.commit();


            setButtonsEnabledState();

            Toast.makeText(
                    this,
                    getString(mGeofencesAdded ? R.string.geofences_added :
                            R.string.geofences_removed),
                    Toast.LENGTH_SHORT
            ).show();
        } else {

            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
           // Log.e(TAG, errorMessage);
        }
    }

    private void setButtonsEnabledState() {
        if (mGeofencesAdded) {
            mAddGeofencesButton.setEnabled(false);
            mRemoveGeofencesButton.setEnabled(true);
        } else {
            mAddGeofencesButton.setEnabled(true);
            mRemoveGeofencesButton.setEnabled(false);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        //Log.i(TAG, "Connected to GoogleApiClient");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

       // Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {

       // Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    public void populateGeofenceList() {

       // for (Map.Entry<String, LatLng> entry : Constants.LANDMARKS.entrySet()) {

            mGeofenceList.add(new Geofence.Builder()

                   .setRequestId(geoFence.getName())


                    .setCircularRegion(
                            geoFence.getPoint().latitude,
                            geoFence.getPoint().longitude,
                            geoFence.getRadius()
                    )


                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)


                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)


                    .build());
       // }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    public void addGeofencesButtonHandler(View view) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,

                    getGeofencingRequest(),

                    getGeofencePendingIntent()
            ).setResultCallback(this);
        } catch (SecurityException securityException) {

            logSecurityException(securityException);
        }
    }
    private void logSecurityException(SecurityException securityException) {
        //Log.e(TAG, "Invalid location permission. " +
        //        "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();


        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);


        builder.addGeofences(mGeofenceList);


        return builder.build();
    }
    private PendingIntent getGeofencePendingIntent() {

        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


}
