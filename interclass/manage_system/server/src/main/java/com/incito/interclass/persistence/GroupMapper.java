package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.junit.experimental.theories.ParametersSuppliedBy;

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
	/**
	 * 删除该注册学生在同一个教室、同一门课程、同一个老师的其他注册记录（实现某人在其他分组登陆后，删除现有分组的关联关系）
	 * @param groupId
	 * @param studentId
	 * @param courseId
	 * @param teacherId
	 * @param classId
	 */
	void delStudentInOtherGroup(@Param("groupId") int groupId,
			@Param("studentId") int studentId, @Param("courseId") int courseId,
			@Param("teacherId") int teacherId, @Param("classId") int classId);

	void updateGroup(Group group);

	void delete(int id);
}
