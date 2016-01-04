package com.homelane.phoenixapp.main.dashboard;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hl.hlcorelib.HLCoreLib;
import com.hl.hlcorelib.mvp.events.HLCoreEvent;
import com.hl.hlcorelib.mvp.events.HLEventDispatcher;
import com.hl.hlcorelib.mvp.presenters.HLCoreFragment;
import com.hl.hlcorelib.orm.HLObject;
import com.hl.hlcorelib.utils.HLNetworkUtils;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.main.MainPresenter;
import com.homelane.phoenixapp.main.project.ProjectPresenter;
import com.homelane.phoenixapp.main.project.overdue.OverduePresenter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hl0395 on 16/12/15.
 */
public class DashboardPresenter extends HLCoreFragment<DashboardView> {

    /**
     * RequestQueue for volley
     */
    RequestQueue volleyReqQueue;

    @Override
    protected void onBindView() {
        super.onBindView();

        ((MainPresenter)getActivity()).setTabVisible(true);

        volleyReqQueue = Volley.newRequestQueue(getActivity());

        if(HLNetworkUtils.isNetworkAvailable(getActivity()))
            getProjectList();
        else{
            Bundle bundle = new Bundle();
            bundle.putString(PhoenixConstants.SNACKBAR_DISPLAY_MESSAGE, "Please check your internet connection");

            HLCoreEvent event = new HLCoreEvent(PhoenixConstants.SNACKBAR_DISPLAY_EVENT,
                    bundle);
            HLEventDispatcher.acquire().dispatchEvent(event);
        }
/*
        setupViewPager();
        ((MainPresenter)getActivity()).getTabLayout().setupWithViewPager(mView.mViewPager);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("list", new ArrayList<Parcelable>());
        mProjectPresenter.setArguments(bundle);
*/


    }

    ProjectPresenter mProjectPresenter;
    OverduePresenter mOverduePresenter;

    /**
     * Function to create view pager adapter and set it to the view pager.
     * getChildFragmentManager() is used because, it has to recreate when the parent fragment
     * is created.
     */
    private void setupViewPager() {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(mProjectPresenter, "Projects Assigned");
        adapter.addFragment(mOverduePresenter, "Overdue Tasks");
        adapter.addFragment(new OverduePresenter(), "Pending Tasks");
        mView.mViewPager.setAdapter(adapter);
    }

    private void getProjectList(){
        mProjectPresenter = new ProjectPresenter();
        mOverduePresenter = new OverduePresenter();

        final String baseUrl = HLCoreLib.readProperty(PhoenixConstants.AppConfig.HL_PROJECT_DETAILS_URL);

        StringRequest request = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray projects = jsonObject.getJSONArray("projects");

                    ArrayList<HLObject> projectList = new ArrayList<HLObject>();

                    for (int i=0; i < projects.length(); i++){
                        HLObject hlProject = new HLObject(PhoenixConstants.Project.NAME);
                        JSONObject project = projects.getJSONObject(i);

                        hlProject.put(PhoenixConstants.Project.PROJECT_NAME, project.getString("projectName"));
                        hlProject.put(PhoenixConstants.Project.PROJECT_LOCATION, project.getString("location"));
                        if(project.has("state"))
                        hlProject.put(PhoenixConstants.Project.PROJECT_STATE, project.getString("state"));
                        hlProject.put(PhoenixConstants.Project.PROJECT_STATUS, project.getString("status"));

                        projectList.add(hlProject);
                    }

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", projectList);

                    mProjectPresenter.setArguments(bundle);




                    setupViewPager();
                    ((MainPresenter)getActivity()).getTabLayout().setupWithViewPager(mView.mViewPager);

                }catch (Exception e){
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
