package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.incito.interclass.business.RoomService;
import com.incito.interclass.business.SchoolService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Room;
import com.incito.interclass.entity.School;
import com.incito.parent.pagehelper.PageHelper;
import com.incito.parent.pagehelper.PageInfo;

@RestController
@RequestMapping("/room")
public class RoomCtrl extends BaseCtrl {

	@Autowired
	private RoomService roomService;
	
	@Autowired
	private SchoolService schoolService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public ModelAndView list(String schoolName,String mac,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
		ModelAndView res = new ModelAndView("room/roomList");
		PageHelper.startPage(pageNum, PAGE_SIZE);
		List<Room> rooms = roomService.getRoomListByByCondition(schoolName,mac);
		PageInfo<Room> page = new PageInfo<Room>(rooms);
		res.addObject("page", page);
		res.addObject("schoolName", schoolName);
		res.addObject("mac", mac);
		return res;
	}
	
	/**
	 * 添加
	 */
	@RequestMapping("/add")
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView("room/roomAdd");
		return mav;
	}
	
	/**
	 * 保存
	 * @return
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save( Room room,String schoolName,Model model) {
		int schoolId = schoolService.getSchoolIdByName(schoolName);
		System.out.println(schoolId);
		room.setSchoolId(schoolId);
		roomService.saveRoom(room);
		return new ModelAndView("redirect:list");
		
	}
	
	@RequestMapping(value = "/delete")
	public ModelAndView delete(int roomId) {
		roomService.deleteRoom(roomId);
		return new ModelAndView("redirect:list");
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/edit")
	public ModelAndView edit(Integer roomId) {
		ModelAndView res = new ModelAndView("room/roomEdit");
		Room room = roomService.getRoomById(roomId);
		res.addObject("room", room);
		return res;
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public ModelAndView update(Room room) {
		boolean result = roomService.update(room);
		if (result) {
			return new ModelAndView("redirect:list");
		}
		return new ModelAndView("room/roomEdit");
	}
	
	/**
	 * 模糊查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "/search", produces = { "application/json;charset=UTF-8" })
	public String search(String name,int pageNum){
		List<School> room=roomService.search(name,pageNum);
		return JSON.toJSONString(room);
	}
}
