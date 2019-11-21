package Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import aplikacja.projektzespokowy2019.MainActivity;
import aplikacja.projektzespokowy2019.PanelInfoOUzytkownikuActivity;
import aplikacja.projektzespokowy2019.R;
import model.Informacje;

import static android.content.ContentValues.TAG;

public class fragmentSetings extends Fragment {

    private View v;
    private View v1;
    private View v2;
    private View v3;
    private LinearLayout linearLayout;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;
    private String OldPass;
    private FirebaseUser user;
    Fragment fragment = null;
    private Informacje inf;
    private DatabaseReference databaseReference;
    private DatePickerDialog datePicker;
    private           String Data = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_fragments_setings, container, false);v1 = inflater.inflate(R.layout.layout_usun_konto,container,false);
        v1 = inflater.inflate(R.layout.layout_usun_konto,container,false);
        v2 = inflater.inflate(R.layout.layout_zmiana_hasla,container,false);
        v3 = inflater.inflate(R.layout.laytout_aktualizacja_info,container,false);
        linearLayout = (LinearLayout) v.findViewById(R.id.activity_fragmet_settings);
        progressDialog = new ProgressDialog(getActivity());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        UsunKonto();
        ZmianaHasla();
        AktualizacjaInfo();
        return v;
    }

    public void UsunKonto()
    {
        TextView text  = (TextView) v1.findViewById(R.id.nameUsunKonto);
        text.setText("Usuń Konto");
        linearLayout.removeView(v1);
        linearLayout.addView(v1);
        Button UsunKonto = (Button) v1.findViewById(R.id.buttonUsunKonto);
        UsunKonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle("Czy Napewno Chcesz Usunąć Konto?");
                        dialog.setMessage("Usunięcie wszystkich danych spowoduje utratę wszysytkich danych zwiazanych z tym użytkownikiem");
                        dialog.setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Usuwanie Konta trwa ...");
                                progressDialog.show();
                                firebaseUser = firebaseAuth.getCurrentUser();
                                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.cancel();
                                        if(task.isSuccessful())
                                        {
                                            DatabaseReference baza = FirebaseDatabase.getInstance().getReference();
                                            baza.child("Informacje").child(firebaseUser.getUid()).setValue(null);
                                            Toast.makeText(getActivity(), "Konto zostało Usunięte", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            intent.putExtra("czyWylogowac","nie");
                                            startActivity(intent);
                                            return;

                                        }else
                                        {
                                            Toast.makeText(getActivity(), "Coś poszło nie tak , Spróbuj Ponownie", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    }
                                });
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

    public void ZmianaHasla()
    {
        TextView text  = (TextView) v2.findViewById(R.id.nameZmainaHasla);
        text.setText("Zmień Hasło");
        linearLayout.removeView(v2);
        linearLayout.addView(v2);

        final EditText OldPassw = (EditText) v2.findViewById(R.id.editTextZmianaHasla);
        Button ZmienHaslo = (Button) v2.findViewById(R.id.buttonZamianaHasla);

        ZmienHaslo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OldPass = OldPassw.getText().toString().trim();
                user = FirebaseAuth.getInstance().getCurrentUser();
                final String email = user.getEmail();
                AuthCredential credential = EmailAuthProvider.getCredential(email, OldPass);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            LinearLayout linear = (LinearLayout) v2.findViewById(R.id.linearLayoutZmianaHasla);
                            ConstraintLayout constraint = (ConstraintLayout) v2.findViewById(R.id.constraintLayoutZmianaHasla);
                            constraint.setVisibility(View.GONE);
                            linear.setVisibility(View.VISIBLE);
                            Button buttonzmiana = (Button) v2.findViewById(R.id.buttonZmienHaslo2);
                            buttonzmiana.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EditText PotwierdzPassword = (EditText) v2.findViewById(R.id.editTextZmianaHaslaPoprawneNowe);
                                    EditText Password = (EditText) v2.findViewById(R.id.editTextZmianaHaslaNowe);

                                    final String password = Password.getText().toString().trim();
                                    String potwierdzPassword = PotwierdzPassword.getText().toString().trim();

                                    if (TextUtils.isEmpty(password) || password.length() < 4 || password.length() > 10) {
                                        Toast.makeText(getActivity(), "Podaj Poprawne Hasło", Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                    if (!password.equals(potwierdzPassword)) {
                                        Toast.makeText(getActivity(), "Hasła się nie zgadzają", Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                    progressDialog.setMessage("Zmiana Hasła trwa ...");
                                    progressDialog.show();

                                    user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(getActivity(), "Coś poszło nie tak ,Spróbuj ponownie za chwile", Toast.LENGTH_LONG).show();
                                                fragment = new fragmentSetings();
                                            } else {
                                                Toast.makeText(getActivity(), "Hasło Zmodyfikowane", Toast.LENGTH_LONG).show();
                                                fragment = new fragmentHome();
                                            }
                                            progressDialog.dismiss();
                                            if (fragment != null) {
                                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                ft.replace(R.id.content_frame, fragment);
                                                ft.commit();
                                            }
                                        }
                                    });
                                }
                            });

                        } else {
                            Toast.makeText(getActivity(), "Uwierzytelnianie nie powiodło się, Podaj Poprawne Hasło!", Toast.LENGTH_LONG).show();
                            fragment = new fragmentSetings();
                        }
                    }
                });

            }
        });
    }
    public void AktualizacjaInfo()
    {
        TextView text  = (TextView) v3.findViewById(R.id.nameAktualizacjaInfo);
        text.setText("Zaktualizuj Swoje Dane");
        linearLayout.removeView(v3);
        linearLayout.addView(v3);
        final EditText AktualizacjaImie =(EditText) v3.findViewById(R.id.editTextAktualizacjaImie);
        final EditText AktualizacjaData =(EditText) v3.findViewById(R.id.editTextAktualizacjaData);
        final EditText AktualizacjaWzrost =(EditText) v3.findViewById(R.id.editTextAktualizacjaWzrost);
        final Button Aktualizuj = (Button) v3.findViewById(R.id.buttonAktualizacjaInfo);
        final RadioButton PlecM = (RadioButton) v3.findViewById(R.id.radioMezczyznaAkt);
        final RadioButton PlecK = (RadioButton) v3.findViewById(R.id.radioKobietaAkt);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String Uid;
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Informacje/" + Uid);
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 inf = dataSnapshot.getValue(Informacje.class);
                AktualizacjaImie.setText(inf.getImie());
                AktualizacjaData.setText(inf.getDataUrodzenia());
                AktualizacjaWzrost.setText(inf.getWzrost().toString());
                String Plec = inf.getPlec();

                if(Plec.equals("Meżczyzna"))
                {
                    PlecM.setChecked(true);
                }
                else if(Plec.equals("Kobieta"))
                {
                    PlecK.setChecked(true);
                }

                Button data = (Button) v3.findViewById(R.id.buttonDateTimePickerAktualizcja);
                data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Integer dzien = Integer.parseInt(AktualizacjaData.getText().toString().substring(0,2));
                        final  Integer mies = Integer.parseInt(AktualizacjaData.getText().toString().substring(3,4));
                        final Integer rok = Integer.parseInt(AktualizacjaData.getText().toString().substring(5,9));
                        datePicker = new DatePickerDialog(getContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                        AktualizacjaData.setText(day + "/" + month + "/" + year);
                                    }
                                }, rok, mies, dzien);
                        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                        datePicker.show();
                    }
                });

                Aktualizuj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Sex = null, Imie = null;
                        Integer Wzrost = null;
                        if (PlecM.isChecked()) {
                            Sex = "Meżczyzna";
                        } else if (PlecK.isChecked()) {
                            Sex = "Kobieta";
                        }
                        Imie = AktualizacjaImie.getText().toString();
                        Data = AktualizacjaData.getText().toString();

                        if (AktualizacjaWzrost.getText()!=null)
                        {
                            Wzrost = Integer.parseInt(AktualizacjaWzrost.getText().toString());
                        }
                        if(Sex == null || Imie == null || Data == null || Wzrost == null)
                        {
                            Toast.makeText(getActivity(), "Wprowadź Dane", Toast.LENGTH_LONG).show();
                        }

                        Informacje nowe = new Informacje(Imie,Data,Sex,Wzrost,inf.getListaWagi());
                        databaseReference.child("Informacje").child(firebaseAuth.getCurrentUser().getUid()).setValue(nowe);
                        Toast.makeText(getActivity(), "Zaktualizowano Dane o Użytkowniku", Toast.LENGTH_LONG).show();
                        fragment = new fragmentHome();

                        if (fragment != null) {
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, fragment);
                            ft.commit();
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
        getActivity().setTitle("Ustawienia");
    }
}

