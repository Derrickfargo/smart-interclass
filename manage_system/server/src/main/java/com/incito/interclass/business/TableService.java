package com.incito.interclass.business;

import java.util.List;

import org.springframework.stereotype.Service;

import com.incito.base.dao.BaseService;
import com.incito.interclass.entity.Table;

@Service
public class TableService extends BaseService {


	public List<Table> getTableList(){
		return findForList("getTableList", null);
	}
	
	public boolean saveTable(Table room) {
		int id = (Integer) addObject("saveTable", room);
		return id != 0;
	}

	public void deleteTable(int roomId) {
		delObject("deleteTable", roomId);
	}
}
