package com.oxygen.www.enties;

import java.io.Serializable;

import org.apache.http.entity.SerializableEntity;

public class Photo implements Serializable{
public int id;
public String filename;
public String path;
public String target_type;
public int target_id;
public String url;
public int created_by;
public String created_at;
public String modified_at;
public int modified_by;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getFilename() {
	return filename;
}
public void setFilename(String filename) {
	this.filename = filename;
}
public String getPath() {
	return path;
}
public void setPath(String path) {
	this.path = path;
}
public String getTarget_type() {
	return target_type;
}
public void setTarget_type(String target_type) {
	this.target_type = target_type;
}
public int getTarget_id() {
	return target_id;
}
public void setTarget_id(int target_id) {
	this.target_id = target_id;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public int getCreated_by() {
	return created_by;
}
public void setCreated_by(int created_by) {
	this.created_by = created_by;
}
public String getCreated_at() {
	return created_at;
}
public void setCreated_at(String created_at) {
	this.created_at = created_at;
}
public String getModified_at() {
	return modified_at;
}
public void setModified_at(String modified_at) {
	this.modified_at = modified_at;
}
public int getModified_by() {
	return modified_by;
}
public void setModified_by(int modified_by) {
	this.modified_by = modified_by;
}
}
