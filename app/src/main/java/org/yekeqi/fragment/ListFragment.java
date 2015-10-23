package org.yekeqi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.yekeqi.R;
import org.yekeqi.activity.MainActivity;
import org.yekeqi.adapter.MusicListAdapter;
import org.yekeqi.api.Params;
import org.yekeqi.application.PlayerApplication;
import org.yekeqi.service.MusicService;
import org.yekeqi.util.SpUtil;

/**
 * Created by yekeqi on 2015/9/23.
 */
public class ListFragment extends BaseFragment{
    private EditText edt_search;
    private ImageButton imgbtn_locate;
    private ListView lv_music;
    private MusicListAdapter adapter;
    private TextView tv_tip;

    @Override
    protected String setTag() {
        return "ListFragment";
    }

    @Override
    protected void findView() {
        edt_search = findViewByID(R.id.edt_search);
        imgbtn_locate = findViewByID(R.id.imgbtn_locate);
        lv_music = findViewByID(R.id.lv_music);
        tv_tip = findViewByID(R.id.tv_tip);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        adapter = new MusicListAdapter(getActivity());
        lv_music.setAdapter(adapter);
        lv_music.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MusicService.class);
                intent.putExtra(Params.OP, Params.OP_CHANGE);
                intent.putExtra(Params.POSITION, adapter.getId(position));
                getActivity().startService(intent);
                SpUtil.setLastPlatId(getActivity(), adapter.getItem(position).getId());
                adapter.notifyDataSetChanged();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) getActivity()).clickTab(0);
                    }
                });
            }
        });
        if(adapter.getCount() == 0){
            tv_tip.setVisibility(View.VISIBLE);
        }
        goToPlayingPosition();
        imgbtn_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPlayingPosition();
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.searchMusic(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void goToPlayingPosition() {
        int position = adapter.getPlayingPosition();
        if(position != -1)
            lv_music.smoothScrollToPosition(adapter.getPlayingPosition());
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            edt_search.setText("");
            if (((PlayerApplication)getActivity().getApplication()).getMusicList().size() == 0) {
                tv_tip.setVisibility(View.VISIBLE);
            } else {
                tv_tip.setVisibility(View.GONE);
            }
            adapter.resetList();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("musicListChanged");
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }
}
