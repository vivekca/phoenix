package com.homelane.phoenixapp.main.project.pending;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.hl.hlcorelib.orm.HLObject;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hl0395 on 21/12/15.
 */
public class PendingListAdapter extends RecyclerView.Adapter<PendingListAdapter.ViewHolder> implements Filterable{


    /**
     * Contains the raw data
     */
    private ArrayList<HLObject> mDataSet;

    public List<HLObject> mFilteredProjectList;


    @Override
    public android.widget.Filter getFilter() {
        return new ProjectFilter();
    }

    private class ProjectFilter extends android.widget.Filter {
        private ProjectFilter() {
        }

        protected FilterResults performFiltering(CharSequence query) {
            FilterResults results = new FilterResults();

                if (query.length() == 0) {
                    results.values = PendingListAdapter.this.mDataSet;
                    results.count = PendingListAdapter.this.mDataSet.size();
                }else {
                    List<HLObject> tempList = new ArrayList();
                    for (int i = 0; i < PendingListAdapter.this.mDataSet.size(); i++) {
                        HLObject project = (HLObject) PendingListAdapter.this.mDataSet.get(i);
                        if (listContains(project, query)) {
                            tempList.add(project);
                        }
                    }
                    results.values = tempList;
                    results.count = tempList.size();
                }

            return results;
        }



        private boolean listContains(HLObject project, CharSequence query) {
            query = query.toString().trim().toLowerCase();
            String string =(String)query;
             mFlag = string.contains("/");
            if(!mFlag) {
                if (project.getString(PhoenixConstants.Task.TASK_NAME).trim().toLowerCase().contains(query) ||
                        project.getString(PhoenixConstants.Task.START_DATE).toLowerCase().contains(query) ||
                        project.getString(PhoenixConstants.Task.TASK_STATUS).toLowerCase().contains(query)) {
                    return true;
                }
            }else {
                String k[]=string.split("/");
                mSearchFlag = true;
                if(k.length == 1) {
                    if (project.getString(PhoenixConstants.Task.TASK_STATUS).trim().toLowerCase().
                            equals(k[0].toLowerCase().trim()))
                        return true;
                }else if(k.length == 3){

                    Date start = stringToDate(k[1]);
                    Date end = stringToDate(k[2]);
                    Date taskDate = stringToDate(project.getString(PhoenixConstants.Task.START_DATE));


                    if (project.getString(PhoenixConstants.Task.TASK_STATUS).trim().toLowerCase().
                            equals(k[0].toLowerCase().trim()) && isWithinRange(taskDate,start,end) )
                        return true;
                }

            }
            return false;
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(mSearchFlag){
                mFilterList = (ArrayList) results.values;
            }

                PendingListAdapter.this.mFilteredProjectList.clear();
                PendingListAdapter.this.mFilteredProjectList.addAll((ArrayList) results.values);
                PendingListAdapter.this.notifyDataSetChanged();
        }
    }


    /**
     * clearing the SearchFilter Object;
     */
    public void clearFilterList(){
        mFilterList.clear();
    }

    List<HLObject> mFilterList = new ArrayList();
    boolean mFlag, mSearchFlag =false;


    /**
     * Function to convert the date from string to Date object
     * @param dateInString is the date in string format
     * @return the Date object
     */

    private Date stringToDate(String dateInString){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = formatter.parse(dateInString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Function to check whether the given date is within the certain range
     * @param testDate is the date to the checked
     * @param startDate is the initial starting date range
     * @param endDate is the final ending date range
     * @return
     */
    boolean isWithinRange(Date testDate, Date startDate, Date endDate) {
        return testDate.getTime() >= startDate.getTime() &&
                testDate.getTime() <= endDate.getTime();
    }

    /**
     * ViewHolder class loads the views for the Recyler view item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTaskName;
        public TextView mTaskDueDate;
        public TextView mTaskStatus;
        public TextView mCustomerStatus;

        /**
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            mTaskName = (TextView)itemView.findViewById(R.id.customer_name);
            mTaskDueDate = (TextView)itemView.findViewById(R.id.customer_email);
            mTaskStatus = (TextView)itemView.findViewById(R.id.customer_mobile);
            mCustomerStatus = (TextView)itemView.findViewById(R.id.customer_status);
            mCustomerStatus.setVisibility(View.GONE);

            mTaskName.setTypeface(null, Typeface.NORMAL);
            mTaskName.setPadding(10, 5, 5, 5);
            mTaskDueDate.setGravity(Gravity.RIGHT);
            mTaskDueDate.setPadding(10, 5, 5, 5);
            mTaskStatus.setTypeface(null, Typeface.BOLD);
            mTaskStatus.setPadding(10,0,0,0);

        }
    }

    /**
     * getter function for mDataSet
     * @return the ArrayList containing the values
     */
    public ArrayList<HLObject> getmDataSet() {
        return mDataSet;
    }

    /**
     * setter function for mDataSet
     */

    public void setmDataSet(ArrayList<HLObject> mDataSet) {
        this.mDataSet = mDataSet;
        this.mFilteredProjectList = new ArrayList();
        this.mFilteredProjectList.addAll(mDataSet);
    }


    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return this.mFilteredProjectList != null ? this.mFilteredProjectList.size() : 0;

    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int)} (ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p/>
     * Override {@link #onBindViewHolder(ViewHolder, int)} (ViewHolder, int, List)} instead if Adapter can
     * handle effcient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HLObject customer = (HLObject) this.mFilteredProjectList.get(position);

        holder.mTaskName.setText(customer.getString(PhoenixConstants.Task.TASK_NAME));
        holder.mTaskStatus.setText("Count: "+customer.getInteger(PhoenixConstants.Task.TASK_STATUS));

        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = format.parse(customer.getString(PhoenixConstants.Task.START_DATE));

            holder.mTaskDueDate.setText(new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(date));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
