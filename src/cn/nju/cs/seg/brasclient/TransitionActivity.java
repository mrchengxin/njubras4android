package cn.nju.cs.seg.brasclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;

public class TransitionActivity extends Activity {
	private final static int DEFAULT_DELAY_MILLIS = 50;
	
	private AnimationDrawable airplaneAnimation;
	
	private int operation;
	private String username;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_transition);
		
		ImageView airplaneImage = (ImageView)findViewById(R.id.gif_transition);
		airplaneImage.setBackgroundResource(R.drawable.airplane);
		airplaneAnimation = (AnimationDrawable) airplaneImage.getDrawable();
		airplaneAnimation.start();
		
		// Get the message from the intent
	    Intent intent = getIntent();
	    operation = intent.getIntExtra(MainActivity.OPERATION, 0);
	    username = intent.getStringExtra(MainActivity.USERNAME);
	    password = intent.getStringExtra(MainActivity.PASSWORD);
	}
	
	/**
	 * 核心代码
	 * 根据不同的消息（LOGIN, LOGOUT, FORCELOGOUT）执行不同的过程
	 */
	Handler mShowHandler = new Handler();
	Runnable mShowRunnable = new Runnable() {
		@Override
		public void run() {
			Intent intent = null;
			if (operation == MainActivity.LOGIN)
			{
				try {
					if (username.equals(""))
					{
						intent = new Intent(getApplicationContext(), LoginActivity.class);
						intent.putExtra(LoginActivity.WARNING_MSG, R.string.msg_username_error);
					}
					else if (password.equals(""))
					{
						intent = new Intent(getApplicationContext(), LoginActivity.class);
						intent.putExtra(LoginActivity.WARNING_MSG, R.string.msg_password_error);
					}
					else
					{
						int msg = new Login().execute(username, password).get();
						if (msg == R.string.msg_already_online || msg == R.string.msg_login_success) {
							intent = new Intent(getApplicationContext(), LogoutActivity.class);
						} else if (msg == R.string.msg_other_online) {
							intent = new Intent(getApplicationContext(), ForceLogoutActivity.class);
						} else {
							intent = new Intent(getApplicationContext(), LoginActivity.class);
							intent.putExtra(LoginActivity.WARNING_MSG, msg);
						}
					}
					startActivity(intent);
				} catch (Exception e) {
					// TODO
				}
			}
			else if (operation == MainActivity.LOGOUT) 
			{
				try {
					int msg = new Logout().execute().get();
					if (msg == R.string.msg_logout_fail) {
						intent = new Intent(getApplicationContext(), LogoutActivity.class);
					} else {
						intent = new Intent(getApplicationContext(), LoginActivity.class);
						intent.putExtra(LoginActivity.WARNING_MSG, msg);
					}
					startActivity(intent);
				} catch (Exception e) {
					// TODO
				}
			} 
			else if (operation == MainActivity.FORCELOGOUT) 
			{
				try {
					int msg = new ForceLogout().execute(username, password).get();
					if (msg == R.string.msg_force_logout_fail) {
						intent = new Intent(getApplicationContext(), ForceLogout.class);
					} else {
						intent = new Intent(getApplicationContext(), LoginActivity.class);
						intent.putExtra(LoginActivity.WARNING_MSG, msg);
					}
					startActivity(intent);
				} catch (Exception e) {
					// TODO
				}
			}
		}
	};
	
	/**
	 * 延迟调用核心代码
	 * @param delayMillis
	 */
	private void delayedCall(int delayMillis) {
		mShowHandler.removeCallbacks(mShowRunnable);
		mShowHandler.postDelayed(mShowRunnable, delayMillis);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// 延迟调用核心代码
		delayedCall(DEFAULT_DELAY_MILLIS);
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
	
	/**
	 * 登陆
	 * @param username, password
	 */
	private class Login extends AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... params) {
			return MainActivity.client.Login(params[0], params[1]);
		}
	}
	
	/**
	 * 下线
	 */
	private class Logout extends AsyncTask<Void, Void, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			return MainActivity.client.Logout();
		}
	}
	
	/**
	 * 强制下线
	 */
	private class ForceLogout extends AsyncTask<String , Void, Integer> {
		@Override
		protected Integer doInBackground(String... params) {
			return MainActivity.client.ForceLogout(params[0], params[1]);
		}
	}

}
