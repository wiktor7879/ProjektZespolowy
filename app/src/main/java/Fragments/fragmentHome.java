package Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import aplikacja.projektzespokowy2019.R;
import model.Cwiczenie;
import model.Informacje;
import model.OstatniaTrasa;
import model.Plan;
import model.Waga;
import model.WykonanyPlan;

import static android.content.ContentValues.TAG;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class fragmentHome extends Fragment  implements OnMapReadyCallback {

    private TextView tVwzrost;
    private TextView tVwaga;
    private TextView tvplec;
    private String plec;
    private String waga;
    private String wzrost;
    Informacje inf;
    LineChart lineChart;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    ArrayList<Entry> entries = new ArrayList<>();
    ArrayList<Entry> entries1 = new ArrayList<>();
    LineChart weightChart;
    private String AktualnaWaga;
    private Marker mCurrLocationMarker;
    private Integer AktualnyWzrost;
    private String OstatniaData;
    private PolylineOptions polyOptions;
    private List<Waga> wagiK = new ArrayList<Waga>();
    private Integer NaleznaWagaCiala;
    private List<LatLng> trasa = new ArrayList<LatLng>();
    private  CardView cardMapa;
    private GoogleMap mMap;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View v = inflater.inflate(R.layout.activity_fragment_home, container, false);
        basicQueryValueListener();


        weightChart = v.findViewById(R.id.weightChart);
        formatChart();

         cardMapa = (CardView) v.findViewById(R.id.CardViewMapaPodgląd);
        final SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.CardViewMapa);
        mapFragment.getMapAsync(this);

        tVwaga = (TextView) v.findViewById(R.id.tV_home_waga);
        tVwzrost = (TextView) v.findViewById(R.id.tV_home_wzrost);
        tvplec = (TextView) v.findViewById(R.id.tV_home_plec);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        return v;
    }

    private void formatChart() {
        weightChart.setTouchEnabled(true);
        weightChart.setPinchZoom(true);
        weightChart.animate();
        XAxis x = weightChart.getXAxis();
        x.setEnabled(false);
        YAxis yRight= weightChart.getAxisRight();
        yRight.setEnabled(false);
        YAxis yLeft= weightChart.getAxisLeft();
        yLeft.setEnabled(false);
        yLeft.setDrawAxisLine(false);
        weightChart.setDescription(null);
    }




    private void drwaweightChart() { //rysowanie wykresu

        AktualnyWzrost=Integer.parseInt(inf.getWzrost().toString());
        if(inf.getPlec().toString().equals("Kobieta"))
        {
            NaleznaWagaCiala= AktualnyWzrost-100 - ((AktualnyWzrost-150)/2);
        }
        else if(inf.getPlec().toString().equals("Meżczyzna"))
        {
            NaleznaWagaCiala= AktualnyWzrost-100 - ((AktualnyWzrost-150)/4);
        }

        ArrayList<Entry> values = new ArrayList<>();
        wagiK = inf.getListaWagi();

        for (int x = 0; x < wagiK.size(); x++) {
            values.add(new Entry(x, wagiK.get(x).getWaga()));
            if(x==wagiK.size()-1)
            {
                OstatniaData = wagiK.get(x).getData().toString();
                AktualnaWaga= wagiK.get(x).getWaga().toString();
            }
        }

        LineDataSet set1;
        if (weightChart.getData() != null &&
                weightChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) weightChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            weightChart.getData().notifyDataChanged();
            weightChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "Ostatnie wagi");
            //   set1.enableDashedLine(10f, 5f, 0f);
            //  set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.parseColor("#cecd42"));
            set1.setCircleColor(R.color.colorPink);

            set1.setValueTextSize(10f);

            //  set1.setLineWidth(1f);
            //    set1.setCircleRadius(3f);
            //  set1.setDrawCircleHole(false);

            // set1.setDrawFilled(true); //pole pod wykresem
            //  set1.setFormLineWidth(1f);
            // set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            // set1.setFormSize(15.f);
            weightChart.setTouchEnabled(true);
            weightChart.setPinchZoom(true);
            weightChart.animate();
            XAxis x = weightChart.getXAxis();
            x.setEnabled(false);
            YAxis yRight= weightChart.getAxisRight();
            yRight.setEnabled(true);
            YAxis yLeft= weightChart.getAxisLeft();
            yLeft.setEnabled(false);
            yLeft.setDrawAxisLine(false);
            weightChart.setDescription(null);
            LimitLine ll = new LimitLine(NaleznaWagaCiala); // set where the line should be drawn
            ll.setLineColor(Color.RED);
            ll.setLineWidth(1);
            yRight.addLimitLine(ll);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            weightChart.setData(data);
        }


    }


    private void setInformation() {
        tvplec.setText("Płeć: " + inf.getPlec());
        tVwzrost.setText("Wzrost: " + inf.getWzrost());
        tVwaga.setText("Aktualna Waga: " + AktualnaWaga);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }

    public void basicQueryValueListener() {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String Uid;
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Informacje/" + Uid);
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                inf = dataSnapshot.getValue(Informacje.class);

                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("OstatniaTrasa").child( FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("trasa");
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            Double lat;
                            Double lng;
                            LatLng position;
                            for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){
                                lat = Double.parseDouble(childDataSnapshot.child("latitude").getValue().toString());
                                lng = Double.parseDouble(childDataSnapshot.child("longitude").getValue().toString());
                                position = new LatLng(lat, lng);
                               trasa.add(position);
                            }
                        }
                        if(!trasa.isEmpty())
                        {
                          cardMapa.setVisibility(View.VISIBLE);
                          drawPolyLineOnMap(trasa);
                        }

                        drwaweightChart();
                        setInformation();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

    }

    public void drawPolyLineOnMap(List<LatLng> list) {
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        polyOptions = new PolylineOptions();
        polyOptions.color(Color.RED);
        polyOptions.width(20);
        polyOptions.addAll(list);
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trasa.get(trasa.size()/2),17.0f));
        mMap.addPolyline(polyOptions);
        LatLng start = new LatLng(trasa.get(0).latitude,trasa.get(0).longitude);
        mMap.addMarker(new MarkerOptions().position(start).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Start"));

        LatLng koniec = new LatLng(trasa.get(trasa.size()-1).latitude,trasa.get(trasa.size()-1).longitude);
        mMap.addMarker(new MarkerOptions().position(koniec).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Koniec"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(list.get(list.size()-1)));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }
    }
}