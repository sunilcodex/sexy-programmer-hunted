package com.hunted;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageListAdapter extends BaseAdapter
{
	public int MaxMessages = 4;
	private LinkedList<Message> _messages = new LinkedList<Message>();
	private Context _context;
	private int _textSize;
	public int ItemHeight;
	public int ItemPadding;
	
	public MessageListAdapter(Context context)
	{
		_context = context;
	}
	
	public void setTextSize(int pxSize)
	{
		_textSize = pxSize;
	}
	
	public void setItemHeight(int pxSize)
	{
		ItemHeight = pxSize;
	}

	@Override
	public int getCount()
	{
		return _messages.size();
	}

	@Override
	public Object getItem(int index)
	{
		return _messages.get(index);
	}

	@Override
	public long getItemId(int index)
	{
		return index;
	}
	
	@Override
	public View getView(int index, View itemView, ViewGroup parent)
	{
		ItemViewHolder item;

		if (itemView == null)
		{
			LayoutInflater inflater = LayoutInflater.from(_context);
			itemView = inflater.inflate(R.layout.message_item, null);
			
			item = new ItemViewHolder();
			item.Icon = (ImageView)itemView.findViewById(R.id.message_icon);
			item.Message = (TextView)itemView.findViewById(R.id.message);
			item.Time = (TextView)itemView.findViewById(R.id.message_time);
			
			item.Message.setTextSize(TypedValue.COMPLEX_UNIT_PX, _textSize);
			item.Time.setTextSize(TypedValue.COMPLEX_UNIT_PX, _textSize);
			
			itemView.setTag(item);
			itemView.setMinimumHeight(ItemHeight);
			item.Icon.setAdjustViewBounds(true);
			item.Icon.setMaxHeight(ItemHeight - itemView.getPaddingTop() - itemView.getPaddingBottom());
		}
		else
		{
			item = (ItemViewHolder)itemView.getTag();
		}
		
		Message msg = _messages.get(index);
		item.Icon.setImageBitmap(msg.Icon);
		item.Message.setText(msg.Message);
		item.Time.setText(DateUtils.formatElapsedTime(msg.Time));
		
		return itemView;
	}

	public void AddMessage(Message msg)
	{
		_messages.add(msg);
		if(_messages.size() > this.MaxMessages)
			_messages.poll();
		
		notifyDataSetChanged();
	}


	class ItemViewHolder
	{
		ImageView Icon;
		TextView Message;
		TextView Time;
	}
}
