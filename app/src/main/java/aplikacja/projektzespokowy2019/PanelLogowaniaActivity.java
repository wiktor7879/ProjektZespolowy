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
import com.google.firebase.auth.FirebaseUser;


public class PanelLogowaniaActivity extends AppCompatActivity implements View.OnClickListener  {

    private Button buttonSingup;
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_logowania);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       if (user != null) {
            finish();
            Intent intent = new Intent(PanelLogowaniaActivity.this, PanelMenuActivity.class);
            startActivity(intent);
        }
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonZaloguj);

        buttonSingup = (Button) findViewById(R.id.buttonNoweKonto);
        buttonSingup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PanelLogowaniaActivity.this, PanelRejestracjiActivity.class);
                startActivity(intent);
            }
        });

        progressDialog = new ProgressDialog(this);
        buttonSignIn.setOnClickListener(this);




    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(PanelLogowaniaActivity.this,"Podaj Email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(PanelLogowaniaActivity.this,"Podaj Hasło",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Logowanie Trwa...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(PanelLogowaniaActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(PanelLogowaniaActivity.this,"Zalogowano",Toast.LENGTH_LONG).show();
                            finish();
                            Intent intent = new Intent(PanelLogowaniaActivity.this, PanelMenuActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(PanelLogowaniaActivity.this,"Logowanie Nieudane",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
    @Override
    public void onClick(View view) {
        if(view == buttonSignIn){
            userLogin();
        }
    }

}




