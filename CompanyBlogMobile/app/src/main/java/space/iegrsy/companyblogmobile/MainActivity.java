package space.iegrsy.companyblogmobile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.home_container)
    FrameLayout container;

    @BindView(R.id.home_navigation)
    BottomNavigationView navigation;

    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchFragment(homeFragment);
                    return true;
                case R.id.navigation_profile:
                    switchFragment(profileFragment);
                    return true;
            }
            return false;
        }
    };

    private int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userid = getIntent().getIntExtra("userid", -1);
        if (userid < 0)
            Snackbar.make(findViewById(android.R.id.content),
                    "Not create user try again later.", Snackbar.LENGTH_LONG).show();

        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        homeFragment = new HomeFragment(this);
        profileFragment = new ProfileFragment(this, userid);

        fragmentManager = getSupportFragmentManager();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    private void switchFragment(Fragment fragment) {
        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.home_container, fragment).commit();
    }
}
