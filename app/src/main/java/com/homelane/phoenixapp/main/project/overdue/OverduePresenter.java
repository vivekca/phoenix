package com.homelane.phoenixapp.main.project.overdue;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hl0395 on 23/12/15.
 */
public class OverduePresenter extends HLCoreFragment<OverdueView> {


    OverdueListAdapter mOverDueListAdapter;
    /**
     * RequestQueue for volley
     */
    RequestQueue volleyReqQueue;

    @Override
    protected void onBindView() {
        super.onBindView();

        volleyReqQueue = Volley.newRequestQueue(getActivity());

        if (HLNetworkUtils.isNetworkAvailable(getActivity()))
            getOverDueTasks();
        else {
            Bundle bundle = new Bundle();
            bundle.putString(PhoenixConstants.SNACKBAR_DISPLAY_MESSAGE, "Please check your internet connection");

            HLCoreEvent event = new HLCoreEvent(PhoenixConstants.SNACKBAR_DISPLAY_EVENT,
                    bundle);
            HLEventDispatcher.acquire().dispatchEvent(event);
        }
        mOverDueListAdapter = new OverdueListAdapter();
        mView.mOverdueList.setAdapter(mOverDueListAdapter);
    }

    /**
     * Function to request the server to fetch the overdue tasks
     * and update the adapter
     */
    private void getOverDueTasks() {
        ArrayList<String> overdueList = getArguments().getStringArrayList("list");
        String str = "";

        for (String s : overdueList)
            str = str + s + ",";

        try {
            str = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //   mView.mProgressView.showProgress();
        final String baseUrl = HLCoreLib.readProperty(PhoenixConstants.AppConfig.HL_AGGREGATE_TASK_URL) + "tasks=" +
                str + "&corelationId=123&status=COMPLETED&owners=hltestblrdesigner6@homelane.com";

        StringRequest request = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray aggregates = jsonObject.getJSONArray("agrRes");
                    ArrayList<HLObject> taskList = new ArrayList();
                    //     mView.mProgressView.hideProgress();

                    for (int i = 0; i < aggregates.length(); i++) {
                        JSONObject agg = aggregates.getJSONObject(i);

                        JSONArray tasks = agg.getJSONArray("tasks");

                        for (int j = 0; j < tasks.length(); j++) {
                            JSONObject task = tasks.getJSONObject(j);

                            HLObject hlTask = new HLObject(PhoenixConstants.Task.TASK_NAME);
                            hlTask.put(PhoenixConstants.Task.TASK_NAME, task.getString("taskname"));
                            hlTask.put(PhoenixConstants.Task.TASK_STATUS, "Count: "+task.getInt("count"));

                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            Date date = format.parse(jsonObject.getString("todate"));

                            hlTask.put(PhoenixConstants.Task.START_DATE,
                                    new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(date));
                            taskList.add(hlTask);
                        }

                    }

                    mOverDueListAdapter.setmDataSet(taskList);
                    mOverDueListAdapter.notifyDataSetChanged();

                    if (taskList.size() > 0) {
                        mView.mOverdueList.setVisibility(View.VISIBLE);
                        mView.mErrorText.setVisibility(View.GONE);
                    } else {
                        mView.mOverdueList.setVisibility(View.GONE);
                        mView.mErrorText.setVisibility(View.VISIBLE);
                        mView.mErrorText.setText("NO Tasks Found");
                    }

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

    /**
     * Function to filter the list wrt date and status from the
     * right navigation menu
     *
     * @param filterObj is the details selected from the navigation menu
     */
    public void filterList(HLObject filterObj) {

        if (filterObj != null) {
          /*  if(!(this.mOverDueListAdapter.getSearchFlag())){
                this.mOverDueListAdapter.setSearchFlag(true);
            }*/
            if (!filterObj.getString(PhoenixConstants.Task.START_DATE).equals(getString(R.string.select_date)) &&
                    !filterObj.getString(PhoenixConstants.Task.COMPLETED_DATE).equals(getString(R.string.select_date))) {

                String status = filterObj.getString(PhoenixConstants.Task.TASK_STATUS) + "/" +
                        filterObj.getString(PhoenixConstants.Task.START_DATE) + "/" +
                        filterObj.getString(PhoenixConstants.Task.COMPLETED_DATE);
                this.mOverDueListAdapter.getFilter().filter(status);
            } else {
                String status = filterObj.getString(PhoenixConstants.Task.TASK_STATUS) + "/";
                this.mOverDueListAdapter.getFilter().filter(status);
            }
        } else {

            this.mOverDueListAdapter.clearFilterList();
            this.mOverDueListAdapter.getFilter().filter("");
            this.mOverDueListAdapter.notifyDataSetChanged();
        }

    }


    /**
     * Function to search from the search view
     *
     * @param query is the searched string
     */
    public void searchList(String query) {
        this.mOverDueListAdapter.getFilter().filter(
                query);
    }

    @Override
    protected void onDestroyHLView() {
        super.onDestroyHLView();

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
