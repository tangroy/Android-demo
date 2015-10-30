package com.oxygen.www.module.challengs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;

/**
 * 选择挑战类型
 * 
 * @author sambatang
 * 
 */
public class ChooseChallengesTypeActivity extends BaseActivity implements
		OnClickListener {
	private ImageView iv_back;
	private LinearLayout ll_challenges_person, ll_challenges_twoteam,
			ll_challenges_moreteam;
	private ImageView iv_challenges_person, iv_challenges_twoteam,
	iv_challenges_moreteam;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challengs_choosetype);
		initViews();
		initViewsEvent();
		initValues();
	}


	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		ll_challenges_person = (LinearLayout) findViewById(R.id.ll_challenges_person);
		ll_challenges_moreteam = (LinearLayout) findViewById(R.id.ll_challenges_moreteam);
		ll_challenges_twoteam = (LinearLayout) findViewById(R.id.ll_challenges_twoteam);
		iv_challenges_person = (ImageView) findViewById(R.id.iv_challenges_person);
		iv_challenges_moreteam = (ImageView) findViewById(R.id.iv_challenges_moreteam);
		iv_challenges_twoteam = (ImageView) findViewById(R.id.iv_challenges_twoteam);

	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		ll_challenges_person.setOnClickListener(this);
		ll_challenges_moreteam.setOnClickListener(this);
		ll_challenges_twoteam.setOnClickListener(this);

	}

	private void initValues() {
		intent = new Intent(this,CreatChallengesActivity.class);
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.ll_challenges_person:
			intent.putExtra("type", Constants.CHALLENGES_TYPE_PERSON);
			iv_challenges_person.startAnimation(getScaleAnimationAnimation());
			break;
		case R.id.ll_challenges_twoteam:
			intent.putExtra("type", Constants.CHALLENGES_TYPE_TWOTEAM);
			iv_challenges_twoteam.startAnimation(getScaleAnimationAnimation());
			break;
		case R.id.ll_challenges_moreteam:
			intent.putExtra("type", Constants.CHALLENGES_TYPE_MORETEAM);
			iv_challenges_moreteam.startAnimation(
					getScaleAnimationAnimation());
			break;

		default:
			break;
		}
	}
	
	public  Animation getScaleAnimationAnimation() {
		ScaleAnimation Scale = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		Scale.setDuration(200);
		Scale.setFillAfter(false);
		Scale.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				startActivity(intent);
			}
		});
		return Scale;
	}

}
