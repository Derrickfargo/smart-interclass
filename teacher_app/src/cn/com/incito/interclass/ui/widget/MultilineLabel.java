package cn.com.incito.interclass.ui.widget;

import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.LookAndFeel;

public class MultilineLabel extends JTextArea {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5100490242592581888L;


	public MultilineLabel(String s) {
		super(s);
	}

	public void updateUI() {
		super.updateUI();

		// 设置为自动换行
		setLineWrap(true);
		setWrapStyleWord(true);
		setHighlighter(null);
		setEditable(false);
		setBackground(new Color(0,0,0,0));//透明
		// 设置为label的边框，颜色和字体
		LookAndFeel.installBorder(this, "Label.border");

		LookAndFeel.installColorsAndFont(this, "Label.background", "Label.foreground", "Label.font");
	}

}
