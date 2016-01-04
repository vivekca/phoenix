package com.homelane.phoenixapp.main.project;

import android.os.Bundle;
import android.view.View;

import com.hl.hlcorelib.mvp.events.HLCoreEvent;
import com.hl.hlcorelib.mvp.events.HLEvent;
import com.hl.hlcorelib.mvp.events.HLEventListener;
import com.hl.hlcorelib.mvp.presenters.HLCoreFragment;
import com.hl.hlcorelib.orm.HLObject;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.SearchEvent;

import java.util.ArrayList;

/**
 * Created by hl0395 on 16/12/15.
 */
public class ProjectPresenter extends HLCoreFragment<ProjectView> implements HLEventListener {

    ProjectListAdapter mProjectListAdapter;

    boolean mEmptyFlag=true;

    @Override
    protected void onBindView() {
        super.onBindView();

        //   ArrayList<HLObject> projectList = getArguments().getParcelableArrayList("list");

        ArrayList<HLObject> customerList = new ArrayList();


        HLObject customer = new HLObject(PhoenixConstants.Project.NAME);


        customer.put(PhoenixConstants.Project.PROJECT_NAME, "Vivek CA");
        customer.put(PhoenixConstants.Project.PROJECT_LOCATION, "Covai");
        customer.put(PhoenixConstants.Project.PROJECT_STATE, "Tamil Nadu");
        customer.put(PhoenixConstants.Project.PROJECT_STATUS, "To Send Initial Quote");
        customerList.add(customer);

        mProjectListAdapter = new ProjectListAdapter();
        mProjectListAdapter.setmDataSet(customerList);

        mView.mProjectList.setAdapter(mProjectListAdapter);

        if(customerList.size() > 0) {
            mView.mProjectList.setVisibility(View.VISIBLE);
            mView.mErrorText.setVisibility(View.GONE);
        }else{

            mView.mProjectList.setVisibility(View.GONE);
            mView.mErrorText.setVisibility(View.VISIBLE);
            mView.mErrorText.setText("No projects found.");

        }


        if(! hasEventListener(PhoenixConstants.NAVIGATE_TO_CUSTOMER_DASHBOARD_EVENT,this))
            addEventListener(PhoenixConstants.NAVIGATE_TO_CUSTOMER_DASHBOARD_EVENT,this);



        if (!hasEventListener(PhoenixConstants.SEARCH_EVENT, this)) {
            addEventListener(PhoenixConstants.SEARCH_EVENT, this);
        }


    }

    @Override
    protected void onDestroyHLView() {
        super.onDestroyHLView();
        removeEventListener(PhoenixConstants.NAVIGATE_TO_CUSTOMER_DASHBOARD_EVENT, this);

        removeEventListener(PhoenixConstants.SEARCH_EVENT,this);

    }

    @Override
    protected Class<ProjectView> getVuClass() {
        return ProjectView.class;
    }

    /**
     * Delegate method which will be called against respective events
     *
     * @param hlEvent the event which is dispatched by the {@link com.hl.hlcorelib.mvp.events.HLDispatcher}
     */

    @Override
    public void onEvent(HLEvent hlEvent) {
        HLCoreEvent e = (HLCoreEvent)hlEvent;
        Bundle bundle=e.getmExtra();

        if(e.getType().equals(PhoenixConstants.NAVIGATE_TO_CUSTOMER_DASHBOARD_EVENT)) {
//            HLFragmentUtils.HLFragmentTransaction transaction =
//                    new HLFragmentUtils.HLFragmentTransaction();
//            transaction.mFrameId = R.id.fragment_frame;
//            transaction.mFragmentClass = DesignerPresenter.class;
//            push(transaction);
        } else if (e.getType().equals(PhoenixConstants.SEARCH_EVENT)) {
            SearchEvent searchEvent = (SearchEvent) hlEvent;
            if ((searchEvent.getmCategory() == 2)) {
                this.mProjectListAdapter.getFilter().filter(searchEvent.getmExtra().getString("android.intent.action.SEARCH"));
            }
        }


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
