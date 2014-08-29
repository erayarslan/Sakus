
package com.guguluk.sakus.dto;

public class Bus {
   	private Coordinate coordinate;
   	private String id;

 	public Coordinate getCoordinate(){
		return this.coordinate;
	}
	public void setCoordinate(Coordinate coordinate){
		this.coordinate = coordinate;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
}
