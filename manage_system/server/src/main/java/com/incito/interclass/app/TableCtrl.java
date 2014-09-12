package com.incito.interclass.app;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incito.base.exception.AppException;
import com.incito.interclass.business.DeviceService;
import com.incito.interclass.business.GroupService;
import com.incito.interclass.business.TableService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Group;
import com.incito.interclass.entity.Table;

@RestController
@RequestMapping("/api/table")
public class TableCtrl extends BaseCtrl {

	private static final int SAVE_TABLE_ERROR = 1;
	private static final int ADD_DEVICE_ERROR = 2;

	@Autowired
	private TableService tableService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private GroupService groupService;

	/**
	 * 判断设备是否已绑定
	 * 
	 * @param roomId
	 *            教室id
	 * @param imei
	 *            设备号imei
	 * @return
	 */
	@RequestMapping(value = "/hasBind", produces = { "application/json;charset=UTF-8" })
	public String hasBind(int roomId, String imei) {
		try {
			Map<String,Object> data = new HashMap<String,Object>();
			Table table = tableService.hasBind(roomId, imei);
			if (table == null || table.getId() == 0) {
				data.put("isbind", Boolean.FALSE);
				return renderJSONString(SUCCESS, data);
			} else {
				data.put("isbind", Boolean.TRUE);
				data.put("desknum", table.getId());
				return renderJSONString(SUCCESS, data);
			}
		} catch (AppException e) {
			return renderJSONString(ADD_DEVICE_ERROR);
		}
	}
	
	 
	
	/**
	 * 添加设备到课桌
	 * 
	 * @param roomId
	 *            教室id
	 * @param number
	 *            课桌号
	 * @param imei
	 *            设备号imei
	 * @return
	 */
	@RequestMapping(value = "/bind", produces = { "application/json;charset=UTF-8" })
	public String addDevice(int roomId, int number, String imei, int teacherId, int courseId, int classId) {
		try {
			int tableId = tableService.addDevice(roomId, number, imei);
			if (tableId > 0) {
				// 检查组是否存在
				Group group = groupService.getGroupByTableId(tableId, teacherId, courseId, classId);
				if (group == null || group.getId() == 0) {
					group = new Group();
					group.setClassId(classId);
					group.setCourseId(courseId);
					group.setTeacherId(teacherId);
					group.setTableId(tableId);
					groupService.save(group);// 创建分组
				}
				return renderJSONString(SUCCESS, group);
			}
			return renderJSONString(ADD_DEVICE_ERROR);
		} catch (AppException e) {
			return renderJSONString(ADD_DEVICE_ERROR);
		}
	}
}
