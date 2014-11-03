
package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.School;

public interface SchoolMapper {
	List<School> getSchoolListByCondition(@Param("name") String name,
			@Param("schoolType") int schoolType);
	
	Integer save(School school);
	
	void delete(int id);
	List<Integer> getSchoolIdByName(@Param("schoolName")String schoolName);
}

