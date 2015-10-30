package com.oxygen.www.module.sport.construct;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oxygen.www.enties.Comment;

public class CommentsConstruct {
	public static ArrayList<Comment> ToCommentlist(JSONArray jsonarray_data) {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			comments.add(ToComment(c));
		}

		return comments;
	}

	public static Comment ToComment(JSONObject c) {
		Comment comment = new Comment();
		try {
			if (!c.isNull("id")) {
				comment.id = c.getInt("id");
			}
			 
			if (!c.isNull("read_count")) {
				comment.read_count = c.getInt("read_count");
			}
			if (!c.isNull("reply_count")) {
				comment.reply_count = c.getInt("reply_count");
			}
			if (!c.isNull("target_type")) {
				comment.target_type = c.getString("target_type");
			}if (!c.isNull("target_id")) {
				comment.target_id = c.getInt("target_id");
			}
			if (!c.isNull("content")) {
				comment.content = c.getString("content");
			}
			if (!c.isNull("created_by")) {
				comment.created_by = c.getInt("created_by");
			}
			if (!c.isNull("created_at")) {
				comment.created_at = c.getString("created_at");
			}
			if (!c.isNull("modified_at")) {
				comment.modified_at = c.getString("modified_at");
			}
			if (!c.isNull("modified_by")) {
				comment.modified_by = c.getInt("modified_by");
			}
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return comment;

	}

}
