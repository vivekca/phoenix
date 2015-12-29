package com.homelane.phoenixapp.main;

import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.homelane.phoenixapp.main.project.filter.DatePickerFragment;
import com.homelane.phoenixapp.views.CircleImageView;
import com.homelane.phoenixapp.R;
import com.homelane.phoenixapp.login.LoginPresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainPresenter extends HLCoreActivityPresenter<MainView>
        implements NavigationView.OnNavigationItemSelectedListener, HLEventListener {

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onBindView() {
        super.onBindView();

        setSupportActionBar(mView.mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mView.mDrawerLayout, mView.mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mView.mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        setLeftNavigationView();
        setRightNavigationView();

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
        mView.mLeftNavigationView.setCheckedItem(R.id.nav_dashboard);

        if(! hasEventListener(PhoenixConstants.SNACKBAR_DISPLAY_EVENT,this))
            addEventListener(PhoenixConstants.SNACKBAR_DISPLAY_EVENT,this);
        if(! hasEventListener(PhoenixConstants.SELECTED_DATE_EVENT,this))
            addEventListener(PhoenixConstants.SELECTED_DATE_EVENT,this);

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
    protected void onDestroyHLView() {
        super.onDestroyHLView();
        removeEventListener(PhoenixConstants.SNACKBAR_DISPLAY_EVENT, this);
        removeEventListener(PhoenixConstants.SELECTED_DATE_EVENT, this);
    }

    @Override
    public void onEvent(HLEvent hlEvent) {
        HLCoreEvent e = (HLCoreEvent)hlEvent;
        Bundle bundle=e.getmExtra();

        if(e.getType().equals(PhoenixConstants.SNACKBAR_DISPLAY_EVENT))
            showSnackBar(bundle.getString(PhoenixConstants.SNACKBAR_DISPLAY_MESSAGE));
        else if(e.getType().equals(PhoenixConstants.SELECTED_DATE_EVENT)){
            if(isFromDateSelected) {
                mFromDate.setText(bundle.getString(PhoenixConstants.DatePicker.SELECTED_DAY) + "-" +
                        bundle.getString(PhoenixConstants.DatePicker.SELECTED_MONTH) + "-" +
                                bundle.getString(PhoenixConstants.DatePicker.SELECTED_YEAR));
            }else {
                mToDate.setText(bundle.getString(PhoenixConstants.DatePicker.SELECTED_DAY) + "-" +
                        bundle.getString(PhoenixConstants.DatePicker.SELECTED_MONTH) + "-" +
                                bundle.getString(PhoenixConstants.DatePicker.SELECTED_YEAR));
            }
        }

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
    private void setLeftNavigationView() {
        mView.mLeftNavigationView.setNavigationItemSelectedListener(this);

        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, mView.mLeftNavigationView);
        CircleImageView imageView = (CircleImageView) headerView.findViewById(R.id.imageView);
        TextView userName = (TextView) headerView.findViewById(R.id.customer_name);
        TextView userEmail = (TextView) headerView.findViewById(R.id.customer_email);

        userName.setText(LoginPresenter.mGoogleAccount.getDisplayName());
        userEmail.setText(LoginPresenter.mGoogleAccount.getEmail());

        if(LoginPresenter.mGoogleAccount.getPhotoUrl() != null)
            imageView.loadImageURL(LoginPresenter.mGoogleAccount.getPhotoUrl().toString());
    }

    boolean isFromDateSelected = true;
    TextView mFromDate, mToDate;

    private void setRightNavigationView() {
//        mView.mLeftNavigationView.setNavigationItemSelectedListener(this);

        View headerView = LayoutInflater.from(this).inflate(R.layout.filter_layout, mView.mRightNavigationView);
        mFromDate = (TextView) headerView.findViewById(R.id.start_date);
        mToDate = (TextView) headerView.findViewById(R.id.end_date);
        final Calendar calendar = Calendar.getInstance();

        Spinner spinner = (Spinner) headerView.findViewById(R.id.status_spinner);

        ArrayList<String> spinnerItems = new ArrayList<String>();
        spinnerItems.add("To send Initial Quote");
        spinnerItems.add("Initial Quote Sent");
        spinnerItems.add("Initial Quote Approved");
        spinnerItems.add("Initial Quote - Requested For Rev");
        spinnerItems.add("Initial Quote - Revision Sent");
        spinnerItems.add("Initial Quote - Revision Approved");

        ArrayAdapter<String> stringArrayAdapter =new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,spinnerItems);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringArrayAdapter);


        mFromDate.setText(calendar.get(Calendar.DAY_OF_MONTH)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.YEAR));
        mToDate.setText(calendar.get(Calendar.DAY_OF_MONTH)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.YEAR));


        mFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromDateSelected = true;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), MainPresenter.class.getName());

            }
        });

        mToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromDateSelected = false;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), MainPresenter.class.getName());

            }
        });

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

        Snackbar.make(mView.mDrawerLayout,message,Snackbar.LENGTH_SHORT).show();
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
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            signOut();
            return false;
        }else if(id == R.id.action_filter)
            mView.mDrawerLayout.openDrawer(GravityCompat.END);

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

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_share)
            openGmailApp();


        mView.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openGmailApp(){
        Spanned text = Html.fromHtml("Android OS Version: "+android.os.Build.VERSION.SDK_INT+"<br>"+
                "Device Information: "+getDeviceName()+"<br>");

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ "rajesh.c@homelane.com,vivek.c@homelane.com," +
                "sunil.a@homelane.com","bhanuprasad.m@homelane.com","vinith.k@homelane.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback for Mobile Phoenix app");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        emailIntent.setType("text/plain");
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for(final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(emailIntent);

    }
}
