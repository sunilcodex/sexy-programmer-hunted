package com.hunted;
import java.util.HashMap;

import android.text.format.Time;

public class GameState 
{
	public int PlayerNumber;
	public int HunterNumber;
	public int Alive;
	public int Time;
	
	public GameState()
	{
	}
	
	public void Set(HashMap<String, Object> values)
	{
		if(values.containsKey("PLAYER"))
			this.PlayerNumber = Integer.parseInt((String)values.get("PLAYER"));
		if(values.containsKey("HUNTER"))
			this.HunterNumber = Integer.parseInt((String)values.get("HUNTER"));
		if(values.containsKey("ALIVE"))
			this.Alive = Integer.parseInt((String)values.get("ALIVE"));
		if(values.containsKey("TIME"))
			this.Time = (int)Float.parseFloat((String)values.get("TIME"));
	}
}
