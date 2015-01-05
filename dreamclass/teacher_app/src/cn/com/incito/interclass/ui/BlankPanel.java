package cn.com.incito.interclass.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BlankPanel extends JPanel implements MouseListener{
	private static final long serialVersionUID = 6941994182518306344L;
	JLabel showPre ;
	
	public BlankPanel(){
		showPre = new JLabel();
		ImageIcon image = new ImageIcon("images/main/btn_showGroup.png");
		showPre.setIcon(image);
		showPre.setBorder(null);
		showPre.setBounds(300, 350, image.getIconWidth(),image.getIconHeight());
		showPre.setVisible(true);
		showPre.addMouseListener(this);
		add(showPre); 
		
		this.setLayout(null);
		this.setOpaque(true);
		setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		MainFrame.getInstance().showPrepare();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource()==showPre){
			showPre.setIcon(new ImageIcon("images/main/btn_showGroup_hover.png"));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource()==showPre){
			showPre.setIcon(new ImageIcon("images/main/btn_showGroup.png"));
		}
	}
}
