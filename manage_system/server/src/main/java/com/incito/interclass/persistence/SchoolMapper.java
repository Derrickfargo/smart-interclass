package com.incito.interclass.persistence;

import java.util.List;

import com.incito.interclass.entity.School;

public interface SchoolMapper {
	List<School> getSchoolList();

	Integer save(School school);

	void delete(int id);
}
