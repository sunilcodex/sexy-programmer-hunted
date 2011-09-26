package com.hunted;

import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;


public class GameActivity extends MapActivity
{
  
  RelativeLayout _mainLayout;
  
  private LocationManager mLocationManager01;
  private String strLocationPrivider = "";
  private Location mLocation01=null;
  private TextView mTextView01;
  private TextView money_got;
  private TextView during_time;
  private TextView hunter_n;
  private TextView player_n;
  private MapView mMapView01;
  private GeoPoint currentGeoPoint;
  private int intZoomLevel = 20;
  private MyLocationOverlay mylayer;
  private MapController mapController;

  @Override
  protected void onCreate(Bundle icicle)
  {
    // TODO Auto-generated method stub
    super.onCreate(icicle);
    
   // setContentView(R.layout.main);
   
    // create UI helper for ui scaling
    Display display = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    UIHelper uiHelper = new UIHelper(display.getWidth(),display.getHeight());

    // basic display setting
      this.requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
      setRequestedOrientation(1);

    // create button
    ImageButton bottom = this.getButton(R.drawable.bottom);
    ImageButton button_message = this.getButton(R.drawable.button_message);
    ImageButton button_status = this.getButton(R.drawable.button_status);
    ImageButton top = this.getButton(R.drawable.top);
    
    // set UI to proper location and size
    uiHelper.SetImageView(bottom, 540, 960, 10, 790);
    uiHelper.SetImageView(button_message, 540, 960, 0, 800);
    uiHelper.SetImageView(button_status, 540, 960, 415, 800);
    uiHelper.SetImageView(top, 540, 960, 0, 0);
    bottom.setScaleType(ScaleType.FIT_XY);
    top.setScaleType(ScaleType.FIT_XY);
    
    LayoutInflater inflater = LayoutInflater.from(this);
    _mainLayout = (RelativeLayout)inflater.inflate(R.layout.game, null);
    _mainLayout.addView(bottom);
    _mainLayout.addView(button_message);
    _mainLayout.addView(button_status);
    _mainLayout.addView(top);
   
    mTextView01 = (TextView)_mainLayout.findViewById(R.id.myTextView1);
    
    //create text argument
    Argument a=new Argument();
    a.hunter_n="3";
    a.player_n="4";
    a.time = "300";
    a.money="10000";
    // set text argument to textView
    
    //mTextView01 = (TextView)_mainLayout.findViewById(R.id.myTextView1);
   // mTextView01.setText(a.hunter_n);
   // _mainLayout.addView(mTextView01);
   
    /* �إ�MapView���� */
    mMapView01 = (MapView)_mainLayout.findViewById(R.id.myMapView1);
    
    //setContentView(R.layout.main);
    
    
    
    /* �إ�LocationManager������o�t��LOCATION�A�� */
    mLocationManager01 = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    
    /* �Ĥ@������VLocation Provider���oLocation */
    mLocation01 = getLocationPrivider(mLocationManager01);
    
    if(mLocation01!=null)
    {
      processLocationUpdated(mLocation01);
    }
    else
    {
    	//FIXME
//      mTextView01.setText
//      (
//        getResources().getText(R.string.str_err_location).toString()
//      );
    }
    /* �إ�LocationManager����A��ťLocation�ܧ�ɨƥ�A��sMapView */
    mLocationManager01.requestLocationUpdates(strLocationPrivider, 2000, 10, mLocationListener01);

    //setContentView(R.layout.main);
    setContentView(_mainLayout);
    
  }
  
  public final LocationListener mLocationListener01 = new LocationListener()
  {
    @Override
    public void onLocationChanged(Location location)
    {
      // TODO Auto-generated method stub
      
      /* ���������m�ܧ�ɡA�Nlocation�ǤJ���o�a�z�y�� */
      processLocationUpdated(location);
    }
    
    @Override
    public void onProviderDisabled(String provider)
    {
      // TODO Auto-generated method stub
      /* ��Provider�w���}�A�Ƚd��� */
    }
    
    @Override
    public void onProviderEnabled(String provider)
    {
      // TODO Auto-generated method stub
    }
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
      // TODO Auto-generated method stub
      
    }
  };
  
  public String getAddressbyGeoPoint(GeoPoint gp)
  {
    String strReturn = "";
    try
    {
      /* ��GeoPoint������null */
      if (gp != null)
      {
        /* �إ�Geocoder���� */
        Geocoder gc = new Geocoder(GameActivity.this, Locale.getDefault());
        
        /* ���X�a�z�y�иg�n�� */
        double geoLatitude = (int)gp.getLatitudeE6()/1E6;
        double geoLongitude = (int)gp.getLongitudeE6()/1E6;
        
        /* �۸g�n�ר��o�a�}�]�i�঳�h��a�}�^ */
        List<Address> lstAddress = gc.getFromLocation(geoLatitude, geoLongitude, 1);
        StringBuilder sb = new StringBuilder();
        
        /* �P�_�a�}�O�_���h�� */
        if (lstAddress.size() > 0)
        {
          Address adsLocation = lstAddress.get(0);

          for (int i = 0; i < adsLocation.getMaxAddressLineIndex(); i++)
          {
            sb.append(adsLocation.getAddressLine(i)).append("\n");
          }
          sb.append(adsLocation.getLocality()).append("\n");
          sb.append(adsLocation.getPostalCode()).append("\n");
          sb.append(adsLocation.getCountryName());
        }
        
        /* �N�^���쪺�a�}�A�զX���bStringBuilder���󤤿�X�� */
        strReturn = sb.toString();
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    return strReturn;
  }
  
  public Location getLocationPrivider(LocationManager lm)
  {
    Location retLocation = null;
    try
    {
      Criteria mCriteria01 = new Criteria();
      mCriteria01.setAccuracy(Criteria.ACCURACY_FINE);
      mCriteria01.setAltitudeRequired(false);
      mCriteria01.setBearingRequired(false);
      mCriteria01.setCostAllowed(true);
      mCriteria01.setPowerRequirement(Criteria.POWER_LOW);
      strLocationPrivider = lm.getBestProvider(mCriteria01, true);
      retLocation = lm.getLastKnownLocation(strLocationPrivider);
    }
    catch(Exception e)
    {
      mTextView01.setText(e.toString());
      e.printStackTrace();
    }
    return retLocation;
  }
  
  private GeoPoint getGeoByLocation(Location location)
  {
    GeoPoint gp = null;
    try
    {
      /* ��Location�s�b */
      if (location != null)
      {
        double geoLatitude = location.getLatitude()*1E6;
        double geoLongitude = location.getLongitude()*1E6;
        gp = new GeoPoint((int) geoLatitude, (int) geoLongitude);
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    return gp;
  }
  
  public void refreshMapViewByGeoPoint(GeoPoint gp, MapView mv, int zoomLevel, boolean bIfSatellite)
  {
    try
    {
      mv.displayZoomControls(true);
      /* ���oMapView��MapController */
      MapController mc = mv.getController();
      /* ���ܸӦa�z�y�Ц�} */
      mc.animateTo(gp);
      //��ܥX�ۤv�ثe����m
      List<com.google.android.maps.Overlay> ol = mv.getOverlays();
      mylayer= new MyLocationOverlay(this, mv);
      mylayer.enableCompass();//ù�L
      mylayer.enableMyLocation();
      ol.add(mylayer);
      
      
    //create text argument
      Argument a=new Argument();
      a.hunter_n="3";
      a.player_n="4";
      a.time = "300";
      a.money="10000";
      hunter_n = (TextView)_mainLayout.findViewById(R.id.textView1); 
      player_n = (TextView)_mainLayout.findViewById(R.id.textView2);
      during_time = (TextView)_mainLayout.findViewById(R.id.textView3);
      money_got = (TextView)_mainLayout.findViewById(R.id.textView4);
      
      during_time.setText(a.time+"(s)");
      during_time.setTextSize(25);
      during_time.setTextColor(Color.YELLOW);
      //_mainLayout.addView(during_time);
      money_got.setText(a.money);
      money_got.setTextSize(25);
      money_got.setTextColor(Color.YELLOW);
      _mainLayout.addView(money_got);
      /* ��j�a�ϼh�� */
      mc.setZoom(zoomLevel);
      
      /* �����ǲߡG���oMapView���̤j��j�h�� */
      //mv.getMaxZoomLevel()
      
      
      /* �]�wMapView����ܿﶵ�]�ìP�B��D�^*/
      if(bIfSatellite)
      {
        mv.setSatellite(true);
        mv.setStreetView(true);
      }
      else
      {
        mv.setSatellite(false);
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
  
  /* ���������m�ܧ�ɡA�Nlocation�ǤJ��s��UGeoPoint��MapView */
  private void processLocationUpdated(Location location)
  {
    /* �ǤJLocation����A���oGeoPoint�a�z�y�� */
    currentGeoPoint = getGeoByLocation(location);
    
    /* ��sMapView���Google Map */
    refreshMapViewByGeoPoint(currentGeoPoint, mMapView01, intZoomLevel, true);
    
    /*mTextView01.setText
    (
      getResources().getText(R.string.str_my_location).toString()+"\n"+
      // �����ǲߡG���XGPS�a�z�y�СG 
      
      getResources().getText(R.string.str_longitude).toString()+
      String.valueOf((int)currentGeoPoint.getLongitudeE6()/1E6)+"\n"+
      getResources().getText(R.string.str_latitude).toString()+
      String.valueOf((int)currentGeoPoint.getLatitudeE6()/1E6)+"\n"+
      
      getAddressbyGeoPoint(currentGeoPoint)
    );*/
  }
  
  @Override
  protected boolean isRouteDisplayed()
  {
    // TODO Auto-generated method stub
    return false;
  }
  
  ImageButton getButton(int id)
  {
    ImageButton button = new ImageButton(this);
    button.setImageResource(id);
    button.setBackgroundColor(Color.TRANSPARENT);
    button.setPadding(0, 0, 0, 0);
    button.setScaleType(ScaleType.FIT_XY);
  return button;
  }
}
