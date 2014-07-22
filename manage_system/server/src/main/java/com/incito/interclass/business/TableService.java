package com.incito.interclass.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incito.base.exception.AppException;
import com.incito.interclass.entity.Device;
import com.incito.interclass.entity.Table;
import com.incito.interclass.persistence.DeviceMapper;
import com.incito.interclass.persistence.TableMapper;

@Service
public class TableService {

	@Autowired
	private TableMapper tableMapper;
	@Autowired
	private DeviceMapper deviceMapper;

	public List<Table> getTableList() {
		return tableMapper.getTableList();
	}

	public boolean saveTable(Table table) {
		int id = (Integer) tableMapper.save(table);
		return id != 0;
	}

	public void deleteTable(int tableId) {
		tableMapper.delete(tableId);
	}

	@Transactional(rollbackFor = AppException.class)
	public boolean addDevice(int roomId, String number, String imei) throws AppException {
		Table table = tableMapper.getTableByNumber(number);
		if (table == null || table.getId() == 0) {
			table = new Table();
			table.setRoomId(roomId);
			table.setNumber(number);
			tableMapper.save(table);
			if (table.getId() <= 0) {
				throw AppException.database(0);
			}
		}
		Device device = new Device();
		device.setImei(imei);
		device.setTableId(table.getId());
		deviceMapper.save(device);
		if (device.getId() <= 0) {
			throw AppException.database(0);
		}
		return true;
	}
}
