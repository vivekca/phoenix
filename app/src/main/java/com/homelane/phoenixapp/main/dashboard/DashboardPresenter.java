package com.homelane.phoenixapp.main.dashboard;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hl.hlcorelib.HLCoreLib;
import com.hl.hlcorelib.mvp.events.HLCoreEvent;
import com.hl.hlcorelib.mvp.events.HLEvent;
import com.hl.hlcorelib.mvp.events.HLEventDispatcher;
import com.hl.hlcorelib.mvp.events.HLEventListener;
import com.hl.hlcorelib.mvp.presenters.HLCoreFragment;
import com.hl.hlcorelib.orm.HLObject;
import com.hl.hlcorelib.utils.HLNetworkUtils;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.SearchEvent;
import com.homelane.phoenixapp.main.MainPresenter;
import com.homelane.phoenixapp.main.project.ProjectPresenter;
import com.homelane.phoenixapp.main.project.overdue.OverduePresenter;
import com.homelane.phoenixapp.main.project.pending.PendingPresenter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by hl0395 on 16/12/15.
 */
public class DashboardPresenter extends HLCoreFragment<DashboardView> implements HLEventListener {

    /**
     * RequestQueue for volley
     */
    RequestQueue volleyReqQueue;
    ProjectPresenter mProjectPresenter;
    OverduePresenter mOverduePresenter;
    PendingPresenter mPendingPresenter;

    @Override
    protected void onBindView() {
        super.onBindView();

        ((MainPresenter) getActivity()).setTabVisible(true);

        volleyReqQueue = Volley.newRequestQueue(getActivity());

        if (HLNetworkUtils.isNetworkAvailable(getActivity()))
            getProjectList();
        else {
            Bundle bundle = new Bundle();
            bundle.putString(PhoenixConstants.SNACKBAR_DISPLAY_MESSAGE, "Please check your internet connection");

            HLCoreEvent event = new HLCoreEvent(PhoenixConstants.SNACKBAR_DISPLAY_EVENT,
                    bundle);
            HLEventDispatcher.acquire().dispatchEvent(event);
        }

        mView.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Bundle bundle = new Bundle();

                if (position == 0)
                    bundle.putBoolean(PhoenixConstants.FILTER_STATUS, false);
                else
                    bundle.putBoolean(PhoenixConstants.FILTER_STATUS, true);

                HLCoreEvent event = new HLCoreEvent(PhoenixConstants.DISABLE_FILTER_EVENT,
                        bundle);
                HLEventDispatcher.acquire().dispatchEvent(event);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                HLCoreEvent event = new HLCoreEvent(PhoenixConstants.DISABLE_SEARCH_EVENT,
                        null);
                HLEventDispatcher.acquire().dispatchEvent(event);

            }
        });


        if (!hasEventListener(PhoenixConstants.SEARCH_EVENT, this)) {
            addEventListener(PhoenixConstants.SEARCH_EVENT, this);
        }

        if (!hasEventListener(PhoenixConstants.FILTER_EVENT, this))
            addEventListener(PhoenixConstants.FILTER_EVENT, this);

    }

    /**
     * Function to create view pager adapter and set it to the view pager.
     * getChildFragmentManager() is used because, it has to recreate when the parent fragment
     * is created.
     */
    private void setupViewPager() {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(mProjectPresenter, "Projects Assigned");
        adapter.addFragment(mOverduePresenter, "Overdue Tasks");
        adapter.addFragment(mPendingPresenter, "Pending Tasks");
        mView.mViewPager.setAdapter(adapter);
    }

    @Override
    public void onEvent(HLEvent hlEvent) {
        HLCoreEvent e = (HLCoreEvent) hlEvent;
        Bundle bundle = e.getmExtra();

        if (e.getType().equals(PhoenixConstants.FILTER_EVENT)) {
            if (mView.mViewPager.getCurrentItem() == 1) {
                if (bundle != null)
                    mOverduePresenter.filterList((HLObject) bundle.getParcelable(PhoenixConstants.Task.FILTER));
                else
                    mOverduePresenter.filterList(null);
            }else if(mView.mViewPager.getCurrentItem() == 2){
                if(bundle != null)
                    mPendingPresenter.filterList((HLObject) bundle.getParcelable(PhoenixConstants.Task.FILTER));
                else
                    mPendingPresenter.filterList(null);

            }
        } else if (e.getType().equals(PhoenixConstants.SEARCH_EVENT)) {
            SearchEvent searchEvent = (SearchEvent) hlEvent;
            if ((searchEvent.getmCategory() == 2)) {
                if (mView.mViewPager.getCurrentItem() == 0) {
                    this.mProjectPresenter.searchList(searchEvent.getmExtra().getString("android.intent.action.SEARCH"));
                }
                else if(mView.mViewPager.getCurrentItem() == 2) {
                    this.mPendingPresenter.searchList(searchEvent.getmExtra().getString("android.intent.action.SEARCH"));
                }else{
                    this.mOverduePresenter.searchList(searchEvent.getmExtra().getString("android.intent.action.SEARCH"));
                }
            }
        }
    }

    /**
     * Function to request the server to fetch the project list
     * and set the set the view pager
     */
    private void getProjectList(){
        mProjectPresenter = new ProjectPresenter();
        mOverduePresenter = new OverduePresenter();
        mPendingPresenter = new PendingPresenter();

        final String baseUrl = HLCoreLib.readProperty(PhoenixConstants.AppConfig.HL_PROJECT_DETAILS_URL);

        StringRequest request = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray projects = jsonObject.getJSONArray("projects");
               //     mView.mProgressView.hideProgress();

                    ArrayList<HLObject> projectList = new ArrayList<HLObject>();
                    HashSet<String> taskList = new HashSet<String>();

                    for (int i = 0; i < projects.length(); i++) {
                        HLObject hlProject = new HLObject(PhoenixConstants.Project.NAME);
                        JSONObject project = projects.getJSONObject(i);

                        hlProject.put(PhoenixConstants.Project.PROJECT_NAME, project.getString("projectName"));
                        hlProject.put(PhoenixConstants.Project.PROJECT_LOCATION, project.getString("location"));
                        if (project.has("state"))
                            hlProject.put(PhoenixConstants.Project.PROJECT_STATE, project.getString("state"));
                        hlProject.put(PhoenixConstants.Project.PROJECT_STATUS, project.getString("status"));

                        projectList.add(hlProject);

                        JSONArray tasks = project.getJSONArray("tasks");

                        for (int j = 0; j < tasks.length(); j++) {
                            JSONObject task = tasks.getJSONObject(j);
                            taskList.add(task.getString("name"));
                        }
                    }
                    ArrayList<String> list =new ArrayList<String> (taskList);

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", projectList);
                    mProjectPresenter.setArguments(bundle);
                    bundle = new Bundle();
                    bundle.putStringArrayList("list", list);
                    mOverduePresenter.setArguments(bundle);

                    mPendingPresenter.setArguments(bundle);


                    bundle = new Bundle();
                    bundle.putStringArrayList(PhoenixConstants.STATUS_LIST, list);

                    HLCoreEvent event = new HLCoreEvent(PhoenixConstants.UPDATE_STATUS_EVENT,
                            bundle);
                    HLEventDispatcher.acquire().dispatchEvent(event);


                    setupViewPager();
                    ((MainPresenter) getActivity()).getTabLayout().setupWithViewPager(mView.mViewPager);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast(error.getMessage());
                error.printStackTrace();
            }
        });

        volleyReqQueue.add(request);

    }

    @Override
    protected Class<DashboardView> getVuClass() {
        return DashboardView.class;
    }

    @Override
    protected int getMenuLayout() {
        return 0;
    }

    @Override
    protected int[] getDisabledMenuItems() {
        return new int[0];
    }
}
