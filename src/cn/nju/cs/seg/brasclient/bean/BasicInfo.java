package cn.nju.cs.seg.brasclient.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BasicInfo {
	private long id;
	private String username;
	private String service_name;
	private long acctstarttime;
	private String area_name;
	private String user_ip;
	private String mac;
	private String fullname;
	private String id_num;
	private double payamount;
	private String depart;
	private String total_time;
	private String client_ip;
	private String longin_time;
	private int total;
	private JSONArray rows;
	
	/**
	 * 构造函数
	 * 从JSONObject构造results
	 * @param results
	 */
	public BasicInfo(JSONObject results)
	{
		try {
			if(!results.isNull("id"))
				this.id = results.getLong("id");
			if(!results.isNull("username"))
				this.username = results.getString("username");
			if(!results.isNull("service_name"))
				this.service_name = results.getString("service_name");
			if(!results.isNull("acctstarttime"))
				this.acctstarttime = results.getLong("acctstarttime");
			if(!results.isNull("area_name"))
				this.area_name = results.getString("area_name");
			if(!results.isNull("user_ip"))
				this.user_ip = results.getString("user_ip");
			if(!results.isNull("mac"))
				this.mac = results.getString("mac");
			if(!results.isNull("fullname"))
				this.fullname = results.getString("fullname");
			if(!results.isNull("id_num"))
				this.id_num = results.getString("id_num");
			if(!results.isNull("payamount"))
				this.payamount = results.getDouble("payamount");
			if(!results.isNull("depart"))
				this.depart = results.getString("depart");
			if(!results.isNull("total_time"))
				this.total_time = results.getString("total_time");
			if(!results.isNull("client_ip"))
				this.client_ip = results.getString("client_ip");
			if(!results.isNull("longin_time"))
				this.longin_time = results.getString("longin_time");
			if(!results.isNull("total"))
				this.total = results.getInt("total");
			if(!results.isNull("rows"))
				this.rows = results.getJSONArray("rows");
				
		} catch (JSONException e) {
			//
		}
	}
	
	/**
	 * 无参构造函数
	 */
	public BasicInfo()
	{
		// 默认所有参数均为null
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public double getPayamount() {
		return payamount;
	}
	public void setPayamount(double payamount) {
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
	public String getClient_ip() {
		return client_ip;
	}
	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}
	public String getLongin_time() {
		return longin_time;
	}
	public void setLongin_time(String longin_time) {
		this.longin_time = longin_time;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public JSONArray getRows() {
		return rows;
	}
	public void setRows(JSONArray rows) {
		this.rows = rows;
	}
}
