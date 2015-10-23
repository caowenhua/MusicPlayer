package org.yekeqi.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.yekeqi.R;
import org.yekeqi.fragment.BaseFragment;
import org.yekeqi.fragment.ListFragment;
import org.yekeqi.fragment.PlayFragment;
import org.yekeqi.fragment.SearchFragment;
import org.yekeqi.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yekeqi on 2015/9/23.
 */
public class MainActivity extends BaseFragmentActivity implements View.OnClickListener{

    private View view_moving;
    private ViewPager pager_main;
    private ImageView img_search;
    private ImageView img_play;
    private ImageView img_list;
    private ImageView img_setting;

    private List<BaseFragment> fragments;
    private int screenWidth;

    @Override
    protected String setTag() {
        return "MainActivity";
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void findView() {
        view_moving = findViewByID(R.id.view_moving);
        img_list = findViewByID(R.id.img_list);
        img_search = findViewByID(R.id.img_search);
        img_play = findViewByID(R.id.img_play);
        img_setting = findViewByID(R.id.img_setting);
        pager_main = findViewByID(R.id.pager_main);

        screenWidth = getDeviceW(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth/4, dpToPx(3.0f, getResources()));
        params.addRule(RelativeLayout.BELOW, R.id.llt_top);
        view_moving.setLayoutParams(params);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        img_list.setOnClickListener(this);
        img_search.setOnClickListener(this);
        img_play.setOnClickListener(this);
        img_setting.setOnClickListener(this);

        setupPager();
    }

    private void setupPager() {
        fragments = new ArrayList<>();
        fragments.add(new PlayFragment());
        fragments.add(new ListFragment());
        fragments.add(new SearchFragment());
        fragments.add(new SettingFragment());

        pager_main.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
            }
        });
//        pager_main.setAdapter(new PagerAdapter() {
//            @Override
//            public int getCount() {
//                return fragments.size();
//            }
//
//            @Override
//            public boolean isViewFromObject(View view, Object object) {
//                return (view == object);
//            }
//
//            @Override
//            public Object instantiateItem(ViewGroup container, int position) {
////                super.instantiateItem(container, position);
//                return fragments.get(position);
////                return super.instantiateItem(container, position);
//            }
//
//            @Override
//            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
//            }
//        });

        pager_main.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                clickTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_play:
                clickTab(0);
                break;
            case R.id.img_search:
                clickTab(2);
                break;
            case R.id.img_setting:
                clickTab(3);
                break;
            case R.id.img_list:
                clickTab(1);
                break;
        }
    }

    public void clickTab(int position){
        pager_main.setCurrentItem(position);
        view_moving.animate().x(position*screenWidth/4).setDuration(150).start();
        if(position == 1){
//            ((ListFragment)fragments.get(position)).getAdapter().notifyDataSetChanged();
        }
    }

    private int getDeviceW(Context context) {
        int w = 0;
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        w = dm.widthPixels;
        return w;
    }
    private int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                resources.getDisplayMetrics());
        return (int) px;
    }
}
