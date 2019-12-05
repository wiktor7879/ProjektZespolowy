package aplikacja.projektzespokowy2019;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Informacje;
import model.Waga;

public class PanelInfoOUzytkownikuActivity extends AppCompatActivity {

    private Button buttonGotowe;
    private EditText Imie;
    private EditText DataUrodzenia;
    private EditText Waga3;
    private EditText Wzrost;
    private TextInputLayout textInpImie;
    private TextInputLayout textInpWaga;
    private TextInputLayout textInpWzrost;
    private RadioButton Kobieta;
    private RadioButton Mezczyzna;
    private Button Kalendaz;
    private DatePickerDialog datePicker;
    private String Plec;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private List<Waga> Waga1 = new ArrayList<Waga>();
    private Pattern name_check = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_info);
        buttonGotowe = (Button) findViewById(R.id.buttonGotowe);
        //       Imie = (EditText) findViewById(R.id.editTextImie);
        DataUrodzenia = (EditText) findViewById(R.id.editTextData);
        // Waga3 = (EditText) findViewById(R.id.editTextWaga);
        //   Wzrost = (EditText) findViewById(R.id.editTextWzrost);
        textInpWzrost = (TextInputLayout) findViewById(R.id.textInputWzrost);
        textInpWaga = (TextInputLayout) findViewById(R.id.textInputWaga);
        textInpImie = (TextInputLayout) findViewById(R.id.textInputImie);
        Kobieta = (RadioButton) findViewById(R.id.radioKobieta);
        Mezczyzna = (RadioButton) findViewById(R.id.radioMezczyzna);
        Kalendaz = (Button) findViewById(R.id.buttonDateTimePicker);
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


                if (validateName() || validateVieght() || validateHeight()) {
                    if (Kobieta.isChecked()) {
                        Plec = "Kobieta";
                    } else if (Mezczyzna.isChecked()) {
                        Plec = "Meżczyzna";
                    }

                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c);
                    Waga w = new Waga(formattedDate, Integer.parseInt(textInpWaga.getEditText().getText().toString()   ));
                    Waga1.add(w);
                    Informacje Informacje = new Informacje(textInpImie.getEditText().getText().toString(),
                            DataUrodzenia.getText().toString(), Plec, Integer.parseInt(textInpWzrost.getEditText().getText().toString()),
                            Waga1);
                    databaseReference.child("Informacje").child(firebaseAuth.getCurrentUser().getUid()).setValue(Informacje);
                    Intent intent = new Intent(PanelInfoOUzytkownikuActivity.this, PanelMenuActivity.class);
                    startActivity(intent);

                }
            }
        });

    }


    private boolean validateHeight() {

        int wzr = Integer.parseInt(textInpWzrost.getEditText().getText().toString());
        if (textInpWzrost.getEditText().getText().toString().isEmpty()) {
            textInpWzrost.setError("Pole nie może być puste.");
            return true;
        } else if (wzr < 120 && wzr > 250) {
            textInpWzrost.setError("Wzrost musi byc w granicach (120-250)cm");
            return true;
        } else {
            return false;
        }
    }

    private boolean validateVieght() {

        int waga = Integer.parseInt(textInpWaga.getEditText().getText().toString());
        if (textInpWaga.getEditText().getText().toString().isEmpty()) {
            textInpWaga.setError("Pole nie może być puste.");
            return true;
        } else if (waga < 40 && waga > 250) {
            textInpWaga.setError("Waga musi byc w granicach (40-250)kg");
            return true;
        } else {
            return false;
        }
    }

    private boolean validateName() {
        String workout_name = textInpImie.getEditText().getText().toString().trim();
        Matcher m = name_check.matcher(workout_name);
        Boolean b = m.find();

        if (workout_name.isEmpty()) {
            textInpImie.setError("Pole nie może być puste.");
            return true;
        } else if (b) {
            textInpImie.setError("Zakazane znaki. Dozwolone [A-Z][a-z].");
            return true;
        } else {
            textInpImie.setError(null);
            return false;
        }
    }

}
