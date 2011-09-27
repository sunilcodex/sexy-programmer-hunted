package com.hunted;


import java.io.IOException;

import com.google.android.maps.GeoPoint;

public class Player
{
	public String ID;
	public int PlayerType;
	public String Name;
	public int Money;
	public boolean Self;
	
	private GeoPoint _location;
	
	public Player(String id, int playerType, String name, boolean self)
	{
		this.ID = id;
		this.PlayerType = playerType;
		this.Name = name;
		this.Self = self;
	}
	
	public void set(String[] values)
	{
		this.Name = values[1];
		this.setLocation(new GeoPoint((int)Float.parseFloat(values[2]), (int)Float.parseFloat(values[3])));
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
