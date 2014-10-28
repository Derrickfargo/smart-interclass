
package com.incito.interclass.persistence;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Log;

public interface LogMapper {
	List<Log> getLogListByCondition(@Param("type")int type,@Param("key")String key, @Param("address")String address, @Param("date")Date date, @Param("aftdate")Date aftdate);

	Log getLogById(int id);

	Integer save(Log log);

	void delete(int id);
}

