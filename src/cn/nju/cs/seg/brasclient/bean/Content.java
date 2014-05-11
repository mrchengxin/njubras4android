package cn.nju.cs.seg.brasclient.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class Content {
	public static final int REPLY_CODE_LOGIN_SUCCESS = 101;
	public static final int REPLY_CODE_LOGIN_FAIL_OTHER_ONLINE = 102;
	public static final int REPLY_CODE_LOGIN_FAIL_WRONG_PASSWORD = 103;
	public static final int REPLY_CODE_LOGIN_FAIL_WRONG_USERNAME = 103;
	public static final int REPLY_CODE_LOGOUT_SUCCESS = 201;
	public static final int REPLY_CODE_INFO_ONLINE = 301;
	public static final int REPLY_CODE_INFO_OFFLINE = 302;
	public static final int REPLY_CODE_USERINFO_ONLINE = 401;
	public static final int REPLY_CODE_USERINFO_OFFLINE = 402;
	public static final int REPLY_CODE_ONLINELIST_ONLINE = 501;
	public static final int REPLY_CODE_ONLINELIST_OFFLINE = 502;
	public static final int REPLY_CODE_DISCONNECT_SUCCESS = 601;
	public static final int REPLY_CODE_DISCONNECT_FAIL = 602;

	private int reply_code = 0;
	private String reply_msg;
	private String reply_message;
	private Userinfo userinfo;
	private Online online;
	
	/**
	 * 无参构造函数
	 */
	public Content()
	{
		this.reply_code = 0;
		this.reply_msg = "";
		this.reply_message = "";
		this.userinfo = null;
		this.online = null;
	}
	
	/**
	 * 构造函数
	 * 有JSONObject构造Content
	 * @param content
	 */
	public Content(JSONObject content)
	{
		try {
			if (content != null) {
				if(!content.isNull("reply_code"))
					this.reply_code = content.getInt("reply_code");
				if(!content.isNull("reply_msg"))
					this.reply_msg = content.getString("reply_msg");
				if(!content.isNull("reply_message"))
					this.reply_message = content.getString("reply_message");
				if(!content.isNull("userinfo"))
					this.userinfo = new Userinfo(content.getJSONObject("userinfo"));
				if(!content.isNull("online"))
					this.online = new Online(content.getJSONArray("online").getJSONObject(0));
			} else {
				this.reply_code = 0;
				this.reply_msg = "";
				this.reply_message = "";
				this.userinfo = null;
				this.online = null;
			}
		}
		catch (JSONException e) {
			// Ignore
		}
	}

	public int getReply_code() {
		return reply_code;
	}
	public void setReply_code(int reply_code) {
		this.reply_code = reply_code;
	}
	public String getReply_msg() {
		return reply_msg;
	}
	public void setReply_msg(String reply_msg) {
		this.reply_msg = reply_msg;
	}
	public String getReply_message() {
		return reply_message;
	}
	public void setReply_message(String reply_message) {
		this.reply_message = reply_message;
	}
	public Userinfo getUserinfo() {
		return userinfo;
	}
	public void setUserinfo(Userinfo userinfo) {
		this.userinfo = userinfo;
	}
	public Online getOnline() {
		return online;
	}
	public void setOnline(Online online) {
		this.online = online;
	}
}
