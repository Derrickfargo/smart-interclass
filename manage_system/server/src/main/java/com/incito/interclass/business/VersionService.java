package com.incito.interclass.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incito.interclass.entity.Version;
import com.incito.interclass.persistence.VersionMapper;

@Service
public class VersionService {

	@Autowired
	private VersionMapper versionMapper;

	/**
	 * 更新apk
	 * 
	 * @param oldVersion
	 * @return
	 */
	public Version updateApk(int oldVersion, int type) {
		return versionMapper.updateApk(oldVersion,type);
	}
}
