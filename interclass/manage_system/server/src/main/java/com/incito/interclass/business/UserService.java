package com.incito.interclass.business;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incito.base.exception.AppException;
import com.incito.base.util.Md5Utils;
import com.incito.interclass.entity.Admin;
import com.incito.interclass.entity.Classes;
import com.incito.interclass.entity.Device;
import com.incito.interclass.entity.School;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.Teacher;
import com.incito.interclass.entity.User;
import com.incito.interclass.persistence.ClassMapper;
import com.incito.interclass.persistence.DeviceMapper;
import com.incito.interclass.persistence.SchoolMapper;
import com.incito.interclass.persistence.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ClassMapper classMapper;
	@Autowired
	private SchoolMapper schoolMapper;
	@Autowired
	private DeviceMapper deviceMapper;
	@Autowired
	private ClassService classService;
	@Autowired
	private DeviceService deviceService;

	/**
	 * 改变学生的分数
	 * 
	 * @param studentIdList
	 * @param changeScore
	 */
	public Integer changePoint(String studentId, int score, int groupId) {
		int Score = 0;
		String[] x = studentId.split(",");

		for (int k = 0; k < x.length; k++) {
			if (userMapper.getScore(x[k]).getScore() == 0 && score < 0) {
				userMapper.changePoint(x[k], 0);
			} else {
				userMapper.changePoint(x[k], score);
			}
		}

		for (int i = 0; i < userMapper.getStudentByCaptainId(groupId).size(); i++) {
			int studentScore = userMapper.getStudentByCaptainId(groupId).get(i)
					.getScore();
			Score = Score + studentScore;
		}
		return Score;
	};

	/**
	 * 获得勋章
	 * 
	 * @param groupId
	 * @param medals
	 */
	public Integer updateMedals(int groupId, String medals) {
		return userMapper.updateMedals(groupId, medals);
	};

	/**
	 * 管理员登陆
	 * 
	 * @param user
	 * @return
	 */
	public Admin loginForAdmin(User user) {
		Admin admin = new Admin();
		admin.setUname(user.getUname());
		admin.setPassword(Md5Utils.md5(user.getPassword()));
		return userMapper.loginForAdmin(admin);
	}

	public Teacher loginForTeacher(Teacher teacher) {
		return userMapper.loginForTeacher(teacher);
	}

	public Student loginForStudent(Student student) {
		return userMapper.loginForStudent(student);
	}

	public List<Student> getStudentByGroupId(int groupId) {
		return userMapper.getStudentByGroupId(groupId);
	}

	public List<Teacher> getTeacherListByCondition(String name,
			String schoolName) {
		return userMapper.getTeacherListByCondition(name, schoolName);
	}

	public List<Student> getStudentListByCondition(String name,
			String schoolName) {
		return userMapper.getStudentListByCondition(name, schoolName);
	}

	public Student getStudent(String name, String number) {
		return userMapper.getStudent(name, number);
	}
	
	public List<Student> getStudentByClassId(int classId){
		return userMapper.getStudentByClassId(classId);
	}

	@Transactional(rollbackFor = AppException.class)
	public boolean saveTeacher(Teacher teacher) throws AppException {
		userMapper.saveUser(teacher);
		if (teacher.getId() <= 0) {
			throw AppException.database(0);
		}
		int result = userMapper.saveTeacher(teacher);
		return result == 1;
	}
	@Transactional(rollbackFor=AppException.class)
	public boolean saveStudentOnly(Student student) throws AppException{
		student.setActive(true);
		student.setRole(Student.ROLE_STUDENT);
		userMapper.saveUser(student);
		if(student.getId()<=0){
			throw AppException.database(0);
		}
		int result=userMapper.saveStudent(student);
		return result==1;
	}
	
	/**
	 * 
	 * @param student 需要将class表，device表相关的字段封装在Student类中作为参数传递进来
	 * @return boolean
	 * @throws AppException
	 */
	@Transactional(rollbackFor = AppException.class)
	public boolean saveStudent(Student student) throws AppException {
		//检查class表，没有数据将创建。
		Classes classes= classService.getClassByNumber(student.getSchoolId(), student.getYear(), student.getClassNumber());
		if(classes==null||classes.getId()==0){
			classes= new Classes();
			classes.setNumber(student.getClassNumber());
			classes.setSchoolId(student.getSchoolId());
			classes.setYear(student.getYear());
			boolean flag=classService.saveClass(classes);
			while(flag==false){
				throw AppException.database(0);
			}
		}
		student.setClassId(classes.getId());
		
		//检查Device表，没有数据将创建
		Device device=deviceService.getDeviceByIMEI(student.getImei());
		if(device==null||device.getId()==0){
			device=new Device();
			device.setImei(student.getImei());
			boolean flag=deviceService.saveDevice(device);
			while(flag==false){
				throw AppException.database(0);
			}
		}
		else{
			throw AppException.database(0);
		}
		
		//student表创建
		student.setActive(true);
		student.setRole(User.ROLE_STUDENT);
		student.setClassId(classes.getId());
		student.setDeviceId(device.getId());	
		int flag=userMapper.saveUser(student);
		if (flag!=1) {
			throw AppException.database(0);
		}
		int result = userMapper.saveStudent(student);
		if(result!=1){
			throw AppException.database(0);
		}
		//device表更新studentId
		device.setStudentId(student.getId());
		boolean outcome= deviceService.update(device);
		while(outcome==false){
			throw AppException.database(0);
		}
		return outcome;
	}

	public void deleteTeacher(int teacherId) {
		userMapper.deleteUser(teacherId);
		userMapper.deleteTeacher(teacherId);
	}

	public void deleteStudent(int studentId) {
		userMapper.deleteUser(studentId);
		userMapper.deleteStudent(studentId);
	}

	public Map<String, Object> saveStudent(File file) throws AppException {
		Map<String, Object> result = new HashMap<String, Object>();
		//导入学校
		School school = null;
		try {
			school = getSchool(file);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "1");
			result.put("error", "读取学校信息出错，请检查模板！");
			return result;
		}
		if (school == null || school.getName() == null
				|| school.getName().equals("")) {
			result.put("code", "1");
			result.put("error", "读取学校信息出错，请检查模板！");
			return result;
		}
		School tempSchool = schoolMapper.getSchoolByName(school.getName());//检查学校是否存在
		if (tempSchool == null) {
			schoolMapper.save(school);
			tempSchool = school;
		}
		
		//导入班级和学生、设备
		List<Student> students;
		try {
			students = getStudentList(file);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "2");
			result.put("error", "读取学生信息出错，请检查模板！");
			return result;
		}
		Set<Classes> classSet = getClassSet(students, tempSchool.getId());
		Map<String,Classes> classMap = new HashMap<String,Classes>();
		Iterator<Classes> it = classSet.iterator();
		while (it.hasNext()) {
			Classes classes = it.next();
			Classes temp = classMapper.getClassByNumber(tempSchool.getId(),
					classes.getYear(), classes.getNumber());//检查班级是否存在
			if (temp == null || temp.getId() == 0) {
				classMapper.save(classes);
				temp = classes;
			}
			String key = String.valueOf(temp.getYear()) + temp.getNumber();
			classMap.put(key, temp);
		}

		List<String> exists = new ArrayList<String>();
		List<String> unbind = new ArrayList<String>();
		for (int i = 0; i < students.size(); i++) {
			Student student = students.get(i);
			String key = String.valueOf(student.getYear()) + student.getClassNumber();
			Classes classes = classMap.get(key);
			Student tempStudent = userMapper.getStudentBySchoolId(
					student.getName(), student.getNumber(), tempSchool.getId());//检查学生是否存在
			student.setClassId(classes.getId());
			if (tempStudent == null || tempStudent.getId() == 0) {//不存在
				Device device = deviceMapper.getDeviceByIMEI(student.getImei());
				if (device != null && device.getId() != 0) {
					Student temp = userMapper.getStudentByImei(device.getImei());
					if (temp != null && temp.getId() != 0) {
						//TODO 设备已绑定学生，不能再绑定
						saveStudentOnly(student);
						unbind.add(student.getName() + "(" + student.getNumber()+")");
					} else {
						student.setDeviceId(device.getId());
						saveStudentOnly(student);
						device.setStudentId(student.getId());//device表新增student_id字段
						deviceMapper.update(device);
					}
				} else {
					device = new Device();
					device.setImei(student.getImei());
					deviceMapper.save(device);
					student.setDeviceId(device.getId());
					saveStudentOnly(student);
					device.setStudentId(student.getId());//device表新增student_id字段
					deviceMapper.update(device);
				}
			} else {
				//TODO 学生已存在，不导入
				exists.add(student.getName() + "(" + student.getNumber()+")");
			}
		}
		result.put("exists", exists);
		result.put("unbind", unbind);
		return result;
	}

	private School getSchool(File file) throws Exception {
		Workbook rwb = null;
		rwb = Workbook.getWorkbook(file);
		Sheet sheet = rwb.getSheet(0);// 获取第一个工作表，即学校信息表
		School school = new School();
		school.setName(sheet.getCell(1, 0).getContents());
		school.setZipCode(sheet.getCell(1, 1).getContents());
		school.setEmail(sheet.getCell(1, 2).getContents());
		school.setPhone(sheet.getCell(1, 3).getContents());
		school.setAddress(sheet.getCell(1, 4).getContents());
		school.setSchoolType(getSchoolType(sheet.getCell(1, 5).getContents()));
		school.setEducationalType(getEducationalType(sheet.getCell(1, 6)
				.getContents()));
		rwb.close();
		return school;
	}

	private List<Student> getStudentList(File file) throws Exception {
		List<Student> students = new ArrayList<Student>();// 存储读取的Student内容
		Workbook rwb = null;
		rwb = Workbook.getWorkbook(file);
		Sheet sheet = rwb.getSheet(1);// 获取第二个工作表，即学生信息表
		for (int i = 1; i < sheet.getRows(); i++) { // 行数(表头的目录不需要，从1开始)
			Student student = new Student();
			String year = sheet.getCell(0, i).getContents();// 获取年级
			if (year == null || year.equals("")) {
				break;
			}
			Calendar calendar = Calendar.getInstance();
			int month = calendar.get(Calendar.MONTH) + 1;
			if(month < 9){
				calendar.add(Calendar.YEAR, Integer.parseInt(year) * -1);
			} else {
				calendar.add(Calendar.YEAR, (Integer.parseInt(year)-1) * -1);
			}
			student.setYear(calendar.get(Calendar.YEAR));

			String classNumber = sheet.getCell(1, i).getContents();// 获取班级
			if (classNumber == null || classNumber.equals("")) {
				break;
			}
			student.setClassNumber(Integer.parseInt(classNumber));

			String name = sheet.getCell(2, i).getContents();// 获取姓名
			if (name == null || name.equals("")) {
				break;
			}
			student.setName(name);
			student.setUname(name);
			
			String number = sheet.getCell(3, i).getContents();// 获取学号
			if (number == null || number.equals("")) {
				break;
			}
			student.setNumber(number);

			String sex = sheet.getCell(4, i).getContents();// 获取性别
			if (sex == null || sex.equals("")) {
				break;
			}
			student.setSex(getSex(sex));

			String guardian = sheet.getCell(5, i).getContents();// 获取监护人
			if (guardian == null || guardian.equals("")) {
				break;
			}
			student.setGuardian(guardian);

			String phone = sheet.getCell(6, i).getContents();// 获取联系电话
			if (phone == null || phone.equals("")) {
				break;
			}
			student.setPhone(phone);

			String address = sheet.getCell(7, i).getContents();// 获取通讯地址
			if (address == null || address.equals("")) {
				break;
			}
			student.setAddress(address);

			String imei = sheet.getCell(8, i).getContents();// 获取Pad设备号
			if (imei == null || imei.equals("")) {
				break;
			}
			student.setImei(imei);

			students.add(student);
		}
		rwb.close();
		return students;
	}

	private Set<Classes> getClassSet(List<Student> students, int schoolId) {
		Set<Classes> classSet = new HashSet<Classes>();
		for (int i = 0; i < students.size(); i++) {
			Student student = students.get(i);
			Classes classes = new Classes();
			classes.setNumber(student.getClassNumber());
			classes.setYear(student.getYear());
			classes.setSchoolId(schoolId);
			classSet.add(classes);
		}
		return classSet;
	}

	private int getSchoolType(String schoolType) {
		if (schoolType == null || schoolType.equals("")) {
			return -1;
		}
		if (schoolType.equals("小学")) {
			return 1;
		}
		if (schoolType.equals("中学")) {
			return 2;
		}
		if (schoolType.equals("其他")) {
			return 3;
		}
		return -1;
	}

	private int getEducationalType(String educationalType) {
		if (educationalType == null || educationalType.equals("")) {
			return -1;
		}
		if (educationalType.equals("民办")) {
			return 1;
		}
		if (educationalType.equals("公办")) {
			return 2;
		}
		if (educationalType.equals("民办公助")) {
			return 3;
		}
		if (educationalType.equals("私立学校")) {
			return 4;
		}
		if (educationalType.equals("其他")) {
			return 5;
		}
		return -1;
	}

	private int getSex(String sex) {
		if (sex == null || sex.equals("")) {
			return -1;
		}
		if (sex.equals("男")) {
			return 1;
		}
		if (sex.equals("女")) {
			return 2;
		}
		return -1;
	}

	public int getTeacherByUname(String uname) {
		return userMapper.getTeacherByUname(uname);
	}

	public int getTeacherByIdCard(String idcard) {
		return userMapper.getTeacherByIdCard(idcard);
	}

	public Teacher getTeacherById(int teacherId) {
		return userMapper.getTeacherById(teacherId);
	}
	@Transactional(rollbackFor=AppException.class)
	public boolean updateTeacherById(Teacher teacher) throws AppException{
		teacher.setActive(true);
		teacher.setRole(User.ROLE_TEACHER);
		int flag=userMapper.updateUserById(teacher);
		if(flag<0)
		{
			throw AppException.database(0);
		}
		 flag=userMapper.updateTeacherById(teacher);
		 
		return flag>=0;
	}

	public Student getStudentById(int studentId) {
		return userMapper.getStudentById(studentId);
	}
	
	@Transactional(rollbackFor=AppException.class)
	public boolean updateStudent(Student student) throws AppException {
		student.setActive(true);
		student.setRole(User.ROLE_STUDENT);
		int flag=userMapper.updateUserById(student);
		if(flag<0){
			throw AppException.database(0);
		}
		flag=userMapper.updateStudentById(student);
		
		return flag>=0;
	}	
}
