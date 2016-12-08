package com.gamedesign.seeker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Zixiao on 10/27/2016.
 */

public class CluesAdapter extends BaseAdapter{

    private Context mContext;
    private List<Clue> mClueList;
    private Boolean isPlayer;
    private HashMap<Integer, Boolean> isEnabled;

    private static LayoutInflater mInflater = null;
    public CluesAdapter(Context c, List<Clue> cluelist, Boolean is_Player, HashMap<Integer, Boolean> ie) {
        // TODO Auto-generated constructor stub
        mClueList = cluelist;
        mContext = c;
        isPlayer = is_Player;
        isEnabled = ie;
        mInflater = ( LayoutInflater )mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateMap (HashMap<Integer, Boolean> ie) {
        isEnabled = ie;
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

    public static class ViewHolder2
    {
        TextView hint;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder_creator = new ViewHolder();
        ViewHolder2 viewHolder_player = new ViewHolder2();

        if (convertView == null){


            if (isPlayer) {
                convertView = mInflater.inflate(R.layout.clue_list_player, parent, false);
                viewHolder_player.hint = (TextView) convertView.findViewById(R.id.hint);

                convertView.setTag(viewHolder_player);
            } else {
                convertView = mInflater.inflate(R.layout.clue_list, parent, false);
                viewHolder_creator.hint = (TextView) convertView.findViewById(R.id.hint);
                viewHolder_creator.spot_name = (TextView) convertView.findViewById(R.id.spot_name);
                //viewHolder.number = (TextView) convertView.findViewById(R.id.number);
                //viewHolder.addr = (TextView) convertView.findViewById(R.id.address);

                convertView.setTag(viewHolder_creator);
            }


        } else {
            // Need to understand what does this line for
            if (isPlayer) {
                viewHolder_player = (ViewHolder2) convertView.getTag();
            } else {
                viewHolder_creator  = (ViewHolder) convertView.getTag();
            }
        }

        String name = "Clue " + Integer.toString(mClueList.get(position).getId());
        if (isPlayer) {
            convertView.setEnabled(isEnabled.get(mClueList.get(position).getId()));
            viewHolder_player.hint.setText(name);

            /*
            final int pos = position;
            viewHolder_player.hint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("123456","gesgeadfawefawfs");
                    Intent intent = new Intent(mContext, PlayerClueActivity.class);
                    Clue c = mClueList.get(pos);
                    intent.putExtra(NewGameFragment.CLUE_ID, c.getId());
                    ((Activity)mContext).startActivityForResult(intent, NewGameFragment.PLAYER_CLUE_REQUEST);
                }

            });
            */
        } else {
            viewHolder_creator.hint.setText(name);
            viewHolder_creator.spot_name.setText(mClueList.get(position).getSpot_name());
        }
        //viewHolder.number.setText(mClueList.get(position).getOrder());
        //viewHolder.addr.setText(mClueList.get(position).getAddr());

        return convertView;
    }

}
