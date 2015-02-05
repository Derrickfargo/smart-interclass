package cn.com.incito.common.utils;


import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.incito.classroom.R;

public class ToastHelper {
	
	/**
	 * 显示抢答成功toast
	* @author hm
	* @date 2015年2月5日 下午2:18:28 
	* @Title: showResponderSuccessToast 
	* @Description:  
	* @return void    返回类型 
	* @throws
	 */
	public static void showResponderSuccessToast(Context context){
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setBackgroundResource(R.drawable.responder_success);
		toast.setView(linearLayout);
		toast.show();
	}
	
    /**
     * 显示自定义Toast提示(来自res) *
     */
    public static void showCustomToast(Context context, int resId) {
        View toastRoot = LayoutInflater.from(context).inflate(
                R.layout.common_toast, null);
        ((TextView) toastRoot.findViewById(R.id.toast_text)).setText(context
                .getString(resId));
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastRoot);
        toast.show();
    }

    /**
     * 显示自定义Toast提示(来自String) *
     */
    public static void showCustomToast(Context context, String text) {
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.common_toast, null);
        ((TextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastRoot);
        toast.show();
    }

    public static boolean ShowAddFeedBackResult(Context paramContext,
                                                Boolean paramBoolean) {
        if ((paramBoolean != null) && (!paramBoolean.booleanValue())) {
            Toast.makeText(paramContext, "发送成功", Toast.LENGTH_SHORT).show();
            return true;
        }
        Toast.makeText(paramContext, "网络连接不上，发送失败", Toast.LENGTH_SHORT).show();
        return false;
    }

    public static void ShowDelCommentResult(Context paramContext, int paramInt) {
        if (paramInt >= 0) {
            Toast.makeText(paramContext, "删除评论成功，" + paramInt + "条被删除",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(paramContext, "删除评论失败", Toast.LENGTH_SHORT).show();
    }

    public static void ShowDelStreamResult(Context paramContext, int paramInt) {
        if (paramInt >= 0) {
            Toast.makeText(paramContext, "删除成功，" + paramInt + "条被删除",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(paramContext, "删除失败", Toast.LENGTH_SHORT).show();
    }

    public static void ShowSendFailure(Context paramContext) {
        Toast.makeText(paramContext, "网络连接不上，发送失败", Toast.LENGTH_SHORT).show();
    }

    public static void showFieldEmptyErr(Context paramContext,
                                         String paramString) {
        Toast.makeText(paramContext, paramString + "不能为空", Toast.LENGTH_SHORT)
                .show();
    }

    public static void showLoginResult(Context paramContext,
                                       boolean paramBoolean) {
        if (paramBoolean)
            Toast.makeText(paramContext, "用户名或密码错误", Toast.LENGTH_SHORT).show();
    }

    public static void showLogout(Context paramContext) {
        Toast.makeText(paramContext, "注销登出，请重新登录", Toast.LENGTH_SHORT).show();
    }

    public static void showNetworkErr(Activity paramActivity, int paramInt) {
        Toast.makeText(paramActivity,
                "网络错误:" + paramActivity.getString(paramInt), Toast.LENGTH_SHORT)
                .show();
    }

    public static void showNetworkErr(Activity paramActivity, String paramString) {
        Toast.makeText(paramActivity, "网络错误:" + paramString, Toast.LENGTH_LONG)
                .show();
    }

    public static void showNetworkErr(Context paramContext) {
        Toast.makeText(paramContext, "请检查网络...", Toast.LENGTH_SHORT).show();
    }

    public static void showPicViewFailue(Context paramContext) {
        Toast.makeText(paramContext, "网络错误，获取图片失败", Toast.LENGTH_SHORT).show();
    }

    public static void showSavePicResult(Context paramContext,
                                         String paramString) {
        if (paramString != null) {
            Toast.makeText(paramContext, "图片保存成功，保存在：" + paramString,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(paramContext, "图片保存失败", Toast.LENGTH_SHORT).show();
    }

    public static void showBlueToothSupportErr(Context paramContext) {
        Toast.makeText(paramContext, "您的手机不支持蓝牙或蓝牙版本过低!", Toast.LENGTH_SHORT)
                .show();
    }
}