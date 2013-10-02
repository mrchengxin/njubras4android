package cn.nju.cs.seg.brasclient;

import java.io.IOException;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.nju.cs.seg.brasclient.bean.Content;
import cn.nju.cs.seg.brasclient.bean.LoginUrl;
import cn.nju.cs.seg.brasclient.bean.BasicInfo;
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
			Content content = new Content(JSONUtil.getJSONByPOST("http://bras.nju.edu.cn:8080/selfservice/online", username, password));
			if(content.getResults().getTotal() > 0)
			{
				BasicInfo onlineInfo = new BasicInfo((JSONObject) content.getResults().getRows().get(0));
				content.setResults(onlineInfo);
				
				JSONObject userinfo = JSONUtil.getJSONByPOST("http://bras.nju.edu.cn:8080/selfservice/userinfo", username, password);
				double payamount = Double.parseDouble(userinfo.toString().split("\"payamount\":")[1].split(",")[0]);
				content.getResults().setPayamount(payamount);
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
		// 检查是否自己在线
		Content content = new Content(JSONUtil.getJSON("http://p.nju.edu.cn/content/proxy.php"));
		if(!content.isEmpty())
		{
			if(content.getReply_code() == Content.REPLY_CODE_P_SUCCESS_GET_CONTENT)
			{
				return R.string.msg_online_state;
			}
		}
		
		// 检查是否异地在线
		content = new Content(JSONUtil.getJSONByPOST("http://bras.nju.edu.cn:8080/selfservice/online", username, password));
		if(!content.isEmpty())
		{
			if(content.getReply_code() == Content.REPLY_CODE_B_NO_ONLINE)
			{
				return R.string.msg_no_online;
			}
			else if(content.getReply_code() == Content.REPLY_CODE_B_SUCCESS_GET_CONTENT)
			{
				if(content.getResults().getTotal() > 0)
				{
					return R.string.msg_other_online;
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
				if (content.getReply_code() == Content.REPLY_CODE_P_SUCCESS_GET_CONTENT)
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
					else if (doc.html().contains("其他地点在线"))
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
				if (content.getReply_code() == Content.REPLY_CODE_P_SUCCESS_GET_CONTENT)
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
			Content content = new Content(JSONUtil.getJSONByPOST("http://bras.nju.edu.cn:8080/selfservice/online", username, password));
			BasicInfo onlineInfo = new BasicInfo((JSONObject) content.getResults().getRows().get(0));
			long id = onlineInfo.getId();
			content = new Content(JSONUtil.getJSONByPOST("http://bras.nju.edu.cn:8080/selfservice/disconnect?id=" + id, username, password));
			if(content.getReply_code() == Content.REPLY_CODE_B_SUCCESS_GET_CONTENT)
			{
				return R.string.msg_force_logout_success;
			}
			else
			{
				return R.string.msg_force_logout_fail;
			}
			
		} catch (Exception e) {
			//e.printStackTrace();
			return R.string.msg_check_network;
		}
	}
}
