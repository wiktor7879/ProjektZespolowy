package aplikacja.projektzespokowy2019;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Informacje;

public class PanelInfoOUzytkownikuActivity extends AppCompatActivity {

    private Button buttonGotowe;
    private EditText Imie;
    private EditText DataUrodzenia;
    private EditText Waga;
    private EditText Wzrost;
    private RadioButton Kobieta;
    private RadioButton Mezczyzna;
    private Button Kalendaz;
    private DatePickerDialog datePicker;
    private String Plec;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_info);
        buttonGotowe = (Button) findViewById(R.id.buttonGotowe);
        Imie = (EditText) findViewById(R.id.editTextImie);
        DataUrodzenia = (EditText) findViewById(R.id.editTextData);
        Waga = (EditText) findViewById(R.id.editTextWaga);
        Wzrost = (EditText) findViewById(R.id.editTextWzrost);
        Kobieta = (RadioButton) findViewById(R.id.radioKobieta);
        Mezczyzna = (RadioButton) findViewById(R.id.radioMezczyzna);
        Kalendaz = (Button)findViewById(R.id.buttonDateTimePicker);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        Kalendaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker = new DatePickerDialog(PanelInfoOUzytkownikuActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                DataUrodzenia.setText(day + "/" + month + "/" + year);
                            }
                        }, 2000, 1, 1);
                datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePicker.show();
            }
        });

        buttonGotowe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Kobieta.isChecked())
                {
                    Plec = "Kobieta";
                }
                else if (Mezczyzna.isChecked())
                {
                    Plec = "Meżczyzna";
                }
                Informacje Informacje = new Informacje(Imie.getText().toString(),DataUrodzenia.getText().toString(),Plec,Integer.parseInt(Wzrost.getText().toString()),Integer.parseInt(Waga.getText().toString()));
                databaseReference.child("Informacje").child(firebaseAuth.getCurrentUser().getUid()).setValue(Informacje);
                Intent intent = new Intent(PanelInfoOUzytkownikuActivity.this,PanelMenuActivity.class);
                startActivity(intent);
            }
        });
    }

}
