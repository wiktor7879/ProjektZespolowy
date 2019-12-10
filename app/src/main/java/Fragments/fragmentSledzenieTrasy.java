package Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import aplikacja.projektzespokowy2019.R;

import static android.content.Context.LOCATION_SERVICE;

public class fragmentSledzenieTrasy extends Fragment implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener {
    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private GoogleMap mMap;
    LocationManager locationManager;
    private View v;
    private GoogleApiClient mGoogleApiClient;
    private  LocationRequest mLocationRequest;
    private List<LatLng> listaLokalizacji = new ArrayList<LatLng>();
    private Marker mCurrLocationMarker;
    private Button Start;
    private Button End;
    private  String work="0";
    private static final long INTERVAL = 1000;  //1 minute
    private static final long FASTEST_INTERVAL = 1000 ; // 1 minute
    private static final float SMALLEST_DISPLACEMENT = 0.1F; //quarter of a meter
    private PolylineOptions polyOptions;
    private PolylineOptions polyline;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_fragment_sledzenie_trasy, container, false);

       final  SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setVisibility(View.GONE);

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else requestLocation();
        if (!isLocationEnabled())
            showAlert(1);


      Start = (Button) v.findViewById(R.id.buttonStart);
      End = (Button) v.findViewById(R.id.buttonEnd);
      End.setVisibility(View.GONE);


        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapFragment.getView().setVisibility(View.VISIBLE);
                Start.setVisibility(View.GONE);
                End.setVisibility(View.VISIBLE);
                work = "1";
                requestLocation();
            }
        });

        End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaLokalizacji.clear();
                work = "2";
                mapFragment.getView().setVisibility(View.GONE);
                End.setVisibility(View.GONE);
                 polyline = polyOptions;
                TextView t = v.findViewById(R.id.distance);
                t.setText(calculateMiles());
            }
        });

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if (work.equals("1"))
        {
            if(listaLokalizacji.isEmpty())
            {
                LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
                listaLokalizacji.add(myCoordinates);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(myCoordinates);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myCoordinates));
            }
            else
            {
                LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
                Integer Odleglosc = Distance(myCoordinates);
                if(Odleglosc>=10)
                {

                    listaLokalizacji.add(myCoordinates);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(myCoordinates);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    mCurrLocationMarker = mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myCoordinates));
                    drawPolyLineOnMap(listaLokalizacji);
                }
            }
        }
    }


    public void drawPolyLineOnMap(List<LatLng> list) {
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        polyOptions = new PolylineOptions();
        polyOptions.color(Color.GREEN);
        polyOptions.width(20);
        polyOptions.addAll(list);
        mMap.clear();
        mMap.addPolyline(polyOptions);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(list.get(list.size()-1));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(list.get(list.size()-1)));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }
    }


    protected String calculateMiles() {
        float totalDistance = 0;
        String totalDistanceString;

        if(polyline.getPoints().isEmpty())
        {
            totalDistanceString = "0";
        }
        else
        {
            for(int i = 1; i < polyline.getPoints().size(); i++) {

                double earthRadius = 3958.75;
                double dLat = Math.toRadians( polyline.getPoints().get(i).latitude -  polyline.getPoints().get(i-1).latitude);
                double dLng = Math.toRadians(polyline.getPoints().get(i).longitude - polyline.getPoints().get(i-1).longitude);
                double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(polyline.getPoints().get(i-1).latitude))
                        * Math.cos(Math.toRadians(polyline.getPoints().get(i).latitude)) * Math.sin(dLng / 2)
                        * Math.sin(dLng / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double dist = earthRadius * c;

                int meterConversion = 1609;
                totalDistance += dist*meterConversion;

            }
            totalDistanceString  = String.valueOf(totalDistance);
        }
        return totalDistanceString;
    }



    protected Integer Distance(LatLng Punkt)
    {
        float FirstDistance = 0;

        double earthRadius = 3958.75;
        double dLat = Math.toRadians( Punkt.latitude -  listaLokalizacji.get(listaLokalizacji.size()-1).latitude);
        double dLng = Math.toRadians(Punkt.longitude - listaLokalizacji.get(listaLokalizacji.size()-1).longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(listaLokalizacji.get(listaLokalizacji.size()-1).latitude))
                * Math.cos(Math.toRadians(Punkt.latitude)) * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        int meterConversion = 1609;
        FirstDistance += dist*meterConversion;

        Integer Distance =  Math.round(FirstDistance);
        return Distance;
    }
    private void requestLocation() {

        if(work.equals("1"))
        {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }
    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }



    private boolean isPermissionGranted() {
        if (ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v("mylog", "Permission is granted");
            return true;
        } else {
            Log.v("mylog", "Permission not granted");
            return false;
        }
    }
    private void showAlert(final int status) {
        String message, title, btnText;
        if (status == 1) {
            message = "Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                    "use this app";
            title = "Enable Location";
            btnText = "Location Settings";
        } else {
            message = "Please allow this app to access location!";
            title = "Permission access";
            btnText = "Grant";
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setCancelable(false);
        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (status == 1) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        } else
                            requestPermissions(PERMISSIONS, PERMISSION_ALL);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Sledzenie Trasy");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
            mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT); //added
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,  this);
            }
        }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}






