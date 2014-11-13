package com.incito.interclass.admin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.incito.base.exception.AppException;
import com.incito.interclass.business.ClassService;
import com.incito.interclass.business.DeviceService;
import com.incito.interclass.business.SchoolService;
import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.constant.Constants;
import com.incito.interclass.entity.Classes;
import com.incito.interclass.entity.Device;
import com.incito.interclass.entity.School;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.User;
import com.incito.parent.pagehelper.PageHelper;
import com.incito.parent.pagehelper.PageInfo;

@RestController
@RequestMapping("/student")
public class StudentCtrl extends BaseCtrl {

	@Autowired
	private UserService userService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private ClassService classService;
	@Autowired
	private DeviceService deviceService;
	
	/**
	 * 学生列表
	 */
	@RequestMapping("/list")
	public ModelAndView index(String name, String schoolName,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
		ModelAndView res = new ModelAndView("student/studentList");
		PageHelper.startPage(pageNum, PAGE_SIZE);
		List<Student> students = userService.getStudentListByCondition(name, schoolName);
		PageInfo<Student> page = new PageInfo<Student>(students);
		res.addObject("page", page);
		res.addObject("pageNum", pageNum);
		res.addObject("name", name);
		res.addObject("schoolName", schoolName);
		return res;
	}
	
	/**
	 * 添加学生
	 */
	@RequestMapping("/add")
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView("student/studentAdd");
		List<School> schools = schoolService.getSchoolList();
		if(schools != null && schools.get(0) != null){
			School school = schools.get(0);
			List<Classes> classes = classService.getClassList(school.getId());
			mav.addObject("classes", classes);
		}
		mav.addObject("schools", schools);
		return mav;
	}
	
	/**
	 * 保存学生
	 * @return
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(Integer schoolId, Integer year, Integer classNumber, String name, 
			String number, Integer sex, String guardian, String phone, String address, String imei) {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		if(month < 9){
			calendar.add(Calendar.YEAR, year * -1);
		} else {
			calendar.add(Calendar.YEAR, (year-1) * -1);
		}
		int newYear = calendar.get(Calendar.YEAR);

		//更新学生表
		Student student = new Student();
		student.setClassNumber(classNumber);
		student.setSchoolId(schoolId);
		student.setImei(imei);
		student.setYear(newYear);
		student.setUname(name);
		student.setNumber(number);
		student.setName(name);
		student.setSex(sex);
		student.setGuardian(guardian);
		student.setPhone(phone);
		student.setAddress(address);
		student.setActive(true);
		//用户角色为学生
		student.setRole(User.ROLE_STUDENT);
		
		try {
			userService.saveStudent(student);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("redirect:list");
	}
//	/**
//	 * 删除学生
//	 * @param studentId
//	 * @return
//	 */
//	@RequestMapping(value = "/delete")
//	public ModelAndView delete(int studentId) {
//		userService.deleteStudent(studentId);
//		return new ModelAndView("redirect:list");
//	}
	
	@RequestMapping(value = "/import")
	public ModelAndView importStudent() {
		return new ModelAndView("student/import");
	}
	
	/**
	 * 批量导入
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/batchSave")
	public ModelAndView batchSave(MultipartFile file) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat time = new SimpleDateFormat("HH-mm-ss");
		String filename = file.getOriginalFilename();
		File dir = new File(Constants.STUDENT_DIR + File.separator
				+ sdf.format(date) + File.separator + time.format(date));
		dir.mkdirs();
		File newFile = new File(dir, filename);
		try {
			file.transferTo(newFile);//注意这里
		} catch (IOException e) {
			ModelAndView mav = new ModelAndView("student/import");
			mav.addObject("message", "文件获取失败，请重试！");
			return mav;
		}
		Map<String, Object> result =null; 
			try {
				result=	userService.saveStudent(newFile);
			} catch (AppException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		ModelAndView mav = new ModelAndView("redirect:list");
		Object code= null;
		try{
			 code = result.get("code");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		if (code != null && Integer.parseInt(code.toString()) != 0){
			mav.addObject("code", Integer.parseInt(code.toString()));
		}
		mav.addObject("exists", result.get("exists"));
		mav.addObject("unbind", result.get("unbind"));
		return mav;
	}
	/**
	 *编辑学生基本信息 
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(int studentId){
		ModelAndView res = new ModelAndView("student/studentEdit");
		Student student =userService.getStudentById(studentId);
		res.addObject("student", student);
		//入学学年转换为班级年
		int year=student.getYear();
		int newYear=0;
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		if(month < 9){
			newYear=calendar.get(Calendar.YEAR)-year;
		} else {
			newYear=calendar.get(Calendar.YEAR)-year+1;
		}
		List<School> schools = schoolService.getSchoolList();
		res.addObject("schools", schools);
		res.addObject("year", newYear);
		return res;
	}
	/**
	 * 更新学生
	 * @param student
	 * @param className
	 * @param deviceId
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/update")
	public ModelAndView update(Student student,int id){
		//班年级转换为入学学年
				Calendar calendar = Calendar.getInstance();
				int month = calendar.get(Calendar.MONTH) + 1;
				if(month < 9){
					calendar.add(Calendar.YEAR, student.getYear() * -1);
				} else {
					calendar.add(Calendar.YEAR, (student.getYear()-1) * -1);
				}
				int newYear = calendar.get(Calendar.YEAR);
		
		//更新班级表
		Classes classes = classService.getClassByNumber(student.getSchoolId(), newYear,student.getClassNumber());
		if(classes==null || classes.getId()==0){
			classes=new Classes();
			classes.setNumber(student.getClassNumber());
			classes.setSchoolId(student.getSchoolId());
			classes.setYear(newYear);
			classService.saveClass(classes);
		}
		student.setClassId(classes.getId());
		//更新device表
		Device device=deviceService.getDeviceByIMEI(student.getImei());
		if(device==null||device.getId()==0){
			device= new Device();
			device.setImei(student.getImei());
			device.setStudentId(id);
			deviceService.saveDevice(device);
		}
		
		student.setDeviceId(device.getId());
		
		//更新user表和student表
		try {
			student.setUname(student.getName());
			userService.updateStudent(student);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("redirect:list");
	}
	
	@RequestMapping(value="/check")
	public boolean check(String imei,@RequestParam(value="studentId",defaultValue="-1")int studentId){
		Device device=deviceService.getDeviceByIMEI(imei);
		if(device==null||device.getId()==0){
			return true;
		}
		if(device.getStudentId()==studentId){
			return true;
		}
		
		
		return false;
	}
	/**
	 * 模版下载
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/download")
	public ModelAndView download(HttpServletRequest request,HttpServletResponse response) throws Exception{
			
		String downLoadPath=request.getSession().getServletContext().getRealPath("/")+"\\"+"documention\\"+"学生信息模板.xls";
		File file = new File(downLoadPath);
		String fileName= "学生信息模版.xls";
		response.setContentType("text/html;charset=utf-8");   
	    request.setCharacterEncoding("UTF-8");   
	    java.io.BufferedInputStream bis = null;   
	    java.io.BufferedOutputStream bos = null; 
	    try{
	        long fileLength= file.length();
	        response.setContentType("application/vnd.ms-excel");
	        response.setHeader("Content-disposition", "attachment; filename="  
                    + new String(fileName.getBytes("utf-8"), "ISO8859-1")); 
	        response.setHeader("Content-Length", String.valueOf(fileLength)); 
	        bis = new BufferedInputStream(new FileInputStream(downLoadPath));   
            bos = new BufferedOutputStream(response.getOutputStream());   
            byte[] buff = new byte[2048];   
            int bytesRead;   
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {   
                bos.write(buff, 0, bytesRead);   
            }  
	        }
	    catch(Exception e){
	        	
	        }
	    finally{
	        	if (bis != null)   
	                bis.close();   
	            if (bos != null)   
	                bos.close();
	        }
		return  null;
	}

}
