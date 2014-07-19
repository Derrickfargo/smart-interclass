package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.incito.interclass.business.RoomService;
import com.incito.interclass.business.SchoolService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Room;
import com.incito.interclass.entity.School;

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
	public ModelAndView index(Room room,Integer page) {
		ModelAndView res = new ModelAndView("room/roomList");
		if (page == null) {
			page = 0;
		}
		List<Room> rooms = roomService.getRoomList();
		res.addObject("rooms", rooms);
		return res;
	}
	
	/**
	 * 添加
	 */
	@RequestMapping("/add")
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView("room/roomAdd");
		List<School> schools = schoolService.getSchoolList();
		mav.addObject("schools", schools);
		return mav;
	}
	
	/**
	 * 保存
	 * @return
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(Room room,Model model) {
		roomService.saveRoom(room);
		return new ModelAndView("redirect:list");
	}
	
	@RequestMapping(value = "/delete")
	public ModelAndView delete(int roomId) {
		roomService.deleteRoom(roomId);
		return new ModelAndView("redirect:list");
	}
}
