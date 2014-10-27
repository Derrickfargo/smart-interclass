package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Group;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.StudentGroup;

public interface GroupMapper {
	List<Group> getGroupList(@Param("teacherId") int teacherId,
			@Param("classId") int classId);

	Integer save(Group group);

	Integer saveStudentGroup(StudentGroup sg);

	Group getGroupById(int id);

	Group getGroupByIMEI(@Param("imei") String imei,
			@Param("teacherId") int teacherId,
			@Param("classId") int classId);

	Group getGroupByTableId(@Param("teacherId") int teacherId,
			@Param("classId") int classId);

	Student getStudentByStudentId(@Param("groupId") int groupId,
			@Param("studentId") int studentId);
	/**
	 * 删除该注册学生在同一个教室、同一门课程、同一个老师的其他注册记录（实现某人在其他分组登陆后，删除现有分组的关联关系）
	 * @param groupId
	 * @param studentId
	 * @param teacherId
	 * @param classId
	 */
	void delStudentInOtherGroup(@Param("groupId") int groupId,
			@Param("studentId") int studentId, 	@Param("teacherId") int teacherId, @Param("classId") int classId);

	void updateGroup(Group group);

	/**
	 * 根据小组Id 删除小组
	 * @param groupId
	 */
	Integer delete(String groupId);

	/**
	 * 创建小组
	 * @param name
	 * @param logo
	 * @param teacherId
	 * @param classId
	 * @param studentId
	 */
	Integer creatGroup(Group group);
	
	/**
	 * 加入小组
	 * @param groupId
	 * @param studentId
	 */
	Group joinGroup(@Param("groupId") String groupId,@Param("studentId") String studentId);

	/**
	 * 查找队长所在的组的groupId
	 * @param studentId
	 */
	Group getGroupIdByCaptainId(String studentId);

	
}
