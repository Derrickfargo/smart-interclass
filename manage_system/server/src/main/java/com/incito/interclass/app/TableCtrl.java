package com.incito.interclass.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incito.base.exception.AppException;
import com.incito.interclass.business.DeviceService;
import com.incito.interclass.business.TableService;
import com.incito.interclass.common.BaseCtrl;

@RestController
@RequestMapping("/api/table")
public class TableCtrl extends BaseCtrl {

	private static final int SAVE_TABLE_ERROR = 1;
	private static final int ADD_DEVICE_ERROR = 2;

	@Autowired
	private TableService tableService;
	@Autowired
	private DeviceService deviceService;

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
	@RequestMapping(value = "/addDevice", produces = { "application/json;charset=UTF-8" })
	public String addDevice(int roomId, String number, String imei) {
		try {
			if (tableService.addDevice(roomId, number, imei)) {
				return renderJSONString(ADD_DEVICE_ERROR);
			} else {
				return renderJSONString(SUCCESS);
			}
		} catch (AppException e) {
			return renderJSONString(ADD_DEVICE_ERROR);
		}
	}
}
