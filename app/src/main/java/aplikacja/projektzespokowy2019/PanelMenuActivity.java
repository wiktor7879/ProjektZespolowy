package aplikacja.projektzespokowy2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import model.Informacje;

public class PanelMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TextView nazwaUzytkownika;
    TextView adresEmail;
    private DatabaseReference reference_Database;
    private FirebaseDatabase firebase_Database;
    private FirebaseAuth mAuth;
    private DatabaseReference dReference;
    private Informacje info;
    String Imie;
    FirebaseDatabase mDatabase; //usunac
    DatabaseReference  mDbRef;

    String nazwa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_panel_zmenu); //ustawia caly layaout

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override //jezli przycisk ten na gorze wcisniety
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START); //zamyka drawera

        } else {
            super.onBackPressed();
        }
    }

    public void basicQueryValueListener() {
        String Uid;
        String  emeil;
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Informacje/"+Uid+"/imie");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Imie=dataSnapshot.getValue(String.class);
                nazwaUzytkownika.setText("Witaj " + Imie);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        nazwaUzytkownika = (TextView) findViewById(R.id.tVNazwaUzytkownika);
        adresEmail = (TextView) findViewById(R.id.tVadresEmail);
        reference_Database = FirebaseDatabase.getInstance().getReference(); //tutaj referencja, zeby dzialalo
        emeil = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        adresEmail.setText(emeil);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.panel_zmenu, menu);
        basicQueryValueListener();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Wyloguj) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_MojeZdrowie) { //tutaj sie musza otworzyc trzy poodokienka

        } else if (id == R.id.nav_Statystyka) {

        } else if (id == R.id.navWykonajPlan) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
