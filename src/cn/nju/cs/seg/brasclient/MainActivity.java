package cn.nju.cs.seg.brasclient;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import cn.nju.cs.seg.brasclient.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MainActivity extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 0;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	
	private AnimationDrawable airplaneAnimation;
	
	private SharedPreferences sharedPreferences;
	
	/**
	 * Activity之间传递消息的key
	 */
	public final static String OPERATION = "cn.nju.cs.seg.brasclient.OPERATION";
	public final static String USERNAME = "cn.nju.cs.seg.brasclient.USERNAME";
	public final static String PASSWORD = "cn.nju.cs.seg.brasclient.PASSWORD";
	/**
	 * Activity之间传递不同消息的具体含义
	 */
	public final static int LOGIN = 1;
	public final static int LOGOUT = 2;
	public final static int FORCELOGOUT = 3;
	/**
	 * 管理bras操作的客户端类
	 */
	public final static BrasClient client = new BrasClient();
	/**
	 * 用户名和密码
	 */
	public static String username;
	public static String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ImageView airplaneImage = (ImageView)findViewById(R.id.gif_airplane);
		airplaneImage.setBackgroundResource(R.drawable.airplane);
		airplaneAnimation = (AnimationDrawable) airplaneImage.getDrawable();
		airplaneAnimation.start();
		
		sharedPreferences = this.getSharedPreferences("userinfo", MODE_PRIVATE);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(0);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// 延迟调用检查当前用户的在线状况
		delayedCheckOnline(AUTO_HIDE_DELAY_MILLIS);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	// 检查是否在线的handler
	Handler checkHandler = new Handler();
	// 检查当前用户的在线情况
	Runnable checkRunnable = new Runnable() {
		@Override
		public void run() {
			MainActivity.username = sharedPreferences.getString("username", "");
			MainActivity.password = sharedPreferences.getString("password", "");
			Intent intent = null;
			if (!MainActivity.username.equals("") && !MainActivity.password.equals("")) {
				try {
					int msg = new CheckOnline().execute(MainActivity.username, MainActivity.password).get();
					if (msg == R.string.msg_online_state) {
						intent = new Intent(getApplicationContext(), LogoutActivity.class);
					} else if (msg == R.string.msg_other_online) {
						intent = new Intent(getApplicationContext(), ForceLogoutActivity.class);
					} else if (msg == R.string.msg_check_network) {
						intent = new Intent(getApplicationContext(), LoginActivity.class);
						intent.putExtra(LoginActivity.WARNING_MSG, msg);
					} else {
						intent = new Intent(getApplicationContext(), LoginActivity.class);
					}
					startActivity(intent);
				} catch (Exception e) {
					// TODO
				}
			} else {
				intent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
			}
		}
	};
	// 延迟调用检查是否在线
	private void delayedCheckOnline(int delayMillis) {
		checkHandler.removeCallbacks(checkRunnable);
		checkHandler.postDelayed(checkRunnable, delayMillis);
	}
	// 检查当前用户的在线情况
	private class CheckOnline extends AsyncTask<String , Void, Integer> {
		@Override
		protected Integer doInBackground(String... params) {
			return MainActivity.client.CheckOnline(params[0], params[1]);
		}
	}
}
