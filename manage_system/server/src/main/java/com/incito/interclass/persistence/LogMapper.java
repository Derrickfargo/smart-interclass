package com.incito.interclass.persistence;

import java.util.List;

import com.incito.interclass.entity.Log;

public interface LogMapper {
	List<Log> getLogList();

	Log getLogById(int id);

	Integer save(Log log);

	void delete(int id);
}
