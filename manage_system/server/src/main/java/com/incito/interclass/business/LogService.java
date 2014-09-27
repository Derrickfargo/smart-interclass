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
	
	public List<Log> getLogListByCondition(int type,String key){
		return logMapper.getLogListByCondition(type,key);
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
