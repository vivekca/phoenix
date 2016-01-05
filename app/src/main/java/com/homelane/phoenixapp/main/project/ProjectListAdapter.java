package com.homelane.phoenixapp.main.project;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.hl.hlcorelib.orm.HLObject;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hl0395 on 21/12/15.
 */
public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder>  implements Filterable {


    /**
     * Constains the raw data
     */
    private ArrayList<HLObject> mDataSet;

    public List<HLObject> mFilteredProjectList;


    private class ProjectFilter extends Filter {
        private ProjectFilter() {
        }

        protected FilterResults performFiltering(CharSequence query) {
            FilterResults results = new FilterResults();
            if (query.length() == 0) {
                results.values = ProjectListAdapter.this.mDataSet;
                results.count = ProjectListAdapter.this.mDataSet.size();
            } else {
                List<HLObject> tempList = new ArrayList();
                for (int i = 0; i < ProjectListAdapter.this.mDataSet.size(); i++) {
                    HLObject project = (HLObject) ProjectListAdapter.this.mDataSet.get(i);
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
            if (project.getString(PhoenixConstants.Project.PROJECT_NAME).trim().toLowerCase().contains(query) ||
                    project.getString(PhoenixConstants.Project.PROJECT_LOCATION).toLowerCase().contains(query)) {
                return true;
            }
            String name = project.getString(PhoenixConstants.Project.PROJECT_STATUS);
            if (name.toLowerCase().contains(query) || name.toLowerCase().contains(query)) {
                return true;
            }
            return false;
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {
            ProjectListAdapter.this.mFilteredProjectList.clear();
            ProjectListAdapter.this.mFilteredProjectList.addAll((ArrayList) results.values);
            ProjectListAdapter.this.notifyDataSetChanged();
        }
    }


    @Override
    public Filter getFilter() {
        return new ProjectFilter();
    }


    /**
     * ViewHolder class loads the views for the Recyler view item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mCustomerName;
        public TextView mCustomerEmail;
        public TextView mCustomerMobile;
        public TextView mCustomerStatus;

        /**
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            mCustomerName = (TextView)itemView.findViewById(R.id.customer_name);
            mCustomerEmail = (TextView)itemView.findViewById(R.id.customer_email);
            mCustomerMobile = (TextView)itemView.findViewById(R.id.customer_mobile);
            mCustomerStatus = (TextView)itemView.findViewById(R.id.customer_status);
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

        holder.mCustomerName.setText(customer.getString(PhoenixConstants.Project.PROJECT_NAME));
        holder.mCustomerEmail.setText(customer.getString(PhoenixConstants.Project.PROJECT_LOCATION));
        holder.mCustomerMobile.setText(customer.getString(PhoenixConstants.Project.PROJECT_STATE));
        holder.mCustomerStatus.setText(customer.getString(PhoenixConstants.Project.PROJECT_STATUS));

//        holder.mDesignerName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HLCoreEvent event = new HLCoreEvent(PhoenixConstants.NAVIGATE_TO_CUSTOMER_DASHBOARD_EVENT,
//                        null);
//                HLEventDispatcher.acquire().dispatchEvent(event);
//
//            }
//        });
    }
}
