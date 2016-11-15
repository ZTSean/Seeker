package com.gamedesign.seeker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Zixiao on 11/8/2016.
 */

public class GamesAdapter extends BaseAdapter {
    private Context mContext;
    private List<Game> mGameList;
    private static LayoutInflater mInflater = null;

    public GamesAdapter(Context c, List<Game> glist) {
        // TODO Auto-generated constructor stub
        mGameList = glist;
        mContext = c;
        mInflater = ( LayoutInflater )mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mGameList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mGameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder // Hole the view of items will be shown
    {
        TextView game_name; // text hint of this clue
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GamesAdapter.ViewHolder viewHolder;

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.game_list, parent, false);

            viewHolder = new GamesAdapter.ViewHolder();
            viewHolder.game_name = (TextView) convertView.findViewById(R.id.game_name);

            convertView.setTag(viewHolder);
        } else {
            // Need to understand what does this line for
            viewHolder = (GamesAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.game_name.setText(mGameList.get(position).getGame_name());

        return convertView;
    }
}
