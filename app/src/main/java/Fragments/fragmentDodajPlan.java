package Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aplikacja.projektzespokowy2019.CustomListAdapter;
import aplikacja.projektzespokowy2019.R;
import model.Cwiczenie;

import static android.content.ContentValues.TAG;

public class fragmentDodajPlan extends Fragment {

    private EditText edNazwaPlanu;
    private Button btDodajCwiczenie;
    private Integer i =0;
    private List<Cwiczenie> lista =  new ArrayList<Cwiczenie>();
    private List<Integer> listaWybranych = new ArrayList<Integer>();
    private String[] listaNazwCw ;
     ListView listView;
     ListView listView1;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_dodaj_plan, container, false);
        edNazwaPlanu = (EditText) v.findViewById(R.id.editTextNazwaPlanu);
        btDodajCwiczenie = (Button) v.findViewById(R.id.btnDodajCwiczenieDoPlanu);
        listView1 = (ListView) v.findViewById(R.id.listViewDialogg);

        btDodajCwiczenie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaNazwCw = new String[lista.size()];
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.layout_dialog);
                dialog.setCancelable(true);

                for(Integer i=0;i<lista.size();i++)
                {
                    listaNazwCw[i] = lista.get(i).getNazwa().toString();
                }
                listView = (ListView) dialog.findViewById(R.id.listViewDialog);
                CustomListAdapter adapter = new CustomListAdapter(getActivity(), listaNazwCw);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        listaWybranych.add(position);
                        ListView();
                        dialog.dismiss();
                    }
                });


                dialog.show();

            }
        });

        return v;
    }

    public void ListView()
    {
        String[] nazwy = new String[listaWybranych.size()];
        for(Integer i=0;i<lista.size();i++)
        {
            for(Integer j=0;j<listaWybranych.size();j++)
            {
                if(i==listaWybranych.get(j))
                {
                    nazwy[j]=lista.get(i).getNazwa();
                }
            }
        }

       CustomListAdapter adapter1 = new CustomListAdapter(getActivity(), nazwy);
        listView1.setAdapter(adapter1);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage(R.string.czyNapewno);
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        R.string.usun,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                listaWybranych.remove(position);
                                ListView();
                            }
                        });

                builder1.setNegativeButton(
                        R.string.anuluj,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

    }

    public void basicQueryValueListener() {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("wlasne_cwiczenia").child(currentFirebaseUser.getUid().toString());
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lista.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Cwiczenie cw = childDataSnapshot.getValue(Cwiczenie.class);
                    lista.add(cw);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });



    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Dodaj Plan");
    }

    @Override
    public void onStart() {
        super.onStart();
        basicQueryValueListener();

    }
}
