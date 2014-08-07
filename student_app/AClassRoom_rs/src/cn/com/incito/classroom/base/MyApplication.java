package cn.com.incito.classroom.base;

import android.app.Application;

public class MyApplication extends Application {

//    public static LoginResVo loginResVo;
//    private static MenuItems item;
//
//    public static MenuItems getItem() {
//        return item;
//    }
//
//    public static void setItem(MenuItems item) {
//        MyApplication.item = item;
//    }
//
//    public static LoginResVo getLoginResVo() {
//        return loginResVo;
//    }
//
//    public static void setLoginResVo(LoginResVo loginResVo) {
//        MyApplication.loginResVo = loginResVo;
//    }
	static Application application ;
	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
	}
	public static Application getApplication(){
		return application;
	}

}
