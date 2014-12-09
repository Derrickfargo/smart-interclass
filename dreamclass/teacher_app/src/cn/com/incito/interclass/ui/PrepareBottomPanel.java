package cn.com.incito.interclass.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.ApiClient;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.exception.AppException;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.RdmGroup;
import cn.com.incito.server.utils.UIHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class PrepareBottomPanel extends JPanel implements MouseListener{
	private static final long serialVersionUID = -9135075807085951600L;
	private JLabel lblExpected,lblClass,lblClassBackground,lblCourse,lblCourseBackground;
	private JButton btnBegin, btnGroup,btnResponder;
	private Application app = Application.getInstance();
	Logger logger =  Logger.getLogger(PrepareBottomPanel.class.getName());
	private JButton btnRdmGroup;
	public JLabel getLblExpected() {
		return lblExpected;
	}

	public PrepareBottomPanel(){
		setSize(878, 48);
		setLayout(null);
		setOpaque(false);
		
		int total = 0;
		for (Group group : app.getGroupList()) {
			total += group.getStudents().size();
		}
		String msg = "应到 %d 人  | 实到 %d 人";
		lblExpected = new JLabel(String.format(msg, total, app
				.getOnlineStudent().size()), JLabel.CENTER);
		lblExpected.setForeground(UIHelper.getDefaultFontColor());
		lblExpected.setBounds(10, 15, 150, 35);
		add(lblExpected);
		lblExpected.setVisible(false);
		
		ImageIcon iconClass = new ImageIcon("images/main/btn_gray.png");
		lblClass = new JLabel("", JLabel.CENTER);
		lblClass.setText(app.getClasses().getClassName());
		lblClass.setForeground(UIHelper.getDefaultFontColor());
		add(lblClass);
		lblClass.setVisible(false);
		lblClass.setBounds(180, 0, iconClass.getIconWidth(), iconClass.getIconHeight() - 4);
		lblClassBackground = new JLabel();
		lblClassBackground.setIcon(iconClass);
		add(lblClassBackground);
		lblClassBackground.setVisible(false);
		lblClassBackground.setBounds(180, -4, iconClass.getIconWidth(), iconClass.getIconHeight());
		
		ImageIcon iconCourse = new ImageIcon("images/main/btn_gray.png");
		lblCourse = new JLabel("", JLabel.CENTER);
		lblCourse.setText(app.getCourse().getName());
		lblCourse.setForeground(UIHelper.getDefaultFontColor());
		add(lblCourse);
		lblCourse.setVisible(false);
		lblCourse.setBounds(340, 0, iconCourse.getIconWidth(), iconCourse.getIconHeight() -4 );
		lblCourseBackground = new JLabel();
		lblCourseBackground.setIcon(iconCourse);
		add(lblCourseBackground);
		lblCourseBackground.setVisible(false);
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
		btnGroup.setVisible(false);
		
		btnBegin = new JButton();// 创建按钮对象
		btnBegin.setFocusPainted(false);
		btnBegin.setBorderPainted(false);// 设置边框不可见
		btnBegin.setContentAreaFilled(false);// 设置透明
		ImageIcon btnImage = new ImageIcon("images/main/btn_begin.png");
		btnBegin.setIcon(btnImage);// 设置图片
		add(btnBegin);// 添加按钮
		btnBegin.setBounds(660, -4, btnImage.getIconWidth(), btnImage.getIconHeight());
		btnBegin.addMouseListener(this);
		btnBegin.setVisible(false);
		
		btnResponder = new JButton("抢答");//创建抢答对象
		btnResponder.setFocusPainted(false);
		btnResponder.setBorderPainted(false);
//		btnResponder.setContentAreaFilled(false);
//		ImageIcon image = new ImageIcon();
//		btnResponder.setIcon(image);
		btnResponder.setBounds(810, -4, 40, 40);
		add(btnResponder);
		btnResponder.addMouseListener(this);
		btnResponder.setVisible(false);
		
		btnRdmGroup = new JButton("随机分组");//创建随机分组按钮
		btnRdmGroup.setFocusable(false);
		btnRdmGroup.setBorderPainted(false);
		btnRdmGroup.setBounds(810, -4, 70, 40);
		add(btnRdmGroup);
		btnRdmGroup.addMouseListener(this);
		btnRdmGroup.setVisible(false);
	}
	
	public void refresh(){
		List<Table> tables = app.getTableList();
		if (tables.size() != 0) {
			lblExpected.setVisible(true);
			lblClass.setVisible(true);
			lblClassBackground.setVisible(true);
			lblCourse.setVisible(true);
			lblCourseBackground.setVisible(true);
			btnGroup.setVisible(true);
			btnBegin.setVisible(true);
			btnRdmGroup.setVisible(true);
		}
	}
	private void doBegin() {
		if (app.isGrouping()) {
			int result = JOptionPane.showConfirmDialog(getParent().getParent(),
					"学生正在分组，是否立即结束分组开始上课？", "提示", JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION){
				app.setGrouping(false);
			}else{
				return;
			}
		}
		if(app.getOnlineStudent().size() == 0){
			JOptionPane.showMessageDialog(getParent().getParent(),
					"当前还没有学生登陆，请先登录后再上课!");
			return;
		}
		
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
		btnResponder.setVisible(true);
		Application.isOnResponder=false;
		MainFrame.getInstance().setVisible(false);
		setOnClass(true); 
	}

	private void doEditGroup() {
		if(app.getOnlineStudent().size() == 0){
			JOptionPane.showMessageDialog(getParent().getParent(), "当前还没有学生登陆，请先登陆后再分组!");
			return;
		}
		List<Table> tableList = app.getTableList();
		if (tableList == null || tableList.size() == 0) {
			JOptionPane.showMessageDialog(getParent().getParent(), "设备还未绑定课桌，请先绑定课桌!");
			return;
		}
		if (app.isGrouping()) {
			JOptionPane.showMessageDialog(getParent().getParent(), "学生正在分组，请等待分组完成!");
			return;
		}
		if (Application.hasQuiz) {//TODO 格式不一致，统一修改重构
			JOptionPane.showMessageDialog(getParent().getParent(), "学生正在做作业，不能分组!");
			return;
		}
		// 编辑小组信息
		app.setGrouping(true);
		MainFrame.getInstance().showGrouping();
		List<Group> groupList = app.getGroupList();
		for (Group group : groupList) {
			JSONObject json = new JSONObject();
			json.put("id", group.getId());
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_EDIT);
			messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json.toString()));
			final List<SocketChannel> channels = app.getClientChannelByGroup(group.getId());
			sendMessageToGroup(messagePacking, channels);
		}
	}

	public void setOnClass(boolean isOnClass) {
		UIHelper.sendLockScreenMessage(true);
		if (isOnClass) { 
			btnBegin.setIcon(new ImageIcon("images/main/btn_end.png"));// 设置图片
			btnResponder.setVisible(true);//设置抢答按钮可见
			Application.isOnClass = true;
			Application.getInstance().setLessionid(
					UUID.randomUUID().toString());
		} else {
			btnBegin.setIcon(new ImageIcon("images/main/btn_begin.png"));// 设置图片
			btnResponder.setVisible(false);//设置抢答按钮不可见
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
				int result = JOptionPane.showConfirmDialog(MainFrame.getInstance()
						.getFrame(), "确定要下课吗？", "提示",
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
		if(e.getSource() == btnGroup){
			doEditGroup();
		}
		if(e.getSource()==btnResponder){
			if(!Application.isOnResponder){
			UIHelper.sendResponderMessage(true);
			Application.isOnResponder=true;
			}
			else{
				JOptionPane.showMessageDialog(MainFrame.getInstance().getFrame(), "抢答尚未结束，请稍等！");
			}
		}
		if(e.getSource()==btnRdmGroup){
			doRdmGroup();
		}
	}

	private void doRdmGroup() {
		if(app.getOnlineStudent().size() == 0){
			JOptionPane.showMessageDialog(getParent().getParent(), "当前还没有学生登陆，请先登陆后再随机分组!");
			return;
		}
		List<Table> tableList = app.getTableList();
		if (tableList == null || tableList.size() == 0) {
			JOptionPane.showMessageDialog(getParent().getParent(), "设备还未绑定课桌，请先绑定课桌!");
			return;
		}
		if (app.isGrouping()) {
			JOptionPane.showMessageDialog(getParent().getParent(), "学生正在分组，请等待分组完成!");
			return;
		}
		if (Application.hasQuiz) {// 格式不一致，统一修改重构
			JOptionPane.showMessageDialog(getParent().getParent(), "学生正在做作业，不能分组!");
			return;
		}
		// 发送小组信息
		app.setGrouping(true);
		List<Group> groupList = app.getGroupList();//要确定是在线小组，而且contain在线device;
		Queue<List<Student>> students =  RdmGroup.getStudentQue();
		
		for (Group group : groupList) {//遍历小组发送分组消息
			int i=0;
			group.setName("梦想小组"+i);
			group.setLogo("rainbow");
			app.addGroup(group);
			List<Device> devices = group.getDevices();
			for(Device device:devices){
				Map<String, Object> rdmMsg = new HashMap<String, Object>();
				rdmMsg.put("group", group);
				rdmMsg.put("students", students.poll());
				try {
					ApiClient.updateGroup(group.getId(), "梦想小组"+i, "rainbow");
				} catch (AppException e) {
					logger.info("随机分组失败："+group.getId());
					e.printStackTrace();
				}
				
				MessagePacking rdmPacking = new MessagePacking(Message.MESSAGE_GROUP_RDMGROUP);
				rdmPacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(JSON.toJSONString(rdmMsg)));
				SocketChannel clientChannel = app.getClientChannel().get(device.getImei());
				sendMsgToClient(rdmPacking,clientChannel);			
			}
			++i;
		}
		MainFrame.getInstance().showGrouping();//注意这可能是要修改的地方
		app.refresh();
		app.setGrouping(false);
		
	}

	private void sendMsgToClient(final MessagePacking rdmPacking,
			final SocketChannel clientChannel) {
		new Thread(){
			@Override
			public void run(){
				byte[] data=rdmPacking.pack().array();
				ByteBuffer bytes =ByteBuffer.allocate(data.length);
//				if(clientChannel.isConnected()){
//					return;
//				}
				bytes.clear();
				bytes.put(data);
				bytes.flip();
				try {
					clientChannel.write(bytes);
				} catch (IOException e) {
					logger.info("随机分组消息发送失败！"+new String(data));
					e.printStackTrace();
				}
			}
		}.start();;
		
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
				btnBegin.setIcon(new ImageIcon("images/main/btn_begin_hover.png"));
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
