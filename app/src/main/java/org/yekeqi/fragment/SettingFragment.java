package org.yekeqi.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.yekeqi.R;
import org.yekeqi.activity.DownloadedActivity;
import org.yekeqi.activity.MainActivity;
import org.yekeqi.api.Params;
import org.yekeqi.application.PlayerApplication;
import org.yekeqi.bean.MusicListItemBean;
import org.yekeqi.dao.MusicListItemDAO;
import org.yekeqi.service.MusicService;
import org.yekeqi.util.SpUtil;
import org.yekeqi.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yekeqi on 2015/9/23.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener{

    private Button btn_exit;
    private Button btn_auto;
    private Button btn_mex;
    private Button btn_clean;
    private Button btn_download;
    private ImageView img_switch;
    private LoadingDialog dialog;

    private PlayerApplication application;

    @Override
    protected String setTag() {
        return "SettingFragment";
    }

    @Override
    protected void findView() {
        img_switch = findViewByID(R.id.img_switch);
        btn_auto = findViewByID(R.id.btn_auto);
        btn_exit = findViewByID(R.id.btn_exit);
        btn_mex = findViewByID(R.id.btn_mex);
        btn_clean = findViewByID(R.id.btn_clean);
        btn_download = findViewByID(R.id.btn_download);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        application = (PlayerApplication) getActivity().getApplication();
        btn_mex.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_auto.setOnClickListener(this);
        btn_clean.setOnClickListener(this);
        btn_download.setOnClickListener(this);
        img_switch.setOnClickListener(this);

        if(SpUtil.getIsNotificationOpen(getActivity())){
            img_switch.setImageResource(R.mipmap.icon_setting_checked);
        }
        else{
            img_switch.setImageResource(R.mipmap.icon_setting_uncheck);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_auto:
                dialog = new LoadingDialog(getActivity(), "正在搜索歌曲");
                dialog.show();
                autoSearch();
                break;
            case R.id.btn_mex:
                break;
            case R.id.btn_exit:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                }, 300);
                break;
            case R.id.btn_download:
                startActivity(DownloadedActivity.class, null, 0);
                break;
            case R.id.img_switch:
                SpUtil.setIsNotificationOpen(getActivity(), !SpUtil.getIsNotificationOpen(getActivity()));
                if(SpUtil.getIsNotificationOpen(getActivity())){
                    img_switch.setImageResource(R.mipmap.icon_setting_checked);
                }
                else{
                    img_switch.setImageResource(R.mipmap.icon_setting_uncheck);
                }
                break;
            case R.id.btn_clean:
                Intent intent = new Intent(getActivity(), MusicService.class);
                intent.putExtra(Params.OP, Params.OP_STOP);
                getActivity().startService(intent);
                application.getMusicDao().deleteAll();
                application.notifyMusicListChanged();
                showToast("清除播放列表成功");
                ((MainActivity) getActivity()).clickTab(1);
                break;
        }
    }

    private void autoSearch() {
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                Looper.prepare();
//                getActivity().getSupportLoaderManager().initLoader(0, null, callBack);
//            }
//        }.start();
        getActivity().getSupportLoaderManager().initLoader(0, null, callBack);
    }

    private LoaderManager.LoaderCallbacks<Cursor> callBack = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };

        @Override
        public Loader onCreateLoader(int id, Bundle args) {
            CursorLoader cursorLoader = new CursorLoader(
                    getActivity(),
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[1]);
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader loader, final Cursor data) {
            if (data != null) {
                int count = data.getCount();
                if (count > 0) {
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            data.moveToFirst();
                            MusicListItemDAO dao = application.getMusicDao();
//                    MusicListItemDAO dao = PlayerApplication.getInstance().getMusicDao();
                            dao.deleteAll();
                            List<MusicListItemBean> tmpList = new ArrayList<MusicListItemBean>();
                            do {
                                MusicListItemBean bean = new MusicListItemBean();
//                        bean.setIsPlaying(false);
                                bean.setIsVaild(true);
                                bean.setPath(data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0])));
                                bean.setName(data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1])));
                                bean.setLength(data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2])));
//                                dao.addMusic(bean);
                                tmpList.add(bean);
//                                System.out.println(data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0])) + "--"
//                                        + data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1])) + "--"
//                                        + data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2])));
                                dialog.setText("搜索中: " + data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1])));
                            } while (data.moveToNext());
                            dao.addMusicList(tmpList);
                            dialog.dismiss();
                            application.notifyMusicListChanged();
//                            Looper.prepare();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showToast("搜索完成");
                                    ((MainActivity) getActivity()).clickTab(1);
                                }
                            });
                        }
                    }.start();
                }
            }
            else{
                showToast("音乐读取失败");
                dialog.dismiss();
                application.notifyMusicListChanged();
            }
//            PlayerApplication.getInstance().notifyMusicListChanged();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }

    };
}
