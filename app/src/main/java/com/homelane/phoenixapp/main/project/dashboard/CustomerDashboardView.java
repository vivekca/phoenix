package com.homelane.phoenixapp.main.project.dashboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hl.hlcorelib.mvp.HLView;
import com.homelane.phoenixapp.R;
import com.homelane.phoenixapp.views.HLProgressView;

/**
 * Created by hl0395 on 21/12/15.
 */
public class CustomerDashboardView implements HLView {

    private View mView;
    RecyclerView mCustomerList;
    HLProgressView mProgressView;
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
        mView = inflater.inflate(R.layout.customer_layout, parent, false);
        mCustomerList = (RecyclerView)mView.findViewById(R.id.customer_list);
        mCustomerList.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(mCustomerList.getContext(),2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position == 0)
                    return 2;
                else
                    return 1;
            }
        });
        mCustomerList.setLayoutManager(mLayoutManager);
        mProgressView = (HLProgressView)mView.findViewById(R.id.progress_view);
    }

}
