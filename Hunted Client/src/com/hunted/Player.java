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
	public PlayerStatus Status;
	private boolean _statusChanged;
	
	private GeoPoint _location;
	
	public Player(String id, int playerType, String name, boolean self)
	{
		this.Status = PlayerStatus.Alive;
		this.ID = id;
		this.PlayerType = playerType;
		this.Name = name;
		this.Self = self;
		_location = new GeoPoint(0,0);
		_statusChanged = false;
	}
	
	public void set(String[] values)
	{
		this.Name = values[1];
		
		PlayerStatus newStatus = PlayerStatus.values()[Integer.parseInt(values[4])];
		if(!_statusChanged)
			_statusChanged = newStatus != this.Status;
		this.Status = newStatus;
		
		
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
	
	public boolean acceptStatusChange()
	{
		boolean value = _statusChanged;
		_statusChanged = false;
		return value;
	}
}
