package com.incito.interclass.persistence;

import java.util.List;

import com.incito.interclass.entity.Course;

public interface CourseMapper {
	List<Course> getCourseList();
	
	Integer save(Course course);
	
	void delete(int id);
}
