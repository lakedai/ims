package com.sealtalk.dao;

import java.util.List;

import com.sealtalk.common.IBaseDao;
import com.sealtalk.model.TFriend;

/**
 * 好友关系 
 * @author hao_dy
 * @since jdk1.7
 * @date 2017/01/09
 */
public interface FriendDao extends IBaseDao<TFriend, Long> {

	/**
	 * 获取一对好友关系
	 * @param accountId
	 * @param friendId
	 * @return
	 */
	public TFriend getFriendRelation(int accountId, int friendId);

	/**
	 * 增加好友关系 
	 * @param accountId
	 * @param friendId
	 */
	public void addFriend(int accountId, int friendId);

	/**
	 * 删除好友关系
	 * @param accountId
	 * @param friendId
	 */
	public void delFriend(int accountId, int friendId);

	/**
	 * 获取用户的好友列表
	 * @param account
	 * @return
	 */
	public List<TFriend> getFriendRelationForAccount(String account);
	
} 
