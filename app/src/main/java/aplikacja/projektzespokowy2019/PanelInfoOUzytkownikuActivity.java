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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Informacje;
import model.Waga;

public class PanelInfoOUzytkownikuActivity extends AppCompatActivity {

    private Button buttonGotowe;
    private EditText Imie;
    private EditText DataUrodzenia;
    private EditText Waga3;
    private EditText Wzrost;
    private RadioButton Kobieta;
    private RadioButton Mezczyzna;
    private Button Kalendaz;
    private DatePickerDialog datePicker;
    private String Plec;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private List<Waga> Waga1 = new ArrayList<Waga>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_info);
        buttonGotowe = (Button) findViewById(R.id.buttonGotowe);
        Imie = (EditText) findViewById(R.id.editTextImie);
        DataUrodzenia = (EditText) findViewById(R.id.editTextData);
        Waga3 = (EditText) findViewById(R.id.editTextWaga);
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
                        }, 2000, 0, 1);
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

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);
                Waga w = new Waga(formattedDate,Integer.parseInt(Waga3.getText().toString()));
                Waga1.add(w);
                Informacje Informacje = new Informacje(Imie.getText().toString(),DataUrodzenia.getText().toString(),Plec,Integer.parseInt(Wzrost.getText().toString()),Waga1);
                databaseReference.child("Informacje").child(firebaseAuth.getCurrentUser().getUid()).setValue(Informacje);
                Intent intent = new Intent(PanelInfoOUzytkownikuActivity.this,PanelMenuActivity.class);
                startActivity(intent);
            }
        });
    }

}
