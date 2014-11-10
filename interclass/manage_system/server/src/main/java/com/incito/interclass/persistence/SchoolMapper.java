package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.School;

public interface SchoolMapper {
	List<School> getSchoolList();

	List<School> getSchoolListByCondition(@Param("name") String name,
			@Param("schoolType") int schoolType);
	
	Integer save(School school);

	School getSchoolByName(String name);
	void delete(int id);

	int searchSchoolByName(String name);

	School getSchoolById(int schoolId);

	void editSchool(School school);
}
