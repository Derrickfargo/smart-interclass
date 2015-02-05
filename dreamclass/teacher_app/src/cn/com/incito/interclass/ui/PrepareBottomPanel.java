package cn.com.incito.interclass.ui;

import io.netty.channel.ChannelHandlerContext;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
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
import cn.com.incito.server.core.CoreService;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.SocketServiceCore;
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
	private JButton btnBegin/*, btnGroup, btnRdmGroup*/;
	private Application app = Application.getInstance();
	Logger logger =  Logger.getLogger(PrepareBottomPanel.class.getName());
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
		lblClass.setBounds(170, 0, iconClass.getIconWidth(), iconClass.getIconHeight() - 4);
		lblClassBackground = new JLabel();
		lblClassBackground.setIcon(iconClass);
		add(lblClassBackground);
		lblClassBackground.setVisible(false);
		lblClassBackground.setBounds(170, -4, iconClass.getIconWidth(), iconClass.getIconHeight());
		
		ImageIcon iconCourse = new ImageIcon("images/main/btn_gray.png");
		lblCourse = new JLabel("", JLabel.CENTER);
		lblCourse.setText(app.getCourse().getName());
		lblCourse.setForeground(UIHelper.getDefaultFontColor());
		add(lblCourse);
		lblCourse.setVisible(false);
		lblCourse.setBounds(330, 0, iconCourse.getIconWidth(), iconCourse.getIconHeight() -4 );
		lblCourseBackground = new JLabel();
		lblCourseBackground.setIcon(iconCourse);
		add(lblCourseBackground);
		lblCourseBackground.setVisible(false);
		lblCourseBackground.setBounds(330, -4, iconCourse.getIconWidth(), iconCourse.getIconHeight());

//		btnGroup = new JButton();// 创建按钮对象
//		btnGroup.setFocusPainted(false);
//		btnGroup.setBorderPainted(false);// 设置边框不可见
//		btnGroup.setContentAreaFilled(false);// 设置透明
//		ImageIcon iconGroup = new ImageIcon("images/main/btn_group.png");
//		btnGroup.setIcon(iconGroup);// 设置图片
//		add(btnGroup);// 添加按钮
//		btnGroup.setBounds(358, -4, iconGroup.getIconWidth(), iconGroup.getIconHeight());
//		btnGroup.addMouseListener(this);
//		btnGroup.setVisible(false);
		
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
		
//		btnRdmGroup = new JButton("");//创建随机分组按钮
//		btnRdmGroup.setFocusPainted(false);
//		btnRdmGroup.setContentAreaFilled(false);
//		btnRdmGroup.setBorderPainted(false);
//		ImageIcon rdmImage = new ImageIcon("images/main/btn_randomization.png");
//		btnRdmGroup.setIcon(rdmImage);
//		btnRdmGroup.setBounds(200, -4, rdmImage.getIconWidth(),rdmImage.getIconHeight());
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
//			btnGroup.setVisible(true);
			btnBegin.setVisible(true);
//			btnRdmGroup.setVisible(true);
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

	public void doEditGroup() {
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
			List<Device> devices = group.getDevices();
			List<ChannelHandlerContext> ctxs = new ArrayList<ChannelHandlerContext>();
			for(Device device : devices){
				ChannelHandlerContext ctx = app.getClientChannel().get(device.getImei());
				List<Student> students = app.getStudentByImei(device.getImei());
				if(ctx!=null&&students!=null){
					if(students.size()!=0){
						ctxs.add(ctx);
					}
				}
			}
			sendMessageToGroup(messagePacking, ctxs);
		}
	}

	public void setOnClass(boolean isOnClass) {
		if (isOnClass) { 
			UIHelper.sendLockScreenMessage(true);
			btnBegin.setIcon(new ImageIcon("images/main/btn_end.png"));// 设置图片
			Application.isOnClass = true;
			Application.getInstance().setLessionid(UUID.randomUUID().toString());
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
				Iterator<ChannelHandlerContext> it = channels.iterator();
				while (it.hasNext()) {
					ChannelHandlerContext channel = it.next();
					if (!channel.channel().isActive()) {
						it.remove();
						continue;
					}
					SocketServiceCore.getInstance().sendMsg(messagePacking, channel);
				}
			};
		}.start();
	}

	/**
	 * 随机分组
	 * @author caicai
	 */
	public void doRdmGroup() {
//		new CoverPanel();
//		新界面不显示这个
//		MainFrame.getInstance().showBlank();
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
			List<Student> groupStudent = new ArrayList<Student>();//该小组所有学生
			List<Device> devices =groupDevice.get(group.getId());//该小组所有设备
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
				rdmMsg.put("students", student);
//				rdmMsg.put("group", new CoreService().getGroupByIMEI(device.getImei()));发送小组信息
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
				SocketServiceCore.getInstance().sendMsg(rdmPacking, clientChannel);
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
//		if(e.getSource() == btnGroup){
//			doEditGroup();
//		}
//		if(e.getSource()==btnRdmGroup){
//			doRdmGroup();
//		}
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
//		if (e.getSource() == btnGroup) {
//			btnGroup.setIcon(new ImageIcon("images/main/btn_group_hover.png"));
//		}
//		if(e.getSource()==btnRdmGroup){
//			btnRdmGroup.setIcon(new ImageIcon("images/main/btn_randomization_hover.png"));
//		}
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
//		if (e.getSource() == btnGroup) {
//			btnGroup.setIcon(new ImageIcon("images/main/btn_group.png"));
//		}
//		if(e.getSource()==btnRdmGroup){
//			btnRdmGroup.setIcon(new ImageIcon("images/main/btn_randomization.png"));
//		}
	}
}
