package com.oxygen.www.module.sport.activity;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.module.sport.eventbus_enties.SmallScore;
import de.greenrobot.event.EventBus;
/**
 * 小局分类  分局记录页(添加局数 修改比分)
 * @author kunyuan
 *
 */
public class RecordActivity extends BaseActivity implements OnClickListener{
	/**
	 * 小局记录
	 */
	private ListView lv_record;
	private TextView record_tv_submit;
	private ImageView record_iv_back;
	/**
	 * 添加小局记录
	 */
	private ImageView add_record;
	private RelativeLayout rl_record;
	private NumberPicker np_our;
	private NumberPicker np_other;
	private TextView chooseover;
	private RecordAdapter adapter;
	private int positionFlag;
	private List<String> s_doubles;
	private List<Integer> ourScores;
	private List<Integer> otherScores;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		record_tv_submit = (TextView) findViewById(R.id.record_tv_submit);
		record_iv_back = (ImageView) findViewById(R.id.record_iv_back);
		lv_record = (ListView) findViewById(R.id.lv_record);
		add_record = (ImageView) findViewById(R.id.add_record);
		rl_record = (RelativeLayout) findViewById(R.id.rl_record);
		np_our = (NumberPicker) findViewById(R.id.np_our);
		np_other = (NumberPicker) findViewById(R.id.np_other);
		chooseover = (TextView) findViewById(R.id.chooseover);
		
		s_doubles = getIntent().getExtras().getStringArrayList("s_doubles");
		ourScores = getIntent().getExtras().getIntegerArrayList("ourScores");
		otherScores = getIntent().getExtras().getIntegerArrayList("otherScores");
		if(s_doubles == null){
			s_doubles = new ArrayList<String>();
			ourScores = new ArrayList<Integer>();
			otherScores = new ArrayList<Integer>();
		}
	}

	private void initViewsEvent() {
		record_iv_back.setOnClickListener(this);
		record_tv_submit.setOnClickListener(this);
		add_record.setOnClickListener(this);
		chooseover.setOnClickListener(this);
		
		adapter = new RecordAdapter();
		lv_record.setAdapter(adapter);
		setListViewHeightBasedOnChildren(lv_record);
	}

	private void initValues() {
		np_our.setMaxValue(21);
		np_our.setMinValue(0);
		np_other.setMaxValue(21);
		np_other.setMinValue(0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.record_iv_back:
			this.finish();
			break;
		case R.id.record_tv_submit:
			EventBus.getDefault().post(new SmallScore(s_doubles,ourScores,otherScores));
			this.finish();
			break;
			//添加一小局比分
		case R.id.add_record:
			//添加一局比分
			s_doubles.add("单");
			ourScores.add(0);
			otherScores.add(0);
			adapter.notifyDataSetChanged();
			setListViewHeightBasedOnChildren(lv_record);
			break;
		//记录小局比分确定时 记录双方比分
		case R.id.chooseover:
			ourScores.set(positionFlag,np_our.getValue());
			otherScores.set(positionFlag,np_other.getValue());
			rl_record.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}
	
	class RecordAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			if(s_doubles != null){
				return s_doubles.size();
			}
			return 0;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(RecordActivity.this).inflate(R.layout.item_record, null);
				holder.item = (TextView) convertView.findViewById(R.id.item);
				holder.my_score = (TextView) convertView.findViewById(R.id.my_score);
				holder.opponent_score = (TextView) convertView.findViewById(R.id.opponent_score);
				holder.record_type = (CheckBox) convertView.findViewById(R.id.record_type);
				holder.record_ll_score = (LinearLayout) convertView.findViewById(R.id.record_ll_score);
	 			convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.item.setText(position+1+"");
			holder.my_score.setText(ourScores.get(position)+"");
			holder.opponent_score.setText(otherScores.get(position)+"");
			if(s_doubles.size()-1>=position&&"双".equals(s_doubles.get(position))){
				holder.record_type.setChecked(true);
			}
			//点击比分弹出修改比分的popwindow
			holder.record_ll_score.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					positionFlag = position;
					rl_record.setVisibility(View.VISIBLE);
				}
			});
			holder.record_type.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						s_doubles.set(position, "双");
					}else{
						s_doubles.set(position, "单");
					}
				}
			});
			return convertView;
		}

	}
	public static class ViewHolder {
		public TextView item;
		public CheckBox record_type;
		public TextView my_score;
		public TextView opponent_score;
		public LinearLayout record_ll_score;
	}
	
	/**
	 * 测量listView的高度的方法
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {  
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            return;  
        }  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        listView.setLayoutParams(params);  
    }
}
