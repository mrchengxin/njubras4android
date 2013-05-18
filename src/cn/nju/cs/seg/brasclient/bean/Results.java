package cn.nju.cs.seg.brasclient.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class Results {
	private String username;
	private String service_name;
	private String acctstarttime;
	private String area_name;
	private String user_ip;
	private String mac;
	private String fullname;
	private String id_num;
	private String payamount;
	private String depart;
	private String total_time;
	
	/**
	 * 构造函数
	 * 从JSONObject构造results
	 * @param results
	 */
	public Results(JSONObject results)
	{
		try {
			this.username = results.getString("username");
			this.service_name = results.getString("service_name");
			this.acctstarttime = results.getString("acctstarttime");
			this.area_name = results.getString("area_name");
			this.user_ip = results.getString("user_ip");
			this.mac = results.getString("mac");
			this.fullname = results.getString("fullname");
			this.id_num = results.getString("id_num");
			this.payamount = results.getString("payamount");
			this.depart = results.getString("depart");
			this.total_time = results.getString("total_time");
		} catch (JSONException e) {
			//
		}
	}
	
	/**
	 * 无参构造函数
	 */
	public Results()
	{
		this.username = "";
		this.service_name = "";
		this.acctstarttime = "";
		this.area_name = "";
		this.user_ip = "";
		this.mac = "";
		this.fullname = "";
		this.id_num = "";
		this.payamount = "";
		this.depart = "";
		this.total_time = "";
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
	public String getAcctstarttime() {
		return acctstarttime;
	}
	public void setAcctstarttime(String acctstarttime) {
		this.acctstarttime = acctstarttime;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	public String getUser_ip() {
		return user_ip;
	}
	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
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
	public String getId_num() {
		return id_num;
	}
	public void setId_num(String id_num) {
		this.id_num = id_num;
	}
	public String getPayamount() {
		return payamount;
	}
	public void setPayamount(String payamount) {
		this.payamount = payamount;
	}
	public String getDepart() {
		return depart;
	}
	public void setDepart(String depart) {
		this.depart = depart;
	}
	public String getTotal_time() {
		return total_time;
	}
	public void setTotal_time(String total_time) {
		this.total_time = total_time;
	}
}
