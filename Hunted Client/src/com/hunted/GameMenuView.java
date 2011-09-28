package com.hunted;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class GameMenuView extends ListView
{
	private GameMenuAdapter _adapter;

	public GameMenuView(Context context)
	{
		super(context);
		
		_adapter = new GameMenuAdapter(this.getContext());
		this.setAdapter(_adapter);
	}
}
