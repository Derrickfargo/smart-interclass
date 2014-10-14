package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Classes;

public interface ClassMapper {

	List<Classes> getClassListByCondition();

	/**
	 * 根据教师id和学年查询所教班级列表
	 * 
	 * @return
	 */
	List<Classes> getClassList(@Param("schoolId") int schoolId);

	/**
	 * 根据学校id查询所教班级列表
	 * 
	 * @return
	 */
	List<Classes> getClassBySchoolId(@Param("schoolId") int schoolId,
			@Param("year") int year);

	Classes getClassByNumber(@Param("schoolId") int schoolId,
			@Param("year") int year, @Param("number") int number);

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

	int update(Classes classes);
}
