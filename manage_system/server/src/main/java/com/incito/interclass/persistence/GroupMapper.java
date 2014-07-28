package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Group;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.StudentGroup;

public interface GroupMapper {
	List<Group> getGroupList(@Param("teacherId") int teacherId,
			@Param("courseId") int courseId, @Param("classId") int classId);

	Integer save(Group group);

	Integer saveStudentGroup(StudentGroup sg);

	Group getGroupById(int id);

	Group getGroupByIMEI(@Param("imei") String imei,
			@Param("teacherId") int teacherId, @Param("courseId") int courseId,
			@Param("classId") int classId);

	Group getGroupByTableId(@Param("tableId") int tableId,
			@Param("teacherId") int teacherId, @Param("courseId") int courseId,
			@Param("classId") int classId);

	Student getStudentByStudentId(@Param("groupId") int groupId,
			@Param("studentId") int studentId);
	
	void delete(int id);
}
