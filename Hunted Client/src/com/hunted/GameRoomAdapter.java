package com.hunted;

/* import相關class */
import java.io.File;
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

/* 自定義的Adapter，繼承android.widget.BaseAdapter */
public class GameRoomAdapter extends BaseAdapter
{
  /* 變數宣告 
     mIcon1：回到根目錄的圖檔
     mIcon2：回到上一層的圖檔
     mIcon3：資料夾的圖檔
     mIcon4：檔案的圖檔
  */
  private LayoutInflater mInflater;
  private Bitmap mHunter;
  private Bitmap mPlayer;
  private Bitmap mready;
  private List<String> items;
  private List<String> paths;
  /* MyAdapter的建構子，傳入三個參數  */  
  public GameRoomAdapter(Context context,List<String> it,List<String> pa)
  {
    /* 參數初始化 */
    mInflater = LayoutInflater.from(context);
    items = it;
    paths = pa;
    mHunter = BitmapFactory.decodeResource(context.getResources(), R.drawable.ninjaboy2);
    mPlayer = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy);
    mready = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
  }
  
  /* 因繼承BaseAdapter，需覆寫以下method */
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
      /* 使用自定義的file_row作為Layout */
      convertView = mInflater.inflate(R.layout.row, null);
      /* 初始化holder的text與icon */
      holder = new ViewHolder();
      holder.text = (TextView) convertView.findViewById(R.id.text);
      
      convertView.setTag(holder);
    }
    else
    {
      holder = (ViewHolder) convertView.getTag();
    }

    
    /* 設定[回到根目錄]的文字與icon */
    if(items.get(position).toString().equals("b1"))
    {
      holder.text.setText(" Hunter");
      holder.icon = (ImageView) convertView.findViewById(R.id.icon);
      holder.icon.setImageBitmap(mHunter);
    }
    /* 設定[回到上一層]的文字與icon */
    else if(items.get(position).toString().equals("b2"))
    {
      holder.text.setText(" PlayerX");
      holder.icon = (ImageView) convertView.findViewById(R.id.icon);
      holder.icon.setImageBitmap(mPlayer);
      holder.icon = (ImageView) convertView.findViewById(R.id.icon2);
      holder.icon.setImageBitmap(mready);
    }
    /* 設定[檔案或資料夾]的文字與icon */

    return convertView;
  }
  /* class ViewHolder */
  private class ViewHolder
  {
    TextView text;
    ImageView icon;
  }
}
