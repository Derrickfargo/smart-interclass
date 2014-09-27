package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.incito.interclass.business.RoomService;
import com.incito.interclass.business.SchoolService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Room;
import com.incito.interclass.entity.School;
import com.incito.interclass.persistence.RoomMapper;

@RestController
@RequestMapping("/room")
public class RoomCtrl extends BaseCtrl {

	@Autowired
	private RoomService roomService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private RoomMapper roomMapper;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public ModelAndView query(Room room,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
		ModelAndView res = new ModelAndView("room/roomList");
		PageHelper.startPage(pageNum, PAGE_SIZE);
		List<Room> rooms = roomMapper.getRoomList();
		PageInfo<Room> page = new PageInfo<Room>(rooms);
		res.addObject("page", page);
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
