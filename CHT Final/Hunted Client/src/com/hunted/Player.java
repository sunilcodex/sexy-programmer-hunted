package com.hunted;


import java.io.IOException;

import android.location.Location;

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
		this.setStatus(newStatus);
		
		this.setLocation(new GeoPoint((int)Float.parseFloat(values[2]), (int)Float.parseFloat(values[3])));
	}
	
	public void setStatus(PlayerStatus status)
	{
		if(this.Status == PlayerStatus.Alive)
		{
			if(!_statusChanged)
				_statusChanged = status != this.Status;
			this.Status = status;
		}
	}
	
	public void Surrender()
	{
		this.setStatus(PlayerStatus.Surrendered);	
	}
	
	public void setLocation(GeoPoint loc)
	{
		_location = loc;
	}
	
	public GeoPoint getLocation()
	{
		return _location;
	}
	
	public double distanceTo(Player player)
	{
		GeoPoint p1 = this._location;
		GeoPoint p2 = player.getLocation();
		
		final double DEG_RATE = 1E6;

	    double startLatitude = p1.getLatitudeE6() / DEG_RATE;
	    double startLongitude= p1.getLongitudeE6() / DEG_RATE;
	    double endLatitude = p2.getLatitudeE6() / DEG_RATE;
	    double endLongitude= p2.getLongitudeE6() / DEG_RATE;
	    float[] result = new float[1];
	    Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, result);

	    return result[0];
	}
	
	public boolean acceptStatusChange()
	{
		boolean value = _statusChanged;
		_statusChanged = false;
		return value;
	}
}
