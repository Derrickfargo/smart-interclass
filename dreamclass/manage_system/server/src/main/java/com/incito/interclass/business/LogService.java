<<<<<<< HEAD
package com.incito.interclass.business;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incito.interclass.entity.Log;
import com.incito.interclass.persistence.LogMapper;

@Service
public class LogService {

	@Autowired
	private LogMapper logMapper;
	
	public List<Log> getLogListByCondition(int type,String key, String address, String date){
		Date aftdate=null;
		Date formdate=null;
		if(date!=null){
		
			try {
				String strdate=date+" 23:59:59";
				String bgndate=date+" 00:00:00";
				aftdate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(strdate);
				formdate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(bgndate);
				System.out.println("a:"+aftdate+"b:"+formdate);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			return logMapper.getLogListByCondition(type,key,address,formdate,aftdate);
		
		
		
	}
	
	public Log getLogById(int id){
		return logMapper.getLogById(id);
	}
	
	public boolean saveLog(Log log) {
		int id = (Integer) logMapper.save(log);
		return id != 0;
	}

	public void deleteLog(int logId) {
		Log log = logMapper.getLogById(logId);
		File file = new File(log.getUrl());
		file.delete();
		logMapper.delete(logId);
	}
}
=======
package com.incito.interclass.business;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incito.interclass.entity.Log;
import com.incito.interclass.persistence.LogMapper;

@Service
public class LogService {

	@Autowired
	private LogMapper logMapper;
	
	public List<Log> getLogListByCondition(int type,String key, String address){
		return logMapper.getLogListByCondition(type,key,address);
	}
	
	public Log getLogById(int id){
		return logMapper.getLogById(id);
	}
	
	public boolean saveLog(Log log) {
		int id = (Integer) logMapper.save(log);
		return id != 0;
	}

	public void deleteLog(int logId) {
		Log log = logMapper.getLogById(logId);
		File file = new File(log.getUrl());
		file.delete();
		logMapper.delete(logId);
	}
}
>>>>>>> branch 'master' of https://github.com/incito/smart-interclass.git
