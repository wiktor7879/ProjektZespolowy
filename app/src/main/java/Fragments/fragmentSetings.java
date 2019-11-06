package Fragments;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import aplikacja.projektzespokowy2019.MainActivity;
import aplikacja.projektzespokowy2019.R;

public class fragmentSetings extends Fragment {

    private EditText Password;
    private EditText PotwierdzPassword;
    private Button ZmienHaslo;
    private EditText OldPassword;
    private Button Next;
    private String OldPass;
    private FirebaseUser user;
    Fragment fragment = null;
    private ProgressDialog progressDialog;
    private Button UsunKonto;
    private TextView UsunKontoTextView;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragments_setings, container, false);
        Password = (EditText) v.findViewById(R.id.editPasswordSettings);
        PotwierdzPassword = (EditText) v.findViewById(R.id.editPassword2Settings);
        ZmienHaslo = (Button) v.findViewById(R.id.buttonZmienHasloSettings);
        OldPassword = (EditText) v.findViewById(R.id.editOldPasswordSettings);
        Next = (Button) v.findViewById(R.id.buttonNext);
        UsunKonto = (Button) v.findViewById(R.id.buttonUsunKonto);
        UsunKontoTextView = (TextView) v.findViewById(R.id.textView5) ;
        progressDialog = new ProgressDialog(getActivity());

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OldPass = OldPassword.getText().toString().trim();
                if(TextUtils.isEmpty(OldPass) || OldPass.length() < 4 || OldPass.length() > 10 ){
                    Toast.makeText(getActivity(),"Podaj Poprawne Hasło",Toast.LENGTH_LONG).show();
                    return;
                }

                OldPassword.setVisibility(View.GONE);
                Next.setVisibility(View.GONE);
                UsunKontoTextView.setVisibility(View.GONE);
                UsunKonto.setVisibility(View.GONE);
                Password.setVisibility(View.VISIBLE);
                PotwierdzPassword.setVisibility(View.VISIBLE);
                ZmienHaslo.setVisibility(View.VISIBLE);
            }
        });

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
                        firebaseAuth = FirebaseAuth.getInstance();
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


        ZmienHaslo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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


                user = FirebaseAuth.getInstance().getCurrentUser();
                final String email = user.getEmail();
                AuthCredential credential = EmailAuthProvider.getCredential(email, OldPass);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
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
                        } else {
                            Toast.makeText(getActivity(), "Uwierzytelnianie nie powiodło się", Toast.LENGTH_LONG).show();
                            fragment = new fragmentSetings();
                        }

                        if (fragment != null) {
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, fragment);
                            ft.commit();
                        }
                    }
                });
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Ustawienia");
    }
}
