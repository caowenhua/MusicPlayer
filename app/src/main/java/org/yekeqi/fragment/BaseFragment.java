package org.yekeqi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment {

	protected boolean firstClick;
	protected String TAG;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		TAG = setTag();
		findView();
		initData(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(setLayout() == 0){
			return super.onCreateView(inflater, container, savedInstanceState);
		}
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(setLayout(), container, false);
		return view;
	}
	
	/**
	 * 设置类名
	 * @return
	 */
	protected abstract String setTag();
	
	/**
	 * 初始化View
	 * @return
	 */
	protected abstract void findView();
	
	/**
	 * 设置布局
	 */
	protected abstract int setLayout();
	
	/**
	 * 为控件设置内容或者监听器
	 */
	protected abstract void initData(Bundle savedInstanceState);
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends View> T findViewByID(int id) {
		return (T) getView().findViewById(id);
	}
	
	public void onResume() {
	    super.onResume();
	    firstClick = true;
	}
	
	public void onPause() {
	    super.onPause();
	}
	
	/**
	 * 全局的 页面跳转方法
	 * @param dst  跳转页面
	 * @param bundle 
	 * @param requestCode  大于0有回调
	 */
	public void startActivity(Class<?> dst, Bundle bundle, int requestCode) {

		if(firstClick){
			firstClick = false;
			Intent intent = new Intent(getActivity(), dst);
			
			if (bundle != null) {
				intent.putExtras(bundle);
			}

			if (requestCode > 0) {
				startActivityForResult(intent, requestCode);
			} else {
				startActivity(intent);
			}
		}

	}
	
	protected String getTAG(){
		return TAG;
	}
	
	protected void showToast(String text){
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}
	
	
	protected void showToastLong(String text){
		Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}
}
