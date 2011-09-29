package com.hunted;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.TrackballGestureDetector;

import android.R.id;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.Contacts.Data;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import com.google.android.maps.MyLocationOverlay;

public class GameActivity extends MapActivity
{

	RelativeLayout _mainLayout;

	private LocationManager mLocationManager01;
	private String strLocationPrivider = "";
	private Location mLocation01 = null;
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
	
	private TextView _txtHunterNum;
	private TextView _txtPlayerNum;
	private TextView _txtMoney;
	private TextView _txtTime;
	
	private Player _player;
	private HashMap<String, Player> _players;
	
	private GameState _gameState;
	private Thread _syncThread;
	private Handler _uiUpdateHandler;
	
	private MessageListView _messageListView;

	private UIHelper _uiHelper;
	private final int MENU_REQUEST = 0;
	private boolean _stopSync;
	
	private GameAI _gameAI;
	private boolean _singlePlayerMode = false;
	private boolean _initMapSuccessed;
	@Override
	protected void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		
		_singlePlayerMode = this.getIntent().getBooleanExtra("single_player", true);

		// basic display setting
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(1);

		_uiUpdateHandler = new Handler();
		
		this.initComponents();
		_initMapSuccessed = this.initMap();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		
		if(_initMapSuccessed)
		{
			// Initialise sync thread
			_stopSync = false;
			_syncThread = new Thread(_syncProcess);
			_syncThread.start();
			
			_uiUpdateHandler.postDelayed(_uiUpdateProcess, 500);
			
			mLocationManager01.requestLocationUpdates(strLocationPrivider, 2000, 10, mLocationListener01);
		}
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		
		if(_initMapSuccessed)
		{
			_uiUpdateHandler.removeCallbacks(_uiUpdateProcess);
			_syncThread.stop();
			_stopSync = true;
			
			mLocationManager01.removeUpdates(mLocationListener01);
		}
	}
	
	private void initComponents()
	{
		// create UI helper for ui scaling
		Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		_uiHelper = new UIHelper(display.getWidth(), display.getHeight());
		
		// create game components
		_gameState = new GameState();
		
		// get player name
		String playerName = this.getIntent().getStringExtra("player_name");
		
		if(_singlePlayerMode)
			SocketConnect.SessionID = new String[] { "100", "100" };	// create fake session id
				
		// create player
		_player = new Player(SocketConnect.SessionID[1], PlayerType.Player,  playerName, true);
		
		if(_singlePlayerMode)
		{
			_gameAI = new GameAI(_gameState, _player);
			_gameAI.init();
		}
		
		
		
		// create player collection
		_players = _singlePlayerMode ? _gameAI.Players : new HashMap<String, Player>();
		_players.put(_player.ID, _player);
		
				
		LayoutInflater inflater = LayoutInflater.from(this);
		_mainLayout = (RelativeLayout) inflater.inflate(R.layout.game, null);
		
		// Get instance of all views
		_txtHunterNum = (TextView)_mainLayout.findViewById(R.id.textView1);
		_txtPlayerNum = (TextView) _mainLayout.findViewById(R.id.textView2);
		_txtTime = (TextView)_mainLayout.findViewById(R.id.textView3);
		_txtMoney = (TextView) _mainLayout.findViewById(R.id.textView4);
		mMapView01 = (MapView) _mainLayout.findViewById(R.id.myMapView1);
		_messageListView = new MessageListView(this);
		
		
		// Create players and map
		GameMapView gameMapView = new GameMapView(this, mMapView01, _player.PlayerType, _uiHelper, _players);
		_mainLayout.addView(gameMapView);
		
		// message list
		_messageListView.setVerticalScrollBarEnabled(false);
		_messageListView.setHorizontalScrollBarEnabled(false);
		MessageListAdapter msgListAdapter = (MessageListAdapter)_messageListView.getAdapter();
		
		msgListAdapter.setTextSize(_uiHelper.scaleHeight(1280, 35));
		msgListAdapter.ItemHeight = _uiHelper.scaleHeight(1280, 90);
		msgListAdapter.ItemPadding = _uiHelper.scaleHeight(1280, 10);
		
		_mainLayout.addView(_messageListView);
		
		
		
		
		// create button
		ImageView bottom = this.getImageView(R.drawable.bottom);
		ImageView top = this.getImageView(R.drawable.top);
		ImageButton button_message = this.getButton(R.drawable.button_message);
		ImageButton button_status = this.getButton(R.drawable.button_status);
		button_status.setOnClickListener(_onMenuButtonClick);	
		// set UI to proper location and size
		
		_uiHelper.SetImageView(bottom, 540, 960, 10, 810);
		_uiHelper.SetImageView(button_message, 540, 960, 5, 825);
		_uiHelper.SetImageView(button_status, 540, 960, 415, 825);
		_uiHelper.SetImageView(top, 540, 960, 0, 0);

		
		_mainLayout.addView(bottom);
		_mainLayout.addView(button_message);
		_mainLayout.addView(button_status);
		_mainLayout.addView(top);

		_txtHunterNum.bringToFront();
		_txtPlayerNum.bringToFront();
		_txtTime.bringToFront();
		_txtMoney.bringToFront();
		
		RelativeLayout.LayoutParams params;
		
		// Hunter number
		int[] newPos = _uiHelper.scalePoint(540, 960, 475, 5);
		params = new RelativeLayout.LayoutParams(_txtHunterNum.getLayoutParams());
		params.setMargins(newPos[0], newPos[1], 0, 0);
		_txtHunterNum.setText("0");
		_txtHunterNum.setLayoutParams(params);
		_txtHunterNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, _uiHelper.scaleHeight(1280, 35));
		
		// Player number
		newPos = _uiHelper.scalePoint(540, 960, 380, 5);
		params = new RelativeLayout.LayoutParams(_txtPlayerNum.getLayoutParams());
		params.setMargins(newPos[0], newPos[1], 0, 0);
		_txtPlayerNum.setText("0");
		_txtPlayerNum.setLayoutParams(params);
		_txtPlayerNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, _uiHelper.scaleHeight(1280, 35));
		
		// Time
		newPos = _uiHelper.scalePoint(720, 1280, 265, 1080);
		params = new RelativeLayout.LayoutParams(_txtTime.getLayoutParams());
		params.setMargins(newPos[0], newPos[1], 0, 0);
		_txtTime.setText("00:00:00");
		_txtTime.setLayoutParams(params);
		_txtTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, _uiHelper.scaleHeight(1280, 65));
		
		// Money
		newPos = _uiHelper.scalePoint(720, 1280, 265, 1180);
		params = new RelativeLayout.LayoutParams(_txtMoney.getLayoutParams());
		params.setMargins(newPos[0], newPos[1], 0, 0);
		_txtMoney.setText("0");
		_txtMoney.setLayoutParams(params);
		_txtMoney.setTextSize(TypedValue.COMPLEX_UNIT_PX, _uiHelper.scaleHeight(1280, 65));
		
		setContentView(_mainLayout);
	}
	
	private void AddMessage(Message msg)
	{
		MessageListAdapter msgListAdapter = (MessageListAdapter)_messageListView.getAdapter();
		msgListAdapter.AddMessage(msg);
		int height = msgListAdapter.getCount() * msgListAdapter.ItemHeight + 5;

		int[] newPos = _uiHelper.scalePoint(720, 1280, 20, 1050);
		int[] newSize = _uiHelper.scaleSize(720, 1280, 680, 0);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(newSize[0], height);
		params.setMargins(newPos[0], newPos[1] - height, 0, 0);
		
		_messageListView.setLayoutParams(params);
	}
	
	private boolean initMap()
	{
		mMapView01.setClickable(false);
		mMapView01.setSatellite(true);
		
		MapController mc = mMapView01.getController();
		mc.setZoom(intZoomLevel);
		
		
		/* 建立LocationManager物件取得系統LOCATION服務 */
		mLocationManager01 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		/* 第一次執行向Location Provider取得Location */
		mLocation01 = getLocationPrivider(mLocationManager01);

		if (mLocation01 != null)
		{
			processLocationUpdated(mLocation01);
		}
		else
		{

			if (this.getResources().getBoolean(R.bool.Debug))
			{
				// FIXME: Give default location (for debug used)
				_player.setLocation(new GeoPoint(24789423, 121003075));
				refreshMapViewByGeoPoint(_player.getLocation());
			}
			else
			{
				// show alert dialog
				Builder alertDlgBuilder = new AlertDialog.Builder(this);
				alertDlgBuilder.setTitle(this.getResources().getString(R.string.error));
				alertDlgBuilder.setMessage(this.getResources().getString(R.string.loc_provider_not_found));
				alertDlgBuilder.setNeutralButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface arg0, int arg1)
					{
						GameActivity.this.finish();
					}
				});
				alertDlgBuilder.show();
				return false;
			}
		}
		
		return true;
	}
	
	
	public final LocationListener mLocationListener01 = new LocationListener() {
		@Override
		public void onLocationChanged(Location location)
		{
			/* 當手機收到位置變更時，將location傳入取得地理座標 */
			processLocationUpdated(location);
		}

		@Override
		public void onProviderDisabled(String provider)
		{
			/* 當Provider已離開服務範圍時 */
		}

		@Override
		public void onProviderEnabled(String provider)
		{
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
		}
	};

	public String getAddressbyGeoPoint(GeoPoint gp)
	{
		String strReturn = "";
		try
		{
			/* 當GeoPoint不等於null */
			if (gp != null)
			{
				/* 建立Geocoder物件 */
				Geocoder gc = new Geocoder(GameActivity.this, Locale.getDefault());

				/* 取出地理座標經緯度 */
				double geoLatitude = (int) gp.getLatitudeE6() / 1E6;
				double geoLongitude = (int) gp.getLongitudeE6() / 1E6;

				/* 自經緯度取得地址（可能有多行地址） */
				List<Address> lstAddress = gc.getFromLocation(geoLatitude, geoLongitude, 1);
				StringBuilder sb = new StringBuilder();

				/* 判斷地址是否為多行 */
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

				/* 將擷取到的地址，組合後放在StringBuilder物件中輸出用 */
				strReturn = sb.toString();
			}
		}
		catch (Exception e)
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
		catch (Exception e)
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
			/* 當Location存在 */
			if (location != null)
			{
				double geoLatitude = location.getLatitude() * 1E6;
				double geoLongitude = location.getLongitude() * 1E6;
				gp = new GeoPoint((int) geoLatitude, (int) geoLongitude);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return gp;
	}

	public void refreshMapViewByGeoPoint(GeoPoint gp)
	{
		try
		{
			MapController mc = mMapView01.getController();
			mc.setCenter(gp);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/* 當手機收到位置變更時，將location傳入更新當下GeoPoint及MapView */
	private void processLocationUpdated(Location location)
	{
		/* 傳入Location物件，取得GeoPoint地理座標 */
		currentGeoPoint = getGeoByLocation(location);
		
		_player.setLocation(currentGeoPoint);

		/* 更新MapView顯示Google Map */
		refreshMapViewByGeoPoint(currentGeoPoint);

		/*
		 * mTextView01.setText (
		 * getResources().getText(R.string.str_my_location).toString()+"\n"+ //
		 * 延伸學習：取出GPS地理座標：
		 * 
		 * getResources().getText(R.string.str_longitude).toString()+
		 * String.valueOf((int)currentGeoPoint.getLongitudeE6()/1E6)+"\n"+
		 * getResources().getText(R.string.str_latitude).toString()+
		 * String.valueOf((int)currentGeoPoint.getLatitudeE6()/1E6)+"\n"+
		 * 
		 * getAddressbyGeoPoint(currentGeoPoint) );
		 */
	}

	@Override
	protected boolean isRouteDisplayed()
	{
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
	
	ImageView getImageView(int id)
	{
		ImageView image = new ImageView(this);
		image.setImageResource(id);
		image.setBackgroundColor(Color.TRANSPARENT);
		image.setPadding(0, 0, 0, 0);
		image.setScaleType(ScaleType.FIT_XY);
		return image;
	}
	
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{ 
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == MENU_REQUEST && resultCode==RESULT_OK)
		{  
			// check selected menu
			int selectedItem = data.getExtras().getInt("selected_menu_id");
			
			switch (selectedItem) 
			{  
			case GameMenuAdapter.MENU_SURRENDER:
				_player.Surrender();
				break;
			default:
				break;
			}
		}
	}
	
	private final int TIMEOUT_DIALOG = 0;
	
	
	protected Dialog onCreateDialog(int id) 
	{  
		if(id == TIMEOUT_DIALOG)
		{
			Dialog dialog = new Dialog(this, R.style.TimeoutDialog); 
			
			LayoutInflater inflater = LayoutInflater.from(this);
			LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.timeout_dlg, null);
			dialog.setContentView(layout);


			TextView txtView = (TextView)layout.findViewById(R.id.txtTimeout);
			txtView.setTextSize(TypedValue.COMPLEX_UNIT_PX, _uiHelper.scaleHeight(1280, 64));

			Button btn = (Button)layout.findViewById(R.id.btnTimeoutOK);
			btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, _uiHelper.scaleHeight(1280, 30));
			btn.setHeight(_uiHelper.scaleHeight(1280, 40));
			btn.setMinimumHeight(_uiHelper.scaleHeight(1280, 40));
			btn.setTag(dialog);
			btn.setOnClickListener(_onTimeoutOKClick);
			
			int paddingH = _uiHelper.scaleHeight(1280, 40);
			int paddingW = _uiHelper.scaleHeight(1280, 60);
			layout.setPadding(paddingW, paddingH, paddingW, paddingH);
			
			return dialog;
		}
		return null;
	}
	
	private  OnClickListener _onTimeoutOKClick = new OnClickListener(){
		@Override
		public void onClick(View v)
		{
			((Dialog)v.getTag()).dismiss();
			
			// Back to end screen
			Intent intent = new Intent();
			intent.setClass(GameActivity.this, GameoverActivity.class);
			startActivity(intent);
		}
	};
	
	private OnClickListener _onMenuButtonClick = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(GameActivity.this, GameMenuActivity.class);
			startActivityForResult(intent, MENU_REQUEST);
		}
	};
	
	private Runnable _uiUpdateProcess = new Runnable()
	{
		public void run()
		{
			_txtPlayerNum.setText(Integer.toString(_gameState.Alive) + "/" + Integer.toString(_gameState.PlayerNumber));
			_txtHunterNum.setText(Integer.toString(_gameState.HunterNumber));
			_txtTime.setText(DateUtils.formatElapsedTime(_gameState.Time));
			_txtMoney.setText(Integer.toString(_player.Money));
			
			refreshMapViewByGeoPoint(_player.getLocation());
			
			// Check player status
			HashMap<String, Player> players = _players;
			for(Player player: players.values())
			{
				boolean statusChanged = player.acceptStatusChange();
				if(statusChanged)
				{
					Message msg = new Message();
					switch(player.Status)
					{
					case Caught:
						msg.Message = player.Name + " " + getResources().getString(R.string.been_caught);
						break;
					case Surrendered:
						msg.Message = player.Name + " " + getResources().getString(R.string.surrendered);
						break;
					default:
						continue;
					}
					
					msg.Time = _gameState.Time;
					msg.Icon = BitmapFactory.decodeResource(getResources(), R.drawable.boy_small);
					GameActivity.this.AddMessage(msg);
				}
			}

			if(_gameState.Time == 0)
			{
				GameActivity.this.showDialog(GameActivity.this.TIMEOUT_DIALOG);
			}
			else
			{
				_uiUpdateHandler.postDelayed(_uiUpdateProcess, 500);
			}
		}
	};

	private Runnable _syncProcess = new Runnable(){
		@Override
		public void run() 
		{
			while (true) 
			{
				try 
				{
					if(_stopSync)
						return;
					
					if(!_singlePlayerMode)
					{
						String response;
	
						if (_player.PlayerType == PlayerType.Player) 
						{
							response = SocketConnect.Instance.PlayerInGame(SocketConnect.Instance, SocketConnect.SessionID, 
									Integer.toString(_player.getLocation().getLatitudeE6()), Integer.toString(_player.getLocation().getLongitudeE6()), _player.Status == PlayerStatus.Surrendered);
						} 
						else 
						{
							response = SocketConnect.Instance.HunterInGame(SocketConnect.Instance, SocketConnect.SessionID, 
									Integer.toString(_player.getLocation().getLatitudeE6()), Integer.toString(_player.getLocation().getLongitudeE6()));
						}
	
						HashMap<String, Object> values = SocketConnect.parse(response);
						_gameState.Set(values);
	
						// Set player state
						_player.Money = Integer.parseInt((String) values.get("MONEY"));
	
						// Set all players state
						if (values.containsKey("USER")) 
						{
							for (String[] userData : ((HashMap<String, String[]>) values.get("USER")).values()) 
							{
								String id = userData[0];
								Player player;
								if (!_players.containsKey(id))
									_players.put(id, new Player(id, _player.PlayerType, userData[1],
											id == _player.ID));
	
								player = _players.get(id);
	
								if (getResources().getBoolean(R.bool.Debug)) 
								{
									if (player.Self) 
									{
										player.setLocation(_player.getLocation());
									} 
									else 
									{
										player.set(userData);
	
										player.setLocation(new GeoPoint(
												(int) (_player.getLocation().getLatitudeE6() + player.getLocation().getLatitudeE6() * 0.5f),
												(int) (_player.getLocation().getLongitudeE6() + player.getLocation().getLongitudeE6() * 0.5f)));
									}
								} 
								else 
								{
									player.set(userData);
								}
							}
						}
						
					}
					else
					{
						_gameAI.update();
					}
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}

				catch (Exception e) 
				{
					e.printStackTrace();
				}

				try 
				{
					Thread.sleep(1000);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
	};
}
