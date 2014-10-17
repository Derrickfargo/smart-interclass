package com.incito.interclass.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incito.interclass.entity.Course;
import com.incito.interclass.persistence.CourseMapper;

@Service
public class CourseService {

	@Autowired
	private CourseMapper courseMapper;
	
	public List<Course> getCourseList(){
		return courseMapper.getCourseList();
	}
	
	public List<Course> getCourseListByCondition(String name){
		return courseMapper.getCourseListByCondition(name);
	}
	
	public Course getCourseById(int id){
		return courseMapper.getCourseById(id);
	}
	
}
