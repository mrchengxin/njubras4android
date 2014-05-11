package cn.nju.cs.seg.brasclient.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class Online {
	private String acctsessionid;
	private String area_name;
	private long acctstarttime;
	private long user_ip;
	private int area_type;
	private long src_ip;
	private String mac;
	
	/**
	 * 无参构造函数
	 */
	public Online()
	{
		// 默认所有参数均为null
	}
	
	public Online(JSONObject online)
	{
		try {
			if(!online.isNull("acctsessionid"))
				this.acctsessionid = online.getString("acctsessionid");
			if(!online.isNull("area_name"))
				this.area_name = online.getString("area_name");
			if(!online.isNull("acctstarttime"))
				this.acctstarttime = online.getLong("acctstarttime");
			if(!online.isNull("user_ip"))
				this.user_ip = online.getLong("user_ip");
			if(!online.isNull("area_type"))
				this.area_type = online.getInt("area_type");
			if(!online.isNull("src_ip"))
				this.src_ip = online.getLong("src_ip");
			if(!online.isNull("mac"))
				this.mac = online.getString("mac");
		} catch (JSONException e) {
			// Ignore
		}
	}	
	
	public String getAcctsessionid() {
		return acctsessionid;
	}
	public void setAcctsessionid(String acctsessionid) {
		this.acctsessionid = acctsessionid;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	public long getAcctstarttime() {
		return acctstarttime;
	}
	public void setAcctstarttime(long acctstarttime) {
		this.acctstarttime = acctstarttime;
	}
	public long getUser_ip() {
		return user_ip;
	}
	public void setUser_ip(long user_ip) {
		this.user_ip = user_ip;
	}
	public int getArea_type() {
		return area_type;
	}
	public void setArea_type(int area_type) {
		this.area_type = area_type;
	}
	public long getSrc_ip() {
		return src_ip;
	}
	public void setSrc_ip(long src_ip) {
		this.src_ip = src_ip;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
}
