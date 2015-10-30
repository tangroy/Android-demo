package com.oxygen.www.wxapi;


import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.module.sport.eventbus_enties.WeChatPay;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 微信支付结果回调
 * 
 * @author 张坤
 *
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler, OnClickListener{
	
    private IWXAPI api;
	private ImageView iv_back;
	private TextView tv_save;
	private TextView tv_result;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
        initViews();
        initEvents();
    	api = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APPID);

        api.handleIntent(getIntent(), this);
    }

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_save = (TextView) findViewById(R.id.tv_save);
		tv_result = (TextView) findViewById(R.id.tv_result);
		
	}

	private void initEvents() {
		iv_back.setOnClickListener(this);
		tv_save.setOnClickListener(this);
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.i("pay", "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			
			if (resp.errCode == 0) {
				// 通知 活动详情页 支付成功
				EventBus.getDefault().post(new WeChatPay(1));

				// 支付成功
				tv_result.setText("支付成功!");
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.app_tip);
				builder.setMessage("支付结果： " + "成功!");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
						
					}
				});
				
				AlertDialog dialog = builder.create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
				
				
			} else if (resp.errCode == -2) {
				// 取消支付
				tv_result.setText("支付已取消!");
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.app_tip);
				builder.setMessage("支付结果： " + "支付已取消!");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
						
					}
				});
				AlertDialog dialog = builder.create();
				
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
				
			} else {
				// 支付失败
				tv_result.setText("支付失败!");
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.app_tip);
				builder.setMessage("支付结果： " + "失败!");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
						
					}
				});
				AlertDialog dialog = builder.create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();

			}
			
			
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.iv_back:
			finish();
			break;
			
		case R.id.tv_save:
			finish();
			break;

		default:
			break;
		}
		
	}
	
	
	
	
}