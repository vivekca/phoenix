package com.homelane.phoenixapp.main.project.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hl.hlcorelib.orm.HLObject;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.R;
import com.homelane.phoenixapp.main.MainPresenter;

import java.util.ArrayList;

/**
 * Created by hl0395 on 21/12/15.
 */
public class HistroryListAdapter extends RecyclerView.Adapter<HistroryListAdapter.ViewHolder> {


    /**
     * Constains the raw data
     */
    private ArrayList<HLObject> mDataSet;


    /**
     * ViewHolder class loads the views for the Recyler view item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTaskName;
        public TextView mTaskDuration;
        public ImageView mCircleView;
        public RelativeLayout mTaskLayout;

        public TextView mStartDate;
        public TextView mCompleteDate;
        public View mStatusView;
        public TextView mStateName;
        public LinearLayout mStateLayout;

        /**
         * @param itemView
         */
        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if(viewType == 0) {
                mTaskName = (TextView) itemView.findViewById(R.id.task_name);
                mTaskDuration = (TextView) itemView.findViewById(R.id.task_detail);
                mCircleView = (ImageView) itemView.findViewById(R.id.circle_view);
                mTaskLayout = (RelativeLayout) itemView.findViewById(R.id.task_layout);
            }else {
                mStartDate = (TextView) itemView.findViewById(R.id.from_date);
                mCompleteDate = (TextView) itemView.findViewById(R.id.to_date);
                mStatusView = (View) itemView.findViewById(R.id.status_view);
                mStateName = (TextView) itemView.findViewById(R.id.name);
                mStateLayout = (LinearLayout) itemView.findViewById(R.id.state_layout);
            }
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
        View v;
        ViewHolder vh;

        switch (viewType) {
            case 0:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.project_task_item, parent, false);
                vh = new ViewHolder(v,viewType);
                return vh;
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.project_states_item, parent, false);
                vh = new ViewHolder(v,viewType);
                return vh;
        }
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

        final HLObject task = mDataSet.get(position);
        if(task.getInteger("Type") == 0) {
            holder.mTaskName.setText(task.getString(PhoenixConstants.Task.TASK_NAME));
            holder.mTaskDuration.setText(task.getString(PhoenixConstants.Task.START_DATE));

            if (task.getString(PhoenixConstants.Task.TASK_STATUS).equals("COMPLETED")) {
                holder.mCircleView.setBackgroundResource(R.drawable.green_circle);
                holder.mCircleView.setImageResource(R.drawable.ic_done_white_18dp);
            } else if (task.getString(PhoenixConstants.Task.TASK_STATUS).equals("IN_PROGRESS")) {
                holder.mCircleView.setBackgroundResource(R.drawable.yellow_circle);
                holder.mCircleView.setImageResource(R.drawable.ic_refresh_white_18dp);
            } else
                holder.mCircleView.setBackgroundResource(R.drawable.red_circle);

            holder.mTaskLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (holder.mStateLayout.getVisibility() == View.VISIBLE) {
//                        collapse(holder.mStateLayout);
//                    } else
//                        expand(holder.mStateLayout);

                }
            });
        }else {
//        ArrayList<HLObject> states = (ArrayList) task.getList(PhoenixConstants.Task.TASK_ID);
//
//        for (HLObject state : states) {

            holder.mStateLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collapse(holder.mStateLayout);
                }
            });

            holder.mStartDate.setText(task.getString(PhoenixConstants.State.STATE_START_DATE));
            holder.mCompleteDate.setText(task.getString(PhoenixConstants.State.STATE_COMPLETED_DATE));
            holder.mStateName.setText(task.getString(PhoenixConstants.State.STATE_NAME));

            if (task.getString(PhoenixConstants.State.STATE_STATUS).equals("COMPLETED"))
                holder.mStatusView.setBackgroundResource(R.drawable.green_circle);
            else if (task.getString(PhoenixConstants.State.STATE_STATUS).equals("IN_PROGRESS"))
                holder.mStatusView.setBackgroundResource(R.drawable.yellow_circle);
            else
                holder.mStatusView.setBackgroundResource(R.drawable.red_circle);

//        }
        }

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
        if(mDataSet.get(position).getInteger("Type") == 0)
            return 0;
        else
            return 1;
    }

    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(750);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(750);
        v.startAnimation(a);
    }

}
