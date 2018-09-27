package shiftboard.datpham.com.profileapp.Activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import shiftboard.datpham.com.profileapp.Fragments.DashboardFragment;
import shiftboard.datpham.com.profileapp.Fragments.HomeFragment;
import shiftboard.datpham.com.profileapp.Fragments.ProfileFragment;
import shiftboard.datpham.com.profileapp.R;
import shiftboard.datpham.com.profileapp.Unit.Constant;

public class ScreenActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener,DashboardFragment.OnFragmentInteractionListener{


    private Toolbar         mToolbar;
    private FragmentManager mManagerFragment;
    private TextView          mtvTitleToolBar;
    private ProfileFragment   mProfileFragment;
    private HomeFragment      mHomeFragment;
    private DashboardFragment mDashboardFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mtvTitleToolBar.setText(Constant.TITLE_HOME_PAGE);
                    loadHomeFragment();
                    return true;
                case R.id.navigation_dashboard:
                    mtvTitleToolBar.setText(Constant.TITLE_DASHBOARD_PAGE);
                    loadDashboardFragment();
                    return true;
                case R.id.navigation_profile:
                    mtvTitleToolBar.setText(Constant.TITLE_PROFILE_PAGE);
                    loadProfileFragment();
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        //Sets the toolbar to act as the ActionBar for this Activity.
        mToolbar        = (Toolbar)findViewById(R.id.toolbar);
        mtvTitleToolBar = (TextView)findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolbar);

        //Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Initial FragmentManager
        mManagerFragment = getSupportFragmentManager();
        //start by load home fragment first.
        mtvTitleToolBar.setText(Constant.TITLE_HOME_PAGE);

        loadHomeFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tootbar, menu);
        Drawable people = menu.getItem(0).getIcon();
        people.setColorFilter(getResources().getColor(R.color.colorWhite),PorterDuff.Mode.SRC_IN);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itemProfile) {
            if(mProfileFragment != null) {
                FragmentTransaction ft = mManagerFragment.beginTransaction();
                ft.detach(mProfileFragment).attach(mProfileFragment).commit();

            }else
                loadProfileFragment();
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadProfileFragment(){
        if(mProfileFragment == null)
            mProfileFragment = new ProfileFragment();
        FragmentTransaction transaction = mManagerFragment.beginTransaction();
        transaction.replace(R.id.content,mProfileFragment);
        transaction.commitAllowingStateLoss();
    }

    private void loadDashboardFragment(){
        if(mDashboardFragment == null)
            mDashboardFragment = new DashboardFragment();
        FragmentTransaction transaction = mManagerFragment.beginTransaction();
        transaction.replace(R.id.content,mDashboardFragment);
        transaction.commitAllowingStateLoss();
    }

    private void loadHomeFragment(){
        if(mHomeFragment == null)
            mHomeFragment = new HomeFragment();
        FragmentTransaction transaction = mManagerFragment.beginTransaction();
        transaction.replace(R.id.content,mHomeFragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
