package com.oxygen.www.module.find.construct;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.oxygen.www.enties.Businesses;
import com.oxygen.www.enties.Categories;

public class BusinessConstruct {
	public static ArrayList<Businesses> ToBusinesseslist(JSONObject jsonobject) {
		ArrayList<Businesses> businessess = new ArrayList<Businesses>();
		try {
			if (jsonobject.getInt("status") == 200) {
				JSONArray jsonarray_data = jsonobject.getJSONArray("data");
				for (int i = 0; i < jsonarray_data.length(); i++) {
					JSONObject c = (JSONObject) jsonarray_data.opt(i);
					businessess.add(ToBusinesses(c));
				}
				return businessess;

			} else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return businessess;
	}

	public static Businesses ToBusinesses(JSONObject c) {

		Businesses businesses = new Businesses();
		if (!c.isNull("id")) {
			try {
				businesses.id = c.getInt("id");
				 
				if (!c.isNull("name"))
					businesses.name = c.getString("name");
				if (!c.isNull("telephone")) {
					businesses.telephone = c.getString("telephone");

				}
				if (!c.isNull("address")) {
					businesses.address = c.getString("address");
				}
				if (!c.isNull("s_photo_url")) {
					businesses.s_photo_url = c.getString("s_photo_url");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return businesses;

	}
	public static ArrayList<Categories> ToCategorieslist(JSONObject jsonobject) {
		ArrayList<Categories> categoriess = new ArrayList<Categories>();
		try {
			if (jsonobject.getInt("status") == 200) {
				JSONArray jsonarray_categories = jsonobject.getJSONArray("categories");
				for (int i = 0; i < jsonarray_categories.length(); i++) {
					JSONObject c = (JSONObject) jsonarray_categories.opt(i);
					categoriess.add(toCategories(c));
				}
				return categoriess;
			} else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categoriess;
	}
	
	public static Categories toCategories(JSONObject c) {
		
		Categories categories = new Categories();
		if (!c.isNull("id")) {
			try {
				categories.id = c.getInt("id");
				if (!c.isNull("name"))
					categories.name = c.getString("name");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return categories;
	}
}
