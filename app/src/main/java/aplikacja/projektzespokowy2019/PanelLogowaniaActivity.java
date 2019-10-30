package aplikacja.projektzespokowy2019;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


public class PanelLogowaniaActivity extends Fragment {

    View v;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private Button buttonLogin;
    private EditText editTextEmail;
    private  EditText editTextPassword;
    private ProgressDialog progressDialog;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.activity_panel_logowania, container, false);


            buttonLogin = (Button) v.findViewById(R.id.buttonZaloguj);
            editTextEmail = (EditText) v.findViewById(R.id.editTextEmail);
            editTextPassword = (EditText) v.findViewById(R.id.editTextPassword);
            mAuth = FirebaseAuth.getInstance();
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            progressDialog = new ProgressDialog(getActivity());

            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userLogin();
                }
            });
            return v;
    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(),"Podaj Email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(getActivity(),"Podaj Hasło",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Logowanie Trwa...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(),"Zalogowano",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(), PanelMenuActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getActivity(),"Logowanie Nieudane", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
}
