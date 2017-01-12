package com.sealtalk.service.msg.impl;

import java.util.List;

import net.sf.json.JSONObject;

import com.sealtalk.dao.member.MemberDao;
import com.sealtalk.model.TMember;
import com.sealtalk.service.msg.UserServiceService;
import com.sealtalk.utils.PropertiesUtils;
import com.sealtalk.utils.RongCloudUtils;
import com.sealtalk.utils.StringUtils;

public class UserServiceServiceImpl implements UserServiceService {

	@Override
	public String getToken(String id) {
		JSONObject jo = new JSONObject();
		
		try {
			TMember tm = memberDao.getMemberForId(Integer.valueOf(id));
			
			String name = null;
			String logo = null;
			if (tm != null) {
				name = tm.getFullname();
				logo = tm.getLogo();
			}
			String domain = PropertiesUtils.getDomain();
			String uploadDir = PropertiesUtils.getUploadDir();
			String url = domain + uploadDir + logo;
			String token = RongCloudUtils.getInstance().getToken(id, name, url);
			
			if (StringUtils.getInstance().isBlank(token)) {
				jo.put("code", 0);
				jo.put("text", "fail");
			} else {
				jo.put("code", 1);
				jo.put("text", token);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jo.toString();
	}
	
	@Override
	public String refreshUser(String userId) {
		JSONObject jo = new JSONObject();
		
		try {
			TMember tm = memberDao.getMemberForId(Integer.valueOf(userId));
			
			String name = null;
			String logo = null;
			
			if (tm != null) {
				name = tm.getFullname();
				logo = tm.getLogo();
			}
			
			String domain = PropertiesUtils.getDomain();
			String uploadDir = PropertiesUtils.getUploadDir();
			String url = domain + uploadDir + logo;
			int code = RongCloudUtils.getInstance().refreshUser(userId, name, url);
			
			jo.put("code", code);
			jo.put("text", "ok");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jo.toString();
	}
	
	@Override
	public String checkOnline(String userId) {
		String code = RongCloudUtils.getInstance().checkOnLine(userId);
		
		return code;
	}

	
	private MemberDao memberDao;

	public MemberDao getMemberDao() {
		return memberDao;
	}

	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}

}