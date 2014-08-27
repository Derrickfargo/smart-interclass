package cn.com.incito.interclass.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.UIHelper;

import com.alibaba.fastjson.JSONObject;

public class PrepareBottomPanel extends JPanel implements MouseListener{
	private static final long serialVersionUID = -9135075807085951600L;
	private JButton btnBegin, btnGroup;
	private Application app = Application.getInstance();
	
	public PrepareBottomPanel(){
		setSize(878, 48);
		setLayout(null);
		setOpaque(false);
		
		JLabel lblExpected = new JLabel("应到 %d 人  | 实到 %d 人", JLabel.CENTER);
		lblExpected.setForeground(UIHelper.getDefaultFontColor());
		lblExpected.setBounds(10, 15, 150, 35);
		add(lblExpected);
		
		JLabel lblClassBackground = new JLabel();
		ImageIcon iconClass = new ImageIcon("images/main/btn_gray.png");
		lblClassBackground.setIcon(iconClass);
		add(lblClassBackground);
		lblClassBackground.setBounds(180, -4, iconClass.getIconWidth(), iconClass.getIconHeight());
		
		JLabel lblCourseBackground = new JLabel();
		ImageIcon iconCourse = new ImageIcon("images/main/btn_gray.png");
		lblCourseBackground.setIcon(iconCourse);
		add(lblCourseBackground);
		lblCourseBackground.setBounds(340, -4, iconCourse.getIconWidth(), iconCourse.getIconHeight());

		btnGroup = new JButton();// 创建按钮对象
		btnGroup.setFocusPainted(false);
		btnGroup.setBorderPainted(false);// 设置边框不可见
		btnGroup.setContentAreaFilled(false);// 设置透明
		ImageIcon iconGroup = new ImageIcon("images/main/btn_group.png");
		btnGroup.setIcon(iconGroup);// 设置图片
		add(btnGroup);// 添加按钮
		btnGroup.setBounds(500, -4, iconGroup.getIconWidth(), iconGroup.getIconHeight());
		btnGroup.addMouseListener(this);
		
		btnBegin = new JButton();// 创建按钮对象
		btnBegin.setFocusPainted(false);
		btnBegin.setBorderPainted(false);// 设置边框不可见
		btnBegin.setContentAreaFilled(false);// 设置透明
		ImageIcon btnImage = new ImageIcon("images/main/btn_begin.png");
		btnBegin.setIcon(btnImage);// 设置图片
		add(btnBegin);// 添加按钮
		btnBegin.setBounds(660, -4, btnImage.getIconWidth(), btnImage.getIconHeight());
		btnBegin.addMouseListener(this);
	}
	
	private void doBegin() {
		List<Table> tableList = app.getTableList();
		if (tableList == null || tableList.size() == 0) {
			JOptionPane.showMessageDialog(getParent().getParent(),
					"设备还未绑定课桌，请先绑定课桌!");
			return;
		}

		Map<Integer, Group> tableGroup = app.getTableGroup();
		if (tableGroup == null || tableGroup.size() == 0) {
			JOptionPane.showMessageDialog(getParent().getParent(),
					"还未进行小组分组，请先进行分组!");
			return;
		}

		boolean hasTeamInfo = true;
		for (Group group : tableGroup.values()) {
			if (group.getName() == null || group.getName().equals("")) {
				hasTeamInfo = false;
			}
		}
		if (!hasTeamInfo) {
			int result = JOptionPane.showConfirmDialog(MainFrame.getInstance()
					.getFrame(), "还有小组未编辑小组信息，是否编辑小组信息？", "提示",
					JOptionPane.YES_NO_OPTION);
			if (JOptionPane.YES_OPTION == result) {
				doEditGroup();
			} else if (JOptionPane.NO_OPTION == result) {
				MainFrame.getInstance().setVisible(false);
				// 开始上课
				setOnClass(true);
			}
		}else{
			MainFrame.getInstance().setVisible(false);
			setOnClass(true); 
			
		}
	}

	private void doEditGroup() {
		// 编辑小组信息
		List<Group> groupList = app.getGroupList();
		for (Group group : groupList) {
			JSONObject json = new JSONObject();
			json.put("id", group.getId());
			MessagePacking messagePacking = new MessagePacking(
					Message.MESSAGE_GROUP_EDIT);
			messagePacking.putBodyData(DataType.INT,
					BufferUtils.writeUTFString(json.toString()));
			final List<SocketChannel> channels = app
					.getClientChannelByGroup(group.getId());
			sendMessageToGroup(messagePacking, channels);
		}
	}

	public void setOnClass(boolean isOnClass) {
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_LOCK_SCREEN);
		messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString("true"));
		CoreSocket.getInstance().sendMessage(messagePacking.pack().array());
		
		if (isOnClass) { 
			btnBegin.setIcon(new ImageIcon("images/main/btn_begin_hover.png"));// 设置图片
			Application.isOnClass = true;
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
				setOnClass(false);
			} else {
				doBegin();
			}
		}
		if(e.getSource() == btnGroup){
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
			btnBegin.setIcon(new ImageIcon("images/main/btn_begin_hover.png"));
		}
		if (e.getSource() == btnGroup) {
			btnGroup.setIcon(new ImageIcon("images/main/btn_group_hover.png"));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == btnBegin) {
			btnBegin.setIcon(new ImageIcon("images/main/btn_begin.png"));
		}
		if (e.getSource() == btnGroup) {
			btnGroup.setIcon(new ImageIcon("images/main/btn_group.png"));
		}
	}
}
