package cn.com.incito.interclass.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
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
	private JButton btnBegin, btnGroup;
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
		lblExpected.setBounds(5, 15, 150, 35);
		add(lblExpected);
		lblExpected.setVisible(false);
		
		ImageIcon iconClass = new ImageIcon("images/main/btn_gray.png");
		lblClass = new JLabel("", JLabel.CENTER);
		lblClass.setText(app.getClasses().getClassName());
		lblClass.setForeground(UIHelper.getDefaultFontColor());
		add(lblClass);
		lblClass.setVisible(false);
		lblClass.setBounds(130, 0, iconClass.getIconWidth(), iconClass.getIconHeight() - 4);
		lblClassBackground = new JLabel();
		lblClassBackground.setIcon(iconClass);
		add(lblClassBackground);
		lblClassBackground.setVisible(false);
		lblClassBackground.setBounds(130, -4, iconClass.getIconWidth(), iconClass.getIconHeight());
		
		ImageIcon iconCourse = new ImageIcon("images/main/btn_gray.png");
		lblCourse = new JLabel("", JLabel.CENTER);
		lblCourse.setText(app.getCourse().getName());
		lblCourse.setForeground(UIHelper.getDefaultFontColor());
		add(lblCourse);
		lblCourse.setVisible(false);
		lblCourse.setBounds(290, 0, iconCourse.getIconWidth(), iconCourse.getIconHeight() -4 );
		lblCourseBackground = new JLabel();
		lblCourseBackground.setIcon(iconCourse);
		add(lblCourseBackground);
		lblCourseBackground.setVisible(false);
		lblCourseBackground.setBounds(290, -4, iconCourse.getIconWidth(), iconCourse.getIconHeight());

		btnGroup = new JButton();// 创建按钮对象
		btnGroup.setFocusPainted(false);
		btnGroup.setBorderPainted(false);// 设置边框不可见
		btnGroup.setContentAreaFilled(false);// 设置透明
		ImageIcon iconGroup = new ImageIcon("images/main/btn_group.png");
		btnGroup.setIcon(iconGroup);// 设置图片
		add(btnGroup);// 添加按钮
		btnGroup.setBounds(450, -4, iconGroup.getIconWidth(), iconGroup.getIconHeight());
		btnGroup.addMouseListener(this);
		btnGroup.setVisible(false);
		
		btnBegin = new JButton();// 创建按钮对象
		btnBegin.setFocusPainted(false);
		btnBegin.setBorderPainted(false);// 设置边框不可见
		btnBegin.setContentAreaFilled(false);// 设置透明
		ImageIcon btnImage = new ImageIcon("images/main/btn_begin.png");
		btnBegin.setIcon(btnImage);// 设置图片
		add(btnBegin);// 添加按钮
		btnBegin.setBounds(610, -4, btnImage.getIconWidth(), btnImage.getIconHeight());
		btnBegin.addMouseListener(this);
		btnBegin.setVisible(false);
		
//		btnRdmGroup = new JButton("随机分组");//创建随机分组按钮
//		btnRdmGroup.setFocusable(false);
//		btnRdmGroup.setBorderPainted(false);
//		ImageIcon rdmImage = new ImageIcon("images/main/btn_randomization.png");
//		btnRdmGroup.setIcon(rdmImage);
//		btnRdmGroup.setBounds(770, -4, rdmImage.getIconWidth(),rdmImage.getIconHeight());
//		add(btnRdmGroup);
//		btnRdmGroup.addMouseListener(this);
//		btnRdmGroup.setVisible(false);
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
//			btnRdmGroup.setVisible(true);
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
		MainFrame.getInstance().setVisible(false);
		setOnClass(true); 
	}

	private void doEditGroup() {
		if(Application.isOnResponder){
			JOptionPane.showMessageDialog(getParent(), "学生正在抢答，不能编辑分组！");
			return;
		}
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
			Application.isOnClass = true;
			Application.getInstance().setLessionid(
					UUID.randomUUID().toString());
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
		if(e.getSource()==btnRdmGroup){
			doRdmGroup();
		}
	}
	
	/**
	 * 随机分组
	 * @author caicai
	 */
	private void doRdmGroup() {
		if(app.isDoRdmGrouping()){
			JOptionPane.showMessageDialog(getParent().getParent(), "学生正在随机分组，请等待分组完毕！");
			return;
		}
		if(Application.isOnResponder){
			JOptionPane.showMessageDialog(getParent().getParent(), "学生正在抢答，请等待抢答完毕！");
			return;
		}
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
			app.setGrouping(false);
		}
		if (Application.hasQuiz) {// 格式不一致，统一修改重构
			Application.hasQuiz=false;
		}
		if(Application.isOnClass){//变换上课按钮
			setOnClass(false);
		}
		// 发送小组信息
		app.setDoRdmGrouping(true);
		List<Group> groupList= getTableGroupList();
		Queue<List<Student>> students =  RdmGroup.getStudentQue();
		Map<String,List<Student>> imeiStudents = new HashMap<String, List<Student>>();
		int i=0;
		for (Group group : groupList) {//遍历小组发送分组消息
			group.setName("梦想小组"+i);
			group.setLogo("rainbow");
			List<Student> groupStudent = new ArrayList<Student>();
			List<Device> devices = group.getOnlineDevice();
			for(Device device:devices){
				List<Student> student = new ArrayList<Student>();
				Map<String, Object> rdmMsg = new HashMap<String, Object>();
				student=students.poll();
				groupStudent.addAll(student);
//				rdmMsg.put("group", group);发送小组信息，暂禁止
				rdmMsg.put("students", student);
				try {
					ApiClient.updateGroup(group.getId(), "梦想小组"+i, "rainbow");
				} catch (AppException e) {
					logger.info("随机分组失败："+group.getId());
					e.printStackTrace();
					return;
				}
				for(Student updateStu:student){
					try {
						ApiClient.loginForStudent(updateStu.getName(), updateStu.getSex(),updateStu.getNumber(), device.getImei());
					} catch (AppException e) {
						logger.info("随机分组更新学生小组失败");
						e.printStackTrace();
						return;
					}
				}
				imeiStudents.put(device.getImei(), app.addRdmStudent( student));
				MessagePacking rdmPacking = new MessagePacking(Message.MESSAGE_GROUP_RDMGROUP);
				rdmPacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(JSON.toJSONString(rdmMsg)));
				SocketChannel clientChannel = app.getClientChannel().get(device.getImei());
				sendMsgToClient(rdmPacking,clientChannel);			
			}
			group.setStudents(groupStudent);
			app.addGroup(group);
			++i;
		}
		app.refreshIMEI(imeiStudents);
		app.refresh();
		app.setDoRdmGrouping(false);
		app.refresh();
	}
	/**
	 * 得到在线小组
	 * @return 在线小组
	 */
	private List<Group> getTableGroupList() {
		Set<String> devices = app.getOnlineDevice();
		List<Group> groupList= new ArrayList<Group>();
		//遍历divice，得到在线小组
		for(String device : devices){
			Device deviced = app.getImeiDevice().get(device);
			Table table=app.getDeviceTable().get(deviced.getId());
			Group group = app.getTableGroup().get(table.getId());
			if(groupList.size()==0){
				group.getOnlineDevice().add(deviced);
				groupList.add(group);
			}else{
				boolean flag=true;
				for(Group grouped: groupList){
					if(group.getId()==grouped.getId()){
						grouped.getOnlineDevice().add(deviced);
						flag=false;
					}
				}
				if(flag){
					group.getOnlineDevice().add(deviced);
					groupList.add(group);
				}
			}
		}
		
		return groupList;
	}

	/**
	 * 每个pad启动一个线程发送随机
	 * 分组信息
	 * @param rdmPacking
	 * @param clientChannel
	 */
	private void sendMsgToClient(final MessagePacking rdmPacking,
			final SocketChannel clientChannel) {
		new Thread(){
			@Override
			public void run(){
				byte[] data=rdmPacking.pack().array();
				ByteBuffer bytes =ByteBuffer.allocate(data.length);
				bytes.clear();
				bytes.put(data);
				bytes.flip();
				try {
					clientChannel.write(bytes);
					logger.info("随机分组信息："+new String(bytes.array()));
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
