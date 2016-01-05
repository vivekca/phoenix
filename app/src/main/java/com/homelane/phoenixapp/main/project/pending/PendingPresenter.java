package com.homelane.phoenixapp.main.project.pending;

import android.os.Bundle;
import android.view.View;

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
import com.homelane.phoenixapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by hl0395 on 23/12/15.
 */
public class PendingPresenter extends HLCoreFragment<PendingView> {


    PendingListAdapter mPendingListAdapter;
    /**
     * RequestQueue for volley
     */
    RequestQueue volleyReqQueue;

    @Override
    protected void onBindView() {
        super.onBindView();

        volleyReqQueue = Volley.newRequestQueue(getActivity());

        if(HLNetworkUtils.isNetworkAvailable(getActivity()))
            getPendingTasks();
        else{
            Bundle bundle = new Bundle();
            bundle.putString(PhoenixConstants.SNACKBAR_DISPLAY_MESSAGE, "Please check your internet connection");

            HLCoreEvent event = new HLCoreEvent(PhoenixConstants.SNACKBAR_DISPLAY_EVENT,
                    bundle);
            HLEventDispatcher.acquire().dispatchEvent(event);
        }

        mPendingListAdapter = new PendingListAdapter();
        mView.mPendingList.setAdapter(mPendingListAdapter);

    }

    /**
     * Function to request the server to fetch the pending tasks
     * and update the adapter
     */
     private void getPendingTasks() {
        ArrayList<String> pendingList = getArguments().getStringArrayList("list");
        String str="";

        for (String s: pendingList)
            str = str + s + ",";

        try {
            str = URLEncoder.encode(str, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        final String baseUrl = HLCoreLib.readProperty(PhoenixConstants.AppConfig.HL_AGGREGATE_TASK_URL) + "tasks="+
                str+"&corelationId=123&status=PENDING&owners=hltestblrdesigner6@homelane.com";

        StringRequest request = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray aggregates = jsonObject.getJSONArray("agrRes");
                    ArrayList<HLObject> taskList = new ArrayList();

                    for(int i=0; i<aggregates.length();i++){
                        JSONObject agg = aggregates.getJSONObject(i);

                        JSONArray tasks = agg.getJSONArray("tasks");

                        for(int j=0; j<tasks.length();j++){
                            JSONObject task = tasks.getJSONObject(j);

                            HLObject hlTask = new HLObject(PhoenixConstants.Task.TASK_NAME);
                            hlTask.put(PhoenixConstants.Task.TASK_NAME, task.getString("taskname"));
                            hlTask.put(PhoenixConstants.Task.TASK_STATUS, task.getInt("count"));
                            hlTask.put(PhoenixConstants.Task.START_DATE, jsonObject.getString("todate"));
                            taskList.add(hlTask);
                        }

                    }

                    mPendingListAdapter.setmDataSet(taskList);

                    if(taskList.size() > 0){
                        mView.mPendingList.setVisibility(View.VISIBLE);
                        mView.mErrorText.setVisibility(View.GONE);
                    }else {
                        mView.mPendingList.setVisibility(View.GONE);
                        mView.mErrorText.setVisibility(View.VISIBLE);
                        mView.mErrorText.setText("NO Tasks Found");
                    }

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

    /**
     * Function to filter the list wrt date and status from the
     * right navigation menu
     * @param filterObj is the details selected from the navigation menu
     */
    public void filterList(HLObject filterObj){
        if(filterObj != null) {
            if (!filterObj.getString(PhoenixConstants.Task.START_DATE).equals(getString(R.string.select_date)) &&
                    !filterObj.getString(PhoenixConstants.Task.TO_DATE).equals(getString(R.string.select_date))) {

                String status = filterObj.getString(PhoenixConstants.Task.TASK_STATUS) + "/" +
                        filterObj.getString(PhoenixConstants.Task.START_DATE) + "/" +
                        filterObj.getString(PhoenixConstants.Task.TO_DATE);
                this.mPendingListAdapter.getFilter().filter(status);
            } else {
                String status = filterObj.getString(PhoenixConstants.Task.TASK_STATUS) + "/";
                this.mPendingListAdapter.getFilter().filter(status);
            }
        }else{
            this.mPendingListAdapter.getFilter().filter("");
            this.mPendingListAdapter.notifyDataSetChanged();
        }

    }

    /**
     * Function to search from the search view
     * @param query is the searched string
     */
    public void searchList(String query){
        this.mPendingListAdapter.getFilter().filter(
                query);

    }

    @Override
    protected void onDestroyHLView() {
        super.onDestroyHLView();

    }

    @Override
    protected Class<PendingView> getVuClass() {
        return PendingView.class;
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
