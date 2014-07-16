/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.incito.interclass.lookandfeel;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalProgressBarUI;
import java.awt.*;

/**
 * 这是设置它的外观.当我们把程序的外观设置成跟window外观一样时,此时progressBar的外观也会变window的外观,
 * 但是此时如果我们不想让它为window默认外观,我们可以设置自己的外观,所就继承MetalProgressBarUI
 * 继承之后我们如何使用它呢,
 * 我们知道java程序启动时,UIMamager会自动加载外观.getDefaultUI().
 * 但是当我们put(Obect o,Object value)时,它就会加载我们要的外观,而不会变成其它的外观.就是有这个好外
 * @问题:迅雷下载里的进度条jprogressBar设置外观之后里面显示的 "%"比位置会不大正确,但java默认外观就不会.所以我们就使用
 * 它的默认外观.并重写一些东西,如下面就是画上一个长方形式,为了好看.drawFect(int x,int y,int width,int height)
 * by call in the class "com.tewang.downloadjtable.ondownTableColumnModel.... 
 * static{
 *  }
 * @author liubo
 */
public class MyMetalProgressBarUI extends MetalProgressBarUI {

    private final static MyMetalProgressBarUI cornerButtonUI = new MyMetalProgressBarUI();
    
    public static ComponentUI createUI(JComponent c) {
        return cornerButtonUI;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        g.setColor(Color.BLACK);
        int width = c.getWidth();
        int height = c.getHeight();
        g.drawRect(3, 3, width-6, height-6);
    }

}
