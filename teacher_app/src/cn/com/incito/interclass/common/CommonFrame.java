package cn.com.incito.interclass.common;

import javax.swing.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public abstract class CommonFrame extends JFrame implements
		LanguageChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ResourceManager rm = ResourceManager.getInstance();

	public CommonFrame() throws HeadlessException {
		super();
		init();
	}

	public CommonFrame(GraphicsConfiguration gc) {
		super(gc);
		init();
	}

	public CommonFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		init();
	}

	public CommonFrame(String title) throws HeadlessException {
		super(title);
		init();
	}

	void init() {
		setLanguage(Locale.CHINESE.getDisplayCountry());
	}

	/**
	 * update UI message by I18n
	 */
	@Override
	public void updateResource() {

	}

	/**
	 * get i18n value
	 */
	protected String getString(String key) {
		try {
			return new String(rm.getString(key).getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * set i18n locale
	 */
	protected void setLanguage(String language) {
		rm.changeLang(language);
	}

}
