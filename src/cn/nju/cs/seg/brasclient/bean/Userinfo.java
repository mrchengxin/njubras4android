package cn.nju.cs.seg.brasclient.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class Userinfo {
	private String username;
	private String service_name;
	private long acctstarttime;
	private String area_name;
	private String mac;
	private String fullname;
	private double payamount;
	private long userip;
	private String user_ip;
	private String id_num;
	private String depart;
	private long total_time;
	
	/**
	 * 无参构造函数
	 */
	public Userinfo()
	{
		// 默认所有参数均为null
	}
	
	public Userinfo(JSONObject userinfo)
	{
		try {
			if(!userinfo.isNull("username"))
				this.username = userinfo.getString("username");
			if(!userinfo.isNull("service_name"))
				this.service_name = userinfo.getString("service_name");
			if(!userinfo.isNull("acctstarttime"))
				this.acctstarttime = userinfo.getLong("acctstarttime");
			if(!userinfo.isNull("area_name"))
				this.area_name = userinfo.getString("area_name");
			if(!userinfo.isNull("mac"))
				this.mac = userinfo.getString("mac");
			if(!userinfo.isNull("fullname"))
				this.fullname = userinfo.getString("fullname");
			if(!userinfo.isNull("payamount"))
				this.payamount = userinfo.getDouble("payamount");
			if(!userinfo.isNull("userip"))
				this.userip = userinfo.getLong("userip");
			if(!userinfo.isNull("user_ip"))
				this.user_ip = userinfo.getString("user_ip");
			if(!userinfo.isNull("id_num"))
				this.id_num = userinfo.getString("id_num");
			if(!userinfo.isNull("depart"))
				this.depart = userinfo.getString("depart");
			if(!userinfo.isNull("total_time"))
				this.total_time = userinfo.getLong("total_time");
		} catch (JSONException e) {
			// Ignore
		}
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getService_name() {
		return service_name;
	}
	public void setService_name(String service_name) {
		this.service_name = service_name;
	}
	public long getAcctstarttime() {
		return acctstarttime;
	}
	public void setAcctstarttime(long acctstarttime) {
		this.acctstarttime = acctstarttime;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public double getPayamount() {
		return payamount;
	}
	public void setPayamount(double payamount) {
		this.payamount = payamount;
	}
	public long getUserip() {
		return userip;
	}
	public void setUserip(long userip) {
		this.userip = userip;
	}
	public String getUser_ip() {
		return user_ip;
	}
	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}
	public String getId_num() {
		return id_num;
	}
	public void setId_num(String id_num) {
		this.id_num = id_num;
	}
	public String getDepart() {
		return depart;
	}
	public void setDepart(String depart) {
		this.depart = depart;
	}
	public long getTotal_time() {
		return total_time;
	}
	public void setTotal_time(long total_time) {
		this.total_time = total_time;
	}
}
