package com.incito.interclass.persistence;

import java.util.List;

import com.incito.interclass.entity.Table;

public interface TableMapper {
	List<Table> getTableList();

	Integer save(Table table);

	void delete(int id);
}
