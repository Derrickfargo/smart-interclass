package cn.com.incito.interclass.ui;

/**
 * 互动课堂主界面
 * @author 刘世平
 */
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.interclass.Listener.MySystemTrayEvent;
import cn.com.incito.interclass.Listener.MySystemTrayManager;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

public class BaseFrame extends JFrame {

	protected Application app = Application.getInstance();
	private MySystemTrayEvent mySystemTrayEvent;

	public BaseFrame() {
		mySystemTrayEvent = new MySystemTrayManager();
	}

	public void setVisible(boolean show) {
		if (show) {
			this.show();
		} else {
			this.hide();
		}

	}

	public MySystemTrayEvent getMySystemTrayEvent() {
		return mySystemTrayEvent;
	}

}
