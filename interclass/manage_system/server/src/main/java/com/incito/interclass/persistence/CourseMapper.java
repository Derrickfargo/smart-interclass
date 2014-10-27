package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Course;

public interface CourseMapper {
	List<Course> getCourseList();
	
	List<Course> getCourseListByCondition(@Param("name")String name);

	Course getCourseById(int id);

	Integer save(Course course);

	void delete(int id);
}
