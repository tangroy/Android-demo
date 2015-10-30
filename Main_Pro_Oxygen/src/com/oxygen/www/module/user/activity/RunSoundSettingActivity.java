package com.oxygen.www.module.user.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.utils.SharedPreferencesCompat;

/**
 * 2015-9-1上午11:08:31
 */
public class RunSoundSettingActivity extends BaseActivity implements
		OnClickListener {
	private ImageView iv_back;
	private RadioGroup sex = null;
	private RadioButton male = null;
	private RadioButton female = null;
	private RadioButton close = null;
	/**
	 * -1为不播过语音，1为男，0为女
	 */
	int soundsex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_runsoundsetting);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		sex = (RadioGroup) super.findViewById(R.id.sex);
		male = (RadioButton) super.findViewById(R.id.male);
		female = (RadioButton) super.findViewById(R.id.female);
		close = (RadioButton) super.findViewById(R.id.close);
		sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				SharedPreferences sp = RunSoundSettingActivity.this.getSharedPreferences(Constants.USER_INFO,
						Context.MODE_MULTI_PROCESS);
			SharedPreferences.Editor editor = sp.edit();
			if(checkedId  == female.getId()){
				editor.putInt("soundsex", 0);
			}else if(checkedId  == male.getId()){
				editor.putInt("soundsex", 1);
			}else{
				editor.putInt("soundsex", -1);
			}
			SharedPreferencesCompat.apply(editor);
			}
		});
		SharedPreferences sp = getSharedPreferences(Constants.USER_INFO,
				Context.MODE_MULTI_PROCESS);
		soundsex = sp.getInt("soundsex", 0);
		if (soundsex == 0) {
			female.setChecked(true);
		} else if (soundsex == 1) {
			male.setChecked(true);
		} else {
			close.setChecked(true);
		}
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.equals(iv_back)) {
			finish();
		}

	}
}
