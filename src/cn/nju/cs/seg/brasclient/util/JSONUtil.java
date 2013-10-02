package cn.nju.cs.seg.brasclient.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {
	/**
	 * 根据http地址获取JSON
	 * @param httpUrl
	 * @return json | null
	 */
	public static JSONObject getJSON(String httpUrl)
	{
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setReadTimeout(10*1000);
			conn.setRequestMethod("GET");
			
			InputStreamReader in = new InputStreamReader(conn.getInputStream(), "UTF-8");
			BufferedReader reader = new BufferedReader(in);
			
			StringBuffer buff = new StringBuffer();
			String str;
			while((str = reader.readLine()) != null)
			{
				buff.append(str);
				buff.append("\n");
			}
			
			return new JSONObject(buff.toString());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 根据http地址获取JSON
	 * @param httpUrl
	 * @param cookie
	 * @return json | null
	 */
	public static JSONObject getJSONByPOST(String httpUrl, String username, String password)
	{
		try {
			URL url = new URL("http://bras.nju.edu.cn:8080/selfservice/login?username=" + username + "&password=" + password);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.connect();
			String cookie = conn.getHeaderField("Set-Cookie").split(";")[0];
			
			url = new URL(httpUrl);
			conn = (HttpURLConnection)url.openConnection();
			conn.setReadTimeout(10*1000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Cookie", cookie);
			
			InputStreamReader in = new InputStreamReader(conn.getInputStream(), "UTF-8");
			BufferedReader reader = new BufferedReader(in);
			
			StringBuffer buff = new StringBuffer();
			String str;
			while((str = reader.readLine()) != null)
			{
				buff.append(str);
				buff.append("\n");
			}
			
			return new JSONObject(buff.toString());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
