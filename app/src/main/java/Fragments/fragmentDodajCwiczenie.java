package Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import aplikacja.projektzespokowy2019.R;
import model.Cwiczenie;

public class fragmentDodajCwiczenie extends Fragment {


    ImageView img, img2, img3;
    ImageView x1, x2, x3;

    EditText edNazwaCwiczenia;
    EditText edOpis;
    EditText edIloscSerii;
    private Button  buttonDodajCwiczenie;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_dodaj_cwiczenie, container, false);
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments


        img = (ImageView) v.findViewById(R.id.imageView2);
        img2 = (ImageView) v.findViewById(R.id.imageView3);
        img3 = (ImageView) v.findViewById(R.id.imageView4);

        x1 = (ImageView) v.findViewById(R.id.iVx1);
        x2 = (ImageView) v.findViewById(R.id.iVx2);
        x3 = (ImageView) v.findViewById(R.id.iVx3);

        edNazwaCwiczenia = (EditText) v.findViewById(R.id.editTextNazwaCwiczenia);
        edOpis = (EditText) v.findViewById(R.id.editTextOpis);
        edIloscSerii = (EditText) v.findViewById(R.id.editTextIloscSerii);

        edNazwaCwiczenia.addTextChangedListener(generalTextWatcher);
        edOpis.addTextChangedListener(generalTextWatcher);
        edIloscSerii.addTextChangedListener(generalTextWatcher);
        buttonDodajCwiczenie = (Button) v.findViewById(R.id.btnDodajCwiczenie);
        final Random generator = new Random();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        x1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edNazwaCwiczenia.setText("");
            }
        });

        x2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edOpis.setText("");
            }
        });
        x3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edIloscSerii.setText("");
            }
        });
        // img.setImageResource(R.drawable.ic_ok_icon);

        buttonDodajCwiczenie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer w = generator.nextInt(10000000);
                Cwiczenie c = new Cwiczenie(edNazwaCwiczenia.getText().toString(), edOpis.getText().toString(),
                        Integer.parseInt(edIloscSerii.getText().toString()),null);
                databaseReference.child("wlasne_cwiczenia").child(firebaseAuth.getCurrentUser().getUid()).child(w.toString()).setValue(c);
                Toast.makeText(getActivity(), "Dodano Cwiczenie", Toast.LENGTH_LONG).show();
            }
        });


        return v;
    }


    private TextWatcher generalTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

            if (edNazwaCwiczenia.getText().hashCode() == s.hashCode()) {
                edNazwaCwiczenia_onTextChanged(s, start, before, count);
            } else if (edOpis.getText().hashCode() == s.hashCode()) {
                edOpis_onTextChanged(s, start, before, count);
            } else if (edIloscSerii.getText().hashCode() == s.hashCode()) {
                edIloscSerii_onTextChanged(s, start, before, count);
            }
        }


        private void edIloscSerii_onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) { //tutaj sprawdzic inta
                img3.setImageResource(R.drawable.ic_ok_icon);
            } else {
                img3.setImageResource(R.drawable.ic_gray_circle);
            }

        }


        private boolean checkNameAndDescription(CharSequence s) {
            if (s.length() > 0 && s.length() < 30) {
                return true;
            } else {
                return false;
            }
        }

        private void edOpis_onTextChanged(CharSequence s, int start, int before, int count) {
            if (checkNameAndDescription(s)) {
                img2.setImageResource(R.drawable.ic_ok_icon);
            } else {
                img2.setImageResource(R.drawable.ic_gray_circle);
            }
        }

        private void edNazwaCwiczenia_onTextChanged(CharSequence s, int start, int before, int count) {
            if (checkNameAndDescription(s)) {
                img.setImageResource(R.drawable.ic_ok_icon);
            } else {
                img.setImageResource(R.drawable.ic_gray_circle);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Dodaj Cwiczenie");
    }
}