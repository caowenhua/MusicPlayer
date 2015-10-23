package org.yekeqi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.yekeqi.R;
import org.yekeqi.model.KeySearchResponseSong;

import java.util.List;

/**
 * Created by Administrator on 2015/9/29.
 */
public class SearchListAdapter extends BaseAdapter {

    private Context context;
    private List<KeySearchResponseSong> list;

    public SearchListAdapter(Context context, List<KeySearchResponseSong> list){
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
        return list==null? 0 : list.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_list, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_singer = (TextView) convertView.findViewById(R.id.tv_singer);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_singer.setText(list.get(position).getSinger_name());
        return convertView;
    }

    class ViewHolder{
        TextView tv_name;
        TextView tv_singer;
    }
}
