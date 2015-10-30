package com.oxygen.www.base;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JpushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	public void onReceive(Context context, Intent intent) {
		
        Bundle bundle = intent.getExtras();
//        System.out.println("[MyReceiver] onReceive - " + intent.getAction()  
//                + ", extras: " + printBundle(bundle)); 
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            // 定义用户点击后的行为
        	
            Intent i = new Intent(context, MenuActivity.class);  //自定义打开的界面
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  
            i.putExtras(bundle);
            context.startActivity(i);
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
  }
}

	  // 打印所有的 intent extra 数据  
    private static String printBundle(Bundle bundle) {  
        StringBuilder sb = new StringBuilder();  
        for (String key : bundle.keySet()) {  
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {  
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));  
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {  
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));  
            } else {  
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));  
            }  
        }  
        return sb.toString();  
    } 
}
