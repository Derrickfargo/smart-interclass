package cn.com.incito.classroom.ui.dialog;



import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;

import cn.com.incito.classroom.R;


public class LoadingDialog {

	private static ProgressDialog dialog;

	public static void show(Context context, String msg) {
		if (dialog == null) {
			dialog = new ProgressDialog(context, R.style.loading_dialog);
			dialog.show();
			dialog.setContentView(R.layout.dialog_loading);
			dialog.setCancelable(false);
		}

		TextView text = (TextView) dialog.findViewById(R.id.txtv_loading_text);
		if (msg != null && !msg.equals("")) {
			text.setText(msg);
		} else {
			text.setText(context.getResources().getText(
					R.string.load_dialog_default_text));
		}
	}

	public static void show(Context context, int msgId) {

		String msg = context.getString(msgId);
		show(context, msg);
	}

	public static void hide() {
		if (dialog == null) {
			return;
		}
		dialog.dismiss();
		dialog = null;
	}
}
