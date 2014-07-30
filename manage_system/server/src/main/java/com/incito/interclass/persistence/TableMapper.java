package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Table;

public interface TableMapper {
	List<Table> getTableList();

	List<Table> getTableListByRoomId(int roomId);
	
	Integer save(Table table);

	void delete(int id);
	
	Table getTableByNumber(@Param("roomId") int roomId,
			@Param("number") int number);
}
