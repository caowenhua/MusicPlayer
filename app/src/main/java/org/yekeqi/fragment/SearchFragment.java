package org.yekeqi.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.yekeqi.R;
import org.yekeqi.activity.SearchResultActivity;
import org.yekeqi.adapter.SearchListAdapter;
import org.yekeqi.api.Api;
import org.yekeqi.model.KeySearchResponse;
import org.yekeqi.model.KeySearchResponseSong;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yekeqi on 2015/9/23.
 */
public class SearchFragment extends BaseFragment{

    private EditText edt_search;
    private ListView lv_list;

    private Api api;

    private int page;
    private String key;
    private List<KeySearchResponseSong> songs;
    private SearchListAdapter adapter;

    private ApiTask task;

    @Override
    protected String setTag() {
        return "SearchFragment";
    }

    @Override
    protected void findView() {
        edt_search = findViewByID(R.id.edt_search);
        lv_list = findViewByID(R.id.lv_list);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    key = s.toString();
                    task = new ApiTask();
                    task.execute();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        page = 1;
        api = new Api(getActivity());
        songs = new ArrayList<>();
        adapter = new SearchListAdapter(getActivity(), songs);
        lv_list.setAdapter(adapter);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("key", songs.get(position).getName());
                startActivity(SearchResultActivity.class, bundle, 0);
            }
        });
    }

    private KeySearchResponse response;
    private class ApiTask extends AsyncTask{
        @Override
        protected Object doInBackground(Object... params) {
            response = api.keySearch(key);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(response.getData() != null && response.getData().getSong() != null){
                if(page == 1){
                    songs.clear();
                }
                songs.addAll(response.getData().getSong());
                adapter.notifyDataSetChanged();
            }
        }
    }
//    private AsyncTask<Object, Object, Object> task = new AsyncTask<Object, Object, Object>() {
//
//        @Override
//        protected Object doInBackground(Object... params) {
//            response = api.keySearch(key);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
//            if(response.getData() != null && response.getData().getSong() != null){
//                if(page == 1){
//                    songs.clear();
//                }
//                songs.addAll(response.getData().getSong());
//                adapter.notifyDataSetChanged();
//            }
//        }
//    };
}
