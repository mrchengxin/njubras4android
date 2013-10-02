package cn.nju.cs.seg.brasclient;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import cn.nju.cs.seg.brasclient.bean.Content;

public class ForceLogoutActivity extends Activity {
	
	private TextView username, areaname, logintime, ip, payamount;
	
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_force_logout);
		
		username = (TextView)findViewById(R.id.textview_username_forcelogout);
		areaname = (TextView)findViewById(R.id.textview_areaname_forcelogout);
		logintime = (TextView)findViewById(R.id.textview_logintime_forcelogout);
		ip = (TextView)findViewById(R.id.textview_ip_forcelogout);
		payamount = (TextView)findViewById(R.id.textview_payamount_forcelogout);
		
		sp = this.getSharedPreferences("userinfo", MODE_PRIVATE);
		
		String un = sp.getString("username", "");
		String pw = sp.getString("password", "");
		new ShowContent().execute(un, pw);
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
	
	// 展示用户信息
	private class ShowContent extends AsyncTask<String, Void, Content> {
		@Override
		protected Content doInBackground(String... params) {
			return MainActivity.client.getContent(params[0], params[1]);
		}
		
		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPostExecute(Content result) {
			super.onPostExecute(result);
			if (result != null && result.getResults() != null) {
				if (result.getResults().getUsername() != null)
					username.setText(result.getResults().getUsername());
				if (result.getResults().getArea_name() != null)
					areaname.setText(result.getResults().getArea_name());
				if (result.getResults().getAcctstarttime() > 0)
					logintime.setText(new SimpleDateFormat("MM/dd  HH:mm").format(new Date(result.getResults().getAcctstarttime()*1000)));
				if (result.getResults().getUser_ip() != null)
					ip.setText(result.getResults().getUser_ip());
				if (result.getResults().getPayamount() >= 0.0)
					payamount.setText(String.format("%.2f 元", result.getResults().getPayamount()));
			}
		}
	}

	/**
	 * 准备强制下线
	 * @param view
	 */
	public void forceLogout(View view) {
		Intent intent = new Intent(this, TransitionActivity.class);
		intent.putExtra(MainActivity.OPERATION, MainActivity.FORCELOGOUT);
		intent.putExtra(MainActivity.USERNAME, MainActivity.username);
		intent.putExtra(MainActivity.PASSWORD, MainActivity.password);
		startActivity(intent);
	}
	
	/**
	 * 刷新用户信息
	 * @param view
	 */
	public void refresh(View view) {
		String un = sp.getString("username", "");
		String pw = sp.getString("password", "");
		new ShowContent().execute(un, pw);
	}
}
