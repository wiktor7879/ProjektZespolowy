package Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aplikacja.projektzespokowy2019.AdapterDoWyboruPlanu;
import aplikacja.projektzespokowy2019.AdapterDoWykonywaniaPlanu;
import aplikacja.projektzespokowy2019.CustomListAdapter;
import aplikacja.projektzespokowy2019.MainActivity;
import aplikacja.projektzespokowy2019.R;
import model.Cwiczenie;
import model.Plan;
import model.Seria;

import static android.content.ContentValues.TAG;

public class fragmentWykonajPlan extends Fragment {

    private List<Plan> listaPlanow = new ArrayList<Plan>();
    private List<Cwiczenie> listaCwiczen = new ArrayList<Cwiczenie>();

    private ListView listView;
    private TextView WybierzHead;
    private TextView WykonajHead;
    private Button ZakonczTrening;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View v = inflater.inflate(R.layout.activity_fragment_wykonaj_plan, container, false);
        listView = (ListView) v.findViewById(R.id.listViewWykonajPlan);
        WybierzHead = (TextView) v.findViewById(R.id.WybierzPlanHead);
        WykonajHead = (TextView) v.findViewById(R.id.WykonajPlanHead);
        ZakonczTrening = (Button) v.findViewById(R.id.buttonZakonczTrening);
        PobierzDane();
        listaCwiczen.size();
        return v;
    }


    public void PobierzDane() {
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("wlasne_cwiczenia").child(currentFirebaseUser.getUid().toString());
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Cwiczenie cw = childDataSnapshot.getValue(Cwiczenie.class);
                    listaCwiczen.add(cw);
                }

                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("wlasne_plany").child(currentFirebaseUser.getUid().toString());
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            Plan pl = childDataSnapshot.getValue(Plan.class);
                            listaPlanow.add(pl);
                        }
                        WybierzPlan();
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


    public void WybierzPlan() {
        AdapterDoWyboruPlanu adapter = new AdapterDoWyboruPlanu(getActivity(), listaPlanow, listaCwiczen);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("Czy Napewno Chcesz Rozpocząć Trening");
                dialog.setPositiveButton("Rozpocznij", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WykonajPlan(position);
                    }
                });
                dialog.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

            }
        });
    }

    public void WykonajPlan(int position) {

        listView.setAdapter(null);
        WybierzHead.setVisibility(View.GONE);
        WykonajHead.setVisibility(View.VISIBLE);
        ZakonczTrening.setVisibility(View.VISIBLE);
        WykonajHead.setText(listaPlanow.get(position).getNazwa().toString());

        final AdapterDoWykonywaniaPlanu adapter = new AdapterDoWykonywaniaPlanu(getActivity(), listaPlanow, listaCwiczen, position);
        listView.setAdapter(adapter);





    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Wykonaj Plan");
    }


}
