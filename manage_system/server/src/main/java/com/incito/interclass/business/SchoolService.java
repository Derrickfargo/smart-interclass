package com.incito.interclass.business;

import java.util.List;

import org.springframework.stereotype.Service;

import com.incito.base.dao.BaseService;
import com.incito.interclass.entity.School;

@Service
public class SchoolService extends BaseService {

	public List<School> getSchoolList(Object parameterObject, int skipResults,
			int maxResults) {
		return findForList("getSchoolList", parameterObject, skipResults,
				maxResults);
	}

	public List<School> getSchoolList(){
		return findForList("getSchoolList", null);
	}
	public boolean saveSchool(School school) {
		int id = (Integer) addObject("saveSchool", school);
		return id != 0;
	}

	public void deleteSchool(int schoolId) {
		delObject("deleteSchool", schoolId);
	}
}
