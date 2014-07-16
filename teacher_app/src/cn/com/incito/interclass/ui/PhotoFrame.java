package cn.com.incito.interclass.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class PhotoFrame extends JFrame {
	private static final long serialVersionUID = -2216276219179107707L;
	private Container con;

	private ZPanel zPanel;
	private JScrollPane imgSp;

	public PhotoFrame(String picture) {
		con = getContentPane();

		zPanel = new ZPanel();
		zPanel.setImagePath(picture);
		zPanel.setPreferredSize(new Dimension(zPanel.getImgWidth(), zPanel.getImgHeight()));

		imgSp = new JScrollPane();
		imgSp.setViewportView(zPanel);
		imgSp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		imgSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		con.add(imgSp, BorderLayout.CENTER);

		finalSetting();
	}

	private void finalSetting() {
		setTitle("Picture");
		setSize(800, 600);
		Dimension dime = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int) (dime.getWidth() - getWidth()) / 2, (int) (dime
				.getHeight() - getHeight()) / 2);
		setVisible(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
}
