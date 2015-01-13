package cn.com.incito.classroom.test;

import java.io.FileInputStream;
import org.apache.http.util.EncodingUtils;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.GridView;
import cn.com.incito.classroom.ui.activity.SplashActivity;
import com.robotium.solo.Solo;

public class AClassRoomTest extends
		ActivityInstrumentationTestCase2<SplashActivity> {
	private Solo solo;
	public AClassRoomTest() {
		super(SplashActivity.class);
	}

	public void setUp() {
		// setUp() is run before a test case is started.
		// This is where the solo object is created.
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() {
		// tearDown() is run after a test case has finished.
		// finishOpenedActivities() will finish all the activities that have
		// been opened during the test execution.
		solo.finishOpenedActivities();
	}

	public String readFileSdcard(String fileName) {

		String res = "";

		try {

			FileInputStream fin = new FileInputStream(fileName);

			int length = fin.available();

			byte[] buffer = new byte[length];

			fin.read(buffer);

			res = EncodingUtils.getString(buffer, "UTF-8");

			fin.close();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

		return res;

	}

	// test case named testBindDesk
	public void testBindDesk() {
		
		String iSBindDeskActivity;
		//int group=Integer.parseInt(readFileSdcard("/mnt/sdcard/group.txt"));;
		//String groupName=readFileSdcard("/mnt/sdcard/groupname.txt");
		String name=readFileSdcard("/mnt/sdcard/name.txt");
		String no=readFileSdcard("/mnt/sdcard/no.txt");
		final int deskNumber=Integer.parseInt(readFileSdcard("/mnt/sdcard/desknumber.txt"));

		// wait for 5s to load the APP
		solo.sleep(5000);

		iSBindDeskActivity = solo.getCurrentActivity().toString();
		if (iSBindDeskActivity
				.startsWith("cn.com.incito.classroom.ui.activity.BindDeskActivity")) {
			final GridView gv_desk_number = (GridView) solo
					.getView("gv_desk_number");
			final View rlayout = solo.getView("rlayout");
			Activity mActivity = solo.getCurrentActivity();
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					gv_desk_number.performItemClick(rlayout, deskNumber,
							deskNumber);
				}
			});
			solo.sleep(2000);
			solo.clickOnImageButton(0);
		}

		solo.waitForActivity(
				cn.com.incito.classroom.ui.activity.WaitingActivity.class,
				100000);
		solo.sleep(1000);
		solo.clickOnImageButton(0);
		solo.sleep(1000);
		solo.enterText(0, name);
		solo.sleep(1000);
		solo.enterText(1, no);
		solo.sleep(1000);
		solo.clickOnButton(0);
		solo.sleep(1000);
		solo.clickOnImageButton(0);
		solo.sleep(5000);

		/*
		 * 这一段代码是点灭已注册的学生 if (b < 3) { final GridView gv_group_member =
		 * (GridView) solo .getView("gv_group_member"); final View rlayout =
		 * solo.getView("rlayout"); Activity mActivity =
		 * solo.getCurrentActivity(); mActivity.runOnUiThread(new Runnable() {
		 * 
		 * @Override public void run() {
		 * gv_group_member.performItemClick(rlayout, b * 4 + 0, b * 4 + 0);
		 * solo.sleep(1000); gv_group_member.performItemClick(rlayout, b * 4 +
		 * 1, b * 4 + 1); solo.sleep(1000);
		 * gv_group_member.performItemClick(rlayout, b * 4 + 2, b * 4 + 2);
		 * solo.sleep(1000); gv_group_member.performItemClick(rlayout, b * 4 +
		 * 3, b * 4 + 3); solo.sleep(1000); } }); }
		 */

		
		/* 测试分组的代码
		 * while (solo.waitForActivity(
						cn.com.incito.classroom.ui.activity.EditGroupInfoActivity.class,
						10000000)) 
		{
			if (group == 1) {
				solo.sleep(1000);
				solo.enterText(0, groupName);
				solo.clickOnImageButton(0);
			}

			solo.waitForActivity(
					cn.com.incito.classroom.ui.activity.ConfirmGroupInfoActivity.class,
					10000000);
			solo.sleep(1000);
			solo.clickOnImageButton(1);
		}*/
		
		solo.waitForActivity(
				cn.com.incito.classroom.ui.activity.BindDeskActivity.class,
				10000000);
	}
}
