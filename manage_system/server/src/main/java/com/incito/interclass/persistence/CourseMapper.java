package com.incito.interclass.persistence;

import java.util.List;

import com.incito.interclass.entity.Course;

public interface CourseMapper {
	List<Course> getCourseList();

	Course getCourseById(int id);

	Integer save(Course course);

	void delete(int id);
}
