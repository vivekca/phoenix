package com.homelane.phoenixapp.main.project.overdue;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hl.hlcorelib.mvp.HLView;
import com.homelane.phoenixapp.R;
import com.homelane.phoenixapp.views.HLProgressView;

/**
 * Created by hl0395 on 23/12/15.
 */
public class OverdueView implements HLView {

    private View mView;
    RecyclerView mOverdueList;
    HLProgressView mProgressView;
    public TextView mErrorText;

    /**
     * Return the enclosing view
     *
     * @return return the enclosing view
     */
    @Override
    public View getView() {
        return mView;
    }

    /**
     * To handle the back press
     *
     * @return false if not handled true if handled
     */
    @Override
    public boolean onBackPreseed() {
        return false;
    }

    /**
     * Function which will be triggered when {@link Activity#onRestoreInstanceState(Bundle)}
     * or {@link Fragment#onViewStateRestored(Bundle)}
     *
     * @param savedInstanceState the state which saved on {HLView#onSavedInstanceState}
     */
    @Override
    public void onRecreateInstanceState(Bundle savedInstanceState) {

    }

    /**
     * Function which will be called {@link Activity#onSaveInstanceState(Bundle)}
     * or {@link Fragment#onSaveInstanceState(Bundle)}
     *
     * @param savedInstanceState the state to save the contents
     */
    @Override
    public void onSavedInstanceState(Bundle savedInstanceState) {

    }

    /**
     * Create the view from the id provided
     *
     * @param inflater inflater using which the view shold be inflated
     * @param parent   to which the view to be attached
     */
    @Override
    public void init(LayoutInflater inflater, ViewGroup parent) {
        mView = inflater.inflate(R.layout.project_layout, parent, false);
        mOverdueList = (RecyclerView)mView.findViewById(R.id.project_list);
        mOverdueList.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(inflater.getContext());
        mOverdueList.setLayoutManager(mLayoutManager);
        mProgressView = (HLProgressView)mView.findViewById(R.id.progress_view);
        mErrorText = (TextView) mView.findViewById(R.id.error_display);
        mErrorText.setText("No tasks at the moment");
        mErrorText.setVisibility(View.VISIBLE);
    }



}
