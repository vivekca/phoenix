package com.homelane.phoenixapp.main.project;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.hl.hlcorelib.mvp.events.HLCoreEvent;
import com.hl.hlcorelib.mvp.events.HLEvent;
import com.hl.hlcorelib.mvp.events.HLEventListener;
import com.hl.hlcorelib.mvp.presenters.HLCoreFragment;
import com.hl.hlcorelib.orm.HLObject;
import com.hl.hlcorelib.utils.HLFragmentUtils;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.R;
import com.homelane.phoenixapp.main.dashboard.DashboardPresenter;
import com.homelane.phoenixapp.main.project.history.ProjectHistoryPresenter;

import java.util.ArrayList;

/**
 * Created by hl0395 on 16/12/15.
 */
public class ProjectPresenter extends HLCoreFragment<ProjectView> implements HLEventListener {

    ProjectListAdapter mProjectListAdapter;

    @Override
    protected void onBindView() {
        super.onBindView();


        ArrayList<HLObject> projectList = getArguments().getParcelableArrayList("list");


        mProjectListAdapter = new ProjectListAdapter();
        mProjectListAdapter.setmDataSet(projectList);

        mView.mProjectList.setAdapter(mProjectListAdapter);

        if (projectList.size() > 0) {
            mView.mProjectList.setVisibility(View.VISIBLE);
            mView.mErrorText.setVisibility(View.GONE);
        } else {

            mView.mProjectList.setVisibility(View.GONE);
            mView.mErrorText.setVisibility(View.VISIBLE);
            mView.mErrorText.setText("No projects found.");
        }

        if(!hasEventListener(PhoenixConstants.NAVIGATE_TO_PROJECT_DETAILS_EVENT,this))
            addEventListener(PhoenixConstants.NAVIGATE_TO_PROJECT_DETAILS_EVENT,this);
    }

    @Override
    public void onEvent(HLEvent hlEvent) {
        HLCoreEvent e = (HLCoreEvent)hlEvent;
        Bundle bundle=e.getmExtra();

        if(e.getType().equals(PhoenixConstants.NAVIGATE_TO_PROJECT_DETAILS_EVENT)){
            HLFragmentUtils.HLFragmentTransaction transaction =
                    new HLFragmentUtils.HLFragmentTransaction();
            transaction.mFrameId = R.id.fragment_frame;
            transaction.mFragmentClass = ProjectHistoryPresenter.class;
            transaction.mParameters = bundle;
            push(transaction);
        }

    }


    @Override
    protected Class<ProjectView> getVuClass() {
        return ProjectView.class;
    }

    /**
     * Function to search from the search view
     * @param query is the searched string
     */

    public void searchList(String query){

        this.mProjectListAdapter.getFilter().filter(query);

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
