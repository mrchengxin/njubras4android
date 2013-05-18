package cn.nju.cs.seg.brasclient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import cn.nju.cs.seg.brasclient.bean.Content;
import cn.nju.cs.seg.brasclient.bean.LoginUrl;
import cn.nju.cs.seg.brasclient.util.JSONUtil;
import cn.nju.cs.seg.brasclient.util.OCRUtil;

public class BrasClient {
	
	/**
	 * 获取当前用户的content
	 * @return
	 */
	public Content getContent()
	{
		Content content = new Content(JSONUtil.getJSON("http://p.nju.edu.cn/content/proxy.php"));
		return content;
	}
	
	/**
	 * 获取在线用户的content
	 * @param username
	 * @param password
	 * @return
	 */
	public Content getContent(String username, String password)
	{
		try {
			// 获取Cookie中的selfservice
			Connection ssConn = Jsoup.connect("http://bras.nju.edu.cn/selfservice/auth.html");
			ssConn.get();
			String selfservice = ssConn.response().cookie("selfservice");
			
			// 获取与当前selfservice对应的验证码
			URL imgUrl = new URL("http://bras.nju.edu.cn/selfservice/img.html");
			HttpURLConnection imgConn = (HttpURLConnection)imgUrl.openConnection();
			imgConn.setRequestMethod("GET");
			imgConn.addRequestProperty("Cookie", "selfservice="+selfservice);
			String code = OCRUtil.getAllOcr(imgConn.getInputStream());
			
			// POST登陆请求
			Jsoup.connect("http://bras.nju.edu.cn/selfservice/auth.html")
					.data("action", "login")
					.data("login_username", username)
					.data("login_password", password)
					.data("code", code)
					.cookie("selfservice", selfservice)
					.post();
			
			// 获取强制下线请求链接
			Document offlineDoc = Jsoup.connect("http://bras.nju.edu.cn/selfservice/?action=online")
					.cookie("selfservice", selfservice)
					.get();
			String offlineUrl = offlineDoc.getElementsContainingText("下线").attr("href");
			if (!offlineUrl.equals(""))
			{
				String id = offlineUrl.split("id=")[1];
				Element tr = offlineDoc.getElementById("line"+id);
				Content content = new Content();
				content.getResults().setUsername(tr.child(0).html());
				content.getResults().setArea_name("已在另一地点登陆");
				content.getResults().setAcctstarttime(tr.child(2).html());
				content.getResults().setUser_ip(tr.child(7).html());
				
				return content;
			}
			
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		}
		return null;
	}
	
	/**
	 * 检查本机是否在线
	 * @return
	 */
	public int CheckOnline(String username, String password)
	{
		Content content = new Content(JSONUtil.getJSON("http://p.nju.edu.cn/content/proxy.php"));
		if (!content.isEmpty())
		{
			if (content.getReply_code().equals(Content.ONLINE_CODE)) 
			{
				return R.string.msg_online_state;
			}
			else 
			{
				try {
					// 获取Cookie中的selfservice
					Connection ssConn = Jsoup.connect("http://bras.nju.edu.cn/selfservice/auth.html");
					ssConn.get();
					String selfservice = ssConn.response().cookie("selfservice");
					
					// 获取与当前selfservice对应的验证码
					URL imgUrl = new URL("http://bras.nju.edu.cn/selfservice/img.html");
					HttpURLConnection imgConn = (HttpURLConnection)imgUrl.openConnection();
					imgConn.setRequestMethod("GET");
					imgConn.addRequestProperty("Cookie", "selfservice="+selfservice);
					String code = OCRUtil.getAllOcr(imgConn.getInputStream());
					
					// POST登陆请求
					Jsoup.connect("http://bras.nju.edu.cn/selfservice/auth.html")
							.data("action", "login")
							.data("login_username", username)
							.data("login_password", password)
							.data("code", code)
							.cookie("selfservice", selfservice)
							.post();
					
					// 获取强制下线请求链接
					Document offlineDoc = Jsoup.connect("http://bras.nju.edu.cn/selfservice/?action=online")
							.cookie("selfservice", selfservice)
							.get();
					String offlineUrl = offlineDoc.getElementsContainingText("下线").attr("href");
					if (offlineUrl.equals(""))
					{
						return R.string.msg_no_online;
					}
					else
					{					
						return R.string.msg_other_online;
					}
					
				} catch (Exception e) {
					//e.printStackTrace();
					return R.string.msg_check_network;
				}
			}
		}
		return R.string.msg_check_network;
	}
	
	/**
	 * 登陆
	 * @param username
	 * @param password
	 */
	public int Login(String username, String password)
	{
		try {			
			Content content = new Content(JSONUtil.getJSON("http://p.nju.edu.cn/content/proxy.php"));
			if (!content.isEmpty())
			{
				if (content.getReply_code().equals(Content.ONLINE_CODE))
				{
					return R.string.msg_already_online;
				}
				else
				{
					Document doc = Jsoup.connect("http://p.nju.edu.cn/portal.do").get();
					LoginUrl loginUrl = new LoginUrl(username, password);
					if (doc.getElementById("imgverify") != null)
					{
						loginUrl.setCode(OCRUtil.getAllOcr("http://p.nju.edu.cn/img.html"));
					}
					
					// 通过get方式登陆
					doc = Jsoup.connect(loginUrl.getUrl()).get();
					if (doc.getElementById("userinfo") != null) 
					{
						return R.string.msg_login_success;
					} 
					else if (doc.html().contains("验证码错误")) 
					{
						return R.string.msg_code_error;
					} 
					else if (doc.html().contains("未发现此用户")) 
					{
						return R.string.msg_username_error;
					} 
					else if (doc.html().contains("输入的密码无效")) 
					{
						return R.string.msg_password_error;
					} 
					else if (doc.html().contains("您的登录数已达最大并发登录数"))
					{
						return R.string.msg_other_online;
					}
					else
					{
						return R.string.msg_check_network;
					}
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			return R.string.msg_login_fail;
		}
		return R.string.msg_login_fail;
	}
	
	/**
	 * 下线
	 * @return
	 */
	public int Logout()
	{
		try {
			Content content = new Content(JSONUtil.getJSON("http://p.nju.edu.cn/content/proxy.php"));
			if (!content.isEmpty())
			{
				if (content.getReply_code().equals(Content.ONLINE_CODE))
				{
					Document doc = Jsoup.connect("http://p.nju.edu.cn/portal.do?action=logout").get();
					
					if (doc.getElementById("save_info") != null) 
					{
						return R.string.msg_logout_success;
					} 
					else 
					{
						return R.string.msg_logout_fail;
					}
				}
				else
				{
					return R.string.msg_already_offline;
				}
			}
		} catch (IOException e) {
			//e.printStackTrace();
			return R.string.msg_logout_fail;
		}
		return R.string.msg_logout_fail;
	}
	
	/**
	 * 强制下线
	 */
	public int ForceLogout(String username, String password)
	{
		try {
			// 获取Cookie中的selfservice
			Connection ssConn = Jsoup.connect("http://bras.nju.edu.cn/selfservice/auth.html");
			ssConn.get();
			String selfservice = ssConn.response().cookie("selfservice");
			
			// 获取与当前selfservice对应的验证码
			URL imgUrl = new URL("http://bras.nju.edu.cn/selfservice/img.html");
			HttpURLConnection imgConn = (HttpURLConnection)imgUrl.openConnection();
			imgConn.setRequestMethod("GET");
			imgConn.addRequestProperty("Cookie", "selfservice="+selfservice);
			String code = OCRUtil.getAllOcr(imgConn.getInputStream());
			
			// POST登陆请求			
			Jsoup.connect("http://bras.nju.edu.cn/selfservice/auth.html")
					.data("action", "login")
					.data("login_username", username)
					.data("login_password", password)
					.data("code", code)
					.cookie("selfservice", selfservice)
					.post();
			
			// 获取强制下线请求链接
			Document offlineDoc = Jsoup.connect("http://bras.nju.edu.cn/selfservice/?action=online")
					.cookie("selfservice", selfservice)
					.get();
			String offlineUrl = offlineDoc.getElementsContainingText("下线").attr("href");
			if (offlineUrl.equals(""))
			{
				return R.string.msg_no_online;
			}
			else
			{
				// 强制下线
				Document doc = Jsoup.connect("http://bras.nju.edu.cn"+offlineUrl)
					.cookie("selfservice", selfservice)
					.post();
				
				if (doc.html().contains("下线成功"))
				{
					return R.string.msg_force_logout_success;
				}
				else
				{
					return R.string.msg_force_logout_fail;
				}
			}
			
		} catch (Exception e) {
			//e.printStackTrace();
			return R.string.msg_check_network;
		}
	}
}
