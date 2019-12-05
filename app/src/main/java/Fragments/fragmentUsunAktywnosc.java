package Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
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
import butterknife.internal.Utils;
import model.Cwiczenie;
import model.Plan;
import model.Waga;

import static android.R.layout.simple_list_item_1;
import static android.content.ContentValues.TAG;

public class fragmentUsunAktywnosc extends Fragment {
    private View v;
    private View v1;
    private View v2;
    private List<Cwiczenie> ListaCwiczen = new ArrayList<Cwiczenie>();
    private List<Plan> ListaPlan = new ArrayList<Plan>();
    private  LinearLayout linearLayout;
    private ArrayAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_fragment_usun_aktywnosc, container, false);
        v1 = inflater.inflate(R.layout.layout_usun_cwiczenie,container,false);
        v2 = inflater.inflate(R.layout.layout_usun_plan,container,false);

        UsunCwiczenie();
        UsunPlan();

        return v;
    }


    public void UsunCwiczenie()
    {
        ListaCwiczen.clear();
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("wlasne_cwiczenia").child(currentFirebaseUser.getUid().toString());
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Cwiczenie w = childDataSnapshot.getValue(Cwiczenie.class);
                    ListaCwiczen.add(w);
                }
                linearLayout = (LinearLayout) v.findViewById(R.id.activity_fragmet_usun_aktywnosc);
             //   final TextView text  = (TextView) v1.findViewById(R.id.nameUsunCwiczenie);
              // text.setText("Usuń Cwiczenie");
                linearLayout.removeView(v1);
                linearLayout.addView(v1);
                final TextView text1 = (TextView) v1.findViewById(R.id.kliknijCw);
                text1.setText("Kliknij aby wybrać Cwiczenie");
                text1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ListaCwiczen.isEmpty())
                        {
                            Toast.makeText(getActivity(),"Brak Cwiczen do Wyswietlenia", Toast.LENGTH_LONG).show();
                        }
                        else
                        {

                            String [] stringi = new String[ListaCwiczen.size()];

                            for(Integer i=0;i<ListaCwiczen.size();i++)
                            {
                                stringi[i]=(ListaCwiczen.get(i).getNazwa().toString());
                            }

                            final Dialog dialog1 = new Dialog(getActivity());
                            dialog1.setContentView(R.layout.layout_dialog);
                            dialog1.setCancelable(true);
                            final ListView listView = (ListView) dialog1.findViewById(R.id.listViewDialog);
                            ArrayAdapter<String> itemsAdapter =
                                    new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringi);
                            listView.setAdapter(itemsAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                    text1.setText(ListaCwiczen.get(position).getNazwa().toString());
                                    Button button = (Button) v1.findViewById(R.id.buttonUsunCwiczenie);
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ListaCwiczen.remove(position);
                                            FirebaseDatabase.getInstance().getReference().child("wlasne_cwiczenia").child(currentFirebaseUser.getUid().toString()).setValue(ListaCwiczen);
                                            Toast.makeText(getActivity(),"Cwiczenie Usunięto", Toast.LENGTH_LONG).show();
                                            UsunCwiczenie();
                                        }
                                    });
                                    dialog1.dismiss();
                                }
                            });
                            dialog1.show();
                        }

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void UsunPlan()
    {
        ListaPlan.clear();
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("wlasne_plany").child(currentFirebaseUser.getUid().toString());
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Plan w = childDataSnapshot.getValue(Plan.class);
                    ListaPlan.add(w);
                }
                linearLayout = (LinearLayout) v.findViewById(R.id.activity_fragmet_usun_aktywnosc);
              //  final TextView text  = (TextView) v2.findViewById(R.id.nameUsunPlan);
              //  text.setText("Usuń Plan");
                linearLayout.removeView(v2);
                linearLayout.addView(v2);
                final TextView text1 = (TextView) v2.findViewById(R.id.kliknijPl);
                text1.setText("Kliknij aby wybrać Plan");
                text1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ListaPlan.isEmpty())
                        {
                            Toast.makeText(getActivity(),"Brak Planow do Wyswietlenia", Toast.LENGTH_LONG).show();
                        }
                        else
                        {

                            String [] stringi = new String[ListaPlan.size()];

                            for(Integer i=0;i<ListaPlan.size();i++)
                            {
                                stringi[i]=(ListaPlan.get(i).getNazwa().toString());
                            }

                            final Dialog dialog1 = new Dialog(getActivity());
                            dialog1.setContentView(R.layout.layout_dialog);
                            dialog1.setCancelable(true);
                            final ListView listView = (ListView) dialog1.findViewById(R.id.listViewDialog);
                            ArrayAdapter<String> itemsAdapter =
                                    new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringi);
                            listView.setAdapter(itemsAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                    text1.setText(ListaPlan.get(position).getNazwa().toString());
                                    Button button = (Button) v2.findViewById(R.id.buttonUsunPlan);
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ListaPlan.remove(position);
                                            FirebaseDatabase.getInstance().getReference().child("wlasne_plany").child(currentFirebaseUser.getUid().toString()).setValue(ListaPlan);
                                            Toast.makeText(getActivity(),"Plan Usunięto", Toast.LENGTH_LONG).show();
                                            UsunPlan();
                                        }
                                    });
                                    dialog1.dismiss();
                                }
                            });
                            dialog1.show();
                        }
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Usun Aktywnosc");
    }
}

