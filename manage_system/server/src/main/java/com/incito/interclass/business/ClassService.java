package com.incito.interclass.business;

import java.util.List;

import org.springframework.stereotype.Service;

import com.incito.base.dao.BaseService;
import com.incito.interclass.entity.Classes;

@Service
public class ClassService extends BaseService {
	
	public List<Classes> getClassListBySchoolId(int schoolId) {
		return findForList("getClassListBySchoolId", schoolId);
	}
	
	public List<Classes> getClassList(Object parameterObject, int skipResults, int maxResults) {
		return findForList("getClassList", parameterObject, skipResults, maxResults);
	}

	public boolean saveClass(Classes classes) {
		int id = (Integer) addObject("saveClass", classes);
		return id != 0;
	}

	public void deleteClass(int classId) {
		delObject("deleteClass", classId);
	}
}
