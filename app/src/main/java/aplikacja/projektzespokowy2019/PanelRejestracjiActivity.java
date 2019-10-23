package aplikacja.projektzespokowy2019;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class PanelRejestracjiActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(PanelRejestracjiActivity.this, PanelLogowaniaActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_rejestracji);


        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editTextEmailRejestracja);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswordRejestracja);
        buttonSignup = (Button) findViewById(R.id.buttonZalozKonto);
        progressDialog = new ProgressDialog(this);

        buttonSignup.setOnClickListener(this);



    }

    private void registerUser(){

        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email) ||  !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Podaj Prawidłowy Adres Email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password) || password.length() < 4 || password.length() > 10 ){
            Toast.makeText(this,"Podaj Prawidłowe Hasło",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Rejestracja trwa ...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(PanelRejestracjiActivity.this,"Rejestracja Udana",Toast.LENGTH_LONG).show();
                            finish();
                            Intent intent = new Intent(PanelRejestracjiActivity.this, PanelMenuActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(PanelRejestracjiActivity.this,"Błąd Rejestracji",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        registerUser();
    }

}
