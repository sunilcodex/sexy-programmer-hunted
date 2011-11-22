package com.hunted;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import android.os.Handler;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameMenuAdapter extends BaseAdapter
{
	public static final int MENU_SURRENDER = 0;
	public static final int MENU_ARREST = 0;
	
	public int MessageAlive = 30;
	private String[][] _menus;
	private Bitmap[] _menuIcons;
	private Context _context;
	private int _textSize;
	public int ItemHeight;
	public int ItemPadding;
	public int PlayerType;
	
	public GameMenuAdapter(Context context, int playerType)
	{
		_context = context;
		_menus = new String[][] { 
				{context.getResources().getString(R.string.surrender)},	// for player
				{context.getResources().getString(R.string.arrest)}		// for hunter
				};
		_menuIcons = new Bitmap[] { BitmapFactory.decodeResource(context.getResources(), R.drawable.flag) };
		
		this.PlayerType = playerType;
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
		return _menus[this.PlayerType].length;
	}

	@Override
	public Object getItem(int index)
	{
		return _menus[this.PlayerType][index];
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
			itemView = inflater.inflate(R.layout.game_menu_item, null);
			
			item = new ItemViewHolder();
			//item.Icon = (ImageView)itemView.findViewById(R.id.game_menu_icon);
			item.Title = (TextView)itemView.findViewById(R.id.game_menu_title);
			
			item.Title.setTextSize(TypedValue.COMPLEX_UNIT_PX, _textSize);
			
			itemView.setTag(item);
			
			itemView.setPadding(this.ItemPadding, this.ItemPadding, this.ItemPadding, this.ItemPadding);
			itemView.setMinimumHeight(ItemHeight);
			//item.Icon.setAdjustViewBounds(true);
			//item.Icon.setMaxHeight(ItemHeight - itemView.getPaddingTop() - itemView.getPaddingBottom());
			
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)item.Title.getLayoutParams();
			params.height = ItemHeight - itemView.getPaddingTop() - itemView.getPaddingBottom();
			item.Title.setLayoutParams(params);
		}
		else
		{
			item = (ItemViewHolder)itemView.getTag();
		}
		
		String menu = _menus[this.PlayerType][index];
		//item.Icon.setImageBitmap(_menuIcons[index]);
		item.Title.setText(menu);
		
		return itemView;
	}


	class ItemViewHolder
	{
		ImageView Icon;
		TextView Title;
	}
}
