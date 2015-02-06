package cn.com.incito.interclass.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.UIHelper;

public class ResponderPanel extends JPanel implements MouseListener{
	private static final long serialVersionUID = 3263791015780289798L;
	Application app = Application.getInstance();
	private List<Student> responderStus;
	private JLabel responder,stuLoading;
	private ImageIcon start,over,loading;
	
	public ResponderPanel() {
		this.setLayout(null);
		this.setOpaque(true);
		
		initView();
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
			showLodding(true);
			UIHelper.sendResponderMessage(true);
			Application.isOnResponder=true;
			return;
			}
		JOptionPane.showMessageDialog(MainFrame.getInstance().getFrame(), "抢答尚未结束，请稍等！");
		}
	
	public void refresh(){
		initData();
		if(responderStus==null||responderStus.size()==0){
			return;
		}
		List<Student> students=responderStus;
		StringBuffer stuBuffer= new StringBuffer();
		for(Student student : students){
			stuBuffer.append(student.getName()+" ;");
		}
		String stuName = stuBuffer.toString();
		new ResponderFrame(stuName);
		showLodding(false);
		
		repaint();
		revalidate();
	}
	
	private void initView(){
		
		start = new ImageIcon("images/responder/responder.png");
		over = new ImageIcon("images/responder/responder_over.png");
		loading = new ImageIcon("images/responder/loading.jpg");
		
		stuLoading = new JLabel();
		stuLoading.setIcon(loading);
		stuLoading.setBounds(385, 514, loading.getIconWidth(), loading.getIconHeight());
		stuLoading.setVisible(false);
		this.add(stuLoading);
		
		responder  =  new JLabel();
		responder.setIcon(start);
		responder.addMouseListener(this);
		responder.setBounds(320, 265, start.getIconWidth(), start.getIconHeight());
		responder.setVisible(true);
		this.add(responder);
		
	}
	
	private void initData(){
		if(app.getResponderStudents().size()!=0){
			responderStus=app.getResponderStudents();
			app.setResponderStudents(new ArrayList<Student>());
			return;
		}
		responderStus = new ArrayList<Student>();
	}
	private void showLodding(boolean flag){
		stuLoading.setVisible(flag);
		if(flag){
			responder.setIcon(over);
			return;
		}
		responder.setIcon(start);
	}
	
	private void cancel(){
		UIHelper.sendResponderMessage(false);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()==responder){
			if(Application.isOnResponder){
				showLodding(false);
				cancel();
				Application.isOnResponder = false;
				return;
			}
			doResponder();
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
		if(e.getSource()==responder){
			if(Application.isOnResponder){
				responder.setIcon(new ImageIcon("images/responder/responder_over_hover.png"));
				return;
			}
			responder.setIcon(new ImageIcon("images/responder/responder_hover.png"));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource()==responder){
			if(Application.isOnResponder){
				responder.setIcon(over);
				return;
			}
			responder.setIcon(start);
		}
	}
}
