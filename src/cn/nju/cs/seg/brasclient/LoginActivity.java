package cn.nju.cs.seg.brasclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	public final static String WARNING_MSG = "cn.nju.cs.seg.brasclient.LoginActivity.WARNING_MSG";
	
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	
	private EditText username;
	private EditText password;

	@SuppressLint("CommitPrefEdits")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		username = (EditText)findViewById(R.id.edittext_username);
		password = (EditText)findViewById(R.id.edittext_password);
		
		Intent intent = getIntent();
		int warningMsg = intent.getIntExtra(LoginActivity.WARNING_MSG, 0);
		if (warningMsg == R.string.msg_username_error) {
			Toast.makeText(getApplicationContext(), "用户名错误", Toast.LENGTH_LONG).show();
			username.requestFocus();
		} else if (warningMsg == R.string.msg_password_error) {
			Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_LONG).show();
			password.requestFocus();
		} else if (warningMsg == R.string.msg_check_network || warningMsg == R.string.msg_login_fail) {
			Toast.makeText(getApplicationContext(), "请检查网络连接情况", Toast.LENGTH_LONG).show();
		} else if (warningMsg == R.string.msg_logout_success) {
			Toast.makeText(getApplicationContext(), "下线成功", Toast.LENGTH_LONG).show();
		} else if (warningMsg == R.string.msg_already_offline) {
			Toast.makeText(getApplicationContext(), "您不在线", Toast.LENGTH_LONG).show();
		} else if (warningMsg == R.string.msg_no_online) {
			Toast.makeText(getApplicationContext(), "没有在线用户", Toast.LENGTH_LONG).show();
		} else if (warningMsg == R.string.msg_force_logout_success) {
			Toast.makeText(getApplicationContext(), "强制下线成功", Toast.LENGTH_LONG).show();
		} else if (warningMsg == R.string.msg_code_error) {
			Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_LONG).show();
		}
		
		sp = this.getSharedPreferences("userinfo", MODE_PRIVATE);
		editor = sp.edit();
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
	    	Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			android.os.Process.killProcess(Process.myPid());
		}
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		String un = sp.getString("username", null);
		if (un != null) {
			username.setText(un);
		}
		String pw = sp.getString("password", null);
		if (pw != null) {
			password.setText(pw);
		}
	}
	
	@Override
	public void onBackPressed() {
		// 退出进程
    	Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		android.os.Process.killProcess(Process.myPid());
		return;
	};

	/**
	 * 准备登陆
	 * @param view
	 */
	public void login(View view) {
		// 更新最新的用户名和密码到全局变量
		MainActivity.username = username.getText().toString();
		MainActivity.password = password.getText().toString();
		
		// 记住最新的用户名和密码
		editor.putString("username", MainActivity.username);
		editor.putString("password", MainActivity.password);
		editor.commit();
		
		// 准备登陆
		Intent intent = new Intent(this, TransitionActivity.class);
		intent.putExtra(MainActivity.OPERATION, MainActivity.LOGIN);
		intent.putExtra(MainActivity.USERNAME, MainActivity.username);
		intent.putExtra(MainActivity.PASSWORD, MainActivity.password);
		startActivity(intent);
	}
}
