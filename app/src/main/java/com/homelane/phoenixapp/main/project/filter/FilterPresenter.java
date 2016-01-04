package com.homelane.phoenixapp.main.project.filter;

import android.app.DialogFragment;
import android.view.View;

import com.hl.hlcorelib.mvp.presenters.HLCoreFragmentDialogPresenter;
import com.homelane.phoenixapp.main.MainPresenter;

import java.util.Calendar;

/**
 * Created by hl0395 on 28/12/15.
 */
public class FilterPresenter extends HLCoreFragmentDialogPresenter<FilterView> {

    @Override
    protected void onBindView() {
        super.onBindView();

        Calendar calendar = Calendar.getInstance();

        mView.mFromDate.setText(calendar.get(Calendar.DAY_OF_MONTH)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.YEAR));
        mView.mToDate.setText(calendar.get(Calendar.DAY_OF_MONTH)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.YEAR));

        mView.mFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(((MainPresenter)getActivity()).getFragmentManager(),"Date Picker");

            }
        });
    }

    @Override
    protected Class<FilterView> getVuClass() {
        return FilterView.class;
    }

}
