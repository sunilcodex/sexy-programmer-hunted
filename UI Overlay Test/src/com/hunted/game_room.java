package com.hunted;

/* import����class */
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

/* �۩w�q��Adapter�A�~��android.widget.BaseAdapter */
public class game_room extends BaseAdapter
{
  /* �ܼƫŧi 
     mIcon1�G�^��ڥؿ�������
     mIcon2�G�^��W�@�h������
     mIcon3�G��Ƨ�������
     mIcon4�G�ɮת�����
  */
  private LayoutInflater mInflater;
  private Bitmap mHunter;
  private Bitmap mPlayer;
  private Bitmap mready;
  private List<String> items;
  private List<String> paths;
  /* MyAdapter���غc�l�A�ǤJ�T�ӰѼ�  */  
  public game_room(Context context,List<String> it,List<String> pa)
  {
    /* �Ѽƪ�l�� */
    mInflater = LayoutInflater.from(context);
    items = it;
    paths = pa;
    mHunter = BitmapFactory.decodeResource(context.getResources(), R.drawable.hubter);
    mPlayer = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
    mready = BitmapFactory.decodeResource(context.getResources(), R.drawable.ready);
  }
  
  /* �]�~��BaseAdapter�A���мg�H�Umethod */
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
      /* �ϥΦ۩w�q��file_row�@��Layout */
      convertView = mInflater.inflate(R.layout.row, null);
      /* ��l��holder��text�Picon */
      holder = new ViewHolder();
      holder.text = (TextView) convertView.findViewById(R.id.text);
      
      convertView.setTag(holder);
    }
    else
    {
      holder = (ViewHolder) convertView.getTag();
    }

    
    /* �]�w[�^��ڥؿ�]����r�Picon */
    if(items.get(position).toString().equals("b1"))
    {
      holder.text.setText("Back to /");
      holder.icon = (ImageView) convertView.findViewById(R.id.icon);
      holder.icon.setImageBitmap(mHunter);
    }
    /* �]�w[�^��W�@�h]����r�Picon */
    else if(items.get(position).toString().equals("b2"))
    {
      holder.text.setText("Back to ..");
      holder.icon = (ImageView) convertView.findViewById(R.id.icon);
      holder.icon.setImageBitmap(mPlayer);
      holder.icon = (ImageView) convertView.findViewById(R.id.icon2);
      holder.icon.setImageBitmap(mready);
    }
    /* �]�w[�ɮשθ�Ƨ�]����r�Picon */

    return convertView;
  }
  /* class ViewHolder */
  private class ViewHolder
  {
    TextView text;
    ImageView icon;
  }
}
