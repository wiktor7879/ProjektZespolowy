package aplikacja.projektzespokowy2019;



import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;


    @BindView(R.id.tablica)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PanelLogowaniaActivity(), "Logowanie");
        adapter.addFragment(new PanelRejestracjiActivity(), "Rejestracja");
        viewPager.setAdapter(adapter);
    }
}
