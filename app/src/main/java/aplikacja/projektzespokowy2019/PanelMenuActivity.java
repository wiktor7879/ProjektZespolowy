package aplikacja.projektzespokowy2019;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class PanelMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonWyloguj;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_menu);
        buttonWyloguj = (Button)findViewById(R.id.buttonWyloguj);
        buttonWyloguj.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonWyloguj) {
            firebaseAuth.getInstance().signOut();
            Toast.makeText(PanelMenuActivity.this, "Wylogowano", Toast.LENGTH_LONG).show();
            finish();
            Intent intent = new Intent(PanelMenuActivity.this, PanelLogowaniaActivity.class);
            startActivity(intent);
        }
    }
}
