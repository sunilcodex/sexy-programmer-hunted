package com.hunted;

import java.util.HashMap;
import java.util.Random;

import android.location.Location;

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
		
		//this.Players.put("001", new Player("001", PlayerType.Hunter, "貓", false));
		//this.Players.put("002", new Player("002", PlayerType.Hunter, "Angel", false));
		//this.Players.put("003", new Player("003", PlayerType.Hunter, "柏丞", false));
		
		this.Players.put("004", new Player("004", PlayerType.Player, "孟孟", false));
		this.Players.put("005", new Player("005", PlayerType.Player, "Kailin", false));
		this.Players.put("006", new Player("006", PlayerType.Player, "文翔", false));
		this.Players.put("007", new Player("007", PlayerType.Player, "Ken", false));
		
		GameState.Time = 30;
		GameState.PlayerNumber = 4;
		GameState.HunterNumber = 3;
		GameState.Alive = 4;
	}
	boolean _playersLocSetted = false;
	public void update()
	{
		//TODO: implement this
		boolean playersLocSetted = false;
		for (Player player : this.Players.values())
		{
			if(player.Self)
			{
				// Change player money
				player.Money+=10;
			}
			else
			{   
				playersLocSetted = (!_playersLocSetted && 
									this.Player.getLocation().getLatitudeE6() != 0 && 
									this.Player.getLocation().getLongitudeE6() != 0);
				
				if( playersLocSetted || (player.getLocation().getLatitudeE6() == 0 && player.getLocation().getLongitudeE6() == 0))
				{
					// set location of other player
					player.setLocation(new GeoPoint(
							this.Player.getLocation().getLatitudeE6()+ _random.nextInt(700) - 350, 
							this.Player.getLocation().getLongitudeE6()+ _random.nextInt(700) - 350));
				}
				else
				{
					// set location of other player
					player.setLocation(new GeoPoint(
							player.getLocation().getLatitudeE6() + move_step(generate_random()%10, generate_random()%3), 
							player.getLocation().getLongitudeE6() + move_step(generate_random()%10, generate_random()%2)));
				}
				
				if(_playersLocSetted)
				{
					if(player.PlayerType == PlayerType.Hunter && player.distanceTo(this.Player) ==0)
					{
						this.Player.setStatus(PlayerStatus.Caught);
						GameState.Alive--;
					}
							
					//other virtual player have been caught
					for (Player player1 : this.Players.values())
					{
						if(player.PlayerType == PlayerType.Hunter && player1.PlayerType == PlayerType.Player && !player1.Self)
						{
							if(player.distanceTo(player1) <3 && player1.Status == PlayerStatus.Alive)
							{
								player1.setStatus(PlayerStatus.Caught);
								GameState.Alive--;
						    }				
					     }
					}
				}
				// set player status
				//player.setStatus(PlayerStatus.Caught);
				//player.setStatus(PlayerStatus.Surrendered);
				
			}
		}
		
		if(playersLocSetted)
			_playersLocSetted = true;
		
		// Change game time
		if(GameState.Time > 0)
		{
			GameState.Time--;
		}
	}
	public static int generate_random()
	  {
		  Random myrandom;
		   myrandom = new Random();
		   if(myrandom.nextInt()/1000000 < 0)
		   {
			 return -(myrandom.nextInt()/1000000);
		   }
		   else
		   {
			   return myrandom.nextInt()/1000000;
		   }
	  }
	/*
	 * 本函式目的:　決定下一步要進還是退 以及 幾步
	 * speed: 移動速度
	 * step: 選擇要往哪個方向走 
	 * */
	public int move_step(int speed, int step)
	{
		int return_value=0;
		try
		{
				switch(step){
				case 0://往前走
					return_value= 10*speed;
				    break;
				case 1://往後走
					return_value= -10*speed;
					break;
				case 2://不動
					return_value = 0;
				default:	//不動
					return_value= 0;
				}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return return_value;
	}
}
