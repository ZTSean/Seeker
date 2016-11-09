package com.gamedesign.seeker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Zixiao on 10/27/2016.
 */

public class CluesAdapter extends BaseAdapter{

    private Context mContext;
    private List<Clue> mClueList;

    private static LayoutInflater mInflater = null;
    public CluesAdapter(Context c, List<Clue> cluelist) {
        // TODO Auto-generated constructor stub
        mClueList = cluelist;
        mContext = c;
        mInflater = ( LayoutInflater )mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mClueList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mClueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder // Hole the view of items will be shown
    {
        TextView hint; // text hint of this clue
        TextView spot_name; // Name of place/spot
        //TextView number; // serial number of this chosen spots
        //TextView addr; // specific addr of this spot
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.clue_list, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.hint = (TextView) convertView.findViewById(R.id.hint);
            viewHolder.spot_name = (TextView) convertView.findViewById(R.id.spot_name);
            //viewHolder.number = (TextView) convertView.findViewById(R.id.number);
            //viewHolder.addr = (TextView) convertView.findViewById(R.id.address);
        } else {
            // Need to understand what does this line for
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.hint.setText(mClueList.get(position).getHint());
        viewHolder.spot_name.setText(mClueList.get(position).getSpot_name());
        //viewHolder.number.setText(mClueList.get(position).getOrder());
        //viewHolder.addr.setText(mClueList.get(position).getAddr());

        return convertView;
    }

}
