package cn.com.incito.interclass.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Group;

import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.UIManager;

public class GroupInfoPanel extends JPanel {

    private Group group;
    private JLabel labelLogo;
    private JLabel labelTableNumber;
    private JLabel LabelGroupName;
    private static final String DEFALUT_AVATAR = "images/main/ico_pad_connection.png";

//    @Override
//    protected void paintComponent(Graphics g) {
//        Image iconUser = new ImageIcon("images/main/bg_list.png").getImage();
//        g.drawImage(iconUser, 0, 0, this.getWidth(), this.getHeight(), this);
//    }

    public GroupInfoPanel(Group group) {
        super();
        this.group = group;
        initComponent();
    }

    private void initComponent() {
        setBorder(new LineBorder(SystemColor.inactiveCaption));
        setBackground(SystemColor.inactiveCaptionBorder);
        setLayout(null);

        // 课桌号
//        Icon icon = new ImageIcon("images/main/pic_%s.png");
        labelTableNumber = new JLabel();
        labelTableNumber.setOpaque(true);
        labelTableNumber.setHorizontalAlignment(SwingConstants.CENTER);
        labelTableNumber.setForeground(SystemColor.text);
        labelTableNumber.setFont(new Font("宋体", Font.BOLD, 12));
        labelTableNumber.setBackground(UIManager.getColor("List.selectionBackground"));
//        String tableid = String.(group.getTableId());
        labelTableNumber.setText("1");
//        labelTableNumber.setIcon(icon);
        labelTableNumber.setHorizontalTextPosition(JLabel.CENTER);
        labelTableNumber.setBounds(23, 10, 55, 23);
        add(labelTableNumber);
        

        // 小组名称
        LabelGroupName = new JLabel(
                "",
                new ImageIcon(
                        "images/main/ico_home_foucs.png"),
                JLabel.CENTER);
        LabelGroupName.setFont(new Font("黑体", Font.BOLD, 14));
//        LabelGroupName.setText(group.getName());
        LabelGroupName.setText("第一小组");
        LabelGroupName.setBounds(103, 10, 127, 23);
        LabelGroupName.setVisible(false);
        add(LabelGroupName);
        // 小组头像
        ImageIcon imgLogo = new ImageIcon(DEFALUT_AVATAR);// 默认小组头像
        labelLogo = new JLabel();
        labelLogo.setIcon(imgLogo);
        
        labelLogo.setBounds(40, 60, imgLogo.getIconWidth(),
                imgLogo.getIconHeight());
        add(labelLogo);
        JLabel lblNewLabel = new JLabel();
        lblNewLabel.setForeground(SystemColor.window);
        lblNewLabel.setFont(new Font("幼圆", Font.BOLD, 12));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setText("15");
        lblNewLabel.setOpaque(true);
        lblNewLabel.setBackground(UIManager.getColor("List.selectionBackground"));
        lblNewLabel.setBounds(243, 10, 24, 22);
        add(lblNewLabel);
        invalidate();
    }

    public void setGroup(Group group) {
        this.group = group;
        invalidate();
    }
}
