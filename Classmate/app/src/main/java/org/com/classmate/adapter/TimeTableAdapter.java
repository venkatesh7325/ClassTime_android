package org.com.classmate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.com.classmate.R;
import org.com.classmate.interfaces.TimeSelected;
import org.com.classmate.model.teacher.TimeTable;

import java.util.ArrayList;

/**
 * Created by Parnika on 16-07-2017.
 */

public class TimeTableAdapter extends BaseAdapter{
    private Context mContext;

    // Keep all Images in array
    public ArrayList<TimeTable> list;
    TimeSelected selectedPosition;

    // Constructor
    public TimeTableAdapter(Context c, ArrayList<TimeTable> listtable, TimeSelected selected) {
        mContext = c;
        list = listtable;
        selectedPosition=selected;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        grid = new View(mContext);
        grid = inflater.inflate(R.layout.snippet_time_table, null);
        TextView textView = (TextView) grid.findViewById(R.id.txtTimeValue);
        final View viewBg = (View) grid.findViewById(R.id.viewBackground);
        View viewLine = (View) grid.findViewById(R.id.hriLine);

        if (list.get(position).getEnable().equals("text")) {
            viewBg.setVisibility(View.VISIBLE);
            viewLine.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
            viewBg.setBackground(mContext.getResources().getDrawable(R.drawable.view_round_cyan));
            textView.setTextColor(Color.WHITE);
        } else {
            viewBg.setVisibility(View.INVISIBLE);
            viewLine.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }
        textView.setText(list.get(position).getValue());
        if (position > 0 && position < 7) {
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextColor(Color.BLACK);
            viewBg.setVisibility(View.INVISIBLE);
            //textView.setTextColor(Color.parseColor("#646464"));
        } else {
            textView.setTextColor(Color.WHITE);
            viewBg.setVisibility(View.VISIBLE);
            //textView.setTextColor(Color.parseColor("#C4C5C8"));
        }
       /* viewBg.setBackground(mContext.getResources().getDrawable(R.drawable.view_round_gray));
        viewBg.setBackground(list.get(position).getSlected().equals("true") ?
                mContext.getResources().getDrawable(R.drawable.view_round_cyan) :
                mContext.getResources().getDrawable(R.drawable.view_round_gray));*/
        viewBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  ToastUtils.displayToast("message",mContext);
                String value = list.get(position).getSlected();
                list.get(position).setSlected(value.equals("true") ? "false" : "true");
                notifyDataSetChanged();
                viewBg.setBackground(mContext.getResources().getDrawable(R.drawable.view_round_gray));
                selectedPosition.getTheSelectedPosition(position);
            }
        });
        return grid;
    }

}