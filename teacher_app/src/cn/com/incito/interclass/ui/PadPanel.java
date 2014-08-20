package cn.com.incito.interclass.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Student;

public class PadPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1209084544148357456L;
	private JLabel lblPad;
	private List<JLabel> studentList = new ArrayList<JLabel>();
	private List<Student> students = new ArrayList<Student>();
	
	PadPanel() {
		setLayout(null);
		setVisible(false);
		setBackground(Color.white);
		
		int x = 0;
		int y = 0;
		lblPad = getPadLabel();
		add(lblPad);
		lblPad.setBounds(x, y, 15, 15);
		JLabel label = new JLabel("PAD");
		add(label);
		label.setForeground(new Color(Integer.parseInt("898989", 16)));
		label.setBounds(x + 20, y - 5, 25, 25);
		JLabel lblLine = getLine();
		add(lblLine);
		lblLine.setBounds(x, y + 20, 81, 2);
		for (int j = 0; j < 4; j++) {
			JLabel lblName = getNameLabel();
			add(lblName);
			lblName.setBounds(x, y + 28, 81, 24);
			studentList.add(lblName);
			y += 30;
		}
	}
	
	private JLabel getLine() {
		return new JLabel() {
			private static final long serialVersionUID = 2679733728559406364L;
			@Override
			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				Stroke stroke = g2d.getStroke();
				Color color = g2d.getColor();
				g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
				g2d.setColor(new Color(Integer.parseInt("e1e1e1", 16)));
				g2d.drawLine(0, 0, this.getWidth(), 0);
				g2d.setStroke(stroke);
				g2d.setColor(color);
				this.paintComponents(g2d);
			}
		};
	}
	
	private JLabel getPadLabel(){
		JLabel lblPad = new JLabel("", JLabel.CENTER);
		lblPad.setOpaque(true);
		lblPad.setBackground(new Color(Integer.parseInt("E1E1E1", 16)));
		lblPad.setForeground(new Color(Integer.parseInt("FFFFFF", 16)));
		return lblPad;
	}
	
	private JLabel getNameLabel(){
		JLabel lblName = new JLabel("", JLabel.CENTER);
		lblName.setOpaque(true);
		lblName.setBackground(new Color(Integer.parseInt("E1E1E1", 16)));
		lblName.setForeground(new Color(Integer.parseInt("FFFFFF", 16)));
		return lblName;
	}
	
	public List<Student> getStudents() {
		return students;
	}
	
	public void setStudents(List<Student> students) {
		this.students = students;
		for (int i = 0; i < studentList.size(); i++) {
			JLabel lblName = studentList.get(i);
			if (students != null && i < students.size()) {
				Student student = students.get(i);
				lblName.setText(student.getName());
				lblName.setBackground(new Color(Integer.parseInt("5ec996", 16)));
			} else {
				lblName.setText("");
				lblName.setBackground(new Color(Integer.parseInt("E1E1E1", 16)));
			}
		}
	}
	
	public void isOnline(boolean isOnline){
		if (isOnline) {
			lblPad.setBackground(new Color(Integer.parseInt("5ec996", 16)));
		} else {
			lblPad.setBackground(new Color(Integer.parseInt("e1e1e1", 16)));
		}
	}

}
