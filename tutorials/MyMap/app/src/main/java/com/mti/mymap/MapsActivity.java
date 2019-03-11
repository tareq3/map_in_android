package com.mti.mymap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mti.get_runtime_permissions.GetRuntimePermission;


public class MapsActivity extends AppCompatActivity implements
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnCameraIdleListener,

        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {



    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
    private static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static final LatLng ADELAIDE = new LatLng(-34.92873, 138.59995);
    private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);

    private GoogleMap mMap = null;

    /**
     * Keeps track of the selected marker.
     */
    private Marker mSelectedMarker;

    Context mContext;
    GetRuntimePermission mGetRuntimePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_close_info_window_on_retap_demo);
        mContext=this;
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        new OnMapAndViewReadyListener(mapFragment, this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Toast.makeText(this, "ready", Toast.LENGTH_SHORT).show();
        mMap = map;

        // Hide the zoom controls.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Add lots of markers to the map.
        addMarkersToMap();


        // Set listener for marker click event.  See the bottom of this class for its behavior.
        mMap.setOnMarkerClickListener(this);

        // Set listener for map click event.  See the bottom of this class for its behavior.
        mMap.setOnMapClickListener(this);

        //camera idle listener
        mMap.setOnCameraIdleListener(this);


        //Add My Location

        enableMyLocation();

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localized.
        map.setContentDescription("Demo showing how to close the info window when the currently"
                + " selected marker is re-tapped.");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(PERTH)
                .include(SYDNEY)
                .include(ADELAIDE)
                .include(BRISBANE)
                .include(MELBOURNE)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        // animateMapTo(SYDNEY,10f,true);
    }
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            mGetRuntimePermission=new GetRuntimePermission(mContext, GetRuntimePermission.TYPE_OF_PERMISSIONS.ACCESS_FINE_LOCATION) {
                @SuppressLint("MissingPermission")
                @Override
                public void setTaskCompleteAction() {
                    //Do the task you want to do right there
                    //just write down your task Once
                    //following lines will be played if user accept the permission and after accepting everytime
                    ///So don't need to write the same code again after the semicolon
                    Toast.makeText(mContext, "Permission granted", Toast.LENGTH_SHORT).show();

                    mMap.setMyLocationEnabled(true);
                }
            };
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        mGetRuntimePermission.onRequestPermissionsResult(requestCode,permissions,grantResults);


    }
    /**
     * change camera position on certain position
     */
    protected synchronized void animateMapTo(final LatLng pin, final Float zoomLevel, final boolean useAnimation) {
        final GoogleMap map = mMap;
        if (pin == null || map == null) {
            return;
        }

        // Set zoomlevel to current level if not set.
        final float cameraZoomLevel = zoomLevel == null ? map.getCameraPosition().zoom : zoomLevel;

        // Build camera position
        CameraPosition position = new CameraPosition.Builder()
                .target(pin)
                .zoom(cameraZoomLevel)
                .bearing(0)
                .tilt(45)
                .build();

        // Stop any animations
        map.stopAnimation();
        if (useAnimation) {
            map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
//            map.animateCamera(CameraUpdateFactory.newLatLngZoom(pin, cameraZoomLevel));
        } else {
            map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(pin, cameraZoomLevel));
        }
    }

    private void addMarkersToMap() {
        mMap.addMarker(new MarkerOptions()
                .position(BRISBANE)
                .title("Brisbane")
                .snippet("Population: 2,074,200")
                .icon(vectorToBitmap(R.drawable.ic_local_cafe_black_24dp, Color.parseColor("#A4C639")))
        );

        mMap.addMarker(new MarkerOptions()
                .position(SYDNEY)
                .title("Sydney")
                .snippet("Population: 4,627,300")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)))
        ;

        mMap.addMarker(new MarkerOptions()
                .position(MELBOURNE)
                .title("Melbourne")
                .snippet("Population: 4,137,400"));

        mMap.addMarker(new MarkerOptions()
                .position(PERTH)
                .title("Perth")
                .snippet("Population: 1,738,800"));

        mMap.addMarker(new MarkerOptions()
                .position(ADELAIDE)
                .title("Adelaide")
                .snippet("Population: 1,213,000"));
    }

    @Override
    public void onMapClick(final LatLng point) {
        // Any showing info window closes when the map is clicked.
        // Clear the currently selected marker.
        mSelectedMarker = null;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        // The user has re-tapped on the marker which was already showing an info window.
        if (marker.equals(mSelectedMarker)) {
            // The showing info window has already been closed - that's the first thing to happen
            // when any marker is clicked.
            // Return true to indicate we have consumed the event and that we do not want the
            // the default behavior to occur (which is for the camera to move such that the
            // marker is centered and for the marker's info window to open, if it has one).
            mSelectedMarker = null;
            return true;
        }

        mSelectedMarker = marker;

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur.
        return false;
    }


    /**
     * For updating tilt to certain angle on any camera changes when the camera is not moving we
     * will change the tilt
     */
    @Override
    public void onCameraIdle() {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder(mMap.getCameraPosition()).tilt(30f).build()));


    }

    /**
     * Demonstrates converting a {@link Drawable} to a {@link BitmapDescriptor}, for use as a marker
     * icon.
     */
    private BitmapDescriptor vectorToBitmap(@DrawableRes int id, @ColorInt int color) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}