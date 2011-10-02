package com.hunted_free_v1;

/* import?��?class */
import java.io.File;
import java.io.IOException;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/* ?��?義�?Adapter，繼?�android.widget.BaseAdapter */
public class GameRoomAdapter extends BaseAdapter
{
	
  private LayoutInflater mInflater;
  private Bitmap mHunter;
  private Bitmap mPlayer;
  private Bitmap mready;
  private Bitmap mno;
  private List<String> items;
  private List<String> paths;
  private List<String> ready;
  
  /* MyAdapter?�建構�?，傳?��??��??? */  
  public GameRoomAdapter(Context context,List<String> it,List<String> pa,List<String> re)
  {
    /* ?�數?��???*/
    mInflater = LayoutInflater.from(context);
    items = it;	//name
    paths = pa; //statue
    ready = re;//ready or not
    mHunter = BitmapFactory.decodeResource(context.getResources(), R.drawable.ninjaboy2);
    mPlayer = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy);
    mready = BitmapFactory.decodeResource(context.getResources(), R.drawable.check);
    mno = BitmapFactory.decodeResource(context.getResources(), R.drawable.nothing);
  }

  
  /* ?�繼?�BaseAdapter，�?覆寫以�?method */
  @Override
  public int getCount()
  {
    return items.size();
  }

  @Override
  public Object getItem(int position)
  {
    return items.get(position);
  }
  
  @Override
  public long getItemId(int position)
  {
    return position;
  }
  
  @Override
  public View getView(int position,View convertView,ViewGroup parent)
  {
    ViewHolder holder;
    if(convertView == null)
    {
       //使用?��?義�?file_row作為Layout 
      convertView = mInflater.inflate(R.layout.row, null);
      // ?��??�holder?�text?�icon 
      holder = new ViewHolder();
      holder.text = (TextView) convertView.findViewById(R.id.text);
      
      convertView.setTag(holder);
    }
    else
    {
      holder = (ViewHolder) convertView.getTag();
    }

    
    if(paths.get(position).toString().equals("H"))
    {
           holder.text.setText(items.get(position).toString());
           holder.icon = (ImageView) convertView.findViewById(R.id.icon);
           holder.icon.setImageBitmap(mHunter);
    }
    
    else
    {  	
            holder.text.setText(items.get(position).toString());
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.icon.setImageBitmap(mPlayer);

    }
    
    if(ready.get(position).toString().equals("O")){
        holder.icon = (ImageView) convertView.findViewById(R.id.icon2);
        holder.icon.setImageBitmap(mready);
    }
    else{
        holder.icon = (ImageView) convertView.findViewById(R.id.icon2);
        holder.icon.setImageBitmap(mno);
    }

    return convertView;
  }
  /* class ViewHolder */
  private class ViewHolder
  {
    TextView text;
    ImageView icon;
  }
}
