package com.gamedesign.seeker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.List;

import static com.gamedesign.seeker.GamesFragment.GAME_ID;
import static com.gamedesign.seeker.GamesFragment.GAME_NAME;

/**
 * Created by Zixiao on 11/15/2016.
 */

public class GameListAdapter extends BaseAdapter {
    private final ViewBinderHelper viewBinderHelper;
    private final LayoutInflater mInflater;
    private List<Game> mGameList;
    private Context mContext;
    private DataBaseHelper dbHelper;
    private AdapterView.OnItemClickListener onItemClickListener;
    private FragmentTransaction ft;
    private Bundle args;

    public GameListAdapter (Context ctx, List<Game> glist, DataBaseHelper dbh, FragmentTransaction fragmentTransaction, Bundle as) {
        mContext = ctx;
        mGameList = glist;
        dbHelper = dbh;
        ft = fragmentTransaction;
        args = as;
        mInflater = LayoutInflater.from(ctx);
        viewBinderHelper = new ViewBinderHelper();
    }

    private class ViewHoler {
        TextView game_name;
        TextView deleteView;
        TextView detailView;
        SwipeRevealLayout swipteLayout;
    }

    @Override
    public int getCount() {
        return mGameList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHoler viewHoler;
        final View view = convertView;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.game_list_item, parent, false);

            viewHoler = new ViewHoler();
            viewHoler.game_name = (TextView) convertView.findViewById(R.id.game_name_swipe);
            viewHoler.deleteView = (TextView) convertView.findViewById(R.id.delete_button);
            viewHoler.detailView = (TextView) convertView.findViewById(R.id.detail_button);
            viewHoler.swipteLayout = (SwipeRevealLayout) convertView.findViewById(R.id.swipe_layout);

            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }

        final String gName = mGameList.get(position).getGame_name();
        final int gId = mGameList.get(position).getId();


        if (gName != null) {
            viewBinderHelper.bind(viewHoler.swipteLayout, gName);

            viewHoler.game_name.setText(gName);


            // delete game
            viewHoler.deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHelper.deleteGame(mGameList.get(position), true);
                    mGameList.remove(position);
                    notifyDataSetChanged();
                }

            });

            // detail of game
            viewHoler.detailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // open game fragment
                    Log.d("adapter", "#### gId passed into : " + Long.toString(gId));
                    // load existed game fragment, although name is newgamefragment
                    NewGameFragment tmp = new NewGameFragment();
                    Log.d("#####", Boolean.toString(args.getBoolean(com.gamedesign.seeker.ChooseRoleFragment.IsPlayer)));
                    args.putString(GAME_NAME, gName);
                    args.putLong(GAME_ID, gId);
                    tmp.setArguments(args);

                    ft.replace(R.id.fragment, tmp).addToBackStack(null).commit();
                }

            });


            viewHoler.game_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // load game fragment, although name is newgamefragment
                    NewGameFragment tmp = new NewGameFragment();
                    args.putString(GAME_NAME, gName);
                    args.putLong(GAME_ID, gId);
                    tmp.setArguments(args);

                    ft.replace(R.id.fragment, tmp).addToBackStack(null).commit();
                }
            });
        }

        return convertView;
    }
}
