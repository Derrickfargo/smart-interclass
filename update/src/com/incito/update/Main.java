package com.incito.update;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
//������
public class Main {

	public static void main(String[] args) {
		setLookAndFeel();
		initGlobalFontSetting();
//		args = new String[] { "\"C:\\Users\\JOHN\\Desktop\\dream class  ��װ��\\release\\��������PC��V100R001C00\\backup\\��������_V100R001C00(2).exe\"" };
		if (args.length != 1) {
			JOptionPane.showMessageDialog(null, "Ӧ�ó�������ʧ�ܣ�������δ�ҵ����¸��°���");
			System.exit(0);
		}
		String url = args[0].replace('*', ' ');
		File source = new File(url);
		if(!source.exists()){
			JOptionPane.showMessageDialog(null, "Ӧ�ó�������ʧ�ܣ�������δ�ҵ����¸��°���");
			System.exit(0);
		}
		File target = new File(source.getParentFile().getParent(), "��������.exe");
		target.delete();//ɾ��ԭ�����ļ�
		
		try {
			copy(source, target);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Ӧ�ó�������ʧ�ܣ�δ�ܸ������°�װ����");
			System.exit(0);
		}
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("cmd.exe /c"+" " +target.getAbsolutePath().replace(" ", "\" \""));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void copy(File source, File target) throws Exception {
		FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(target); 
        byte[] buffer = new byte[1024]; 
        int length; 
        while ((length = fis.read(buffer)) != -1) { 
            fos.write(buffer, 0, length); 
        } 
        fis.close(); 
        fos.close();
	}
	
	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void initGlobalFontSetting() {
		Font font = new Font("Microsoft YaHei", Font.PLAIN, 12);
		FontUIResource fontRes = new FontUIResource(font);
		Enumeration<?> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource)
				UIManager.put(key, fontRes);
		}
	}
}
