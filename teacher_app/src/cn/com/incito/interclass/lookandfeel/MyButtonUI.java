/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.incito.interclass.lookandfeel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

/**
 * 
 * @author tewang
 */
public class MyButtonUI extends BasicButtonUI {

	public void installUI(JComponent c) {
		AbstractButton button = (AbstractButton) c;
		Border border = button.getBorder();

		ImageIcon icon = (ImageIcon) button.getIcon();
		int iconW = icon.getIconWidth();
		int iconH = icon.getIconHeight();

		Image scaled = icon.getImage().getScaledInstance(iconW + (iconW / 3),
				iconH + (iconH / 3), Image.SCALE_SMOOTH);

		c.putClientProperty("oldBorder", border);
		c.setBorder(null);

		button.setRolloverIcon(new ImageIcon(scaled));
		installListeners(button);
	}

	public void uninstallUI(JComponent c) {
		Border border = (Border) c.getClientProperty("oldBorder");

		c.putClientProperty("oldBorder", null);
		c.setBorder(border);
		uninstallListeners((AbstractButton) c);
	}

	public Dimension getPreferredSize(JComponent c) {
		Dimension ps = super.getPreferredSize(c);

		ps.width += ps.width / 3;
		ps.height += ps.height / 3;

		return ps;
	}

	public boolean contains(JComponent c, int x, int y) {
		AbstractButton button = (AbstractButton) c;
		ButtonModel model = button.getModel();
		Icon icon = getIcon(button, model);

		Rectangle iconBounds = new Rectangle(0, 0, icon.getIconWidth(),
				icon.getIconHeight());

		return iconBounds.contains(x, y);
	}

	public void paint(Graphics g, JComponent c) {
		AbstractButton button = (AbstractButton) c;
		ButtonModel model = button.getModel();

		Icon icon = getIcon(button, model);
		Insets insets = c.getInsets();

		icon.paintIcon(c, g, insets.left, insets.top);
	}

	private Icon getIcon(AbstractButton b, ButtonModel m) {
		return (m.isRollover() && !m.isPressed()) ? b.getRolloverIcon() : b
				.getIcon();
	}
}
