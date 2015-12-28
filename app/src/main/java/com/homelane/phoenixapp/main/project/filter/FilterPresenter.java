package com.homelane.phoenixapp.main.project.filter;

import com.hl.hlcorelib.mvp.presenters.HLCoreFragment;
import com.homelane.phoenixapp.main.project.overdue.OverdueView;

/**
 * Created by hl0395 on 28/12/15.
 */
public class FilterPresenter extends HLCoreFragment<FilterView> {

    @Override
    protected void onBindView() {
        super.onBindView();
    }

    @Override
    protected Class<FilterView> getVuClass() {
        return FilterView.class;
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
