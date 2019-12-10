package Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aplikacja.projektzespokowy2019.R;
import model.CwiczenieDoPlanu;
import model.Seria;
import model.WykonanyPlan;

import static android.content.ContentValues.TAG;

public class fragmentStatystyka extends Fragment {

    LineChart chart1, chart2;
    private Integer howManyTrennings ;
    private List<WykonanyPlan> doneWorkoutList = new ArrayList<WykonanyPlan>();
    private Button b1, b2, b3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        final View v = inflater.inflate(R.layout.activity_fragment_statystyka, container, false);

        chart1 = v.findViewById(R.id.chart1);
        chart2 = v.findViewById(R.id.chart2);
        formatChart(chart1);
        formatChart(chart2);


        b1 = (Button) v.findViewById(R.id.btn5);
        b2 = (Button) v.findViewById(R.id.btn10);
        b3 = (Button) v.findViewById(R.id.btn15);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                howManyTrennings = 1;
                PobierzDane();
                chart1.invalidate();
                chart2.invalidate();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                howManyTrennings = 2;
                PobierzDane();
                chart1.invalidate();
                chart2.invalidate();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                howManyTrennings = 3;
                PobierzDane();
                chart1.invalidate();
                chart2.invalidate();
            }
        });

        return v;
    }



    private void formatChart(LineChart formatChart) {
        formatChart.setTouchEnabled(false);
        formatChart.setPinchZoom(false);
        formatChart.animate();

        XAxis x = formatChart.getXAxis();
        x.setEnabled(false);

        YAxis yRight = formatChart.getAxisRight();
        yRight.setEnabled(true);
        yRight.setLabelCount(1); //ustawia format wartosci
        YAxis yLeft = formatChart.getAxisLeft();
        yLeft.setEnabled(false);
        yLeft.setDrawAxisLine(false);

        formatChart.setDescription(null);
    }


    public void PobierzDane() {

        doneWorkoutList.clear();
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String Uid;
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        //  WykonanyPlan doneWorkout;

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("wykonany_plan").child(currentFirebaseUser.getUid().toString());
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            ///////////////////////////////////////////////
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    WykonanyPlan w = childDataSnapshot.getValue(WykonanyPlan.class);
                    doneWorkoutList.add(w);
                }

                drawCharts(chart1, "Powtórzenia * Cięzar [kg].");
                drawCharts(chart2, "Czas w minutach.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });


    }

    private void drawCharts(LineChart Variablechart, String description) {

        ArrayList<Entry> values = new ArrayList<>();

        getEntryValues(Variablechart, values);

        LineDataSet set1;
        if (Variablechart.getData() != null && //tutaj wchodze, bo ginie wartosc
                Variablechart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) Variablechart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            Variablechart.getData().notifyDataChanged();
            Variablechart.notifyDataSetChanged();
///////////////////////////////
            set1 = new LineDataSet(values, description);
            set1.setColor(Color.parseColor("#cecd42"));
            set1.setCircleColor(R.color.colorPink);
            set1.setValueTextSize(10f);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            Variablechart.setData(data);


        } else {
            set1 = new LineDataSet(values, description);
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
            Variablechart.setData(data);
        }
    }

    private void getEntryValues(LineChart variablechart, ArrayList<Entry> values) {
        int roz = doneWorkoutList.size();
        int roz2 = howManyTrennings;
        if (roz - howManyTrennings >= 0) {
            roz -= howManyTrennings;
        } else {
            roz = 0;
        }
        if (variablechart == chart1) {

            for (int x = roz; x < doneWorkoutList.size(); x++) {
                int sum = 0;
                for (CwiczenieDoPlanu cw : doneWorkoutList.get(x).getListaCwiczen()) {
                    for (Seria s : cw.getListaSerii()) {

                        sum = s.getPowtorzenia() * s.getCiezar();
                    }
                }
                values.add(new Entry(x, sum));
            }
        } else if (variablechart == chart2) {
            for (int x = roz; x < doneWorkoutList.size(); x++) {

                values.add(new Entry(x, doneWorkoutList.get(x).getCzasWykonania()));
            }

        }

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Statystyka");
    }
}