package com.homelane.phoenixapp.main.project.customer;

import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hl.hlcorelib.orm.HLObject;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.R;

import java.util.ArrayList;

/**
 * Created by hl0395 on 21/12/15.
 */
public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {


    /**
     * Constains the raw data
     */
    private ArrayList<HLObject> mDataSet;


    /**
     * ViewHolder class loads the views for the Recyler view item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mCustomerName;
        public TextView mCustomerEmail;
        public TextView mCustomerMobile;
        public TextView mCustomerProjectStatus;

        /**
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            mCustomerName = (TextView)itemView.findViewById(R.id.customer_name);
            mCustomerEmail = (TextView)itemView.findViewById(R.id.customer_email);
            mCustomerMobile = (TextView)itemView.findViewById(R.id.customer_mobile);
            mCustomerProjectStatus = (TextView)itemView.findViewById(R.id.customer_status);
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
    }


    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return ((mDataSet != null) ? mDataSet.size() : 0);
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

        final HLObject customer = mDataSet.get(position);
            holder.mCustomerName.setText(customer.getString(PhoenixConstants.Customer.CUSTOMER_NAME));
            holder.mCustomerEmail.setText(customer.getString(PhoenixConstants.Customer.CUSTOMER_EMAIL));
            holder.mCustomerMobile.setText(customer.getString(PhoenixConstants.Customer.CUSTOMER_MOBILE));
            holder.mCustomerProjectStatus.setText(customer.getString(PhoenixConstants.Customer.CUSTOMER_STATUS));
    }

    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     * <p/>
     * <p>The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * <code>position</code>. Type codes need not be contiguous.
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
