package cn.nju.cs.seg.brasclient.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class Content {
	public static final String ONLINE_CODE = "3010101";
	
	private String request_url;
	private String request_time;
	/**
	 * 3010104 : 数据库链接失败
	 * 3010105 : 未获取到在线信息
	 * 3010101 : 获取信息成功
	 */
	private String reply_code;
	private String reply_msg;
	private Results results;
	
	/**
	 * 构造函数
	 * 有JSONObject构造Content
	 * @param content
	 */
	public Content(JSONObject content)
	{
		try {
			if (content != null && content.getString("reply_code") != null) {
				this.request_url = content.getString("request_url");
				this.request_time = content.getString("request_time");
				this.reply_code = content.getString("reply_code");
				this.reply_msg = content.getString("reply_msg");
				JSONObject results = content.getJSONObject("results");
				this.results = new Results(results);
			} else {
				this.request_url = "";
				this.request_time = "";
				this.reply_code = "";
				this.reply_msg = "";
				this.results = null;
			}
		}
		catch (JSONException e) {
			// 
		}
	}
	
	/**
	 * 无参构造函数
	 */
	public Content()
	{
		this.request_url = "";
		this.request_time = "";
		this.reply_code = "";
		this.reply_msg = "";
		this.results = new Results();
	}
	
	/**
	 * 判断是否为空
	 * @return
	 */
	public boolean isEmpty() {
		if (this.results == null) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getRequest_url() {
		return request_url;
	}
	public void setRequest_url(String request_url) {
		this.request_url = request_url;
	}
	public String getRequest_time() {
		return request_time;
	}
	public void setRequest_time(String request_time) {
		this.request_time = request_time;
	}
	public String getReply_code() {
		return reply_code;
	}
	public void setReply_code(String reply_code) {
		this.reply_code = reply_code;
	}
	public String getReply_msg() {
		return reply_msg;
	}
	public void setReply_msg(String reply_msg) {
		this.reply_msg = reply_msg;
	}
	public Results getResults() {
		return results;
	}
	public void setResults(Results results) {
		this.results = results;
	}
}
