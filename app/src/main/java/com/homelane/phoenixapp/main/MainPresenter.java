package com.homelane.phoenixapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.hl.hlcorelib.mvp.events.HLCoreEvent;
import com.hl.hlcorelib.mvp.events.HLEvent;
import com.hl.hlcorelib.mvp.events.HLEventListener;
import com.hl.hlcorelib.mvp.presenters.HLCoreActivityPresenter;
import com.hl.hlcorelib.utils.HLFragmentUtils;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.main.dashboard.DashboardPresenter;
import com.homelane.phoenixapp.views.CircleImageView;
import com.homelane.phoenixapp.R;
import com.homelane.phoenixapp.login.LoginPresenter;

public class MainPresenter extends HLCoreActivityPresenter<MainView>
        implements NavigationView.OnNavigationItemSelectedListener, HLEventListener {

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onBindView() {
        super.onBindView();

        setSupportActionBar(mView.mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mView.mDrawer, mView.mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mView.mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        setNavigationView();

        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


         HLFragmentUtils.HLFragmentTransaction transaction =
                 new HLFragmentUtils.HLFragmentTransaction();
         transaction.isRoot = true;
         transaction.mFrameId = R.id.fragment_frame;
         transaction.mFragmentClass = DashboardPresenter.class;
         push(transaction);
        mView.mNavigationView.setCheckedItem(R.id.nav_dashboard);

        if(! hasEventListener(PhoenixConstants.SNACKBAR_DISPLAY_EVENT,this))
            addEventListener(PhoenixConstants.SNACKBAR_DISPLAY_EVENT,this);

    }

    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                }
            });
        }
    }

    @Override
    public void onEvent(HLEvent hlEvent) {
        HLCoreEvent e = (HLCoreEvent)hlEvent;
        Bundle bundle=e.getmExtra();

        if(e.getType().equals(PhoenixConstants.SNACKBAR_DISPLAY_EVENT))
            showSnackBar(bundle.getString(PhoenixConstants.SNACKBAR_DISPLAY_MESSAGE));

    }

    /**
     * Function to change the visiblity of the tab bar
     * @param isVisible true will make the tab layout visible else not
     */
    public void setTabVisible(boolean isVisible){
        if(isVisible)
            mView.mTabLayout.setVisibility(View.VISIBLE);
        else
            mView.mTabLayout.setVisibility(View.GONE);
    }

    /**
     * Getter method for the tab layout
     * @return tab layout
     */
    public TabLayout getTabLayout(){
        return mView.mTabLayout;
    }


    /**
     * Function to the sliding navigation view
     */
    private void setNavigationView() {
        mView.mNavigationView.setNavigationItemSelectedListener(this);

        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, mView.mNavigationView);
        CircleImageView imageView = (CircleImageView) headerView.findViewById(R.id.imageView);
        TextView userName = (TextView) headerView.findViewById(R.id.customer_name);
        TextView userEmail = (TextView) headerView.findViewById(R.id.customer_email);

        userName.setText(LoginPresenter.mGoogleAccount.getDisplayName());
        userEmail.setText(LoginPresenter.mGoogleAccount.getEmail());

        if(LoginPresenter.mGoogleAccount.getPhotoUrl() != null)
            imageView.loadImageURL(LoginPresenter.mGoogleAccount.getPhotoUrl().toString());
    }

    @Override
    protected Class<MainView> getVuClass() {
        return MainView.class;
    }

    /**
     * Fuction to show the snack bar
     * @param message to the displayed in the snack bar
     */
    private void showSnackBar(String message){

        Snackbar.make(mView.mDrawer,message,Snackbar.LENGTH_SHORT).show();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;

            showSnackBar("Please click BACK again to exit");

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {

            signOut();

            return false;
        }else if(id == R.id.action_filter){
            HLFragmentUtils.HLFragmentTransaction transaction =
                    new HLFragmentUtils.HLFragmentTransaction();
            transaction.mFrameId = R.id.fragment_frame;
            transaction.mFragmentClass = DashboardPresenter.class;
            push(transaction);

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Function to signout from the google login
     */
    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent intent = new Intent(MainPresenter.this, LoginPresenter.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        mView.mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
