package com.oxygen.www.db;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.oxygen.www.enties.Route;
import com.oxygen.www.enties.GDAcvitity;

public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;

	public DBManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	/**
	 * 新增一条运动记录
	 * 
	 * @param gdactivity
	 */
	public int addsport(GDAcvitity gdactivity, int _id) {
		Cursor c = querysportsforid(_id);
		if (c.getCount() == 0) {
			db.beginTransaction(); // 开始事务
			try {
				db.execSQL(
						"INSERT INTO "
								+ DBHelper.TABLE_ACTIVITIES
								+ " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?)",
						new Object[] {
								gdactivity.type,
								gdactivity.sport_eng,
								gdactivity.distance,
								gdactivity.duration,
								gdactivity.latitude,
								gdactivity.longitude,
								gdactivity.address,
								gdactivity.start_time,
								gdactivity.end_time,
								gdactivity.array_route == null ? null
										: gdactivity.array_route.toString(),
								gdactivity.created_at, gdactivity.created_by,
								gdactivity.tilte, gdactivity.status,
								gdactivity.getEvent_id(),
								gdactivity.getIntro(),
								gdactivity.getPace_min(),
								gdactivity.getPace_max() });
				db.setTransactionSuccessful(); // 设置事务成功完成
			} finally {
				db.endTransaction(); // 结束事务
			}
			return getlastid();
		} else {
			update(gdactivity, _id);
			return _id;
		}
	}

	/**
	 * 更新一条数据
	 * 
	 * @param eventid
	 */
	public void update(GDAcvitity gdactivity, int _id) {
		ContentValues cv = new ContentValues();// 实例化ContentValues
		cv.put("distance", gdactivity.distance);// 添加要更改的字段及内容
		cv.put("duration", gdactivity.duration);
		cv.put("end_time", gdactivity.end_time);
		cv.put("pace_max", gdactivity.pace_max);
		cv.put("pace_min", gdactivity.pace_min);
		cv.put("route", gdactivity.array_route == null ? null
				: gdactivity.array_route.toString());
		String whereClause = "_id=?";// 修改条件
		String[] whereArgs = { _id + "" };// 修改条件的参数
		db.update(DBHelper.TABLE_ACTIVITIES, cv, whereClause, whereArgs);// 执行修改
	}

	/**
	 * 删除一条运动记录
	 * 
	 * @param sport_id
	 */
	public void delsport(int id) {
		db.delete(DBHelper.TABLE_ACTIVITIES, "_id = ?",
				new String[] { String.valueOf(id) });
	}

	/**
	 * 删除我的所有运动记录
	 * 
	 * @param created_by
	 *            用户id
	 */
	public void delallsproy(int creat_by) {
		db.delete(DBHelper.TABLE_ACTIVITIES, "created_by = ?",
				new String[] { creat_by + "" });

	}

	/**
	 * 查询运动列表
	 * 
	 * @return
	 */
	public ArrayList<GDAcvitity> querysports() {
		Cursor c = querysportsTheCursor();
		return Analytical(c);
	}

	/**
	 * 查询指定event数据
	 * 
	 * @param evnetid
	 * @return
	 */
	public ArrayList<GDAcvitity> querysportsforevent(int evnetid) {
		Cursor c = querysportsforEventid(evnetid);
		return Analytical(c);
	}

	private ArrayList<GDAcvitity> Analytical(Cursor c) {
		ArrayList<GDAcvitity> sports = new ArrayList<GDAcvitity>();
		while (c.moveToNext()) {
			GDAcvitity gdactivity = new GDAcvitity();
			gdactivity.id = c.getInt(c.getColumnIndex("_id"));
			gdactivity.type = c.getString(c.getColumnIndex("type"));
			gdactivity.sport_eng = c.getString(c.getColumnIndex("sport"));
			gdactivity.distance = c.getDouble(c.getColumnIndex("distance"));
			gdactivity.duration = c.getInt(c.getColumnIndex("duration"));
			gdactivity.latitude = c.getDouble(c.getColumnIndex("latitude"));
			gdactivity.longitude = c.getDouble(c.getColumnIndex("longitude"));
			gdactivity.address = c.getString(c.getColumnIndex("address"));
			gdactivity.start_time = c.getString(c.getColumnIndex("start_time"));
			gdactivity.end_time = c.getString(c.getColumnIndex("end_time"));
			gdactivity.pace_min = c.getString(c.getColumnIndex("pace_min"));
			gdactivity.pace_max = c.getString(c.getColumnIndex("pace_max"));
			String route = c.getString(c.getColumnIndex("route"));
			if (route != null) {
				gdactivity.array_route = getRoute(route);
			}
			gdactivity.created_by = c.getInt(c.getColumnIndex("created_by"));
			gdactivity.created_at = c.getString(c.getColumnIndex("created_at"));
			gdactivity.tilte = c.getString(c.getColumnIndex("title"));
			gdactivity.status = c.getString(c.getColumnIndex("status"));
			gdactivity.event_id = c.getInt(c.getColumnIndex("event_id"));
			gdactivity.intro = c.getString(c.getColumnIndex("intro"));
			gdactivity.local = c.getInt(c.getColumnIndex("_id"));
			sports.add(gdactivity);
		}
		c.close();
		return sports;
	}

	private int getlastid() {
		int lastid = 0;
		Cursor c = db
				.rawQuery("SELECT * FROM " + DBHelper.TABLE_ACTIVITIES, null);
		while (c.moveToNext()) {
			lastid = c.getInt(c.getColumnIndex("_id"));
		}
		return lastid;
	}

	private ArrayList<Route> getRoute(String str_route) {
		ArrayList<Route> routes = new ArrayList<Route>();
		try {
			JSONArray json_route = new JSONArray(str_route);
			for (int i = 0; i < json_route.length(); i++) {
				Route route = new Route();
				route.start_longitude = (Double) ((JSONArray) json_route.get(i))
						.get(0);
				if (route.start_longitude != 0.0) {
					route.start_latitude = (Double) ((JSONArray) json_route
							.get(i)).get(1);
					route.location_time = Long
							.parseLong(((JSONArray) json_route.get(i)).get(2)
									.toString());
					routes.add(route);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return routes;
	}

	/**
	 * select all data
	 * 
	 * @return
	 */
	public Cursor querysportsTheCursor() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_ACTIVITIES
				+ " ORDER BY _id DESC", null);
		return c;
	}

	/**
	 * 查询制定event数据
	 * 
	 * @param eventid
	 * @return
	 */
	public Cursor querysportsforEventid(int eventid) {
		Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_ACTIVITIES
				+ " where event_id=" + eventid + " ORDER BY _id DESC", null);
		return c;
	}

	/**
	 * 查询id数据
	 * 
	 * @param id
	 * @return
	 */
	public Cursor querysportsforid(int _id) {
		Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_ACTIVITIES
				+ " where _id=" + _id + " ORDER BY _id DESC", null);
		return c;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}
}
