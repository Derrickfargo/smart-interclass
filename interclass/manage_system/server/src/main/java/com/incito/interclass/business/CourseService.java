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
	
	public Course getCourseById(int id){
		return courseMapper.getCourseById(id);
	}
	
	public boolean saveCourse(Course course) {
		int id = (Integer) courseMapper.save(course);
		return id != 0;
	}

	public void deleteCourse(int courseId) {
		courseMapper.delete(courseId);
	}

	public int getCourseByName(String name) {
		// TODO Auto-generated method stub
		return courseMapper.getCourseByName(name);
	}

	public void update(Course course) {
		// TODO Auto-generated method stub
		 courseMapper.update(course);
	}

	public List<Course> getCourseListByCondition(String name) {
		// TODO Auto-generated method stub
		return courseMapper.getCourseListByCondition(name);
	}
}
