package com.homelane.phoenixapp.main.project.overdue;

import android.os.Bundle;
import android.view.View;

import com.hl.hlcorelib.mvp.events.HLCoreEvent;
import com.hl.hlcorelib.mvp.events.HLEvent;
import com.hl.hlcorelib.mvp.events.HLEventListener;
import com.hl.hlcorelib.mvp.presenters.HLCoreFragment;
import com.hl.hlcorelib.orm.HLObject;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.R;
import com.homelane.phoenixapp.SearchEvent;

import java.util.ArrayList;

/**
 * Created by hl0395 on 23/12/15.
 */
public class OverduePresenter extends HLCoreFragment<OverdueView> implements HLEventListener {


    OverdueListAdapter mOverDueListAdapter;
    @Override
    protected void onBindView() {
        super.onBindView();

//        ArrayList<HLObject> overdueList = getArguments().getParcelableArrayList("list");
        ArrayList<HLObject> arrayList = new ArrayList();

        HLObject task = new HLObject(PhoenixConstants.Task.TASK_NAME);
        task.put(PhoenixConstants.Task.TASK_NAME,"dsdss");
        task.put(PhoenixConstants.Task.START_DATE,"12-04-2015");
        task.put(PhoenixConstants.Task.TASK_STATUS, "To send Initial Quote");
        arrayList.add(task);

        HLObject task1 = new HLObject(PhoenixConstants.Task.TASK_NAME);

        task1.put(PhoenixConstants.Task.TASK_NAME,"dsdsszczc");
        task1.put(PhoenixConstants.Task.START_DATE,"12-05-2015");
        task1.put(PhoenixConstants.Task.TASK_STATUS, "Initial Quote Sent");
        arrayList.add(task1);

        HLObject task2 = new HLObject(PhoenixConstants.Task.TASK_NAME);

        task2.put(PhoenixConstants.Task.TASK_NAME,"cvvzv");
        task2.put(PhoenixConstants.Task.START_DATE,"12-08-2015");
        task2.put(PhoenixConstants.Task.TASK_STATUS, "Initial Quote Sent");


        arrayList.add(task2);

        HLObject task3 = new HLObject(PhoenixConstants.Task.TASK_NAME);

        task3.put(PhoenixConstants.Task.TASK_NAME,"dsdsszxcsc");
        task3.put(PhoenixConstants.Task.START_DATE,"12-10-2015");
        task3.put(PhoenixConstants.Task.TASK_STATUS, "Initial Quote Approved");

        arrayList.add(task3);

        HLObject task4 = new HLObject(PhoenixConstants.Task.TASK_NAME);

        task4.put(PhoenixConstants.Task.TASK_NAME,"33cxz");
        task4.put(PhoenixConstants.Task.START_DATE,"12-11-2015");
        task4.put(PhoenixConstants.Task.TASK_STATUS, "Initial Quote - Requested For Rev");

        arrayList.add(task4);

        HLObject task5 = new HLObject(PhoenixConstants.Task.TASK_NAME);

        task5.put(PhoenixConstants.Task.TASK_NAME,"cxzcz");
        task5.put(PhoenixConstants.Task.START_DATE,"12-12-2015");
        task5.put(PhoenixConstants.Task.TASK_STATUS, "Initial Quote - Requested For Rev");

        arrayList.add(task5);

        mOverDueListAdapter = new OverdueListAdapter();
        mOverDueListAdapter.setmDataSet(arrayList);
        mView.mOverdueList.setAdapter(mOverDueListAdapter);

        if(arrayList.size() > 0){
            mView.mOverdueList.setVisibility(View.VISIBLE);
            mView.mErrorText.setVisibility(View.GONE);
        }else {
            mView.mOverdueList.setVisibility(View.GONE);
            mView.mErrorText.setVisibility(View.VISIBLE);
            mView.mErrorText.setText("NO Tasks Found");
        }

        if (!hasEventListener(PhoenixConstants.SEARCH_EVENT, this)) {
            addEventListener(PhoenixConstants.SEARCH_EVENT, this);
        }
        if (!hasEventListener(PhoenixConstants.FILTER_EVENT, this)) {
            addEventListener(PhoenixConstants.FILTER_EVENT, this);
        }

    }

    @Override
    public void onEvent(HLEvent hlEvent) {
        HLCoreEvent e = (HLCoreEvent)hlEvent;
        Bundle bundle=e.getmExtra();

        if (e.getType().equals(PhoenixConstants.SEARCH_EVENT)) {
            SearchEvent searchEvent = (SearchEvent) hlEvent;
            if ((searchEvent.getmCategory() == 2)) {
                this.mOverDueListAdapter.getFilter().filter(
                        searchEvent.getmExtra().getString("android.intent.action.SEARCH"));
            }
        }
    }

    public void filterList(HLObject filterObj){
        if(filterObj != null) {
            if (!filterObj.getString(PhoenixConstants.Task.START_DATE).equals(getString(R.string.select_date)) &&
                    !filterObj.getString(PhoenixConstants.Task.TO_DATE).equals(getString(R.string.select_date))) {

                String status = filterObj.getString(PhoenixConstants.Task.TASK_STATUS) + "/" +
                        filterObj.getString(PhoenixConstants.Task.START_DATE) + "/" +
                        filterObj.getString(PhoenixConstants.Task.TO_DATE);
                this.mOverDueListAdapter.getFilter().filter(status);
            } else {
                String status = filterObj.getString(PhoenixConstants.Task.TASK_STATUS) + "/";
                this.mOverDueListAdapter.getFilter().filter(status);
            }
        }else{
            this.mOverDueListAdapter.getFilter().filter("");
            this.mOverDueListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onDestroyHLView() {
        super.onDestroyHLView();
        removeEventListener(PhoenixConstants.SEARCH_EVENT, this);

    }

    @Override
    protected Class<OverdueView> getVuClass() {
        return OverdueView.class;
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
