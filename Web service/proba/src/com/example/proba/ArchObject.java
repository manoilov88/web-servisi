package com.example.proba;

public class ArchObject {
	
	private String ObjectName;
	private double Latitude;
	private double Longitude;
	
	public ArchObject() {
		// TODO Auto-generated constructor stub
	}
	
	public ArchObject(String obj_name, double lat, double lon) {
		// TODO Auto-generated constructor stub
		this.ObjectName = obj_name;
		this.Latitude = lat;
		this.Longitude = lon;
	}
	
	public void setObjectName(String obj_name)
	{
		this.ObjectName = obj_name;
	}
	
	public String getObjectName()
	{
		return this.ObjectName;
	}
	
	public void setLatitude(double lat)
	{
		this.Latitude = lat;
	}
	
	public double getLatitude()
	{
		return this.Latitude;
	}
	
	public void setLongitude(double lon)
	{
		this.Longitude = lon;
	}
	
	public double getLongitude()
	{
		return this.Longitude;
	}

}
