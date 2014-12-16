package cn.com.incito.interclass.ui;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.UIHelper;

public class ResponderPanel extends JPanel{
	private static final long serialVersionUID = 3263791015780289798L;
	Application app = Application.getInstance();
	private List<Student> responderStus;
	private JLabel stuList;
	private JLabel stuText;
	
	public ResponderPanel() {
		// this.setSize(878, 620);
		this.setLayout(null);
		this.setOpaque(true);
		initView();
		// 加载数据
		refresh();
	}
	
	public void doResponder(){
		if(app.getOnlineStudent().size()==0){
			JOptionPane.showMessageDialog(getParent().getParent().getParent(), "学生尚未登录，不能抢答！");
			return ;
		}
		if(!Application.isOnClass){
			JOptionPane.showMessageDialog(getParent().getParent().getParent(), "还未上课不能抢答！");
			return;
		}
		if(Application.hasQuiz){
			JOptionPane.showMessageDialog(getParent().getParent().getParent(), "正在做作业，不能抢答！");
			return;
		}
		if(app.isGrouping()){
			JOptionPane.showMessageDialog(getParent().getParent().getParent(), "学生正在编辑分组，不能抢答！");
			return;
		}
		if(app.isDoRdmGrouping()){
			JOptionPane.showMessageDialog(getParent().getParent().getParent(), "学生正在随机分组，不能抢答！");
			return;
		}
		if(!Application.isOnResponder){
			UIHelper.sendResponderMessage(true);
			Application.isOnResponder=true;
			return;
			}
		JOptionPane.showMessageDialog(MainFrame.getInstance().getFrame(), "抢答尚未结束，请稍等！");
		}
	
	public void refresh(){
		initData();
		if(responderStus==null||responderStus.size()==0){
			stuList.setText(null);
			return;
		}
		List<Student> students=responderStus;
		app.setResponderStudents(new ArrayList<Student>());
		StringBuffer stuBuffer= new StringBuffer();
		for(Student student : students){
			stuBuffer.append(student.getNumber()+":"+student.getName()+",");
		}
		String stuName = stuBuffer.toString();
		stuList.setText(stuName);		
		stuList.setFont(new Font("宋体", Font.BOLD, 15));
		repaint();
		revalidate();
	}
	
	private void initView(){
		stuList = new JLabel();
		stuList.setBounds(400, 200, 400, 200);
		this.add(stuList);
		
		stuText = new JLabel();
		stuText.setBounds(200, 200, 200, 200);
		stuText.setText("抢答的学生为：");
		stuText.setFont(new Font("宋体", Font.BOLD, 24));
		this.add(stuText);
	}
	
	private void initData(){
		if(app.getResponderStudents().size()!=0){
			responderStus=app.getResponderStudents();
			return;
		}
		responderStus = new ArrayList<Student>();
	}
}
