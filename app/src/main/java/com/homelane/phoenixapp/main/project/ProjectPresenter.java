package com.homelane.phoenixapp.main.project;

import android.view.View;

import com.hl.hlcorelib.mvp.presenters.HLCoreFragment;
import com.hl.hlcorelib.orm.HLObject;

import java.util.ArrayList;

/**
 * Created by hl0395 on 16/12/15.
 */
public class ProjectPresenter extends HLCoreFragment<ProjectView> {

    ProjectListAdapter mProjectListAdapter;

    @Override
    protected void onBindView() {
        super.onBindView();

        ArrayList<HLObject> projectList = getArguments().getParcelableArrayList("list");

        mProjectListAdapter = new ProjectListAdapter();
        mProjectListAdapter.setmDataSet(projectList);

        mView.mProjectList.setAdapter(mProjectListAdapter);

        if(projectList.size() > 0) {
            mView.mProjectList.setVisibility(View.VISIBLE);
            mView.mErrorText.setVisibility(View.GONE);
        }else{

            mView.mProjectList.setVisibility(View.GONE);
            mView.mErrorText.setVisibility(View.VISIBLE);
            mView.mErrorText.setText("No projects found.");
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
        this.mProjectListAdapter.getFilter().filter(
                query);

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
