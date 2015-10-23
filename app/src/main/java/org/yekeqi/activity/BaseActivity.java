package org.yekeqi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {

	protected boolean firstClick;
	protected String TAG;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		SysApplication.getInstance().addActivity(this);
		TAG = setTag();
		setContentView(setLayout());
		findView();
		initData(savedInstanceState);
		
//		PushAgent.getInstance(this).onAppStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		firstClick = true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		SysApplication.getInstance().removeActivity(this);
	}
	
	protected abstract String setTag();
	
	/**
	 * 设置布局
	 */
	protected abstract int setLayout();
	
	/**
	 * 初始化控件
	 */
	protected abstract void findView();
	
	/**
	 * 为控件设置内容或者监听器
	 */
	protected abstract void initData(Bundle savedInstanceState);
	
	@SuppressWarnings("unchecked")
	protected <T extends View> T findViewByID(int id) {
		return (T) findViewById(id);
	}
	
	/**
	 * 全局的 页面跳转方法
	 * @param src  本页面
	 * @param dst  跳转页面
	 * @param bundle 
	 * @param requestCode  大于0有回调
	 */
	public void startActivity(Activity src, Class<?> dst, Bundle bundle, int requestCode) {

		if(firstClick){
			firstClick = false;
			Intent intent = new Intent(src, dst);
			
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
	
	protected BaseActivity getThis(){
		return this;
	}
	
	protected void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	
	protected void showToastLong(String text){
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
}
