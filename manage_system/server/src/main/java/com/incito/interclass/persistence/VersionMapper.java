package com.incito.interclass.persistence;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Version;


public interface VersionMapper {
	Version updateApk(@Param("oldVersion")int oldVersion,@Param("type")int type);
}
