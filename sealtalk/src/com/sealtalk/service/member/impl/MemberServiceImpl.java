package com.sealtalk.service.member.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.sealtalk.auth.dao.UserValidDao;
import com.sealtalk.common.Tips;
import com.sealtalk.dao.adm.BranchMemberDao;
import com.sealtalk.dao.friend.FriendDao;
import com.sealtalk.dao.fun.DontDistrubDao;
import com.sealtalk.dao.fun.MsgTopDao;
import com.sealtalk.dao.group.GroupMemberDao;
import com.sealtalk.dao.map.MapDao;
import com.sealtalk.dao.member.MemberDao;
import com.sealtalk.dao.member.TextCodeDao;
import com.sealtalk.dao.privilege.MemberRoleDao;
import com.sealtalk.dao.upload.CutLogoTempDao;
import com.sealtalk.model.TMember;
import com.sealtalk.model.TextCode;
import com.sealtalk.service.member.MemberService;
import com.sealtalk.utils.JSONUtils;
import com.sealtalk.utils.PasswordGenerator;
import com.sealtalk.utils.PropertiesUtils;
import com.sealtalk.utils.RongCloudUtils;
import com.sealtalk.utils.StringUtils;
import com.sealtalk.utils.TimeGenerator;

public class MemberServiceImpl implements MemberService {

	@Override
	public TMember searchSigleUser(String name, String password) {
		TMember memeber = null;
		
		try {
			//password = PasswordGenerator.getInstance().getMD5Str(password);  //前端加密
			memeber = memberDao.searchSigleUser(name, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return memeber;
	}
	
	@Override
	public boolean updateUserPwdForAccount(String account, String newPwd) {
		boolean status = false;
		
		try {
			//String md5Pwd = PasswordGenerator.getInstance().getMD5Str(newPwd);
			
			status = memberDao.updateUserPwdForAccount(account, newPwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}

	@Override
	public boolean updateUserPwdForPhone(String phone, String newPwd) {
		boolean status = false;
		
		try {
			status = memberDao.updateUserPwdForPhone(phone, newPwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}
	
	@Override
	public String getOneOfMember(String userId) {

		JSONObject jo = new JSONObject();
		
		try {
			int userIdInt = StringUtils.getInstance().strToInt(userId);
			
			Object[] member = memberDao.getOneOfMember(userIdInt);
			
			if (member == null) {
				jo.put("code", 0);
				jo.put("text", Tips.NULLUSER.getText());
			} else {
				for(int i = 0; i < member.length; i++) {
					jo.put("id", isBlank(member[0]));
					jo.put("account", isBlank(member[1]));
					jo.put("name", isBlank(member[2]));
					jo.put("logo", isBlank(member[3]));
					jo.put("telephone", isBlank(member[4]));
					jo.put("email", isBlank(member[5]));
					jo.put("address", isBlank(member[6]));
					jo.put("token", isBlank(member[7]));
					jo.put("sex", isBlank(member[8]));
					jo.put("birthday", isBlank(member[9]));
					jo.put("workno", isBlank(member[10]));
					jo.put("mobile", isBlank(member[11]));
					jo.put("intro", isBlank(member[12]));
					jo.put("branchid", isBlank(member[13]));
					jo.put("branchname", isBlank(member[14]));
					jo.put("positionid", isBlank(member[15]));
					jo.put("positionname", isBlank(member[16]));
					jo.put("organid", isBlank(member[17]));
					jo.put("organname", isBlank(member[18]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo.toString();
	}
	
	@Override
	public int updateUserTokenForId(String userId, String token) {
		int row = 0;
		
		try {
			row = memberDao.updateUserTokenForId(userId, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return row;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public String searchUser(String account) {
		JSONArray ja = new JSONArray();
		
		try {
			List members = memberDao.searchUser(account);
			
			if (members == null) {
				JSONObject jo = new JSONObject();
				jo.put("code", 0);
				jo.put("text", Tips.NULLUSER.getText());
			} else {
				for(int i = 0; i < members.size(); i++) {
					Object[] member = (Object[]) members.get(i);
					JSONObject jo = new JSONObject();
					jo.put("id", isBlank(member[0]));
					jo.put("account", isBlank(member[1]));
					jo.put("name", isBlank(member[2]));
					jo.put("logo", isBlank(member[3]));
					jo.put("telephone", isBlank(member[4]));
					jo.put("email", isBlank(member[5]));
					jo.put("address", isBlank(member[6]));
					jo.put("birthday", isBlank(member[7]));
					jo.put("workno", isBlank(member[8]));
					jo.put("mobile", isBlank(member[9]));
					jo.put("groupmax", isBlank(member[10]));
					jo.put("groupuse", isBlank(member[11]));
					jo.put("intro", isBlank(member[12]));
					jo.put("branchname", isBlank(member[13]));
					jo.put("positionname", isBlank(member[14]));
					jo.put("organname", isBlank(member[15]));
					jo.put("sex", isBlank(member[16]));
					ja.add(jo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ja.toString();
	}

	@Override
	public boolean valideOldPwd(String account, String oldPwd) {
		boolean b = false;
		
		try {
			b = memberDao.valideOldPwd(account, oldPwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	@Override
	public String getTextCode(String phone) {
		String code = null;
		
		try {
			TextCode tc = textCodeDao.getTextCode(phone);
			
			if (tc != null) {
				long now = TimeGenerator.getInstance().getUnixTime();
				long createTime = tc.getCreateTime();
				long valideTime = StringUtils.getInstance().strToLong(PropertiesUtils.getStringByKey("code.validetime"));
				
				if ((now - createTime) >= valideTime) {
					code = "-1";
				} else {
					code = tc.getTextCode();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return code;
	}

	@Override
	public void saveTextCode(String phone, String code) {
		try {
			textCodeDao.deleteTextCode(phone);
			
			TextCode stc = new TextCode();
			stc.setPhoneNum(phone);
			stc.setTextCode(code);
			stc.setCreateTime(TimeGenerator.getInstance().getUnixTime());
			textCodeDao.saveTextCode(stc);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public String updateMemberInfoForWeb(String userId, String position, String fullName, String sign) {
		
		JSONObject jo = new JSONObject();
		
		if (StringUtils.getInstance().isBlank(userId)) {
			jo.put("code", -1);
			jo.put("text", Tips.WRONGPARAMS.getText());
		} else {
			try {
				int userIdInt = StringUtils.getInstance().strToInt(userId);
				int ret = memberDao.updateMemeberInfoForWeb(userIdInt, fullName, sign);
				
				if (ret > 0) {
					
					if (!StringUtils.getInstance().isBlank(position)) {
						int positionId = StringUtils.getInstance().strToInt(position);
						branchMemberDao.updatePositionByUseId(userIdInt, positionId);
					}
					jo.put("code", 1);
					jo.put("text", Tips.OK.getText());
				} else {
					jo.put("code", 0);
					jo.put("text", Tips.FAIL.getText());
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return jo.toString();
	}
	
	@Override
	public boolean updateUserPwd(String account, String newPwd) {
		boolean status = false;
		
		try {
			String md5Pwd = PasswordGenerator.getInstance().getMD5Str(newPwd);
			
			status = memberDao.updateUserPwd(account, md5Pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}

	@Override
	public String updateMemberForApp(String userId, String email, String mobile, String phone, String address) {
		JSONObject jo = new JSONObject();
		
		if (StringUtils.getInstance().isBlank(userId)) {
			jo.put("code", -1);
			jo.put("text", Tips.WRONGPARAMS.getText());
		} else {
			try {
				int userIdInt = StringUtils.getInstance().strToInt(userId);
				int ret = memberDao.updateMemeberInfoForApp(userIdInt, email, mobile, phone, address);
				
				if (ret > 0) {
					jo.put("code", 1);
					jo.put("text", Tips.OK.getText());
				} else {
					jo.put("code", 0);
					jo.put("text", Tips.FAIL.getText());
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return jo.toString();
	}
	
	@Override
	public TMember getMemberByToken(String token) {
		
		try {
			if (!StringUtils.getInstance().isBlank(token)) {
				TMember member = memberDao.getMemberByToken(token);
				
				if (member != null) {
					String tokenMaxAge = PropertiesUtils.getStringByKey("db.tokenMaxAge");
					
					long tokenMaxAgeLong = 0;
					long now = TimeGenerator.getInstance().getUnixTime();
					long firstTokenDate = member.getCreatetokendate();
					
					if (tokenMaxAge != null && !"".equals(tokenMaxAge)) {
						tokenMaxAgeLong = Long.valueOf(tokenMaxAge);
					}
					
					if ((now - firstTokenDate) <= tokenMaxAgeLong || tokenMaxAgeLong == 0) {
						return member;
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getAllMemberInfo() {
		JSONObject jo = new JSONObject();
		
		try {
			List<TMember> memberList = memberDao.getAllMemberInfo();
			
			if (memberList != null) {
				int memberLen = memberList.size();
				
				JSONArray ja = new JSONArray();
				
				for(int i = 0; i < memberLen; i++) {
					TMember tms = memberList.get(i);
					JSONObject text = JSONUtils.getInstance().modelToJSONObj(tms);
					ja.add(text);
				}
				jo.put("code", 1);
				jo.put("text", ja.toString());
			} else {
				jo.put("code", 0);
				jo.put("text", Tips.NULLGROUPMEMBER.getText());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jo.toString();
	}
	
	@Override
	public String getAllMemberOnLineStatus(String userIds) {
		JSONObject jo = new JSONObject();
		
		try {
			ArrayList<String> idList = null;
			
			if (StringUtils.getInstance().isBlank(userIds)) {
				idList = new ArrayList<String>();
				List<TMember> memberList = memberDao.getAllMemberInfo();
				if (memberList != null) {
					int memberLen = memberList.size();
					for(int i = 0; i < memberLen; i++) {
						TMember tms = memberList.get(i);
						String id = tms.getId()+"";
						idList.add(id);
					}
				}
			} else {
				userIds = StringUtils.getInstance().replaceChar(userIds, "\"", "");
				userIds = StringUtils.getInstance().replaceChar(userIds, "[", "");
				userIds = StringUtils.getInstance().replaceChar(userIds, "]", "");
				
				String[] userIdses = userIds.split(",");
				
				idList = new ArrayList<String>(Arrays.asList(userIdses));
			}
				
			JSONObject ja = new JSONObject();
			//各成员在线状态
			for(int i = 0; i < idList.size(); i++) {
				String id = idList.get(i);
				String status = RongCloudUtils.getInstance().checkOnLine(id);
				ja.put(id, status);
			}
			jo.put("code", 1);
			jo.put("text", ja.toString());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo.toString();
	}
	
	@Override
	public int countMember() {
		try{
			int count = memberDao.getMemberCount();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public String delMemberByUserId(String userids) {
		JSONObject jo = new JSONObject();
		
		try {
			if (StringUtils.getInstance().isBlank(userids)) {
				jo.put("code", 0);
				jo.put("text", Tips.WRONGPARAMS.getText());
			} else {
				userids = StringUtils.getInstance().replaceChar(userids, "\"", "");
				userids = StringUtils.getInstance().replaceChar(userids, "[", "");
				userids = StringUtils.getInstance().replaceChar(userids, "]", "");
				int ret = memberDao.delMemberByUserId(userids);
				int ret1 = branchMemberDao.delRelationByIds(userids);
				int ret2 = cutLogoTempDao.deletePicsByIds(userids);
				int ret3 = dontDistrubDao.deleteByIds(userids);
				int ret4 = friendDao.deleteRelationByIds(userids);
				int ret5 = groupMemberDao.deleteRelationByIds(userids);
				int ret6 = mapDao.deleteRelationByIds(userids);
				int ret7 = memberRoleDao.deleteRelationByIds(userids);
				int ret8 = msgTopDao.deleteRelationByIds(userids);
				int ret9 = userValidDao.deleteRelationByIds(userids);
				
				jo.put("code", 1);
				jo.put("text", Tips.OK.getText());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return jo.toString();
	}


	private String isBlank(Object o) {
		return o == null ? "" : o + "";
	}
	
	
	private TextCodeDao textCodeDao;
	private MemberDao memberDao;
	private BranchMemberDao branchMemberDao;
	private CutLogoTempDao cutLogoTempDao;
	private DontDistrubDao dontDistrubDao;
	private FriendDao friendDao;
	private GroupMemberDao groupMemberDao;
	private MapDao mapDao;
	private MemberRoleDao memberRoleDao;
	private MsgTopDao msgTopDao;
	private UserValidDao userValidDao;
	
	public void setUserValidDao(UserValidDao userValidDao) {
		this.userValidDao = userValidDao;
	}

	public void setMsgTopDao(MsgTopDao msgTopDao) {
		this.msgTopDao = msgTopDao;
	}

	public void setMemberRoleDao(MemberRoleDao memberRoleDao) {
		this.memberRoleDao = memberRoleDao;
	}

	public void setMapDao(MapDao mapDao) {
		this.mapDao = mapDao;
	}

	public void setGroupMemberDao(GroupMemberDao gropuMemberDao) {
		this.groupMemberDao = gropuMemberDao;
	}

	public void setFriendDao(FriendDao friendDao) {
		this.friendDao = friendDao;
	}

	public void setDontDistrubDao(DontDistrubDao dontDistrubDao) {
		this.dontDistrubDao = dontDistrubDao;
	}

	public void setCutLogoTempDao(CutLogoTempDao cutLogoTempDao) {
		this.cutLogoTempDao = cutLogoTempDao;
	}

	public void setBranchMemberDao(BranchMemberDao branchMemberDao) {
		this.branchMemberDao = branchMemberDao;
	}

	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}

	public TextCodeDao getTextCodeDao() {
		return textCodeDao;
	}

	public void setTextCodeDao(TextCodeDao textCodeDao) {
		this.textCodeDao = textCodeDao;
	}

	public MemberDao getMemberDao() {
		return memberDao;
	}

}
