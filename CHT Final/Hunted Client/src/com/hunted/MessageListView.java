package com.hunted;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class MessageListView extends ListView
{
	
	private MessageListAdapter _adapter;

	public MessageListView(Context context)
	{
		super(context);
		_adapter = new MessageListAdapter(this.getContext());
		this.setAdapter(_adapter);
	}

	public void AddMessage(Message msg)
	{
		_adapter.AddMessage(msg);
	}
	
}
