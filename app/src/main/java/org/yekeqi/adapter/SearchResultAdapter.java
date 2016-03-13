package org.yekeqi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.yekeqi.R;
import org.yekeqi.api.Params;
import org.yekeqi.bean.DowningBean;
import org.yekeqi.dao.DowningDAO;
import org.yekeqi.model.SongSearchResponseData;
import org.yekeqi.service.MusicService;
import org.yekeqi.util.HelpUtil;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2015/9/29.
 */
public class SearchResultAdapter extends BaseAdapter {

    private Context context;
    private List<SongSearchResponseData> list;

    public SearchResultAdapter(Context context, List<SongSearchResponseData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list==null? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list==null? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list==null? 0 : list.get(position).getSongId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_result_list, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_singer = (TextView) convertView.findViewById(R.id.tv_singer);
            holder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            holder.tv_ablum = (TextView) convertView.findViewById(R.id.tv_ablum);
            holder.img_down = (ImageView) convertView.findViewById(R.id.img_down);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_singer.setText(list.get(position).getSingerName());
        holder.tv_ablum.setText(list.get(position).getAlbumName());
        int tmp = 0;
        for (int i=1 ; i<list.get(position).getAuditionList().size() ; i++){
            if(list.get(position).getAuditionList().get(tmp).getBitRate() <
                    list.get(position).getAuditionList().get(i).getBitRate()){
                tmp = i;
            }
        }
        final int max = tmp;
        if (list.get(position).getAuditionList().size() > 0) {
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
            holder.tv_size.setText(formatter.format(list.get(position).getAuditionList().get(max).getDuration()) + "  ,  " +
                    HelpUtil.getSize(list.get(position).getAuditionList().get(max).getSize()));
            holder.img_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DowningBean bean = new DowningBean();
                    bean.setName(list.get(position).getName());
                    bean.setSinger(list.get(position).getSingerName());
                    bean.setUrl(list.get(position).getAuditionList().get(list.get(position).getAuditionList().size() - 1).getUrl());
                    bean.setLength(list.get(position).getAuditionList().get(list.get(position).getAuditionList().size() - 1).getSize());
                    bean.setSuffix(list.get(position).getAuditionList().get(list.get(position).getAuditionList().size() - 1).getSuffix());
                    bean.setDuration(list.get(position).getAuditionList().get(list.get(position).getAuditionList().size()-1).getDuration());
                    DowningDAO dao = DowningDAO.getInstance(context);
                    dao.add(bean);
                    Intent intent = new Intent(context, MusicService.class);
                    intent.putExtra(Params.OP, Params.OP_DOWNLOAD);
                    context.startService(intent);
                    Toast.makeText(context, "已加入下载队列", Toast.LENGTH_SHORT).show();
//                System.out.println(list.get(position).getAuditionList().get(max).getUrl());
                }
            });
        }

        return convertView;
    }

    class ViewHolder{
        TextView tv_name;
        TextView tv_singer;
        TextView tv_size;
        TextView tv_ablum;
        ImageView img_down;
    }
}
