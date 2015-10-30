package com.oxygen.www.enties;

import java.io.Serializable;
import java.util.List;

public class LevelInfo implements Serializable{
	public List<Task> tasks;
	public int completed;
	public int total;
	public int total_coins;
	public User user;
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTask(List<Task> tasks) {
		this.tasks = tasks;
	}
	public int getCompleted() {
		return completed;
	}
	public void setCompleted(int completed) {
		this.completed = completed;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getTotal_coins() {
		return total_coins;
	}
	public void setTotal_coins(int total_coins) {
		this.total_coins = total_coins;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
