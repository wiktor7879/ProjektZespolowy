package Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aplikacja.projektzespokowy2019.R;

public class fragmentHome extends Fragment {

    private TextView Welcome;
@Nullable
@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
    View v = inflater.inflate(R.layout.activity_fragment_home, container, false);
        Welcome = (TextView) v.findViewById(R.id.textViewWelcome);
        Welcome.setText("Witaj w Aplikacji, która pozwoli ci na kontrolowanie swojego treningu");
    return v;
        }


@Override
public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
        }
        }
