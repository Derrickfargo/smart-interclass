package com.incito.interclass.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incito.interclass.entity.Group;
import com.incito.interclass.persistence.GroupMapper;

@Service
public class GroupService {
	
	@Autowired
	private GroupMapper groupMapper;
	
	public List<Group> getGroupList(int teacherId,int courseId,int classId){
		return groupMapper.getGroupList(teacherId, courseId, classId);
	}
	
	public boolean saveGroup(Group group) {
		int id = (Integer) groupMapper.save(group);
		return id != 0;
	}

	public void deleteGroup(int groupId) {
		groupMapper.delete(groupId);
	}
}
