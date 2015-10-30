package com.oxygen.www.module.challengs.activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.utils.ToastUtil;
/**
 * 导出挑战数据页面
 * 
 * @author 杨庆雷 2015-10-8下午2:44:12
 */
public class ChallengeDataActivity extends BaseActivity implements
		OnClickListener {
	protected static final int NET_SHOWCHALLENGES = 0;
	private ImageView iv_back;
	private TextView data_url;
	private TextView copy_dataurl;
	private String challengeId;
	private String challengeToken;
	private String dataUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challengedata);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		data_url = (TextView) findViewById(R.id.data_url);
		copy_dataurl = (TextView) findViewById(R.id.copy_dataurl);
		challengeId = getIntent().getExtras().getString("challengeId");
		challengeToken = getIntent().getExtras().getString("challengeToken");
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		copy_dataurl.setOnClickListener(this);
	}

	private void initValues() {
		dataUrl = UrlConstants.API_PREFIX + UrlConstants.CHALLENGE_EXPORT + challengeId+ "?token=" + challengeToken;
		data_url.setText(dataUrl);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.copy_dataurl:
			ClipboardManager cm =(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
			//将文本数据复制到剪贴板
			ClipData clip = ClipData.newPlainText("simple text",dataUrl);
			cm.setPrimaryClip(clip);
			ToastUtil.show(this, "复制链接成功");
			break;
		default:
			break;
		}
	}
}
