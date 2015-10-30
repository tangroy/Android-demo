package com.oxygen.www.module.find.construct;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oxygen.www.enties.Posts;
import com.oxygen.www.enties.PostsTag;

public class PostsConstruct {
	public static ArrayList<Posts> ToPostslist(JSONObject jsonobject) {
		ArrayList<Posts> postses = new ArrayList<Posts>();
		try {
			if (jsonobject.getInt("status") == 200) {
				JSONArray jsonarray_data = jsonobject.getJSONArray("data");
				for (int i = 0; i < jsonarray_data.length(); i++) {
					JSONObject c = (JSONObject) jsonarray_data.opt(i);
					Posts posts = ToPosts(c);
				//	if(posts.headimg!=null){
						postses.add(posts);
				//	}
					
				}
				return postses;

			} else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return postses;
	}

	public static Posts ToPosts(JSONObject c) {

		Posts posts = new Posts();
		if (!c.isNull("id")) {
			try {
				posts.id = c.getInt("id");
				if (!c.isNull("title"))
					posts.title = c.getString("title");
				if (!c.isNull("author")) {
					posts.author = c.getString("author");
				}
				if (!c.isNull("publish_time")) {
					posts.publish_time = c.getString("publish_time");
				}
				if (!c.isNull("pic")) {
					posts.pic = c.getString("pic");
				}
				if (!c.isNull("summary")) {
					posts.summary = c.getString("summary");
				}
				if(!c.isNull("read_count")){
					posts.read_count = c.getInt("read_count");
				}
				if(!c.isNull("vote_count")){
					posts.vote_count = c.getInt("vote_count");
				}
				if(!c.isNull("curruser_voted")){
					posts.curruser_voted = c.getInt("curruser_voted");
				}
				if(!c.isNull("curruser_bookmarked")){
					posts.curruser_bookmarked = c.getInt("curruser_bookmarked");
				}
				if(!c.isNull("token")){
					posts.token = c.getString("token");
				}
				if (!c.isNull("tags")) {
					JSONArray jsonarray_data = c.getJSONArray("tags");
					ArrayList<PostsTag> poststags = new ArrayList<PostsTag>();
					for (int i = 0; i < jsonarray_data.length(); i++) {
						JSONObject json_tag = (JSONObject) jsonarray_data
								.opt(i);
						poststags.add(ToPostsTag(json_tag));
					}
					posts.tags = poststags;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return posts;

	}

	public static PostsTag ToPostsTag(JSONObject c) {
		PostsTag poststag = new PostsTag();
		if (!c.isNull("id")) {
			try {
				poststag.tagid = c.getInt("id");
				if (!c.isNull("name")) {
					poststag.name = c.getString("name");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return poststag;
	}
}
