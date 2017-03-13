package com.sealtalk.auth.dao;

import com.sealtalk.auth.model.AppSecret;
import com.sealtalk.common.IBaseDao;

public interface AppSecretDao extends IBaseDao<AppSecret, Integer> {
	/**
	 * 设置app,secret,url
	 * @param as
	 */
	public void setAppIDAndSecretAndUrl(AppSecret as);
	
	/**
	 * 依据appId获取appSecret
	 * @param appId
	 * @return
	 */
	public AppSecret getAppSecretByAppId(String appId);

	/**
	 * 更新AppSecret
	 * @param as
	 */
	public void updateAppSecret(AppSecret as);

	/**
	 * 依据secret获取appSecret
	 * @param secret
	 * @return
	 */
	public AppSecret getAppSecretBySecret(String secret);

	/**
	 * 依据real token获取appSecret
	 * @param visitToken
	 * @return
	 */
	public AppSecret getAppSecretByRealToken(String visitToken);
}
