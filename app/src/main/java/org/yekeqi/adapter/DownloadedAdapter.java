package org.yekeqi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.yekeqi.R;
import org.yekeqi.bean.DownedBean;
import org.yekeqi.bean.DowningBean;
import org.yekeqi.util.HelpUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/9/30.
 */
public class DownloadedAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<DowningBean> downingBeanList;
    private List<DownedBean> downedBeanList;

    public DownloadedAdapter(Context context,List<DowningBean> downingBeanList, List<DownedBean> downedBeanList){
        this.context = context;
        this.downedBeanList = downedBeanList;
        this.downingBeanList = downingBeanList;
    }

    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition == 0){
            return downingBeanList.size();
        }
        return downedBeanList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_download_group, null);
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        if(groupPosition == 0){
            tv_title.setText("下载中");
        }
        else{
            tv_title.setText("下载完成");
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_download_child, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_length = (TextView) convertView.findViewById(R.id.tv_length);
            holder.img_status = (ImageView) convertView.findViewById(R.id.img_status);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(groupPosition == 0){
            holder.tv_name.setText(downingBeanList.get(childPosition).getName());
            holder.tv_length.setText(HelpUtil.getSize(downingBeanList.get(childPosition).getLength()));
            holder.img_status.setVisibility(View.GONE);
        }
        else{
            holder.tv_name.setText(downedBeanList.get(childPosition).getName());
            holder.tv_length.setText(HelpUtil.getSize(downedBeanList.get(childPosition).getLength()));
            holder.img_status.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder{
        TextView tv_name;
        TextView tv_length;
        ImageView img_status;
    }
}
