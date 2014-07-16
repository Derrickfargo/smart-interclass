/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.incito.interclass.lookandfeel;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalSplitPaneUI;
import java.awt.*;

/**
 * 此类还是有问题
 * 
 * @author liubo
 */
public class MyMetalSplitPaneUI extends MetalSplitPaneUI {

    private final static MyMetalSplitPaneUI cornerButtonUI = new MyMetalSplitPaneUI();

    public static ComponentUI createUI(JComponent c) {
        return cornerButtonUI;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
//        g.setColor(Color.BLACK);
//        int width = c.getWidth();
//        int height = c.getHeight();
//        g.drawRect(3, 3, width - 6, height - 6);
    }
}
