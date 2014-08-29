
package com.guguluk.sakus.dto;

import java.util.List;

public class Line {
   	private List<Bus> busList;
   	private String name;

 	public List<Bus> getBusList(){
		return this.busList;
	}
	public void setBusList(List<Bus> busList){
		this.busList = busList;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
}
