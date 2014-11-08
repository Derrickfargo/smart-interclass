package cn.com.incito.interclass.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.ApiClient;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.exception.AppException;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.UIHelper;

import com.alibaba.fastjson.JSONObject;

public class PrepareBottomPanel extends JPanel implements MouseListener {
	private static final long serialVersionUID = -9135075807085951600L;
	private JLabel lblExpected, lblClass, lblClassBackground, lblCourse,
			lblCourseBackground;
	private JButton btnBegin, btnGroup;
	private Application app = Application.getInstance();
	Logger logger = Logger.getLogger(PrepareBottomPanel.class.getName());

	public JLabel getLblExpected() {
		return lblExpected;
	}

	public PrepareBottomPanel() {
		setSize(878, 48);
		setLayout(null);
		setOpaque(false);

		String msg = "应到 %d 人  | 实到 %d 人";
		lblExpected = new JLabel(String.format(msg, app.getStudentList().size(), 
				app.getOnlineStudent().size()), JLabel.CENTER);
		lblExpected.setForeground(UIHelper.getDefaultFontColor());
		lblExpected.setBounds(10, 15, 150, 35);
		add(lblExpected);
		lblExpected.setVisible(false);

		ImageIcon iconClass = new ImageIcon("images/main/btn_gray.png");
		lblClass = new JLabel("", JLabel.CENTER);
		lblClass.setText(app.getClasses().getName());
		lblClass.setForeground(UIHelper.getDefaultFontColor());
		add(lblClass);
		lblClass.setVisible(false);
		lblClass.setBounds(180, 0, iconClass.getIconWidth(),
				iconClass.getIconHeight() - 4);
		lblClassBackground = new JLabel();
		lblClassBackground.setIcon(iconClass);
		add(lblClassBackground);
		lblClassBackground.setVisible(false);
		lblClassBackground.setBounds(180, -4, iconClass.getIconWidth(),
				iconClass.getIconHeight());

		ImageIcon iconCourse = new ImageIcon("images/main/btn_gray.png");
		lblCourse = new JLabel("", JLabel.CENTER);
		lblCourse.setText(app.getCourse().getName());
		lblCourse.setForeground(UIHelper.getDefaultFontColor());
		add(lblCourse);
		lblCourse.setVisible(false);
		lblCourse.setBounds(340, 0, iconCourse.getIconWidth(),
				iconCourse.getIconHeight() - 4);
		lblCourseBackground = new JLabel();
		lblCourseBackground.setIcon(iconCourse);
		add(lblCourseBackground);
		lblCourseBackground.setVisible(false);
		lblCourseBackground.setBounds(340, -4, iconCourse.getIconWidth(),
				iconCourse.getIconHeight());

		btnGroup = new JButton();// 创建按钮对象
		btnGroup.setFocusPainted(false);
		btnGroup.setBorderPainted(false);// 设置边框不可见
		btnGroup.setContentAreaFilled(false);// 设置透明
		ImageIcon iconGroup = new ImageIcon("images/main/btn_group.png");
		btnGroup.setIcon(iconGroup);// 设置图片
		add(btnGroup);// 添加按钮
		btnGroup.setBounds(500, -4, iconGroup.getIconWidth(),
				iconGroup.getIconHeight());
		btnGroup.addMouseListener(this);
		btnGroup.setVisible(false);

		btnBegin = new JButton();// 创建按钮对象
		btnBegin.setFocusPainted(false);
		btnBegin.setBorderPainted(false);// 设置边框不可见
		btnBegin.setContentAreaFilled(false);// 设置透明
		ImageIcon btnImage = new ImageIcon("images/main/btn_begin.png");
		btnBegin.setIcon(btnImage);// 设置图片
		add(btnBegin);// 添加按钮
		btnBegin.setBounds(660, -4, btnImage.getIconWidth(),
				btnImage.getIconHeight());
		btnBegin.addMouseListener(this);
		btnBegin.setVisible(false);
		refresh();
	}

	public void refresh() {
//		List<Table> tables = app.getTableList();
//		if (tables.size() != 0) {
			lblExpected.setVisible(true);
			lblClass.setVisible(true);
			lblClassBackground.setVisible(true);
			lblCourse.setVisible(true);
			lblCourseBackground.setVisible(true);
			btnGroup.setVisible(true);
			btnBegin.setVisible(true);
//		}
	}

	private void doBegin() {
		if (app.getOnlineStudent().size() == 0) {
			JOptionPane.showMessageDialog(getParent().getParent(), "当前还没有学生登陆，请先登录后再上课!");
			return;
		}
		if(app.getGroupList().size() == 0){
			JOptionPane.showMessageDialog(getParent().getParent(), "学生还未分组，请先分组后再上课!");
			return;
		}
		Set<Student> noGroupStudent = getNoGroupStudent();
		if (noGroupStudent.size() != 0) {
			String message = "还有以下学生未加入小组：\n%s是否要立即结束分组开始上课？";
			StringBuffer args = new StringBuffer();
			Iterator<Student> it = noGroupStudent.iterator();
			while (it.hasNext()) {
				args.append(it.next().getName());
				args.append("、");
			}
			args.append("\n");
			int result = JOptionPane.showConfirmDialog(getParent().getParent(), 
					String.format(message, args), "提示", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				app.setGrouping(false);
			} else {
				return;
			}
		}
		app.setGrouping(false);//TODO 新增
		MainFrame.getInstance().setVisible(false);
		setOnClass(true);
	}

	private Set<Student> getNoGroupStudent() {
		Set<Student> onGroupStudents = new HashSet<Student>();
		Set<Student> onlines = app.getOnlineStudent();
		Iterator<Student> it = onlines.iterator();
		while (it.hasNext()) {
			onGroupStudents.add(it.next());
		}
		Set<Group> groups = app.getGroupList();
		for (Group group : groups) {
			List<Student> studentList = group.getStudents();
			for (Student student : studentList) {
				onGroupStudents.remove(student);
			}
		}
		return onGroupStudents;
	}
	
	/**
	 * 发送分组命令
	 */
	private void doEditGroup() {
		if (app.getOnlineStudent().size() == 0) {
			JOptionPane.showMessageDialog(getParent().getParent(),
					"当前还没有学生登陆，请先登陆后再分组!");
			return;
		}
		if (app.isGrouping()) {
			JOptionPane.showMessageDialog(getParent().getParent(),
					"学生正在分组，请等待分组完成!");
			return;
		}
		if (Application.hasQuiz) {// TODO 格式不一致，统一修改重构
			JOptionPane.showMessageDialog(getParent().getParent(),
					"学生正在做作业，不能分组!");
			return;
		}
		if(!doDeleteGroup()){//先删除分组
			
		}
		app.getGroupList().clear();
		app.getTempGroup().clear();
		app.refresh();
		// 编辑小组信息
		app.setGrouping(true);
//		MainFrame.getInstance().showGrouping();
		Set<Group> groupList = app.getGroupList();
//		for (Group group : groupList) {
			JSONObject json = new JSONObject();
			json.put("data", groupList);
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_CREATE);
			messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(json.toString()));
			Map<String, SocketChannel> channels = app.getClientChannel();
			List<SocketChannel> channelList = new ArrayList<SocketChannel>();
			Set<Entry<String, SocketChannel>> set = channels.entrySet();
			for (Iterator<Entry<String, SocketChannel>> iter = set.iterator(); iter.hasNext();) {
				Map.Entry<String, SocketChannel> entry = (Map.Entry<String, SocketChannel>) iter.next();
				SocketChannel value = (SocketChannel) entry.getValue();
				channelList.add(value);
//			}
			sendMessageToGroup(messagePacking, channelList);
		}
	}

	private boolean doDeleteGroup() {
		int teacherId = app.getTeacher().getId();
		int classId = app.getClasses().getId();
		try {
			ApiClient.deleteGroup(teacherId, classId);
			return true;
		} catch (AppException e) {
			return false;
		}
	}
	
	public void setOnClass(boolean isOnClass) {
		UIHelper.sendLockScreenMessage(true);
		if (isOnClass) {
			btnBegin.setIcon(new ImageIcon("images/main/btn_end.png"));// 设置图片
			Application.isOnClass = true;
			Application.getInstance()
					.setLessionid(UUID.randomUUID().toString());
		} else {
			btnBegin.setIcon(new ImageIcon("images/main/btn_begin.png"));// 设置图片
			Application.isOnClass = false;
		}

	}

	private void sendMessageToGroup(final MessagePacking messagePacking,
			final List<SocketChannel> channels) {
		if (channels == null) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				byte[] data = messagePacking.pack().array();
				ByteBuffer buffer = ByteBuffer.allocate(data.length);
				Iterator<SocketChannel> it = channels.iterator();
				while (it.hasNext()) {
					SocketChannel channel = it.next();
					if (!channel.isConnected()) {
						it.remove();
						continue;
					}
					buffer.clear();
					buffer.put(data);
					buffer.flip();
					try {
						channel.write(buffer);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == btnBegin) {
			if (Application.isOnClass) {
				int result = JOptionPane.showConfirmDialog(MainFrame
						.getInstance().getFrame(), "确定要下课吗？", "提示",
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					UIHelper.sendClassOverMessage();
					logger.info("下课信息发出");
					System.exit(0);
				}
			} else {
				doBegin();
			}
		}
		if (e.getSource() == btnGroup) {
			doEditGroup();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == btnBegin) {
			if (Application.isOnClass) {
				btnBegin.setIcon(new ImageIcon("images/main/btn_end_hover.png"));
			} else {
				btnBegin.setIcon(new ImageIcon(
						"images/main/btn_begin_hover.png"));
			}
		}
		if (e.getSource() == btnGroup) {
			btnGroup.setIcon(new ImageIcon("images/main/btn_group_hover.png"));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == btnBegin) {
			if (Application.isOnClass) {
				btnBegin.setIcon(new ImageIcon("images/main/btn_end.png"));
			} else {
				btnBegin.setIcon(new ImageIcon("images/main/btn_begin.png"));
			}
		}
		if (e.getSource() == btnGroup) {
			btnGroup.setIcon(new ImageIcon("images/main/btn_group.png"));
		}
	}

}
