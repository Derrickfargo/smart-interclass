package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Group;

public interface GroupMapper {
	List<Group> getGroupList(
			@Param("teacherId") int teacherId,
			@Param("courseId") int courseId, 
			@Param("classId") int classId);

	Integer save(Group group);

	void delete(int id);
}
