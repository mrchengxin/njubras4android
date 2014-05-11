package cn.nju.cs.seg.brasclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import cn.nju.cs.seg.brasclient.bean.Content;

public class BrasClient {
	/**
	 * 获取在线用户信息
	 * 当前在线：online不为null
	 * 异地在线：online不为null
	 * 不   在  线：online为空
	 * @param username
	 * @param password
	 * @return
	 */
	public Content GetOnline(String username, String password)
	{
		try {
			URL url = new URL("http://p.nju.edu.cn/proxy/onlinelist.php");
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setInstanceFollowRedirects(true);
			
			DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			String content = "username=" + username + "&password=" + password;
			dataOutputStream.writeBytes(content);
			dataOutputStream.flush();
			dataOutputStream.close();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			String line;
			StringBuffer buffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
	            buffer.append(line);
	        }
			reader.close();
			httpURLConnection.disconnect();
			
			return new Content(new JSONObject(buffer.toString()));
			
		} catch (MalformedURLException e) {
			// Ignore
		} catch (IOException e) {
			// Ignore
		} catch (JSONException e) {
			// Ignore
		}
		
		return null;
	}
	
	/**
	 * 获取当前用户的信息
	 * 已登录：userinfo不为null
	 * 不在线：userinfo为null
	 * 异     地：userinfo为null
	 */
	public Content GetUserinfo()
	{
		try {
			URL url = new URL("http://p.nju.edu.cn/proxy/userinfo.php");
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setInstanceFollowRedirects(true);			
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			String line;
			StringBuffer buffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
	            buffer.append(line);
	        }
			reader.close();
			httpURLConnection.disconnect();
			
			return new Content(new JSONObject(buffer.toString()));
			
		} catch (MalformedURLException e) {
			// Ignore
		} catch (IOException e) {
			// Ignore
		} catch (JSONException e) {
			// Ignore
		}
		
		return null;
	}
	
	/**
	 * 检查账户状态
	 * 在线、不在线、异地在线、未连接网络
	 * @return
	 */
	public int CheckOnline(String username, String password)
	{
		Content content = GetOnline(username, password);
		if(content != null)
		{
			if(content.getOnline() != null)
			{
				content = GetUserinfo();
				if(content != null)
				{
					if(content.getUserinfo() != null)
					{
						return R.string.msg_online_state;
					}
					else
					{
						return R.string.msg_other_online;
					}
				}
				else
				{
					return R.string.msg_check_network;
				}
			}
			else
			{
				return R.string.msg_no_online;
			}
		}
		else
		{
			return R.string.msg_check_network;
		}
	}
	
	/**
	 * 登陆
	 * @param username
	 * @param password
	 * @return
	 */
	public int Login(String username, String password)
	{
		Content content = GetUserinfo();
		if(content != null)
		{
			if(content.getUserinfo() != null)
			{
				return R.string.msg_already_online;
			}
			else
			{
				content = GetOnline(username, password);
				if(content.getOnline() != null)
				{
					return R.string.msg_other_online;
				}
				else
				{
					try {
						URL url = new URL("http://p.nju.edu.cn/portal/portal_io.do");
						HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
						httpURLConnection.setDoOutput(true);
						httpURLConnection.setDoInput(true);
						httpURLConnection.setUseCaches(false);
						httpURLConnection.setRequestMethod("POST");
						httpURLConnection.setInstanceFollowRedirects(true);
						
						DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
						String data = "action=login&username=" + username + "&password=" + password;
						dataOutputStream.writeBytes(data);
						dataOutputStream.flush();
						dataOutputStream.close();
						
						BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
						String line;
						StringBuffer buffer = new StringBuffer();
						while ((line = reader.readLine()) != null) {
				            buffer.append(line);
				        }
						reader.close();
						httpURLConnection.disconnect();
						
						content = new Content(new JSONObject(buffer.toString()));
						if(content != null)
						{
							System.out.println(content.getReply_code()+",  "+content.getReply_message()+",  "+content.getReply_msg());
							if(content.getReply_code() == Content.REPLY_CODE_LOGIN_SUCCESS)
								return R.string.msg_login_success;
							if(content.getReply_code() == Content.REPLY_CODE_LOGIN_FAIL_WRONG_USERNAME)
								return R.string.msg_username_error;
							if(content.getReply_code() == Content.REPLY_CODE_LOGIN_FAIL_WRONG_PASSWORD)
								return R.string.msg_password_error;
						}
					} catch (MalformedURLException e) {
						// Ignore
					} catch (IOException e) {
						// Ignore
					} catch (JSONException e) {
						// Ignore
					}
				}
			}
		}
		else
		{
			return R.string.msg_check_network;
		}
		
		return R.string.msg_login_fail;
	}
	
	/**
	 * 下线
	 * @return
	 */
	public int Logout()
	{
		Content content = GetUserinfo();
		if(content != null)
		{
			if(content.getUserinfo() != null)
			{
				try {
					URL url = new URL("http://p.nju.edu.cn/portal/portal_io.do");
					HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
					httpURLConnection.setDoOutput(true);
					httpURLConnection.setDoInput(true);
					httpURLConnection.setUseCaches(false);
					httpURLConnection.setRequestMethod("POST");
					httpURLConnection.setInstanceFollowRedirects(true);
					
					DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
					String data = "action=logout";
					dataOutputStream.writeBytes(data);
					dataOutputStream.flush();
					dataOutputStream.close();
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
					String line;
					StringBuffer buffer = new StringBuffer();
					while ((line = reader.readLine()) != null) {
			            buffer.append(line);
			        }
					reader.close();
					httpURLConnection.disconnect();
					
					content = new Content(new JSONObject(buffer.toString()));
					if(content != null)
					{
						if(content.getReply_code() == Content.REPLY_CODE_LOGOUT_SUCCESS)
							return R.string.msg_logout_success;
						else
							return R.string.msg_logout_fail;
					}
				} catch (MalformedURLException e) {
					// Ignore
				} catch (IOException e) {
					// Ignore
				} catch (JSONException e) {
					// Ignore
				}
			}
			else
			{
				return R.string.msg_already_offline;
			}
		}
		else
		{
			return R.string.msg_check_network;
		}
		
		return R.string.msg_logout_fail;
	}
	
	/**
	 * 强制下线
	 * @param username
	 * @param password
	 * @return
	 */
	public int ForceLogout(String username, String password)
	{
		Content content = GetOnline(username, password);
		if(content != null)
		{
			if(content.getOnline() != null)
			{
				try {
					URL url = new URL("http://p.nju.edu.cn/proxy/disconnect.php");
					HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
					httpURLConnection.setDoOutput(true);
					httpURLConnection.setDoInput(true);
					httpURLConnection.setUseCaches(false);
					httpURLConnection.setRequestMethod("POST");
					httpURLConnection.setInstanceFollowRedirects(true);
					
					DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
					String data = "username=" + username + "&password=" + password + "&acctsessionid=" + content.getOnline().getAcctsessionid();
					dataOutputStream.writeBytes(data);
					dataOutputStream.flush();
					dataOutputStream.close();
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
					String line;
					StringBuffer buffer = new StringBuffer();
					while ((line = reader.readLine()) != null) {
			            buffer.append(line);
			        }
					reader.close();
					httpURLConnection.disconnect();
					
					content = new Content(new JSONObject(buffer.toString()));
					if(content != null)
					{
						if(content.getReply_code() == Content.REPLY_CODE_DISCONNECT_SUCCESS)
							return R.string.msg_force_logout_success;
						if(content.getReply_code() == Content.REPLY_CODE_DISCONNECT_FAIL)
							return R.string.msg_force_logout_fail;
					}
				} catch (MalformedURLException e) {
					// Ignore
				} catch (IOException e) {
					// Ignore
				} catch (JSONException e) {
					// Ignore
				}
			}
			else
			{
				return R.string.msg_no_online;
			}
		}
		else
		{
			return R.string.msg_check_network;
		}
		
		return R.string.msg_force_logout_fail;
	}
}
