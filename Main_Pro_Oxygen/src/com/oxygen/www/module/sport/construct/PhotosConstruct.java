package com.oxygen.www.module.sport.construct;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oxygen.www.enties.Photo;

public class PhotosConstruct {
	public static ArrayList<Photo> ToPhotolist(JSONArray jsonarray_data) {
		ArrayList<Photo> Photos = new ArrayList<Photo>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			Photos.add(ToPhoto(c));
		}
		return Photos;
	}

	public static Photo ToPhoto(JSONObject c) {
		Photo photo = new Photo();
		try {
			if (!c.isNull("id")) {
				photo.id = c.getInt("id");
			}
			 
			if (!c.isNull("filename")) {
				photo.filename = c.getString("filename");
			}
			if (!c.isNull("path")) {
				photo.path = c.getString("path");
			}
			if (!c.isNull("target_type")) {
				photo.target_type = c.getString("target_type");
			}if (!c.isNull("target_id")) {
				photo.target_id = c.getInt("target_id");
			}
			if (!c.isNull("url")) {
				photo.url = c.getString("url");
			}
			if (!c.isNull("created_by")) {
				photo.created_by = c.getInt("created_by");
			}
			if (!c.isNull("created_at")) {
				photo.created_at = c.getString("created_at");
			}
			if (!c.isNull("modified_at")) {
				photo.modified_at = c.getString("modified_at");
			}
			if (!c.isNull("modified_by")) {
				photo.modified_by = c.getInt("modified_by");
			}
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return photo;

	}

}
