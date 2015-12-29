package com.homelane.phoenixapp.main.project.filter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.hl.hlcorelib.mvp.events.HLCoreEvent;
import com.hl.hlcorelib.mvp.events.HLEventDispatcher;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.R;

import java.util.Calendar;

/**
 * Created by hl0395 on 28/12/15.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current date as the default date in the date picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //Create a new DatePickerDialog instance and return it
        /*
            DatePickerDialog Public Constructors - Here we uses first one
            public DatePickerDialog (Context context, DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth)
            public DatePickerDialog (Context context, int theme, DatePickerDialog.OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth)
         */
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /**
     * @param view        The view associated with this listener.
     * @param year        The year that was set.
     * @param monthOfYear The month that was set (0-11) for compatibility
     *                    with {@link Calendar}.
     * @param dayOfMonth  The day of the month that was set.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Bundle bundle = new Bundle();
        bundle.putString(PhoenixConstants.DatePicker.SELECTED_DAY, dayOfMonth+"");
        bundle.putString(PhoenixConstants.DatePicker.SELECTED_MONTH, (monthOfYear+1)+"");
        bundle.putString(PhoenixConstants.DatePicker.SELECTED_YEAR, year+"");

        HLCoreEvent event = new HLCoreEvent(PhoenixConstants.SELECTED_DATE_EVENT,
                bundle);
        HLEventDispatcher.acquire().dispatchEvent(event);

    }


}
