package cn.com.incito.interclass.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import cn.com.incito.interclass.ui.screencapture.CaptureScreen;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

public class TrayPopMenu extends JPopupMenu {
	Component context;

	public TrayPopMenu() {
		super();
		initComponent();
	}

	public TrayPopMenu(String label) {
		super(label);
		initComponent();
	}

	public TrayPopMenu(Component component) {
		context = component;
		initComponent();

	}

	public void initComponent() {
		JMenuItem item1 = new JMenuItem("发作业");
		item1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// distributePaper();
				CaptureScreen captureScreen = new CaptureScreen(context);
				captureScreen.doStart();

			}
		});
		add(item1);
		JMenuItem item2 = new JMenuItem("收作业");
		item2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// distributePaper();
				collectPaper();

			}
		});
		add(item2);
	}
	/**
	 * 老师主动收作业
	 */
	public void collectPaper() {

		MessagePacking messagePacking = new MessagePacking(
				Message.MESSAGE_SAVE_PAPER);
		JSONObject json = new JSONObject();
		json.put("id", Application.getInstance().getQuizId());
		messagePacking.putBodyData(DataType.INT,
				BufferUtils.writeUTFString(json.toString()));
		CoreSocket.getInstance().sendMessage(messagePacking.pack().array());
	}
}
