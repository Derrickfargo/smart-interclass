package com.incito.interclass.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incito.interclass.entity.Log;
import com.incito.interclass.persistence.LogMapper;

@Service
public class LogService {

	@Autowired
	private LogMapper logMapper;
	
	public List<Log> getLogList(Object parameterObject, int skipResults, int maxResults) {
		return logMapper.getLogList();
	}

	public List<Log> getLogList(){
		return logMapper.getLogList();
	}
	
	public Log getLogById(int id){
		return logMapper.getLogById(id);
	}
	
	public boolean saveLog(Log log) {
		int id = (Integer) logMapper.save(log);
		return id != 0;
	}

	public void deleteLog(int logId) {
		logMapper.delete(logId);
	}
}
