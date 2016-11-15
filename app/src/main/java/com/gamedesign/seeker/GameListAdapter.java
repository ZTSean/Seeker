package com.gamedesign.seeker;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import org.w3c.dom.Text;

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

    public GameListAdapter (Context ctx, List<Game> glist, DataBaseHelper dbh, FragmentTransaction fragmentTransaction) {
        mContext = ctx;
        mGameList = glist;
        dbHelper = dbh;
        ft = fragmentTransaction;
        mInflater = LayoutInflater.from(ctx);
        viewBinderHelper = new ViewBinderHelper();
    }

    private class ViewHoler {
        TextView game_name;
        View deleteView;
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
            viewHoler.deleteView = convertView.findViewById(R.id.delete_layout);
            viewHoler.swipteLayout = (SwipeRevealLayout) convertView.findViewById(R.id.swipe_layout);

            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }

        final String gName = mGameList.get(position).getGame_name();


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

            viewHoler.game_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // open game fragment
                    String gName = mGameList.get(position).getGame_name();
                    long gId = mGameList.get(position).getId();
                    Log.d("adapter", "Called itemsdfasdfawef");

                    // load game fragment, although name is newgamefragment
                    NewGameFragment tmp = new NewGameFragment();
                    Bundle args = new Bundle();
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
