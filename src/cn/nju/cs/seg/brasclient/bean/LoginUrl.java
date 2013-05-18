package cn.nju.cs.seg.brasclient.bean;

public class LoginUrl {
	
	private final String base_url = "http://p.nju.edu.cn/portal.do?action=login";
	
	private String username;
	private String password;
	private String code = null;
	
	public LoginUrl(String username, String password, String code)
	{
		this.username = username;
		this.password = password;
		this.code = code;
	}
	
	public LoginUrl(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
	/**
	 * 获取登陆链接
	 * @return
	 */
	public String getUrl()
	{
		if (this.code == null)
		{
			return base_url + "&username=" + this.username + "&password=" + this.password;
		}
		else
		{
			return base_url + "&username=" + this.username + "&password=" + this.password + "&code=" + this.code;
		}
	}
	
	@Override
	public String toString()
	{
		if (this.code == null)
		{
			return base_url + "&username=" + this.username + "&password=" + this.password;
		}
		else
		{
			return base_url + "&username=" + this.username + "&password=" + this.password + "&code=" + this.code;
		}
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
