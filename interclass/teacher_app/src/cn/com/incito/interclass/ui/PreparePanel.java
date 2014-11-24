package cn.com.incito.interclass.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class PreparePanel extends JPanel{
	private static final long serialVersionUID = 6316121486627261595L;
	private static final String CARD_NO_GROUP = "NO_GROUP";
	private static final String CARD_GROUP = "GROUP";
	
	private CardLayout centerCardLayout;
	private JPanel centerCardPanel;
	private PrepareNoGroupPanel noGroupPanel;
	private PrepareGroupPanel groupPanel;
	
	private JButton btnNoGroup;
	private JButton btnGroup;
	private JLabel lblCardBackground;
	
	public PreparePanel() {
		this.setLayout(null);
		this.setOpaque(true);
		// 初始化选项卡按钮
		btnNoGroup = new JButton("学生");
		btnNoGroup.setFocusPainted(false);
		btnNoGroup.setBorderPainted(false);// 设置边框不可见
		btnNoGroup.setContentAreaFilled(false);// 设置透明
		btnNoGroup.setBorder(BorderFactory.createTitledBorder(""));
		add(btnNoGroup);
		btnNoGroup.setBounds(3, 4, 137, 35);
		btnNoGroup.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	centerCardLayout.show(centerCardPanel, CARD_NO_GROUP);
            	lblCardBackground.setIcon(new ImageIcon("images/prepare/bg_nogroup.jpg"));
            }
        });
		
//		btnGroup = new JButton("小组");
//		btnGroup.setFocusPainted(false);
//		btnGroup.setBorderPainted(false);// 设置边框不可见
//		btnGroup.setContentAreaFilled(false);// 设置透明
//		btnGroup.setBorder(BorderFactory.createTitledBorder(""));
//		add(btnGroup);
//		btnGroup.setBounds(141, 4, 137, 35);
//		btnGroup.addActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent e) {
//            	centerCardLayout.show(centerCardPanel, CARD_GROUP);
//            	lblCardBackground.setIcon(new ImageIcon("images/prepare/bg_group.png"));
//            }
//        });
		
		lblCardBackground = new JLabel();
		lblCardBackground.setIcon(new ImageIcon("images/prepare/bg_nogroup.jpg"));
		lblCardBackground.setBounds(0, 0, 838, 40);
		add(lblCardBackground);
		
		centerCardLayout = new CardLayout();
		centerCardPanel = new JPanel(centerCardLayout);
		centerCardPanel.setBounds(0, 45, 876, 575);
		add(centerCardPanel);
		
		//未分组card
		noGroupPanel = new PrepareNoGroupPanel();
		noGroupPanel.setBackground(Color.WHITE);
		JScrollPane noGroupScroll = new JScrollPane(noGroupPanel);
		noGroupScroll.getVerticalScrollBar().setUnitIncrement(100);
		noGroupScroll.setBorder(null);
		noGroupScroll.setBounds(0, 45, 876, 585);
		noGroupPanel.setPreferredSize(new Dimension(noGroupScroll.getWidth() - 50, (noGroupScroll.getHeight() - 50) * 2));
		centerCardPanel.add(noGroupScroll, CARD_NO_GROUP);
		//分组card
		groupPanel = new PrepareGroupPanel();
		groupPanel.setBackground(Color.WHITE);
        JScrollPane groupScroll = new JScrollPane(groupPanel);
        groupScroll.getVerticalScrollBar().setUnitIncrement(100);
        groupScroll.setBorder(null);
        groupScroll.setBounds(0, 45, 876, 585);
		groupPanel.setPreferredSize(new Dimension(groupScroll.getWidth() - 50, (groupScroll.getHeight() - 50) * 4));
		centerCardPanel.add(groupScroll, CARD_GROUP);
	}

	public void refresh(){
		noGroupPanel.refresh();
		groupPanel.refresh();
	}
}
