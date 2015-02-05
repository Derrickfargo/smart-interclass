package cn.com.incito.interclass.ui;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GroupPanel2 extends JPanel implements MouseListener{
	private static final long serialVersionUID = 3263791015780289798L;
	private static final String ICO_NEXT = "images/group/btn_next2.png";
	
	private JButton /*btnOption1, btnOption2, */btnNext;
//	private int selectedOption = 1;
	public GroupPanel2() {
		this.setLayout(null);
		this.setOpaque(true);
		
		initView();
	}
	
	private void initView(){
		JLabel lblTitle = new JLabel("随机分组已完成",JLabel.LEFT);
		lblTitle.setIcon(new ImageIcon("images/group/pic_check.png"));
		lblTitle.setBounds(10, 20, 500, 50);
		lblTitle.setFont(new Font("Microsoft YaHei", Font.PLAIN, 18));
		lblTitle.setVisible(true);
		add(lblTitle);

		//小组与pad的分割线
//		JLabel lblLine = getLine();
//		lblLine.setBounds(10, 50, 840, 3);
//		add(lblLine);
		
		JLabel lblOption1 = new JLabel("电脑已经对孩子们进行了随机分组，平板电脑上会出现孩子们的姓名，表示该孩子应该坐在这个位置。",JLabel.LEFT);
		lblOption1.setFont(new Font("Microsoft YaHei", Font.PLAIN, 15));
//		lblOption1.setForeground(new Color(Integer.parseInt("efefef", 16)));
//		lblOption1.setIcon(new ImageIcon("images/group/ico1.png"));
		lblOption1.setBounds(30, 80, 800, 35);
		add(lblOption1);
		
//		btnOption1 = new JButton();
//		Icon icoOption1 = new ImageIcon(ICO_OPTION_CHECKED);
//		btnOption1.setIcon(icoOption1);
//		btnOption1.setFocusPainted(false);
//		btnOption1.setBorderPainted(false);// 设置边框不可见
//		btnOption1.setContentAreaFilled(false);// 设置透明
//		btnOption1.setBounds(810, 65, icoOption1.getIconWidth(), icoOption1.getIconHeight());
//		btnOption1.addMouseListener(this);
//		add(btnOption1);
//		
//		JLabel lblLine2 = getLine();
//		lblLine2.setBounds(30, 110, 820, 2);
//		add(lblLine2);
//		
//		JLabel lblOption2 = new JLabel("  现在的位置不是我想要的，让电脑随机分组",JLabel.LEFT);
//		lblOption2.setFont(new Font("Microsoft YaHei", Font.PLAIN, 15));
//		lblOption2.setIcon(new ImageIcon("images/group/ico2.png"));
//		lblOption2.setBounds(30, 125, 800, 35);
//		add(lblOption2);
//		
//		btnOption2 = new JButton();
//		Icon icoOption2 = new ImageIcon(ICO_OPTION_UNCHECKED);
//		btnOption2.setIcon(icoOption2);
//		btnOption2.setFocusPainted(false);
//		btnOption2.setBorderPainted(false);// 设置边框不可见
//		btnOption2.setContentAreaFilled(false);// 设置透明
//		btnOption2.setBounds(810, 125, icoOption2.getIconWidth(), icoOption2.getIconHeight());
//		btnOption2.addMouseListener(this);
//		add(btnOption2);
//		
//		JLabel lblLine3 = getLine();
//		lblLine3.setBounds(30, 170, 820, 2);
//		add(lblLine3);
		
		
		btnNext = new JButton();
		Icon icoNext = new ImageIcon(ICO_NEXT);
		btnNext.setIcon(icoNext);
		btnNext.setFocusPainted(false);
		btnNext.setBorderPainted(false);// 设置边框不可见
		btnNext.setContentAreaFilled(false);// 设置透明
		btnNext.setBounds(140, 210, icoNext.getIconWidth(), icoNext.getIconHeight());
		btnNext.addMouseListener(this);
		add(btnNext);
	}
	
//	private JLabel getLine() {
//		return new JLabel() {
//			private static final long serialVersionUID = 2679733728559406364L;
//			@Override
//			public void paint(Graphics g) {
//				Graphics2D g2d = (Graphics2D) g;
//				Stroke stroke = g2d.getStroke();
//				Color color = g2d.getColor();
//				g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
//				g2d.setColor(new Color(Integer.parseInt("e1e1e1", 16)));
//				g2d.drawLine(0, 0, this.getWidth(), 0);
//				g2d.setStroke(stroke);
//				g2d.setColor(color);
//				this.paintComponents(g2d);
//			}
//		};
//	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
//		if (e.getSource() == btnOption1) {
//			selectedOption = 1;
//			btnOption1.setIcon(new ImageIcon(ICO_OPTION_CHECKED));
//			btnOption2.setIcon(new ImageIcon(ICO_OPTION_UNCHECKED));
//		} else if (e.getSource() == btnOption2) {
//			selectedOption = 2;
//			btnOption1.setIcon(new ImageIcon(ICO_OPTION_UNCHECKED));
//			btnOption2.setIcon(new ImageIcon(ICO_OPTION_CHECKED));
//		}
		if (e.getSource() == btnNext) {
			MainFrame.getInstance().doGrouping();
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
//		if(e.getSource()==responder){
//			responder.setIcon(new ImageIcon("images/responder/responder_hover.png"));
//		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
//		if(e.getSource()==responder){
//			responder.setIcon(new ImageIcon("images/responder/responder.png"));
//		}
	}
}
