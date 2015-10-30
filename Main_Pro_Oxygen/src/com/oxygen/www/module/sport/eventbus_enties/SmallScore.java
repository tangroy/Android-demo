package com.oxygen.www.module.sport.eventbus_enties;

import java.util.List;

public class SmallScore {
	private List<String> s_doubles;
	private List<Integer> ourScores;
	private List<Integer> otherScores;
	public SmallScore(List<String> s_doubles,List<Integer> ourScores,List<Integer> otherScores){
		this.s_doubles = s_doubles;
		this.ourScores = ourScores;
		this.otherScores = otherScores;
	}
	public List<String> getS_doubles() {
		return s_doubles;
	}
	public void setS_doubles(List<String> s_doubles) {
		this.s_doubles = s_doubles;
	}
	public List<Integer> getOurScores() {
		return ourScores;
	}
	public void setOurScores(List<Integer> ourScores) {
		this.ourScores = ourScores;
	}
	public List<Integer> getOtherScores() {
		return otherScores;
	}
	public void setOtherScores(List<Integer> otherScores) {
		this.otherScores = otherScores;
	}
	
	
}
