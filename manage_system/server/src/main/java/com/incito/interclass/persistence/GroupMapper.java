package com.incito.interclass.persistence;

import java.util.List;

import com.incito.interclass.entity.Group;

public interface GroupMapper {
	List<Group> getGroupList();

	Integer save(Group group);

	void delete(int id);
}
