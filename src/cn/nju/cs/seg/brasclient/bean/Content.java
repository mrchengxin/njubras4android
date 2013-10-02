package cn.nju.cs.seg.brasclient.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class Content {
	public static final int REPLY_CODE_P_SUCCESS_GET_CONTENT = 3010101;
	public static final int REPLY_CODE_P_FAIL_GET_CONTENT = 3010105;
	public static final int REPLY_CODE_P_FAIL_DATABASE = 3010104;
	public static final int REPLY_CODE_B_SUCCESS_LOGIN = 2010101;
	public static final int REPLY_CODE_B_SUCCESS_GET_CONTENT = 2030101;
	public static final int REPLY_CODE_B_NO_ONLINE = 2030102;
	
	private String request_url;
	private String request_time;
	/**
	 * 3010104 : 数据库链接失败		(p.nju.edu.cn)
	 * 3010105 : 未获取到在线信息	(p.nju.edu.cn)
	 * 3010101 : 获取信息成功		(p.nju.edu.cn)
	 * 2010101 : 登陆成功			(bras.nju.edu.cn)
	 * 2030101 : 获取信息成功		(bras.nju.edu.cn)
	 */
	private int reply_code = 0;
	private String reply_msg;
	private BasicInfo results;
	
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
				this.reply_code = content.getInt("reply_code");
				this.reply_msg = content.getString("reply_msg");
				if(content.getString("results") != null)
				{
					JSONObject results = content.getJSONObject("results");
					this.results = new BasicInfo(results);
				}
				else
				{
					this.results = null;
				}
			} else {
				this.request_url = "";
				this.request_time = "";
				this.reply_code = 0;
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
		this.reply_code = 0;
		this.reply_msg = "";
		this.results = new BasicInfo();
	}
	
	/**
	 * 判断是否为空
	 * @return
	 */
	public boolean isEmpty() {
		return (this.reply_code == 0);
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
	public BasicInfo getResults() {
		return results;
	}
	public void setResults(BasicInfo results) {
		this.results = results;
	}
}
