package cn.nju.cs.seg.brasclient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import cn.nju.cs.seg.brasclient.bean.Content;

public class LogoutActivity extends Activity {
	
	private TextView username, areaname, logintime, ip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_logout);
		
		username = (TextView)findViewById(R.id.textview_username_logout);
		areaname = (TextView)findViewById(R.id.textview_areaname_logout);
		logintime = (TextView)findViewById(R.id.textview_logintime_logout);
		ip = (TextView)findViewById(R.id.textview_ip_logout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add("退出");
		menu.add("关于");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		if (item.getTitle().equals("退出")) {
			// 退出进程
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
	    	intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			android.os.Process.killProcess(Process.myPid());
		}
		return true;
	}
	
	@Override
	protected void onPostResume() {
		super.onPostResume();
		new ShowContent().execute();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// 延迟1秒调用核心代码
		delayShowContent(2000);
	}
	
	@Override
	public void onBackPressed() {
		// 退出进程
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
    	intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		android.os.Process.killProcess(Process.myPid());
		return;
	};
	
	Handler showContentHandler = new Handler();
	Runnable showContentRunnable = new Runnable() {
		@Override
		public void run() {
			new ShowContent().execute();
		}
	};
	
	private void delayShowContent(int delayMillis) {
		showContentHandler.removeCallbacks(showContentRunnable);
		showContentHandler.postDelayed(showContentRunnable, delayMillis);
	}
	
	// 展示用户信息
	private class ShowContent extends AsyncTask<Void, Void, Content> {
		@Override
		protected Content doInBackground(Void... arg0) {
			return MainActivity.client.getContent();
		}
		
		@Override
		protected void onPostExecute(Content result) {
			super.onPostExecute(result);
			if (result != null && result.getResults() != null) {
				if (result.getResults().getUsername() != null)
					username.setText(result.getResults().getUsername());
				if (result.getResults().getArea_name() != null)
					areaname.setText(result.getResults().getArea_name());
				if (result.getResults().getAcctstarttime() != null)
					logintime.setText(result.getResults().getAcctstarttime().substring(result.getResults().getAcctstarttime().indexOf("-")+1));
				if (result.getResults().getUser_ip() != null)
					ip.setText(result.getResults().getUser_ip());
			}
		}
	}

	/**
	 * 准备下线
	 * @param view
	 */
	public void logout(View view) {		
		Intent intent = new Intent(this, TransitionActivity.class);
		intent.putExtra(MainActivity.OPERATION, MainActivity.LOGOUT);
		startActivity(intent);
	}
	
	/**
	 * 刷新用户信息
	 * @param view
	 */
	public void refresh(View view) {
		new ShowContent().execute();
	}
}
