package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Group;
import com.incito.interclass.entity.StudentGroup;

public interface GroupMapper {
	List<Group> getGroupList(
			@Param("teacherId") int teacherId,
			@Param("courseId") int courseId, 
			@Param("classId") int classId);

	Integer save(Group group);

	Integer saveStudentGroup(StudentGroup sg);
	
	Group getGroupByIMEI(String imei,int teacherId,int courseId,int classId);
	
	Group getGroupByTableId(int tableId,int teacherId,int courseId,int classId);
	
	void delete(int id);
}
