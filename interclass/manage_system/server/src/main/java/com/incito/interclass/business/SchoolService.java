package com.incito.interclass.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incito.interclass.entity.School;
import com.incito.interclass.persistence.SchoolMapper;

@Service
public class SchoolService {

	@Autowired
	private SchoolMapper schoolMapper;
	
	public List<School> getSchoolListByCondition(String name, int schoolType) {
		return schoolMapper.getSchoolListByCondition(name, schoolType);
	}

	public List<School> getSchoolList(){
		return schoolMapper.getSchoolList();
	}
	
	public boolean saveSchool(School school) {
		int id = (Integer) schoolMapper.save(school);
		return id != 0;
	}

	public void deleteSchool(int schoolId) {
		schoolMapper.delete(schoolId);
	}

	public int searchSchoolByName(String name) {
		// TODO Auto-generated method stub
		return schoolMapper.searchSchoolByName(name);
	}

	public  School getSchoolById(int schoolId) {
		// TODO Auto-generated method stub
		return schoolMapper.getSchoolById(schoolId);
	}

	public void editSchool(School school) {
		// TODO Auto-generated method stub
		 schoolMapper.editSchool(school);
	}
}
