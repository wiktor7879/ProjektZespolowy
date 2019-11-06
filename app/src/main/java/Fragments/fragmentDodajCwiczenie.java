package Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import aplikacja.projektzespokowy2019.R;

public class fragmentDodajCwiczenie extends Fragment {


    ImageView img;
    EditText edNazwaCwiczenia;
    EditText edOpis;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_dodaj_cwiczenie, container, false);
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments




        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Dodaj Cwiczenie");
    }


}
