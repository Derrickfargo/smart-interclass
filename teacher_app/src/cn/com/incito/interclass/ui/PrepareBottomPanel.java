package cn.com.incito.interclass.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.interclass.constant.Constants;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.UIHelper;

public class PrepareBottomPanel extends JPanel {
	private static final long serialVersionUID = -9135075807085951600L;
	private JButton btnBegin;
	private Application app = Application.getInstance();

	public PrepareBottomPanel() {
		setSize(878, 48);
		setLayout(null);
		setOpaque(false);

		JLabel lblExpected = new JLabel("应到 %d 人  | 实到 %d 人", JLabel.CENTER);
		lblExpected.setForeground(UIHelper.getDefaultFontColor());
		lblExpected.setBounds(10, 15, 150, 35);
		add(lblExpected);

		JPanel pnlClass = new JPanel() {
			private static final long serialVersionUID = 5365972834168199801L;

			@Override
			protected void paintComponent(Graphics g) {
				Image iconClass = new ImageIcon("images/main/bg_input_kc.png")
						.getImage();
				g.drawImage(iconClass, 0, 0, this.getWidth(), this.getHeight(),
						this);
			}
		};
		pnlClass.setLayout(null);
		add(pnlClass);
		pnlClass.setBounds(180, 10, 120, 35);

		JPanel pnlCourse = new JPanel() {
			private static final long serialVersionUID = 5365972834168199801L;

			@Override
			protected void paintComponent(Graphics g) {
				Image iconClass = new ImageIcon("images/main/bg_input_kc.png")
						.getImage();
				g.drawImage(iconClass, 0, 0, this.getWidth(), this.getHeight(),
						this);
			}
		};
		pnlCourse.setLayout(null);
		add(pnlCourse);
		pnlCourse.setBounds(320, 10, 120, 35);

		btnBegin = new JButton();// 创建按钮对象
		btnBegin.setFocusPainted(false);
		btnBegin.setBorderPainted(false);// 设置边框不可见
		btnBegin.setContentAreaFilled(false);// 设置透明
		ImageIcon btnImage = new ImageIcon("images/main/btn_begin.png");
		btnBegin.setIcon(btnImage);// 设置图片
		add(btnBegin);// 添加按钮
		btnBegin.setBounds(470, -4, btnImage.getIconWidth(),
				btnImage.getIconHeight());
		btnBegin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (app.operationState == Constants.STATE_PROCESSING) {

				} else {
					doBegin();
				}

			}
		});
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
			} else if (JOptionPane.NO_OPTION == result) {
				MainFrame.getInstance().setVisible(false);
				// 开始上课
				app.isOnClass =true;
				ImageIcon btnImage = new ImageIcon(
						"images/main/btn_begin_hover.png");
				btnBegin.setIcon(btnImage);// 设置图片
			}
		} else {
			// TODO JOptionPane.showMessageDialog(getParent().getParent(),
			// "哈哈");
		}
	}

	public void setOnClass(boolean state) {
		ImageIcon btnImage;
		if (state) {
			btnImage = new ImageIcon("images/main/btn_begin_hover.png");
			app.isOnClass = true;
		} else {
			btnImage = new ImageIcon("images/main/btn_begin.png");
			app.isOnClass = false;
		}
		btnBegin.setIcon(btnImage);// 设置图片
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
}
