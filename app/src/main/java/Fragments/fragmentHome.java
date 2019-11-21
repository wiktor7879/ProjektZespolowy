package Fragments;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
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
import model.Waga;

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


public class fragmentHome extends Fragment {

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
    private String OstatniaData;
    private List<Waga> wagiK = new ArrayList<Waga>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View v = inflater.inflate(R.layout.activity_fragment_home, container, false);
        basicQueryValueListener();


        weightChart = v.findViewById(R.id.weightChart);
        formatChart();



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
                drwaweightChart();
                setInformation();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

}
