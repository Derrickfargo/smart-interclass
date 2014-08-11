package cn.com.incito.interclass.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

public class TrayPopMenu extends JPopupMenu {

	public TrayPopMenu() {
		super();
		initComponent();
	}

	public TrayPopMenu(String label) {
		super(label);
		initComponent();
	}

	public void initComponent() {
		JMenuItem item = new JMenuItem("发作业");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				distributePaper();

			}
		});
		add(item);
	}

	/**
	 * 分发试卷
	 */
	private void distributePaper() {

		MessagePacking messagePacking = new MessagePacking(
				Message.MESSAGE_DISTRIBUTE_PAPER);
		JSONObject json = new JSONObject();
		String uuid = UUID.randomUUID().toString();
		Application.getInstance().setQuizId(uuid);
		json.put("id", uuid);
		json.put("paper", "");
		messagePacking.putBodyData(DataType.INT,
				BufferUtils.writeUTFString(json.toString()));
		CoreSocket.getInstance().sendMessage(messagePacking.pack().array());
	}
}
