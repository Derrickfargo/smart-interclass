package com.incito.interclass.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incito.interclass.entity.Table;
import com.incito.interclass.persistence.TableMapper;

@Service
public class TableService {

	@Autowired
	private TableMapper tableMapper;

	public List<Table> getTableList() {
		return tableMapper.getTableList();
	}

	public boolean saveTable(Table table) {
		int id = (Integer) tableMapper.save(table);
		return id != 0;
	}

	public void deleteTable(int tableId) {
		tableMapper.delete(tableId);
	}
}
