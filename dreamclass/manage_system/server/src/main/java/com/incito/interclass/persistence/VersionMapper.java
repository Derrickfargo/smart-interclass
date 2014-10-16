package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Version;

public interface VersionMapper {
	List<Version> getVersionListByCondition(@Param("type")int type);

	Version getVersionById(int id);

	Version getLatestVersion(int type);
	
	Integer save(Version version);

	void delete(int id);
}
