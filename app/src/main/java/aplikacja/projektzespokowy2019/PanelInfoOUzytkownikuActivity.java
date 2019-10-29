package aplikacja.projektzespokowy2019;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class PanelInfoOUzytkownikuActivity extends AppCompatActivity {
    // odpala sie tylko raz po rejestracji uzytkownika po wpisaniu informacji przechodzimy do panelu menu
    // imie
    // data uro
    // waga
    // wzrost
    // plec

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_info);
    }

}
