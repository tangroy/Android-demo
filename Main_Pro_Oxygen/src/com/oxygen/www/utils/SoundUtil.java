package com.oxygen.www.utils;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.oxygen.www.R;
import com.oxygen.www.base.Constants;

public class SoundUtil {
	/**
	 * 播放字符串数组
	 * 
	 * @param c
	 * @param sound_array
	 */
	public static void player_array(final Context c,
			final ArrayList<String> sound_array,int sport) {
		SharedPreferences sp = c.getSharedPreferences(Constants.USER_INFO,
				Context.MODE_MULTI_PROCESS);
		/**
		 * -1为不播过语音，1为男，0为女
		 */
		final int soundsex = sp.getInt("soundsex", 0);
		if (soundsex != -1&&Constants.COUNT_CATEGORY_RUNNING==sport) {
			if (sound_array != null && sound_array.size() > 0) {
				new Thread() {
					public void run() {
						for (int i = 0; i < sound_array.size(); i++) {
							final MediaPlayer player = MediaPlayer.create(c,
									strtoraw(c, sound_array.get(i), soundsex));
							player.start();
							player.setOnCompletionListener(new OnCompletionListener() {
								@Override
								public void onCompletion(MediaPlayer mp) {
									player.release();
								}
							});
							try {
								sleep(player.getDuration());
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}.start();
			}
		}
	}

	/**
	 * 数字转换为字符串（大于10000或者小于0不读）
	 * 
	 * @param c
	 * @param r
	 */
	public static ArrayList<String> player_number(final double sound_number) {
		String str = sound_number + "";
		if (sound_number < 10000 && sound_number >= 0) {
			String st[] = new String[str.length()];
			st = str.split("");
			ArrayList<String> newst = new ArrayList<String>();
			for (int i = 0; i < st.length; i++) {
				if (!"".equals(st[i])) {
					if (".".equals(st[i])) {
						newst.add("dot");
					} else {
						newst.add(st[i]);
					}
				}
			}
			return addSoundCompany(newst, sound_number);
		}
		return null;
	}

	/**
	 * 读取整形数据
	 * 
	 * @param sound_number
	 * @return
	 */
	public static ArrayList<String> player_numberint(final int sound_number) {
		String str = sound_number + "";
		if (sound_number < 10000 && sound_number >= 0) {
			String st[] = new String[str.length()];
			st = str.split("");
			ArrayList<String> newst = new ArrayList<String>();
			for (int i = 0; i < st.length; i++) {
				if (!"".equals(st[i])) {
					newst.add(st[i]);
				}
			}
			return addSoundCompany(newst, sound_number);
		}
		return null;
	}

	/**
	 * 添加声音单位
	 * 
	 * @return
	 */
	public static ArrayList<String> addSoundCompany(ArrayList<String> newst,
			double sound_number) {
		// 后面加上“千”, 后面加“百”， 后面加“十”
		if (sound_number >= 1000) {
			newst.add(1, "shousand");
			// 实例：1111
			if (!"0".equals(newst.get(2)) && !"0".equals(newst.get(3))
					&& !"0".equals(newst.get(4))) {
				newst.add(3, "hundred");
				newst.add(5, "ten");
			}
			// 实例：1011 不读“百”
			else if ("0".equals(newst.get(2)) && !"0".equals(newst.get(3))
					&& !"0".equals(newst.get(4))) {
				newst.add(4, "ten");
			}
			// 实例：1001 不读“百，十”，只读一个“0”
			else if ("0".equals(newst.get(2)) && "0".equals(newst.get(3))
					&& !"0".equals(newst.get(4))) {
				newst.remove(2);
			}
			// 实例：1000 不读“百，十”，不读“0”，删除三个0
			else if ("0".equals(newst.get(2)) && "0".equals(newst.get(3))
					&& "0".equals(newst.get(4))) {
				newst.remove(2);
				newst.remove(2);
				newst.remove(2);
			}
			// 实例：1100 不读“十”，不读后面两个0，删除两个0
			else if (!"0".equals(newst.get(2)) && "0".equals(newst.get(3))
					&& "0".equals(newst.get(4))) {
				newst.add(3, "hundred");
				newst.remove(4);
				newst.remove(4);
			}
			// 实例：1110 不读后面的“0”
			else if (!"0".equals(newst.get(2)) && !"0".equals(newst.get(3))
					&& "0".equals(newst.get(4))) {
				newst.add(3, "hundred");
				newst.add(5, "ten");
				newst.remove(6);
			}
		} else if (sound_number >= 100) {
			newst.add(1, "hundred");
			// 实例：111
			if (!"0".equals(newst.get(2)) && !"0".equals(newst.get(3))) {
				newst.add(3, "ten");
			}
			// 实例：101
			else if ("0".equals(newst.get(2)) && !"0".equals(newst.get(3))) {

			}
			// 实例：100
			else if ("0".equals(newst.get(2)) && "0".equals(newst.get(3))) {
				newst.remove(2);
				newst.remove(2);

			}
			// 实例：110
			else {
				newst.add(3, "ten");
				newst.remove(4);
			}

		} else if (sound_number >= 10) {
			newst.add(1, "ten");
			// 实例10 不读0
			if ("0".equals(newst.get(2))) {
				newst.remove(2);
			}
		}
		return newst;
	}

	/**
	 * 添加声音单位(整数)
	 * 
	 * @return
	 */
	public static ArrayList<String> addSoundCompany(ArrayList<String> newst,
			int time) {
		// 后面加上“千”, 后面加“百”， 后面加“十”
		if (time >= 1000) {
			newst.add(1, "shousand");
			// 实例：1111
			if (!"0".equals(newst.get(2)) && !"0".equals(newst.get(3))
					&& !"0".equals(newst.get(4))) {
				newst.add(3, "hundred");
				newst.add(5, "ten");
			}
			// 实例：1011 不读“百”
			else if ("0".equals(newst.get(2)) && !"0".equals(newst.get(3))
					&& !"0".equals(newst.get(4))) {
				newst.add(4, "ten");
			}
			// 实例：1001 不读“百，十”，只读一个“0”
			else if ("0".equals(newst.get(2)) && "0".equals(newst.get(3))
					&& !"0".equals(newst.get(4))) {
				newst.remove(2);
			}
			// 实例：1000 不读“百，十”，不读“0”，删除三个0
			else if ("0".equals(newst.get(2)) && "0".equals(newst.get(3))
					&& "0".equals(newst.get(4))) {
				newst.remove(2);
				newst.remove(2);
				newst.remove(2);
			}
			// 实例：1100 不读“十”，不读后面两个0，删除两个0
			else if (!"0".equals(newst.get(2)) && "0".equals(newst.get(3))
					&& "0".equals(newst.get(4))) {
				newst.add(3, "hundred");
				newst.remove(4);
				newst.remove(4);
			}
			// 实例：1110 不读后面的“0”
			else if (!"0".equals(newst.get(2)) && !"0".equals(newst.get(3))
					&& "0".equals(newst.get(4))) {
				newst.add(3, "hundred");
				newst.add(5, "ten");
				newst.remove(6);
			}
		} else if (time >= 100) {
			newst.add(1, "hundred");
			// 实例：111
			if (!"0".equals(newst.get(2)) && !"0".equals(newst.get(3))) {
				newst.add(3, "ten");
			}
			// 实例：101
			else if ("0".equals(newst.get(2)) && !"0".equals(newst.get(3))) {

			}
			// 实例：100
			else if ("0".equals(newst.get(2)) && "0".equals(newst.get(3))) {
				newst.remove(2);
				newst.remove(2);

			}
			// 实例：110
			else {
				newst.add(3, "ten");
				newst.remove(4);
			}

		} else if (time >= 10) {
			newst.add(1, "ten");
			// 实例10 不读0
			if ("0".equals(newst.get(2))) {
				newst.remove(2);
			}
		}
		return newst;
	}

	/**
	 * 读取时间
	 * 
	 * @param c
	 * @param duration
	 *            时间（秒）
	 */
	public static ArrayList<String> player_time(final int duration) {
		int hour = 0;
		int minute = 0;
		int second = 0;
		ArrayList<String> hour_st = new ArrayList<String>();
		ArrayList<String> minute_st = new ArrayList<String>();
		ArrayList<String> second_st = new ArrayList<String>();
		ArrayList<String> time_st = new ArrayList<String>();

		if (duration < 0) {
			return null;
		} else {
			hour = (int) (duration / 3600 % 24);
			minute = (int) (duration / 60 % 60);
			second = (int) (duration % 60);
		}
		String st1[] = new String[hour + "".length()];
		st1 = (hour + "").split("");
		for (int i = 0; i < st1.length; i++) {
			if (!"".equals(st1[i])) {
				hour_st.add(st1[i]);
			}
		}

		String st2[] = new String[minute + "".length()];
		st2 = (minute + "").split("");
		for (int i = 0; i < st2.length; i++) {
			if (!"".equals(st2[i])) {
				minute_st.add(st2[i]);
			}
		}
		String st3[] = new String[second + "".length()];
		st3 = (second + "").split("");
		for (int i = 0; i < st3.length; i++) {
			if (!"".equals(st3[i])) {
				second_st.add(st3[i]);
			}
		}
		hour_st = addSoundCompany(hour_st, hour);
		minute_st = addSoundCompany(minute_st, minute);
		second_st = addSoundCompany(second_st, second);
		time_st.addAll(hour_st);
		time_st.add("hour");
		time_st.addAll(minute_st);
		time_st.add("min");
		time_st.addAll(second_st);
		time_st.add("second");
		return time_st;
		// player_array(c, (String[]) time_st.toArray(new
		// String[time_st.size()]));
	}

	/**
	 * 根据字符串返回语音资源
	 * 
	 * @param c
	 * @param str
	 *            字符
	 * @param sex
	 *            性别 0为女 1为男
	 * @return
	 */
	public static int strtoraw(Context c, String str, int sex) {
		Resources res = c.getResources();
		int i = 0;
		if (sex == 0) {
			i = res.getIdentifier("l_" + str, "raw", c.getPackageName());

		} else {
			i = res.getIdentifier("n_" + str, "raw", c.getPackageName());

		}
		if (i > 0)
			return i;
		else
			// 无法识别返回空音
			return R.raw.music_null;
	}

}
