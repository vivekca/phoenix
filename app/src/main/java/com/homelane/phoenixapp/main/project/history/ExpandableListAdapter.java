package com.homelane.phoenixapp.main.project.history;

import android.content.Context;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hl.hlcorelib.orm.HLObject;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

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
        ImageView mStatusView = (ImageView) convertView.findViewById(R.id.status_view);
        TextView mStateName = (TextView) convertView.findViewById(R.id.name);

        Log.d("TAG", "start --- " + task.getString(PhoenixConstants.State.STATE_START_DATE));

        if (task.getString(PhoenixConstants.State.STATE_START_DATE) != null &&
                !task.getString(PhoenixConstants.State.STATE_START_DATE).equals("null"))
            mStartDate.setText(dateConversation1(task.getString(PhoenixConstants.State.STATE_START_DATE)));

        if (task.getString(PhoenixConstants.State.STATE_COMPLETED_DATE) != null &&
                !task.getString(PhoenixConstants.State.STATE_COMPLETED_DATE).equals("null"))
            mCompleteDate.setText(dateConversation1(task.getString(PhoenixConstants.State.STATE_COMPLETED_DATE)));

        mStateName.setText(task.getString(PhoenixConstants.State.STATE_NAME));

        if (task.getString(PhoenixConstants.State.STATE_STATUS).equals("COMPLETED")) {
            mStatusView.setBackgroundResource(R.drawable.green_circle);
            mStatusView.setImageResource(R.drawable.ic_done_white_18dp);
        } else if (task.getString(PhoenixConstants.State.STATE_STATUS).equals("IN_PROGRESS")) {
            mStatusView.setBackgroundResource(R.drawable.yellow_circle);
            mStatusView.setImageResource(R.drawable.ic_refresh_white_18dp);
        } else {
            mStatusView.setBackgroundResource(R.drawable.red_circle);


        }

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

    boolean mFlag = false;

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        HLObject task = (HLObject) getGroup(groupPosition);
        HLObject task1 = new HLObject(PhoenixConstants.Task.TASK_NAME);
        int index = 0;
        System.out.println("the postion  is" + groupPosition);

        System.out.println("the group count is" + getGroupCount());
        index = groupPosition + 1;

        if (index < getGroupCount()) {
            task1 = (HLObject) getGroup(index);
            System.out.println("the next status poisition is" + index + "---status is--- " + task1.getString(PhoenixConstants.Task.TASK_STATUS));
        }
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.project_task_item, null);
        }

        TextView mTaskName = (TextView) convertView.findViewById(R.id.task_name);
        TextView mTaskDuration = (TextView) convertView.findViewById(R.id.task_detail);
        View mLineView1 = (View) convertView.findViewById(R.id.line_view1);
        View mCircleView = (View) convertView.findViewById(R.id.circle_view);
        View mLineView2 = (View) convertView.findViewById(R.id.line_view2);
        mTaskName.setText(task.getString(PhoenixConstants.Task.TASK_NAME));
        String string = dateConversation(task.getString(PhoenixConstants.Task.START_DATE));
        String str[] = string.split(" ");
        string = str[0] + " " + str[1] + "<br>" + str[2] + "<br>" + str[3];
        System.out.println("the string ----" + string);
        mTaskDuration.setText(Html.fromHtml(string));

        if (task.getString(PhoenixConstants.Task.TASK_STATUS).equals("COMPLETED")) {
            mLineView1.setBackgroundColor(mLineView1.getContext().getResources().getColor(R.color.green));
            mCircleView.setBackgroundResource(R.drawable.circle_history_green);
            mLineView2.setBackgroundColor(mLineView2.getContext().getResources().getColor(R.color.green));
            if (groupPosition == 0)
                mLineView1.setBackgroundResource(R.drawable.fading_green_line_top);
            if (mFlag && index < getGroupCount()) {
                mLineView1.setBackgroundResource(R.drawable.fading_green_line_top);
                mFlag = false;
            }
            if (index < getGroupCount() && !(task1.getString(PhoenixConstants.Task.TASK_STATUS).equals("COMPLETED"))) {
                mLineView2.setBackgroundResource(R.drawable.fading_green_line_bottom);
                mFlag = true;
            }


        } else if (task.getString(PhoenixConstants.Task.TASK_STATUS).equals("IN_PROGRESS")) {
            mLineView1.setBackgroundColor(mLineView1.getContext().getResources().getColor(R.color.yellow));

            mCircleView.setBackgroundResource(R.drawable.circle_history_yellow);

            mLineView2.setBackgroundColor(mLineView2.getContext().getResources().getColor(R.color.yellow));
            if (groupPosition == 0)
                mLineView1.setBackgroundResource(R.drawable.fading_yellow_line_top);
            if (mFlag && index < getGroupCount()) {
                mLineView1.setBackgroundResource(R.drawable.fading_yellow_line_top);
                mFlag = false;
            }
            if (index < getGroupCount() && !(task1.getString(PhoenixConstants.Task.TASK_STATUS).equals("IN_PROGRESS"))) {
                mLineView2.setBackgroundResource(R.drawable.fading_yellow_line_bottom);
                mFlag = true;

            }


        } else {
            mLineView1.setBackgroundColor(mLineView1.getContext().getResources().getColor(R.color.red));

            mCircleView.setBackgroundResource(R.drawable.circle_history_red);


            mLineView2.setBackgroundColor(mLineView2.getContext().getResources().getColor(R.color.red));

            if (groupPosition == 0)
                mLineView1.setBackgroundResource(R.drawable.fading_red_line_top);
            if (mFlag && index < getGroupCount()) {
                mLineView1.setBackgroundResource(R.drawable.fading_red_line_top);
                mFlag = false;
            }
            if (index < getGroupCount() && (task1.getString(PhoenixConstants.Task.TASK_STATUS).equals("IN_PROGRESS")) || (task1.getString(PhoenixConstants.Task.TASK_STATUS).equals("COMPLETED"))) {
                mLineView2.setBackgroundResource(R.drawable.fading_yellow_line_bottom);
                mFlag = true;

            }

        }
        return convertView;
    }

    public String dateConversation(String string) {
        if (string != null) {
            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            java.util.Date date = null;
            try {
                //"2011-03-27T09:39:01.607"
                date = form.parse(string);
            } catch (ParseException e) {

                e.printStackTrace();
            }
            SimpleDateFormat postFormater = new SimpleDateFormat("dd MMM yyyy HH:mm");
            String newDateStr = postFormater.format(date);
            return newDateStr;
        } else
            return string;
    }


    public String dateConversation1(String string) {
        if (string != null) {

            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+0000");
            java.util.Date date = null;
            try {
                //"2011-03-27T09:39:01.607"
                date = form.parse(string);
            } catch (ParseException e) {

                e.printStackTrace();
            }
            SimpleDateFormat postFormater = new SimpleDateFormat("dd MMM");
            String newDateStr = postFormater.format(date);
            return newDateStr;
        } else
            return string;
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