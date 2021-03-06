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

	public List<Group> getGroupList(int teacherId, int classId) {
		return groupMapper.getGroupList(teacherId, classId);
	}

	@Transactional(rollbackFor = AppException.class)
	public boolean saveGroup(Group group, String studentIds)
			throws AppException {
		groupMapper.save(group);
		if (group.getId() <= 0) {
			throw AppException.database(0);
		}
		String[] ids = studentIds.split(",");
		for (String id : ids) {
			StudentGroup sg = new StudentGroup();
			sg.setStudentId(Integer.parseInt(id));
			sg.setGroupId(group.getId());
			groupMapper.saveStudentGroup(sg);
			if (sg.getId() <= 0) {
				throw AppException.database(0);
			}
		}
		return true;
	}

	@Transactional(rollbackFor = AppException.class)
	public Group addStudent(int classId, int teacherId,int studentId) throws AppException {
		// 检查组是否存在
		Group group = groupMapper.getGroupByTableId(teacherId,
				 classId);
		if (group == null || group.getId() == 0) {
			group = new Group();
			group.setClassId(classId);
			group.setTeacherId(teacherId);
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
			groupMapper.delStudentInOtherGroup(group.getId(), studentId, teacherId, classId);
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
	
	public Group getGroupByTableId( int teacherId, int classId){
		return groupMapper.getGroupByTableId( teacherId,classId);
	}
	
	public Group getGroupById(int id) {
		return groupMapper.getGroupById(id);
	}

	public Group getGroupByIMEI(String imei, int teacherId,int classId) {
		return groupMapper.getGroupByIMEI(imei, teacherId, classId);
	}

	public void updateGroup(Group group) {
		groupMapper.updateGroup(group);
	}

	public Integer deleteGroupById(int groupId) {
		return groupMapper.delete(groupId);
	}
	
	public void claerGroup(int teacherId,int classId){
		List<Group> groupList = groupMapper.getGroupList(teacherId, classId);
		for (Group group : groupList) {
			groupMapper.delete(group.getId());
			groupMapper.deleteStudentGroup(group.getId());
		}
		
	}

	/**
	 * 通过队长ID 找到组的ID 并删除
	 * @param studentId
	 * @return
	 */
	public Group getGroupIdByCaptainId(int studentId) {
		return groupMapper.getGroupIdByCaptainId(studentId);
	}
}
