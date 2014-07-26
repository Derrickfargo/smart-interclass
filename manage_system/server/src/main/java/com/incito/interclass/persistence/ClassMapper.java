package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Classes;

public interface ClassMapper {
	/**
	 * 根据教师id和学年查询所教班级列表
	 * 
	 * @return
	 */
	List<Classes> getClassList(@Param("teacherId") int teacherId,
			@Param("year") int year);

	Classes getClassById(int id);
	
	/**
	 * 保存班级
	 * 
	 * @param classes
	 * @return
	 */
	Integer save(Classes classes);

	/**
	 * 删除班级
	 */
	void delete(int id);
}
