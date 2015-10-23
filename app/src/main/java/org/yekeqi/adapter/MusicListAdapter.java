package org.yekeqi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.yekeqi.R;
import org.yekeqi.application.PlayerApplication;
import org.yekeqi.bean.MusicListItemBean;
import org.yekeqi.util.SpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yekeqi on 2015/9/23.
 */
public class MusicListAdapter extends BaseAdapter{

    /**
     * 第一次获取歌曲直接刷新，清除一次，之后就反应慢一拍
     */
    private List<MusicListItemBean> musicList;
    private List<MusicListItemBean> showingList;
    private Context context;

    public MusicListAdapter(Context context){
//        list = PlayerApplication.getInstance().getMusicList();
        musicList = ((PlayerApplication)(context.getApplicationContext())).getMusicList();
        showingList = new ArrayList<>();
        showingList.addAll(musicList);
        this.context = context;
    }

    @Override
    public int getCount() {
        return showingList==null? 0:showingList.size();
    }

    @Override
    public MusicListItemBean getItem(int position) {
        return showingList==null? null : showingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return showingList.get(position).getId();
    }

    public int getPlayingPosition(){
        int id = SpUtil.getLastPlatId(context);
        for(int i=0 ; i<showingList.size() ; i++){
            if(showingList.get(i).getId() == id){
                return i;
            }
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_music_list, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_length = (TextView) convertView.findViewById(R.id.tv_length);
            holder.img_status = (ImageView) convertView.findViewById(R.id.img_status);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(!showingList.get(position).isVaild()){
            holder.tv_name.setTextColor(Color.RED);
            holder.tv_length.setTextColor(Color.RED);
        }
        else{
            holder.tv_name.setTextColor(Color.parseColor("#f0f0f0"));
            holder.tv_length.setTextColor(Color.parseColor("#cdcdcd"));
        }
//        if(list.get(position).isPlaying()){
        if(SpUtil.getLastPlatId(context) == showingList.get(position).getId()){
            holder.img_status.setVisibility(View.VISIBLE);
        }
        else{
            holder.img_status.setVisibility(View.GONE);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        holder.tv_name.setText(showingList.get(position).getName());
        holder.tv_length.setText(formatter.format(showingList.get(position).getLength()));
        return convertView;
    }

    public void resetList() {
        showingList.clear();
        showingList.addAll(musicList);
        notifyDataSetChanged();
    }

    public void searchMusic(String poi){
        showingList.clear();
        if(poi.equals("")){
            showingList.addAll(musicList);
        }
        else{
            for(MusicListItemBean bean : musicList){
                if(bean.getName().contains(poi)){
                    showingList.add(bean);
                }
            }
        }
        notifyDataSetChanged();
    }

    public int getId(int position) {
        return showingList.get(position).getId();
    }

    class ViewHolder{
        TextView tv_name;
        TextView tv_length;
        ImageView img_status;
    }
}
