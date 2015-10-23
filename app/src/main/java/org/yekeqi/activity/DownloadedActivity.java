package org.yekeqi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.yekeqi.R;
import org.yekeqi.adapter.DownloadedAdapter;
import org.yekeqi.api.Params;
import org.yekeqi.bean.DownedBean;
import org.yekeqi.bean.DowningBean;
import org.yekeqi.dao.DownedDAO;
import org.yekeqi.dao.DowningDAO;

import java.util.List;

/**
 * Created by Administrator on 2015/9/29.
 */
public class DownloadedActivity extends BaseActivity {

    private ExpandableListView lv_done;
    private ImageButton imgbtn_back;
    private DownloadedAdapter adapter;
    private List<DowningBean> downingBeanList;
    private List<DownedBean> downedBeanList;

    @Override
    protected String setTag() {
        return "DownloadedActivity";
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_download;
    }

    @Override
    protected void findView() {
        imgbtn_back = findViewByID(R.id.imgbtn_back);
        lv_done = findViewByID(R.id.lv_done);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        DownedDAO dao = DownedDAO.getInstance(this);
        downedBeanList = dao.getList();
        DowningDAO dao2 = DowningDAO.getInstance(this);
        downingBeanList = dao2.getList();
        adapter = new DownloadedAdapter(this, downingBeanList, downedBeanList);
        lv_done.setAdapter(adapter);
        lv_done.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition == 0) {
                    Intent intent = new Intent();
                    intent.putExtra(Params.OP, Params.OP_DOWNLOAD);
                    startService(intent);
                    showToast("已经准备下载");
                } else {
                    showToast(downedBeanList.get(childPosition).getName());
                }
                return false;
            }
        });

        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // params.height += 5;// if without this statement,the listview will be
        // a
        // little short
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
