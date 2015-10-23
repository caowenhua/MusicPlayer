package org.yekeqi.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import org.yekeqi.R;
import org.yekeqi.adapter.SearchResultAdapter;
import org.yekeqi.api.Api;
import org.yekeqi.model.SongSearchResponse;
import org.yekeqi.model.SongSearchResponseData;
import org.yekeqi.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/29.
 */
public class SearchResultActivity extends BaseActivity implements OnClickListener{

    private ImageButton imgbtn_back;
    private ImageButton imgbtn_download;
    private ListView lv_list;

    private Api api;
    private String key;
    private int page;
    private SearchResultAdapter adapter;
    private List<SongSearchResponseData> songs;

    private ApiTask task;
    private LoadingDialog dialog;

    @Override
    protected String setTag() {
        return "SearchResultActivity";
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void findView() {
        imgbtn_back = findViewByID(R.id.imgbtn_back);
        imgbtn_download = findViewByID(R.id.imgbtn_download);
        lv_list = findViewByID(R.id.lv_list);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        imgbtn_back.setOnClickListener(this);
        imgbtn_download.setOnClickListener(this);

        page = 1;
        api = new Api(this);
        key = getIntent().getStringExtra("key");
        songs = new ArrayList<>();
        adapter = new SearchResultAdapter(this, songs);
        lv_list.setAdapter(adapter);

        task = new ApiTask();
        task.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.imgbtn_download:
                startActivity(getThis(), DownloadedActivity.class, null, 0);
                break;
        }
    }

    private SongSearchResponse response;
    private class ApiTask extends AsyncTask{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new LoadingDialog(getThis(), "正在载入..");
            dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            response = api.songSearch(1, key);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            dialog.dismiss();
            if(response.getData() != null && response.getData() != null){
                if(page == 1){
                    songs.clear();
                }
                for(int i=0 ; i<response.getData().size() ; i++){
                    if(response.getData().get(i).getAuditionList() == null ||
                            response.getData().get(i).getAuditionList().size() == 0){
                        response.getData().remove(i);
                    }
                }
                songs.addAll(response.getData());
                adapter.notifyDataSetChanged();
            }
        }
    }

//    private AsyncTask<Object, Object, Object> task = new AsyncTask<Object, Object, Object>() {
//
//        @Override
//        protected Object doInBackground(Object... params) {
//            response = api.songSearch(1, key);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
//            if(response.getData() != null && response.getData() != null){
//                if(page == 1){
//                    songs.clear();
//                }
//                for(int i=0 ; i<response.getData().size() ; i++){
//                    if(response.getData().get(i).getAuditionList() == null ||
//                            response.getData().get(i).getAuditionList().size() == 0){
//                        response.getData().remove(i);
//                    }
//                }
//                songs.addAll(response.getData());
//                adapter.notifyDataSetChanged();
//            }
//        }
//    };
}
