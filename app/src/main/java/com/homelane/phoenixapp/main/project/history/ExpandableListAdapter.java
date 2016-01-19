package com.homelane.phoenixapp.main.project.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hl.hlcorelib.orm.HLObject;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hl0395 on 18/1/16.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<HLObject> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Integer, List<HLObject>> _listDataChild;

    public ExpandableListAdapter(Context context, List<HLObject> listDataHeader,
                                 HashMap<Integer, List<HLObject>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(groupPosition)
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final HLObject task = (HLObject) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.project_states_item, null);
        }

        TextView mStartDate = (TextView) convertView.findViewById(R.id.from_date);
        TextView mCompleteDate = (TextView) convertView.findViewById(R.id.to_date);
        View mStatusView = (View) convertView.findViewById(R.id.status_view);
        TextView mStateName = (TextView) convertView.findViewById(R.id.name);

        mStartDate.setText(task.getString(PhoenixConstants.State.STATE_START_DATE));
        mCompleteDate.setText(task.getString(PhoenixConstants.State.STATE_COMPLETED_DATE));
        mStateName.setText(task.getString(PhoenixConstants.State.STATE_NAME));

        if (task.getString(PhoenixConstants.State.STATE_STATUS).equals("COMPLETED"))
            mStatusView.setBackgroundResource(R.drawable.green_circle);
        else if (task.getString(PhoenixConstants.State.STATE_STATUS).equals("IN_PROGRESS"))
            mStatusView.setBackgroundResource(R.drawable.yellow_circle);
        else
            mStatusView.setBackgroundResource(R.drawable.red_circle);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(groupPosition)
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        HLObject task = (HLObject) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.project_task_item, null);
        }

        TextView mTaskName = (TextView) convertView.findViewById(R.id.task_name);
        TextView mTaskDuration = (TextView) convertView.findViewById(R.id.task_detail);
        ImageView mCircleView = (ImageView) convertView.findViewById(R.id.circle_view);

        mTaskName.setText(task.getString(PhoenixConstants.Task.TASK_NAME));
        mTaskDuration.setText(task.getString(PhoenixConstants.Task.START_DATE));

        if (task.getString(PhoenixConstants.Task.TASK_STATUS).equals("COMPLETED")) {
            mCircleView.setBackgroundResource(R.drawable.green_circle);
            mCircleView.setImageResource(R.drawable.ic_done_white_18dp);
        } else if (task.getString(PhoenixConstants.Task.TASK_STATUS).equals("IN_PROGRESS")) {
            mCircleView.setBackgroundResource(R.drawable.yellow_circle);
            mCircleView.setImageResource(R.drawable.ic_refresh_white_18dp);
        } else
            mCircleView.setBackgroundResource(R.drawable.red_circle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}