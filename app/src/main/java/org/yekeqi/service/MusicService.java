package org.yekeqi.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.yekeqi.R;
import org.yekeqi.activity.DownloadedActivity;
import org.yekeqi.activity.MainActivity;
import org.yekeqi.api.Params;
import org.yekeqi.application.PlayerApplication;
import org.yekeqi.bean.DownedBean;
import org.yekeqi.bean.DowningBean;
import org.yekeqi.bean.MusicListItemBean;
import org.yekeqi.dao.DownedDAO;
import org.yekeqi.dao.DowningDAO;
import org.yekeqi.util.HelpUtil;
import org.yekeqi.util.SpUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service{

    private MediaPlayer player;
    private List<MusicListItemBean> musicList;
    private int playingPosition;
    private int currentPlayingPosition;	//播放进度
    private Random random;
    private boolean isPaused;
    private boolean isDowning;
//    private boolean isPlaying;

    private NotificationManager notificationManager;
    private Notification notification;
    private RemoteViews remoteViews;

    private Timer timer;
    private PlayerApplication application;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        musicList = PlayerApplication.getInstance().getMusicList();
        application = (PlayerApplication)getApplication();
        musicList = application.getMusicList();
//        isPlaying = false;
        isPaused = false;
        isDowning = false;
        playingPosition = -1;
        int tmp = SpUtil.getLastPlatId(this);
        if(tmp >= 0){
            for(int i=0 ; i<musicList.size() ; i++){
                if(musicList.get(i).getId() == tmp){
                    playingPosition = i;
                    break;
                }
            }
        }

//        playingPosition =  application.getMusicDao().getWhichIsPlaying();
//        playingPosition = PlayerApplication.getInstance().getMusicDao().getWhichIsPlaying();
        if(playingPosition == -1){
            playingPosition = 0;
        }
        currentPlayingPosition = 0;
        buildPlayer();
        openTimer();
        setupNotification();

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(musicList.size() > 0){
                    if(SpUtil.getIsLoop(MusicService.this)){
                        opRightNext();
                    }
                    else{
                        opRandomNext();
                    }
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            musicList = application.getMusicList();
            handleOperator(intent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(player != null){
            player.stop();
            player.release();
        }
    }

    private void handleOperator(Intent intent){
        switch (intent.getIntExtra(Params.OP, Params.OP_NULL)){
            case Params.OP_LEFT_NEXT:
                if(SpUtil.getIsLoop(this)){
                    opLeftNext();
                }
                else{
                    opRandomNext();
                }
                break;
            case Params.OP_RIGHT_NEXT:
                if(SpUtil.getIsLoop(this)){
                    opRightNext();
                }
                else{
                    opRandomNext();
                }
                break;
            case Params.OP_PLAY:
                opPlay();
                break;
            case Params.OP_EXIT:
                opExit();
                break;
            case Params.OP_SEEK_TO:
                opSeekTo(intent.getIntExtra(Params.POSITION, 0));
                break;
            case Params.OP_CHANGE:
                opChange(intent.getIntExtra(Params.POSITION, 0));
                break;
            case Params.OP_PAUSE:
                break;
            case Params.OP_STOP:
                opStop();
                break;
            case Params.OP_DOWNLOAD:
                opDownload();
                break;
            default:
                break;
        }
    }

    /**
     * player为null则新建，未prepare未设路径
     */
    private void buildPlayer(){
        if(player == null){
            player = new MediaPlayer();
            player.setLooping(false);
        }
    }

    /**
     * player设定路径并开始播放
     */
    private void resetNextPlay(){
        buildPlayer();
        player.reset();
        int pre = -1;
        if(playingPosition < musicList.size() && playingPosition >= 0){
            pre = musicList.get(playingPosition).getId();
        }
        try {
            if(playingPosition >= musicList.size() || playingPosition < 0)
                playingPosition = 0;
            System.out.println(musicList.get(playingPosition).getName() + "---" + musicList.get(playingPosition).getPath());
            player.setDataSource(musicList.get(playingPosition).getPath());
            sendPlayingBroadCast();
//            application.getMusicDao().setWhichIsPlaying(pre, musicList.get(playingPosition).getId());
            SpUtil.setLastPlatId(this, musicList.get(playingPosition).getId());
//            application.notifyMusicListChanged();
//            isPlaying = true;
            isPaused = false;
            sendNullBroadcast();
//            PlayerApplication.getInstance().getMusicDao().setWhichIsPlaying(pre, musicList.get(playingPosition).getId());
            player.prepare();
            player.start();
            setupNotification();
        }catch (IllegalArgumentException | SecurityException
                | IllegalStateException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "文件不存在 或 播放器读取失败", Toast.LENGTH_SHORT).show();
            application.getMusicDao().setWhichIsUnvaild(musicList.get(playingPosition).getId());
            application.notifyMusicListChanged();
            if(!application.getMusicDao().isAllUnVaild()){
                if(SpUtil.getIsLoop(this)){
                    opRightNext();
                }
                else{
                    opRandomNext();
                }
            }
//            PlayerApplication.getInstance().getMusicDao().setWhichIsUnvaild(playingPosition);
        }
    }

    private void sendPlayingBroadCast() {
        Intent cast = new Intent("playingChanged");
        cast.putExtra(Params.IS_PLAYING, player.isPlaying());
        sendBroadcast(cast);
    }

    private void opRightNext() {
        if(musicList.size() > 0){
            if(playingPosition == musicList.size() - 1){
                playingPosition = 0;
            }
            else{
                playingPosition ++;
            }
            currentPlayingPosition = 0;
            resetNextPlay();
        }
    }

    private void opLeftNext() {
        if(musicList.size() > 0){
            if(playingPosition == 0){
                playingPosition = musicList.size() - 1;
            }
            else{
                playingPosition --;
            }
            currentPlayingPosition = 0;
            resetNextPlay();
        }
    }

    private void opPlay() {
        if(musicList.size() > 0){
            if(player.isPlaying()){
                if(player != null){
                    player.pause();
                    isPaused = true;
                }
            }
            else{
                if(player != null){
//                    player.
                    if(isPaused){
                        try {
                            player.seekTo(currentPlayingPosition);
                            player.start();
                        }
                        catch (Exception e){
                        }
                    }
                    else{
                        resetNextPlay();
                    }
                    isPaused = false;
                }
                else{
                    resetNextPlay();
                }
            }
            setupNotification();
            sendPlayingBroadCast();
        }
    }

    private void opExit(){
        cancalNotification();
        System.exit(0);
    }

    private void opChange(int id){
        playingPosition = getPostionById(id);
        resetNextPlay();
    }

    private void opRandomNext(){
        if(musicList.size() > 0){
            if(random == null){
                random = new Random();
            }
            playingPosition = random.nextInt(musicList.size());
            currentPlayingPosition = 0;
            resetNextPlay();
        }

    }

    private void opSeekTo(int position){
        if(musicList.size() > 0){
            if(player != null){
                if(player.isPlaying()){
                    player.seekTo(position);
                }
            }
            currentPlayingPosition = position;
        }
    }

    private void opStop() {
        if(player != null){
//            player.release();
            player.reset();
            Intent intent = new Intent("seekBarUpdateBroadCast");
            intent.putExtra(Params.TOTAL, 0);
            intent.putExtra(Params.DURATION, 0);
            intent.putExtra(Params.PLAYING_NAME, "");
            sendBroadcast(intent);
            setupNotification();
        }
    }

    private int getPostionById(int id){
        for (int i=0 ; i<musicList.size() ;i++){
            if(musicList.get(i).getId() == id)
                return i;
        }
        return 0;
    }

    private class RequestSeekBarTask extends TimerTask {
        @Override
        public void run() {
            sendDetailBroadcast();
        }
    }

    private void sendNullBroadcast() {
        Intent intent = new Intent("seekBarUpdateBroadCast");
        intent.putExtra(Params.TOTAL, 0);
        intent.putExtra(Params.DURATION, 0);
        intent.putExtra(Params.PLAYING_NAME, "");
        sendBroadcast(intent);
    }


    private void sendDetailBroadcast() {
        if(player.isPlaying()){
            int duration = 0;
            try {
                currentPlayingPosition = player.getCurrentPosition();
                duration = player.getDuration();
                Intent intent = new Intent("seekBarUpdateBroadCast");
                intent.putExtra(Params.TOTAL, duration);
                intent.putExtra(Params.DURATION, currentPlayingPosition);
                intent.putExtra(Params.PLAYING_NAME, musicList.get(playingPosition).getName());
//                    System.out.println(duration + "**" + currentPlayingPosition + "**" + musicList.get(playingPosition).getName());
                sendBroadcast(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setupNotification() {
        if(SpUtil.getIsNotificationOpen(this)){
            remoteViews = new RemoteViews(getPackageName(), R.layout.notification_playing);
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            Intent intentPlay = new Intent(this, MusicService.class)
                    .putExtra(Params.OP, Params.OP_PLAY);
            PendingIntent pendingIntentPlay = PendingIntent.getService(this, Params.NOTIFICATION_PLAY,
                    intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intentNext = new Intent(this, MusicService.class)
                    .putExtra(Params.OP, Params.OP_RIGHT_NEXT);
            PendingIntent pendingIntentNext = PendingIntent.getService(this, Params.NOTIFICATION_NEXT,
                    intentNext, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intentX = new Intent(this, MusicService.class)
                    .putExtra(Params.OP, Params.OP_EXIT);
            PendingIntent pendingIntentX = PendingIntent.getService(this, Params.NOTIFICATION_X,
                    intentX, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intentTo = new Intent(this, MainActivity.class);
            PendingIntent pendingIntentTo = PendingIntent.getActivity(this, Params.NOTIFICATION_TO,
                    intentTo, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setOnClickPendingIntent(R.id.imgbtn_x,pendingIntentX);
            remoteViews.setOnClickPendingIntent(R.id.imgbtn_play, pendingIntentPlay);
            remoteViews.setOnClickPendingIntent(R.id.imgbtn_next, pendingIntentNext);
            remoteViews.setOnClickPendingIntent(R.id.tv_name, pendingIntentTo);

            builder.setContent(remoteViews);
            builder.setOngoing(true);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            if(player == null || player.isPlaying()){
                remoteViews.setImageViewResource(R.id.imgbtn_play, R.mipmap.note_btn_pause);
            }
            else{
                remoteViews.setImageViewResource(R.id.imgbtn_play, R.mipmap.note_btn_play);
            }
            if(musicList.size() > 0){
                remoteViews.setTextViewText(R.id.tv_name, musicList.get(playingPosition).getName());
            }
            else{
                remoteViews.setTextViewText(R.id.tv_name, "-");
            }
            //n.contentView = rv;
            notification = builder.build();
            notificationManager.notify(59, notification);
        }
    }

    private void cancalNotification(){
        notificationManager.cancel(59);
    }

    private void openTimer(){
        timer = new Timer();
        timer.schedule(new RequestSeekBarTask(), 500, 1000);
    }

    private DowningBean downingBean;
    private void opDownload(){
        if(!isDowning){
            DowningDAO dao = DowningDAO.getInstance(this);
            downingBean = dao.getBean();
            if(downingBean != null){
                download(downingBean.getUrl(), downingBean.getSinger() + " - " + downingBean.getName() + "." + downingBean.getSuffix());
            }
        }
    }

    private long tmpTime;
    private void download(String url, String name){
        if(!HelpUtil.isDownloadDirExist()){
            HelpUtil.setDownloadDir(this);
        }
        HttpUtils httpUtils = new HttpUtils();
        System.out.println(url + "---"  + HelpUtil.getDownloadPath() + name);
        HttpHandler httpHandler = httpUtils.download(url, HelpUtil.getDownloadPath() + name, true, true, new RequestCallBack<File>() {
            @Override
            public void onStart() {
                super.onStart();
                isDowning = true;
                tmpTime = System.currentTimeMillis();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                if(System.currentTimeMillis() - tmpTime > 500){
                    setupDownloadNotification((int)total, (int)current);
                    tmpTime = System.currentTimeMillis();
                }
                System.out.println("onLoading" + total + "--" + current);
            }

            @Override
            public void onCancelled() {
                super.onCancelled();
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                downloadSuccess();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                isDowning = false;
                if(s.equals("the file has downloaded completely")){
                    cancalDownloadNotification();
                }
                else{
                    downloadSuccess();
                }
                System.out.println("onFailure" + s);
            }
        });
    }

    private void downloadSuccess() {
        cancalDownloadNotification();
        MusicListItemBean bean = new MusicListItemBean();
        bean.setIsVaild(true);
        bean.setName(downingBean.getName());
        bean.setPath(HelpUtil.getDownloadPath() + downingBean.getSinger() + " - " + downingBean.getName()+ "." + downingBean.getSuffix());
        bean.setLength(downingBean.getDuration());
        application.getMusicDao().addMusic(bean);
        DownedDAO dao = DownedDAO.getInstance(MusicService.this);
        DownedBean bea = new DownedBean();
        bea.setUrl(downingBean.getUrl());
        bea.setLength(downingBean.getLength());
        bea.setSinger(downingBean.getSinger());
        bea.setName(downingBean.getName());
        bea.setDuration(downingBean.getDuration());
        bea.setPath(HelpUtil.getDownloadPath() + downingBean.getSinger() + " - " + downingBean.getName() + "." + downingBean.getSuffix());
        dao.add(bea);
        DowningDAO downingDAO = DowningDAO.getInstance(this);
        downingDAO.delete(downingBean.getUrl());
        isDowning = false;
        cancalDownloadNotification();
        opDownload();
    }

    private void setupDownloadNotification(int total, int current) {
        if(SpUtil.getIsNotificationOpen(this)){
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            Intent intentTo = new Intent(this, DownloadedActivity.class);
            PendingIntent pendingIntentTo = PendingIntent.getActivity(this, Params.NOTIFICATION_TO,
                    intentTo, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntentTo);
            builder.setOngoing(true);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setProgress(total, current, false);
            builder.setContentText("下载中..");
            notification = builder.build();
            notificationManager.notify(60, notification);
        }
    }

    private void setupDownloadFailNotification() {
        if(SpUtil.getIsNotificationOpen(this)){
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            Intent intentTo = new Intent(this, DownloadedActivity.class);
            PendingIntent pendingIntentTo = PendingIntent.getActivity(this, Params.NOTIFICATION_TO,
                    intentTo, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntentTo);
            builder.setOngoing(true);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentText("下载失败");
            notification = builder.build();
            notificationManager.notify(60, notification);
        }
    }

    private void cancalDownloadNotification(){
        notificationManager.cancel(60);
    }
}
