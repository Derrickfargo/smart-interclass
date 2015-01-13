package cn.com.incito.classroom.test;

import java.io.FileOutputStream;
import java.io.IOException;

import com.robotium.solo.Solo;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.ui.activity.SplashActivity;
import cn.com.incito.common.utils.UIHelper;
import android.app.Activity;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

public class TestActivity extends Activity {

	public void writeFileSdcard(String fileName, String message) {

		try {
			FileOutputStream fout = new FileOutputStream(fileName);

			byte[] bytes = message.getBytes();

			fout.write(bytes);

			fout.close();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.getWindow().setFlags(Constants.FLAG_HOMEKEY_DISPATCHED,
				Constants.FLAG_HOMEKEY_DISPATCHED);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		Button button = (Button) findViewById(R.id.startTest);

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Runtime run = Runtime.getRuntime();
				try {
					run.exec("am instrument --user 0 -w cn.com.incito.classroom/android.test.InstrumentationTestRunner");
				} catch (IOException e) {
					e.printStackTrace();
				}
				;
			}
		});
		button.performClick();
	}

}
