package cn.com.incito.interclass.ui;

import java.awt.Font;
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
	private JLabel stuList;
	private JLabel stuText;
	private JLabel responder,stuImg,stuLoading;
	
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
			stuList.setText(null);
			showRes(false);
			return;
		}
		List<Student> students=responderStus;
		StringBuffer stuBuffer= new StringBuffer();
		for(Student student : students){
			stuBuffer.append(student.getNumber()+": "+student.getName()+" ; ");
		}
		String stuName = stuBuffer.toString();
		stuList.setText(stuName);		
		stuList.setFont(new Font("微软雅黑", Font.BOLD, 15));
		showRes(true);
		showLodding(false);
		
		repaint();
		revalidate();
	}
	
	private void initView(){
		stuList = new JLabel();
		stuList.setBounds(270, 203, 500, 50);
		stuList.setVisible(false);
		this.add(stuList);
		
		stuImg = new JLabel();
		ImageIcon imag = new ImageIcon("images/login/pic_check.png");
		stuImg.setIcon(imag);
		stuImg.setBounds(50, 207,imag.getIconWidth(), imag.getIconHeight());
		stuImg.setVisible(false);
		this.add(stuImg);
		
		stuLoading = new JLabel();
		ImageIcon img = new ImageIcon("images/main/loading.gif");
		stuLoading.setIcon(img);
		stuLoading.setBounds(385, 514, img.getIconWidth(), img.getIconHeight());
		stuLoading.setVisible(false);
		this.add(stuLoading);
		
		responder  =  new JLabel();
		ImageIcon imgs = new ImageIcon("images/responder/responder.png");
		responder.setIcon(imgs);
		responder.addMouseListener(this);
		responder.setBounds(320, 430, imgs.getIconWidth(), imgs.getIconHeight());
		responder.setVisible(true);
		this.add(responder);
		
		stuText = new JLabel();
		stuText.setBounds(100, 200, 200, 50);
		stuText.setText("抢答的学生为：");
		stuText.setFont(new Font("微软雅黑", Font.BOLD, 24));
		stuText.setVisible(false);
		this.add(stuText);
	}
	
	private void initData(){
		if(app.getResponderStudents().size()!=0){
			responderStus=app.getResponderStudents();
			app.setResponderStudents(new ArrayList<Student>());
			return;
		}
		responderStus = new ArrayList<Student>();
	}
	private void showRes(boolean flag){
		stuList.setVisible(flag);
		stuText.setVisible(flag);
		stuImg.setVisible(flag);
	}
	private void showLodding(boolean flag){
		stuLoading.setVisible(flag);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()==responder){
			showRes(false);
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
			responder.setIcon(new ImageIcon("images/responder/responder_hover.png"));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource()==responder){
			responder.setIcon(new ImageIcon("images/responder/responder.png"));
		}
	}
}
