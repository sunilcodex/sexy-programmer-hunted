package com.hunted;


import com.google.android.maps.GeoPoint;

public class Player
{
	public int PlayerType;
	public String Name;
	
	private GeoPoint _location;
	
	public Player(int playerType, String name)
	{
		this.PlayerType = playerType;
		this.Name = name;
	}
	
	public void setLocation(GeoPoint loc)
	{
		_location = loc;
	}
	
	public GeoPoint getLocation()
	{
		return _location;
	}
}
