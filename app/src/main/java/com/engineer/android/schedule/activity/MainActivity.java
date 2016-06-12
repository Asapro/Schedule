package com.engineer.android.schedule.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.engineer.android.schedule.R;
import com.engineer.android.schedule.fragment.CameraFragment;
import com.engineer.android.schedule.fragment.GalleryFragment;
import com.engineer.android.schedule.interaction.MainAction;
import com.engineer.android.schedule.presenter.MainPresenter;

public class MainActivity extends BaseActivity<MainPresenter>
        implements NavigationView.OnNavigationItemSelectedListener,MainAction {

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }
    private Fragment currentFragment;
    private ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        ImageView avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.circle_image_view_avatar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.addDrawerListener(this.toggle);
        this.toggle.syncState();
    }

    @Override
    protected void onStop() {
        super.onStop();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.removeDrawerListener(this.toggle);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected MainPresenter newPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            this.presenter.onNavCameraClick();
        } else if (id == R.id.nav_gallery) {
            this.presenter.onNavGalleryClick();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_history) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void startSettingActivity() {

    }

    @Override
    public void startHistoryActivity() {

    }

    @Override
    public void startEditActivity() {

    }

    @Override
    public void showCalendarDialog() {

    }

    @Override
    public void openCamera() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("camera");
        if(fragment == null){
            fragment = new CameraFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container,fragment,"camera").commitAllowingStateLoss();
        }else{
            getSupportFragmentManager().beginTransaction().hide(currentFragment).show(fragment).commitAllowingStateLoss();
        }
        this.currentFragment = fragment;
    }

    @Override
    public void openGallery() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("gallery");
        if(fragment == null){
            fragment = new GalleryFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container,fragment,"gallery").commitAllowingStateLoss();
        }else{
            getSupportFragmentManager().beginTransaction().hide(currentFragment).show(fragment).commitAllowingStateLoss();
        }
        this.currentFragment = fragment;
    }

    @Override
    public void openTools() {

    }

    @Override
    public void openHistory() {

    }

    @Override
    public AppCompatActivity provideContext() {
        return this;
    }

    @Override
    public void finishActivity() {

    }
}
