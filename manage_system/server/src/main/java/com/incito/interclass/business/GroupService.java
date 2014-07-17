package com.incito.interclass.business;

import java.util.List;

import org.springframework.stereotype.Service;

import com.incito.base.dao.BaseService;
import com.incito.interclass.entity.Group;

@Service
public class GroupService extends BaseService {
	
	public List<Group> getGroupList(Object parameterObject, int skipResults, int maxResults) {
		return findForList("getGroupList", parameterObject, skipResults, maxResults);
	}

	public List<Group> getGroupList(){
		return findForList("getGroupList", null);
	}
	
	public boolean saveGroup(Group group) {
		int id = (Integer) addObject("saveGroup", group);
		return id != 0;
	}

	public void deleteGroup(int groupId) {
		delObject("deleteGroup", groupId);
	}
}
