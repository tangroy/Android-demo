package com.oxygen.www.module.sport.construct;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oxygen.www.enties.CurrentEventUser;
import com.oxygen.www.enties.Route;
import com.oxygen.www.enties.GDMessage;

public class MessageConstruct {
	public static ArrayList<GDMessage> Toactivitylist(JSONObject jsonobject) {
		ArrayList<GDMessage> messages = new ArrayList<GDMessage>();
		try {
			if (jsonobject.getInt("status") == 200) {
				JSONArray jsonarray_data = jsonobject.getJSONArray("data");
				for (int i = 0; i < jsonarray_data.length(); i++) {
					JSONObject c = (JSONObject) jsonarray_data.opt(i);
					messages.add(ToMessage(c));
				}
				return messages;

			} else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return messages;
	}

	public static ArrayList<GDMessage> ToGDMessage(JSONArray jsonarray_data,JSONObject jsonobject_userinfo_net) {
		ArrayList<GDMessage> messages = new ArrayList<GDMessage>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			GDMessage msg =ToMessage(c);
			try {
				if (jsonobject_userinfo_net != null) {
					msg.headimgurl = jsonobject_userinfo_net.getJSONObject(
							c.getString("sender") + "")
							.getString("headimgurl");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			messages.add(msg);
		}
		return messages;

	}

	public static GDMessage ToMessage(JSONObject c) {

		GDMessage message = new GDMessage();
		if (!c.isNull("id")) {
			try {
				message.id = c.getInt("id");
				if (!c.isNull("sender")) {
					message.sender = c.getInt("sender");
				}
				if (!c.isNull("type"))
					message.type = c.getString("type");
				if (!c.isNull("target_id")) {
					message.target_id = c.getInt("target_id");

				}
				if (!c.isNull("action")) {
					message.action = c.getString("action");
				}
				if (!c.isNull("action_url"))
					message.action_url = c.getString("action_url");
				if (!c.isNull("content"))
					message.content = c.getString("content");
				if (!c.isNull("created_at"))
					message.created_at = c.getString("created_at");
				if (!c.isNull("responsed"))
					message.responsed = c.getString("responsed");
				if (!c.isNull("status")) 
					message.status = c.getString("status");
				if (!c.isNull("target_name"))
					message.target_name = c.getString("target_name");
				if (!c.isNull("target_pic"))
					message.target_pic = c.getString("target_pic");
				if (!c.isNull("target_type"))
					message.target_type = c.getString("target_type");
				if (!c.isNull("target_url"))
					message.target_url = c.getString("target_url");
				if (!c.isNull("title"))
					message.title = c.getString("title");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return message;

	}

}
