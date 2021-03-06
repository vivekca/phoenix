package com.homelane.phoenixapp.main;

import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.hl.hlcorelib.mvp.events.HLEventDispatcher;
import com.hl.hlcorelib.mvp.events.HLEventListener;
import com.hl.hlcorelib.mvp.presenters.HLCoreActivityPresenter;
import com.hl.hlcorelib.orm.HLObject;
import com.hl.hlcorelib.utils.HLFragmentUtils;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.R;
import com.homelane.phoenixapp.SearchEvent;
import com.homelane.phoenixapp.login.LoginPresenter;
import com.homelane.phoenixapp.main.dashboard.DashboardPresenter;
import com.homelane.phoenixapp.main.project.customer.CustomerPresenter;
import com.homelane.phoenixapp.main.project.filter.DatePickerFragment;
import com.homelane.phoenixapp.views.CircleImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainPresenter extends HLCoreActivityPresenter<MainView>
        implements NavigationView.OnNavigationItemSelectedListener, HLEventListener {

    private GoogleApiClient mGoogleApiClient;
    private boolean isFromDateSelected = true;
    private TextView mFromDate, mToDate;

    String mFromValue;
    String mToValue;
    CardView mCardView;
    Button mFilterButton;
    ImageView mCancelButton;
    TextView mDate;
    String mSelectedStatus;
    TextView mStatus;
    Spinner mStatusSpinner;
    boolean doubleBackToExitPressedOnce = false;
    SearchView searchView;
    boolean isFilterVisible = false;


    @Override
    protected void onBindView() {
        super.onBindView();

        setSupportActionBar(mView.mToolbar);
        mView.mToolbar.setTitle("Mobile Phoenix");

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

        if (!hasEventListener(PhoenixConstants.SNACKBAR_DISPLAY_EVENT, this))
            addEventListener(PhoenixConstants.SNACKBAR_DISPLAY_EVENT, this);

        if (!hasEventListener(PhoenixConstants.DISABLE_SEARCH_EVENT, this))
            addEventListener(PhoenixConstants.DISABLE_SEARCH_EVENT, this);

        if (!hasEventListener(PhoenixConstants.SELECTED_DATE_EVENT, this))
            addEventListener(PhoenixConstants.SELECTED_DATE_EVENT, this);

        if (!hasEventListener(PhoenixConstants.DISABLE_FILTER_EVENT, this))
            addEventListener(PhoenixConstants.DISABLE_FILTER_EVENT, this);

        if(!hasEventListener(PhoenixConstants.UPDATE_STATUS_EVENT,this))
            addEventListener(PhoenixConstants.UPDATE_STATUS_EVENT,this);

        setNavigationItemSelection(1);

        mView.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mView.mRightNavigationView);

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
        removeEventListener(PhoenixConstants.DISABLE_FILTER_EVENT, this);
        removeEventListener(PhoenixConstants.DISABLE_SEARCH_EVENT, this);
        removeEventListener(PhoenixConstants.UPDATE_STATUS_EVENT, this);
    }

    @Override
    public void onEvent(HLEvent hlEvent) {
        HLCoreEvent e = (HLCoreEvent) hlEvent;
        Bundle bundle = e.getmExtra();

        if (e.getType().equals(PhoenixConstants.SNACKBAR_DISPLAY_EVENT))
            showSnackBar(bundle.getString(PhoenixConstants.SNACKBAR_DISPLAY_MESSAGE));
        else if (e.getType().equals(PhoenixConstants.SELECTED_DATE_EVENT)) {
            String day = bundle.getString(PhoenixConstants.DatePicker.SELECTED_DAY).length() == 1 ?
                    "0" + bundle.getString(PhoenixConstants.DatePicker.SELECTED_DAY) :
                    bundle.getString(PhoenixConstants.DatePicker.SELECTED_DAY);
            String month = bundle.getString(PhoenixConstants.DatePicker.SELECTED_MONTH).length() == 1 ?
                    "0" + bundle.getString(PhoenixConstants.DatePicker.SELECTED_MONTH) :
                    bundle.getString(PhoenixConstants.DatePicker.SELECTED_MONTH);


            if (isFromDateSelected) {
                mFromDate.setText(day + "-" + month + "-" +
                        bundle.getString(PhoenixConstants.DatePicker.SELECTED_YEAR));
                mFromValue = bundle.getString(PhoenixConstants.DatePicker.SELECTED_DAY) + "-" +
                        bundle.getString(PhoenixConstants.DatePicker.SELECTED_MONTH) + "-" +
                        bundle.getString(PhoenixConstants.DatePicker.SELECTED_YEAR);
            } else {
                mToDate.setText(day + "-" + month + "-" +
                        bundle.getString(PhoenixConstants.DatePicker.SELECTED_YEAR));

                mToValue = day + "-" + month + "-" + bundle.getString(PhoenixConstants.DatePicker.SELECTED_YEAR);

            }
        } else if (e.getType().equals(PhoenixConstants.DISABLE_SEARCH_EVENT))
            searchView.setIconified(true);
        else if (e.getType().equals(PhoenixConstants.DISABLE_FILTER_EVENT)) {
            isFilterVisible = bundle.getBoolean(PhoenixConstants.FILTER_STATUS);
            supportInvalidateOptionsMenu();
        }else if(e.getType().equals(PhoenixConstants.UPDATE_STATUS_EVENT)){

            final ArrayList<String>spinnerItems = bundle.getStringArrayList(PhoenixConstants.STATUS_LIST);

            ArrayAdapter<String> stringArrayAdapter =new ArrayAdapter<String>(
                    this,android.R.layout.simple_spinner_item,spinnerItems);
            stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mStatusSpinner.setAdapter(stringArrayAdapter);
            mFromDate.setText(getString(R.string.select_date));
            mToDate.setText(getString(R.string.select_date));

            mStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String spinnerValue = spinnerItems.get(position);
                    mSelectedStatus = spinnerValue;

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    /**
     * Function to change the visiblity of the tab bar
     *
     * @param isVisible true will make the tab layout visible else not
     */
    public void setTabVisible(boolean isVisible) {
        if (isVisible)
            mView.mTabLayout.setVisibility(View.VISIBLE);
        else
            mView.mTabLayout.setVisibility(View.GONE);
    }

    /**
     * Getter method for the tab layout
     *
     * @return tab layout
     */
    public TabLayout getTabLayout() {
        return mView.mTabLayout;
    }


    /**
     * Function to initialize the left sliding navigation view
     */
    private void setLeftNavigationView() {
        mView.mLeftNavigationView.setNavigationItemSelectedListener(this);

        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, mView.mLeftNavigationView);
        CircleImageView imageView = (CircleImageView) headerView.findViewById(R.id.imageView);
        TextView userName = (TextView) headerView.findViewById(R.id.customer_name);
        TextView userEmail = (TextView) headerView.findViewById(R.id.customer_email);

        userName.setText(LoginPresenter.mGoogleAccount.getDisplayName());
        userEmail.setText(LoginPresenter.mGoogleAccount.getEmail());

        if (LoginPresenter.mGoogleAccount.getPhotoUrl() != null)
            imageView.loadImageURL(LoginPresenter.mGoogleAccount.getPhotoUrl().toString());
    }


    /**
     * Function to initialize the right sliding navigation view
     */
    private void setRightNavigationView() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.filter_layout, mView.mRightNavigationView);
        mFromDate = (TextView) headerView.findViewById(R.id.start_date);
        mToDate = (TextView) headerView.findViewById(R.id.end_date);
        mCardView = (CardView) headerView.findViewById(R.id.filter_cardView);
        mFilterButton = (Button) headerView.findViewById(R.id.filter_button);
        mCancelButton = (ImageView) headerView.findViewById(R.id.cancel_image);
        mDate = (TextView) headerView.findViewById(R.id.date);
        mStatus = (TextView) headerView.findViewById(R.id.task_status);

        final Calendar calendar = Calendar.getInstance();

        mStatusSpinner = (Spinner) headerView.findViewById(R.id.status_spinner);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardView.setVisibility(View.GONE);
                mStatusSpinner.setSelection(0);
                mFromDate.setText(getString(R.string.select_date));
                mToDate.setText(getString(R.string.select_date));

                if (mView.mDrawerLayout.isDrawerOpen(GravityCompat.END))
                    mView.mDrawerLayout.closeDrawer(GravityCompat.END);

                HLCoreEvent event = new HLCoreEvent(PhoenixConstants.FILTER_EVENT, null);
                HLEventDispatcher.acquire().dispatchEvent(event);

            }
        });

        mFromDate.setText(getString(R.string.select_date));
        mToDate.setText(getString(R.string.select_date));

        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardView.setVisibility(View.VISIBLE);
                mStatus.setText(mSelectedStatus);

                if (mFromDate.getText().equals(getString(R.string.select_date)))
                    mDate.setVisibility(View.GONE);
                else {
                    mDate.setVisibility(View.VISIBLE);
                    mDate.setText(mFromDate.getText() + " <=> " + mToDate.getText());
                }

                HLObject task = new HLObject(PhoenixConstants.Task.TASK_NAME);
                task.put(PhoenixConstants.Task.TASK_STATUS, mSelectedStatus);
                task.put(PhoenixConstants.Task.START_DATE, (String) mFromDate.getText());
                task.put(PhoenixConstants.Task.COMPLETED_DATE, (String) mToDate.getText());
                Bundle bundle = new Bundle();
                bundle.putParcelable(PhoenixConstants.Task.FILTER, task);

                HLCoreEvent event = new HLCoreEvent(PhoenixConstants.FILTER_EVENT, bundle);
                HLEventDispatcher.acquire().dispatchEvent(event);

                if (mView.mDrawerLayout.isDrawerOpen(GravityCompat.END))
                    mView.mDrawerLayout.closeDrawer(GravityCompat.END);
            }
        });


        mFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromDateSelected = true;
                final DialogFragment newFragment = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean(PhoenixConstants.Task.TASK_FLAG, isFromDateSelected);
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), MainPresenter.class.getName());
            }
        });

        mToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromDateSelected = false;
                final DialogFragment newFragment = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean(PhoenixConstants.Task.TASK_FLAG, isFromDateSelected);
                bundle.putString(PhoenixConstants.Task.START_DATE, (String) mFromDate.getText());
                newFragment.setArguments(bundle);
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
     *
     * @param message to the displayed in the snack bar
     */
    private void showSnackBar(String message) {

        Snackbar.make(mView.mDrawerLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (mView.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mView.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mView.mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mView.mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {

            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                setNavigationItemSelection(getSupportFragmentManager().getBackStackEntryCount() - 1);
                super.onBackPressed();
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
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
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

                if (newText != null) {
                    final Bundle data = new Bundle();
                    data.putString(Intent.ACTION_SEARCH, newText);
                    final SearchEvent searchEvent = new SearchEvent(PhoenixConstants.SEARCH_EVENT, data,
                            SearchEvent.Category.SEARCH);
                    dispatchEvent(searchEvent);
                }
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
        } else if (id == R.id.action_filter)
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

    /**
     * Function that fetches the mobile manufacturer and mocel
     *
     * @return the manufacturer and mocel
     */
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    /**
     * Function to convert the string to uppercase
     *
     * @param s is the input string to the converted to uppercase
     * @return the uppercase converted string
     */
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

    /**
     * Set the selected background for the navigation item on the left
     * @param position
     */
    private void setNavigationItemSelection(int position){
        switch (position){
            case 1:
                mView.mLeftNavigationView.setCheckedItem(R.id.nav_dashboard);
                mView.mToolbar.setSubtitle("Dashboard");
                mView.mToolbar.setSubtitleTextAppearance(this, android.R.style.TextAppearance_Small);
                mView.mToolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                mView.mLeftNavigationView.setCheckedItem(R.id.nav_my_customers);
                break;
            case 3:
                mView.mLeftNavigationView.setCheckedItem(R.id.nav_all_customers);
                break;

        }
    }

    /**
     * Prepare the Screen's standard options menu to be displayed.  This is
     * called right before the menu is shown, every time it is shown.  You can
     * use this method to efficiently enable/disable items or otherwise
     * dynamically modify the contents.
     * <p/>
     * <p>The default implementation updates the system menu items based on the
     * activity's state.  Deriving classes should always call through to the
     * base class implementation.
     *
     * @param menu The options menu as last shown or first initialized by
     *             onCreateOptionsMenu().
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_filter);

        if (isFilterVisible)
            item.setEnabled(true);
        else
            item.setEnabled(false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            navigateTo(DashboardPresenter.class);
            mView.mToolbar.setSubtitle("Dashboard");
        } else if (id == R.id.nav_my_customers) {
            navigateTo(CustomerPresenter.class);
            mView.mToolbar.setSubtitle("My Customers");
        } else if (id == R.id.nav_share) {
            openGmailApp();
        }

        mView.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Function that navigates to the new presenter
     *
     * @param c is the new presenter class to be navigated
     */
    private void navigateTo(Class c) {
        HLCoreEvent event = new HLCoreEvent(PhoenixConstants.DISABLE_SEARCH_EVENT,
                null);
        HLEventDispatcher.acquire().dispatchEvent(event);
        HLFragmentUtils.HLFragmentTransaction transaction =
                new HLFragmentUtils.HLFragmentTransaction();
        transaction.mFrameId = R.id.fragment_frame;
        transaction.mFragmentClass = c;
        push(transaction);
    }

    /**
     * Function that collects the mobile details like os, model etc and launches
     * the gmail app to send feedback to the developers
     */
    private void openGmailApp() {
        Spanned text = Html.fromHtml("Android OS Version: " + android.os.Build.VERSION.SDK_INT + "<br>" +
                "Device Information: " + getDeviceName() + "<br>");

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"rajesh.c@homelane.com," +
                "vivek.c@homelane.com,", "sunil.a@homelane.com", "bhanuprasad.m@homelane.com", "vinith.k@homelane.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback for Mobile Phoenix app");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        emailIntent.setType("text/plain");
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(emailIntent);

    }
}
