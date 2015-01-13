package cn.com.incito.interclass.ui;

import io.netty.channel.ChannelHandlerContext;

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

import sun.org.mozilla.javascript.internal.ast.ThrowStatement;
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
		lblExpected.setBounds(10, 15, 140, 35);
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
		btnGroup.setBounds(358, -4, iconGroup.getIconWidth(), iconGroup.getIconHeight());
		btnGroup.addMouseListener(this);
		btnGroup.setVisible(false);
		
		btnBegin = new JButton();// 创建按钮对象
		btnBegin.setFocusPainted(false);
		btnBegin.setBorderPainted(false);// 设置边框不可见
		btnBegin.setContentAreaFilled(false);// 设置透明
		ImageIcon btnImage = new ImageIcon("images/main/btn_begin.png");
		btnBegin.setIcon(btnImage);// 设置图片
		add(btnBegin);// 添加按钮
		btnBegin.setBounds(515, -4, btnImage.getIconWidth(), btnImage.getIconHeight());
		btnBegin.addMouseListener(this);
		btnBegin.setVisible(false);
		
		btnRdmGroup = new JButton("");//创建随机分组按钮
		btnRdmGroup.setFocusPainted(false);
		btnRdmGroup.setContentAreaFilled(false);
		btnRdmGroup.setBorderPainted(false);
		ImageIcon rdmImage = new ImageIcon("images/main/btn_randomization.png");
		btnRdmGroup.setIcon(rdmImage);
		btnRdmGroup.setBounds(200, -4, rdmImage.getIconWidth(),rdmImage.getIconHeight());
		add(btnRdmGroup);
		btnRdmGroup.addMouseListener(this);
		btnRdmGroup.setVisible(false);
	}
	
	public void refresh(){
		List<Table> tables = app.getTableList();
		if (tables.size() != 0) {
			lblExpected.setVisible(true);
			lblClass.setVisible(false);
			lblClassBackground.setVisible(false);
			lblCourse.setVisible(false);
			lblCourseBackground.setVisible(false);
			btnGroup.setVisible(true);
			btnBegin.setVisible(true);
			btnRdmGroup.setVisible(true);
		}
	}
	private void doBegin() {
		if(app.isDoRdmGrouping()){
			JOptionPane.showMessageDialog(getParent().getParent(),
					"学生正在随机分组！");
			return;
		}
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
		if(app.isDoRdmGrouping()){
			JOptionPane.showMessageDialog(getParent().getParent(), "学生正在随机分组!");
		}
		if(Application.isOnResponder){
			JOptionPane.showMessageDialog(getParent().getParent(), "学生正在抢答，不能编辑分组！");
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
			final List<ChannelHandlerContext> channels = app.getClientChannelByGroup(group.getId());
			sendMessageToGroup(messagePacking, channels);
		}
	}

	public void setOnClass(boolean isOnClass) {
		if (isOnClass) { 
			UIHelper.sendLockScreenMessage(true);
			btnBegin.setIcon(new ImageIcon("images/main/btn_end.png"));// 设置图片
			Application.isOnClass = true;
			Application.getInstance().setLessionid(
					UUID.randomUUID().toString());
		} else {
			UIHelper.sendLockScreenMessage(false);
			btnBegin.setIcon(new ImageIcon("images/main/btn_begin.png"));// 设置图片
			Application.isOnClass = false;
		}
		
	}

	private void sendMessageToGroup(final MessagePacking messagePacking,
			final List<ChannelHandlerContext> channels) {
		if (channels == null) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				byte[] data = messagePacking.pack().array();
				ByteBuffer buffer = ByteBuffer.allocate(data.length);
				Iterator<ChannelHandlerContext> it = channels.iterator();
				while (it.hasNext()) {
					ChannelHandlerContext channel = it.next();
					if (!channel.channel().isActive()) {
						it.remove();
						continue;
					}
					buffer.clear();
					buffer.put(data);
					buffer.flip();
					channel.writeAndFlush(buffer);
				}
			};
		}.start();
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
			int result = JOptionPane.showConfirmDialog(getParent().getParent(), "学生正在编辑分组，是否立即结束并进行随机分组？",
					"随机分组确认", JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.NO_OPTION){
				return;
			}
			app.setGrouping(false);
		}
		if (Application.hasQuiz) {// 格式不一致，统一修改重构
			int result = JOptionPane.showConfirmDialog(getParent().getParent(), "学生正在作业，是否立即结束并进行分组？",
					"随机分组确认", JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.NO_OPTION){
				return;
			}
			Application.hasQuiz=false;
			Application.getInstance().getFloatIcon().showNoQuiz();
			MainFrame.getInstance().showNoQuiz();
		}
		if(Application.isOnClass){//变换上课状态
			setOnClass(false);
		}
//		new CoverPanel();
		MainFrame.getInstance().showBlank();
		// 发送小组信息
		app.setDoRdmGrouping(true);
		Queue<List<Student>> students =  RdmGroup.getStudentQue();
		Map<String,Object> rdmGroup=RdmGroup.getTableGroupList();
		List<Group> groupList= (List<Group>) rdmGroup.get("groupList");
		Map<Integer,List<Device>> groupDevice = (Map<Integer, List<Device>>) rdmGroup.get("groupDevices");
		Map<String,List<Student>> imeiStudents = new HashMap<String, List<Student>>();
		clearGroupList();
		int i=0;
		for (Group group : groupList) {//遍历小组发送分组消息
			group.setName("梦想小组"+i);
			group.setLogo("rainbow");
			List<Student> groupStudent = new ArrayList<Student>();
			List<Device> devices =groupDevice.get(group.getId());
			try {
				ApiClient.updateGroup(group.getId(), "梦想小组"+i, "rainbow");
			} catch (AppException e) {
				logger.info("随机分组失败："+group.getId()+e);
				app.setDoRdmGrouping(false);
				app.refresh();
				JOptionPane.showMessageDialog(MainFrame.getInstance().getFrame(), "小组信息更新失败，随机分组退出！");
				return;
			}
			for(Device device:devices){
				List<Student> student = new ArrayList<Student>();
				Map<String, Object> rdmMsg = new HashMap<String, Object>();
				student=students.poll();
				groupStudent.addAll(student);
//				rdmMsg.put("group", group);发送小组信息，暂禁止
				rdmMsg.put("students", student);
				for(Student updateStu:student){				
					try {
						ApiClient.loginForStudent(updateStu.getName(), updateStu.getSex(),updateStu.getNumber(), device.getImei());
					} catch (AppException e) {
						logger.info("随机分组更新学生小组失败"+group.getId()+e);
						app.setDoRdmGrouping(false);
						app.refresh();
						JOptionPane.showMessageDialog(MainFrame.getInstance().getFrame(), "学生信息更新失败，随机分组退出！");
						return;
					}
				}
				
				imeiStudents.put(device.getImei(), app.addRdmStudent( student));
				MessagePacking rdmPacking = new MessagePacking(Message.MESSAGE_GROUP_RDMGROUP);
				rdmPacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(JSON.toJSONString(rdmMsg)));
				ChannelHandlerContext clientChannel = app.getClientChannel().get(device.getImei());
				sendMsgToClient(rdmPacking,clientChannel);			
			}
			group.setStudents(groupStudent);//更新小组学生映射
			app.addGroup(group);
			++i;
		}
		app.refreshIMEI(imeiStudents);//更新设备学生映射
		app.setDoRdmGrouping(false);
		app.refresh();
	}
	/**
	 * 清除小组成员
	 */
	private void clearGroupList() {
		List<Group> groupList = app.getGroupList();
		for(Group group: groupList){
			group.getStudents().clear();
		}
		app.setGroupList(groupList);
	}
	/**
	 * 发送随机
	 * 分组信息
	 * @param rdmPacking
	 * @param clientChannel
	 */
	private void sendMsgToClient(final MessagePacking rdmPacking,
			final ChannelHandlerContext clientChannel) {	
		byte[] data=rdmPacking.pack().array();
				ByteBuffer bytes =ByteBuffer.allocate(data.length);
				bytes.clear();
				bytes.put(data);
				bytes.flip();
				clientChannel.writeAndFlush(bytes);
				logger.info("随机分组信息发送失败！"+new String(data));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == btnBegin) {
			if (Application.isOnClass) {
				if(Application.isOnResponder){
					JOptionPane.showMessageDialog(MainFrame.getInstance().getFrame(), "正在抢答，请等待抢答完毕后再下课！");
					return;
				}
				int result = JOptionPane.showConfirmDialog(MainFrame.getInstance()
						.getFrame(), "确定要下课吗？", "提示",
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					UIHelper.sendClassOverMessage();
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
		if(e.getSource()==btnRdmGroup){
			btnRdmGroup.setIcon(new ImageIcon("images/main/btn_randomization_hover.png"));
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
		if(e.getSource()==btnRdmGroup){
			btnRdmGroup.setIcon(new ImageIcon("images/main/btn_randomization.png"));
		}
	}
}
