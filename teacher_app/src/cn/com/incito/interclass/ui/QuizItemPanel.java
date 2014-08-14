package cn.com.incito.interclass.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
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
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class QuizItemPanel extends JPanel {
    Group group;

    @Override
    protected void paintComponent(Graphics g) {
        Image iconUser = new ImageIcon("images/main/bg_list.png").getImage();
        g.drawImage(iconUser, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public QuizItemPanel(Group group) {
        this.group = group;
        setBorder(new LineBorder(SystemColor.inactiveCaption));
        setBackground(SystemColor.inactiveCaptionBorder);
        setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        add(panel, BorderLayout.NORTH);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{224, 224, 0};
        gbl_panel.rowHeights = new int[]{124, 124, 0};
        gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);

        JLabel lblNewLabel = new JLabel("New label");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 0;
        panel.add(lblNewLabel, gbc_lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("New label");
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.fill = GridBagConstraints.BOTH;
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel_1.gridx = 1;
        gbc_lblNewLabel_1.gridy = 0;
        panel.add(lblNewLabel_1, gbc_lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("New label");
        GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
        gbc_lblNewLabel_2.fill = GridBagConstraints.BOTH;
        gbc_lblNewLabel_2.insets = new Insets(0, 0, 0, 5);
        gbc_lblNewLabel_2.gridx = 0;
        gbc_lblNewLabel_2.gridy = 1;
        panel.add(lblNewLabel_2, gbc_lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("New label");
        GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
        gbc_lblNewLabel_3.fill = GridBagConstraints.BOTH;
        gbc_lblNewLabel_3.gridx = 1;
        gbc_lblNewLabel_3.gridy = 1;
        panel.add(lblNewLabel_3, gbc_lblNewLabel_3);
        setVisible(false);
        GroupInfoPanel groupInfoPanel = new GroupInfoPanel(group);
        add(groupInfoPanel, BorderLayout.SOUTH);
    }
}
