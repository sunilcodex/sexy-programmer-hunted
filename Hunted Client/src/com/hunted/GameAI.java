package com.hunted;

import java.util.HashMap;
import java.util.Random;

import com.google.android.maps.GeoPoint;


public class GameAI
{
	public HashMap<String, Player> Players = new HashMap<String, Player>();
	public GameState GameState;
	public Player Player;
	
	//Test
	private Random _random = new Random();
	
	public GameAI(GameState gameState, Player player)
	{
		this.GameState = gameState;
		this.Player = player;
	}
	public void init()
	{
		//TODO: implement this
		this.Players.put(this.Player.ID, this.Player);	// player self
		
		this.Players.put("001", new Player("001", PlayerType.Hunter, "Test Hunter 1", false));
		this.Players.put("002", new Player("002", PlayerType.Hunter, "Test Hunter 2", false));
		this.Players.put("003", new Player("003", PlayerType.Hunter, "Test Hunter 3", false));
		
		this.Players.put("004", new Player("004", PlayerType.Player, "Test Player 1", false));
		this.Players.put("005", new Player("005", PlayerType.Player, "Test Player 2", false));
		this.Players.put("006", new Player("006", PlayerType.Player, "Test Player 3", false));
		
		GameState.Time = 10000;
		GameState.PlayerNumber = 3;
		GameState.HunterNumber = 3;
		GameState.Alive = 3;
	}
	
	public void update()
	{
		//TODO: implement this
		for (Player player : this.Players.values())
		{
			if(player.Self)
			{
				// Change player money
				player.Money+=10;
			}
			else
			{
				// set location of other player
				player.setLocation(new GeoPoint(
						this.Player.getLocation().getLatitudeE6() + _random.nextInt(2000) - 1000, 
						this.Player.getLocation().getLongitudeE6() + _random.nextInt(2000) - 1000));
				
				// set player status
				//player.setStatus(PlayerStatus.Caught);
				//player.setStatus(PlayerStatus.Surrendered);
			}
		}
		
		// Change game time
		if(GameState.Time > 0)
		{
			GameState.Time--;
		}
		
		
	}
}
