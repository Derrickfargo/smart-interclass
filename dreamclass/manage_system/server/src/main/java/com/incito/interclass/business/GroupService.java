package com.incito.interclass.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incito.base.exception.AppException;
import com.incito.interclass.entity.Group;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.StudentGroup;
import com.incito.interclass.persistence.GroupMapper;

@Service
public class GroupService {

	@Autowired
	private GroupMapper groupMapper;
	
	public List<Group> getGroupList(int teacherId, int courseId, int classId) {
		return groupMapper.getGroupList(teacherId, courseId, classId);
	}

	@Transactional(rollbackFor = AppException.class)
	public Group addStudent(int courseId, int classId, int teacherId,
			int tableId, int studentId) throws AppException {
		// 检查组是否存在
		Group group = groupMapper.getGroupByTableId(tableId, teacherId,
				courseId, classId);
		if (group == null || group.getId() == 0) {
			group = new Group();
			group.setClassId(classId);
			group.setCourseId(courseId);
			group.setTeacherId(teacherId);
			group.setTableId(tableId);
			groupMapper.save(group);// 创建分组
			if (group.getId() <= 0) {
				throw AppException.database(0);
			}
		}
		// 检查学生是否在该组中
		Student student = groupMapper.getStudentByStudentId(group.getId(),
				studentId);
		if (student == null || student.getId() == 0) {
			StudentGroup sg = new StudentGroup();
			sg.setStudentId(studentId);
			sg.setGroupId(group.getId());
			groupMapper.saveStudentGroup(sg);
			groupMapper.delStudentInOtherGroup(group.getId(), studentId, courseId, teacherId, classId);
		}
		return group;
	}

	public Group save(Group group) {
		List<Student> students = group.getStudents();
		groupMapper.save(group);
		if(group.getId() > 0){
			for (Student student : students) {
				StudentGroup ug = new StudentGroup();
				ug.setGroupId(group.getId());
				ug.setStudentId(student.getId());
				groupMapper.saveStudentGroup(ug);
			}
			return group;
		}
		return null;
	}
	
	public Group getGroupByTableId(int tableId, int teacherId, int courseId, int classId){
		return groupMapper.getGroupByTableId(tableId, teacherId, courseId, classId);
	}
	
	public Group getGroupById(int id) {
		return groupMapper.getGroupById(id);
	}

	public Group getGroupByIMEI(String imei, int teacherId, int courseId,
			int classId) {
		return groupMapper.getGroupByIMEI(imei, teacherId, courseId, classId);
	}

	public void updateGroup(Group group) {
		groupMapper.updateGroup(group);
	}

	public void deleteGroup(int groupId) {
		groupMapper.delete(groupId);
	}
}
