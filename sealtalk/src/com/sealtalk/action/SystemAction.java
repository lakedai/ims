package com.sealtalk.action;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.sealtalk.common.BaseAction;
import com.sealtalk.common.Constants;
import com.sealtalk.common.Tips;
import com.sealtalk.model.SessionUser;
import com.sealtalk.model.TMember;
import com.sealtalk.service.MemberService;

/**
 * 系统相关
 * @since jdk1.7
 * @author hao_dy
 *
 */
public class SystemAction extends BaseAction {
	
	private static final long serialVersionUID = -3901445181785461508L;
	private static final String LOGIN_ERROR_MESSAGE = "loginErrorMsg";
	private static final Logger logger = Logger.getLogger(SystemAction.class);
	
	/**
	 * 跳转登陆页面
	 * @return
	 * @throws Exception
	 */
	public String login() throws IOException, ServletException
	{
		if (getSessionUser() == null) {
			return "loginPage";
		} else {
			return "loginSuccess";
		}
	}
	
	/**
	 * 登陆验证
	 * @return
	 * @throws Exception
	 */
	public String afterLogin() throws IOException, ServletException
	{
		System.out.println("account: " + account);
		System.out.println("userpwd: " + userpwd);
		
		if (account == null || "".equals(account)) {
			request.setAttribute(LOGIN_ERROR_MESSAGE, Tips.NULLUSER.getName());
			return "loginPage";
		}
		
		TMember member = memberService.searchSigleUser(account, userpwd);
		
		if(member == null) {
			request.setAttribute(LOGIN_ERROR_MESSAGE, Tips.ERRORUSERORPWD.getName());
			return "loginPage";
		}
		
		logger.debug("That logining account is " + account);
		
		SessionUser su = new SessionUser();
		
		if (member != null) {
			su.setAccount(member.getAccount());
			su.setFullname(member.getFullname());
		}
		
		//2.相关权限信息
		//code...
		
		setSessionUser(su);
		
		JSONObject text = new JSONObject();
		
		text.put("code", 1);
		text.put("text", "ok");
		
		returnToClient(text.toString());
		
		return "text";
	}
	
	/**
	 * @function 登出
	 * @return
	 * @throws Exception
	 */
	public String logOut() throws IOException, ServletException
	{
		request.getSession().removeAttribute(Constants.ATTRIBUTE_NAME_OF_SESSIONUSER);
		request.getSession().invalidate();
		return "loginPage";
	}
	
	/**
	 * @function 跳转修改密码页
	 * @return
	 * @throws Exception
	 */
	public String fogetPassword() throws ServletException {
		//request.getRequestDispatcher("/page/web/forgotpassword.jsp").forward(request, response);
		
		return "forgetpwd";
	}
	
	/**
	 * 中转短信平台
	 * @return
	 * @throws Exception
	 */
	public String requestText() throws IOException, ServletException {
		String phone = request.getParameter("phone");
		
		//中转代码
		//.....
		
		JSONObject text = new JSONObject();
		
		text.put("code", 1);
		text.put("text", Tips.SENDTEXTS.getName());
		
		returnToClient(text.toString());
		
		return "text";
	}
	
	/**
	 * @function 验证短信(暂时没有短信平台，模拟)
	 * @return
	 */
	public String testText() throws ServletException {
		String phone = request.getParameter("phone");
		String textCode = request.getParameter("textcode");
		
		JSONObject text = new JSONObject();
		
		if (textCode == null || "".equals(textCode)) {
			text.put("code", -1);
			text.put("text", Tips.NULLTEXTS.getName());
		} else if (!textCode.equals("9999")) {
			text.put("code", 0);
			text.put("text", Tips.ERRORTEXTS.getName());
		} else {
			text.put("code", 1);
			text.put("text", Tips.TRUETEXTS.getName());
		}
		
		returnToClient(text.toString());
		
		return "text";
	}
	
	/**
	 * @function:修改新密码
	 * @return
	 */
	public String newPassword() throws ServletException {
		String account = request.getParameter("account");
		String newPwd = request.getParameter("newpwd");
		String comparePwd = request.getParameter("comparepwd");
		
		System.out.println("account: " + account);
		System.out.println("newPwd: " + newPwd);
		System.out.println("comparePwd: " + comparePwd);
		
		if (account == null || "".equals(account)) {
			//request.setAttribute(LOGIN_ERROR_MESSAGE, Tips.NULLUSER.getName());
			JSONObject jo = new JSONObject();
			jo.put("code", "0");
			jo.put("text", Tips.NULLUSER.getName());
			returnToClient(jo.toString());
			return "textText";
		}
		
		if (!newPwd.equals(comparePwd)) {
			request.setAttribute(LOGIN_ERROR_MESSAGE, Tips.FALSECOMPAREPWD.getName());
			return "fogetpwd";
		}
		
		boolean updateState = memberService.updateUserPwd(account, newPwd);
		
		JSONObject text = new JSONObject();
		
		if (updateState == true) {
			text.put("code", "1");
			text.put("text", Tips.CHANGEPWDSUC.getName());
		} else {
			text.put("code", "0");
			text.put("text", Tips.CHANGEPWDFAIL.getName());
		}
		
		returnToClient(text.toString());
		
		return "text";
	}
	
	private MemberService memberService;
	
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}
	
	private String account;
	private String userpwd;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}
	
}
