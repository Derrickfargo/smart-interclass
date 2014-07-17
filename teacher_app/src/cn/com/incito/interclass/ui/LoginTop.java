package cn.com.incito.interclass.ui;
/**
 * 登录界面顶部
 */
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LoginTop extends JPanel implements MouseListener{

	private JButton set,min,close;
	private List<ImageIcon> list= new ArrayList<ImageIcon>();
	private Login frame;
	
	//构造函数、调用方法
	public LoginTop(Login frame){
		this.frame = frame;
		getImg();
		setLoginToppanel();
	}
	
	//设置面板
	public void setLoginToppanel(){
		this.setSize(460,30);
		this.setLayout(null);
		this.setOpaque(false);
		
		//最小化按钮
		min = Tool.setView(this, list.get(3));
		min.setBounds(385, 9, list.get(3).getIconWidth(), list.get(3).getIconHeight());
		min.addMouseListener(this);
		//关闭按钮
		close = Tool.setView(this, list.get(6));
		close.setBounds(412, 9, list.get(6).getIconWidth(), list.get(6).getIconHeight());
		close.addMouseListener(this);
	}
	//加载图片
	public void getImg(){
		for(int i=1;i<10;i++){
			ImageIcon img = new ImageIcon("images/login/"+i+".png");
			list.add(img);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getSource()==set){
			set.setIcon(list.get(2));
		}
		if(e.getSource()==min){
			min.setIcon(list.get(5));
		}
		if(e.getSource()==close){
			close.setIcon(list.get(8));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if(e.getSource()==set){
			set.setIcon(list.get(2));
		}
		if(e.getSource()==min){
			min.setIcon(list.get(4));
			frame.getFrame().setExtendedState(JFrame.ICONIFIED);
		}
		if(e.getSource()==close){
			close.setIcon(list.get(7));
			System.exit(0);
		}
	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource()==set){
			set.setIcon(list.get(1));
		}
		if(e.getSource()==min){
			min.setIcon(list.get(4));
		}
		if(e.getSource()==close){
			close.setIcon(list.get(7));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource()==set){
			set.setIcon(list.get(0));
		}
		if(e.getSource()==min){
			min.setIcon(list.get(3));
		}
		if(e.getSource()==close){
			close.setIcon(list.get(6));
		}
	}
}
