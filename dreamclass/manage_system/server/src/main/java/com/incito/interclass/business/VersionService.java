package com.incito.interclass.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incito.interclass.entity.Version;
import com.incito.interclass.persistence.VersionMapper;

@Service
public class VersionService {

	@Autowired
	private VersionMapper versionMapper;
	
	public List<Version> getVersionListByCondition(int type) {
		return versionMapper.getVersionListByCondition(type);
	}

	public Version getVersionById(int id){
		return versionMapper.getVersionById(id);
	}
	
	public Version getVersion(int type, int code){
		return versionMapper.getVersion(type, code);
	}
	
	public boolean saveVersion(Version version) {
		int id = (Integer) versionMapper.save(version);
		return id != 0;
	}

	public void deleteVersion(int versionId) {
		versionMapper.delete(versionId);
	}
}
