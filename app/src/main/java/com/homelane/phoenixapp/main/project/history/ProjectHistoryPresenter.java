package com.homelane.phoenixapp.main.project.history;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hl0395 on 16/12/15.
 */
public class ProjectHistoryPresenter extends HLCoreFragment<ProjectHistoryView> {

    /**
     * RequestQueue for volley
     */
    RequestQueue volleyReqQueue;
    HistroryListAdapter histroryListAdapter;
    ArrayList<HLObject> taskList = new ArrayList<HLObject>();

    @Override
    protected void onBindView() {
        super.onBindView();

        ((MainPresenter)getActivity()).setTabVisible(false);

        volleyReqQueue = Volley.newRequestQueue(getActivity());

        if(HLNetworkUtils.isNetworkAvailable(getActivity()))
            getProjectStates();
        else{
            Bundle bundle = new Bundle();
            bundle.putString(PhoenixConstants.SNACKBAR_DISPLAY_MESSAGE, "Please check your internet connection");

            HLCoreEvent event = new HLCoreEvent(PhoenixConstants.SNACKBAR_DISPLAY_EVENT,
                    bundle);
            HLEventDispatcher.acquire().dispatchEvent(event);
        }

        mView.mProjectName.setText(getArguments().getString("ProjectName"));

        histroryListAdapter = new HistroryListAdapter();
        mView.mTaskList.setAdapter(histroryListAdapter);

    }


    /**
     * Function to request the server to fetch the project list
     * and set the set the view pager
     */
    private void getProjectStates(){

        final String baseUrl = HLCoreLib.readProperty(PhoenixConstants.AppConfig.HL_PHOENIX_BASE_URL) +
                getArguments().getString("ID") + HLCoreLib.readProperty(PhoenixConstants.AppConfig.HL_HISTORY_TASK_URL);

        StringRequest request = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);


                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject task = jsonArray.getJSONObject(i);

                        HLObject taskObj = new HLObject(PhoenixConstants.Task.NAME);
                        taskObj.put(PhoenixConstants.Task.TASK_NAME, task.getString("taskName"));
                        taskObj.put(PhoenixConstants.Task.TASK_STATUS, task.getString("status"));
                        taskObj.put(PhoenixConstants.Task.START_DATE, task.getString("taskStartTime"));
                        taskObj.put(PhoenixConstants.Task.COMPLETED_DATE, task.getString("taskCompleteTime"));
                        taskObj.put("Type", 0);
                        if(taskObj.save())
                            taskList.add(taskObj);

                        JSONArray states = task.getJSONArray("states");
                        ArrayList<HLObject> stateList = new ArrayList<HLObject>();

                        for(int j=0; j<states.length(); j++){
                            JSONObject state = states.getJSONObject(j);

                            HLObject stateObj = new HLObject(PhoenixConstants.State.NAME);
                            stateObj.put(PhoenixConstants.State.STATE_NAME, state.getString("stateName"));
                            stateObj.put(PhoenixConstants.State.STATE_STATUS, state.getString("status"));
                            stateObj.put(PhoenixConstants.State.STATE_START_DATE, state.getString("stateStartTime"));
                            stateObj.put(PhoenixConstants.State.STATE_COMPLETED_DATE, state.getString("stateCompleteTime"));
                            stateObj.put("Type", 1);

                            if(stateObj.save())
                                taskList.add(stateObj);
                        }
//                        taskObj.put(PhoenixConstants.Task.TASK_ID, stateList);
                    }

                    histroryListAdapter.setmDataSet(taskList);
                    histroryListAdapter.notifyDataSetChanged();

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
    protected Class<ProjectHistoryView> getVuClass() {
        return ProjectHistoryView.class;
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
