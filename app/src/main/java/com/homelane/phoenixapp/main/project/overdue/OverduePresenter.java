package com.homelane.phoenixapp.main.project.overdue;

import com.hl.hlcorelib.mvp.presenters.HLCoreFragment;
import com.hl.hlcorelib.orm.HLObject;
import com.homelane.phoenixapp.main.project.ProjectView;

import java.util.ArrayList;

/**
 * Created by hl0395 on 23/12/15.
 */
public class OverduePresenter extends HLCoreFragment<OverdueView> {


    @Override
    protected void onBindView() {
        super.onBindView();

//        ArrayList<HLObject> overdueList = getArguments().getParcelableArrayList("list");


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
