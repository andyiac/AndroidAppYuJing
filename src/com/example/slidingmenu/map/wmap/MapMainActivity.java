package com.example.slidingmenu.map.wmap;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.search.core.AMapException;
import com.amap.api.search.core.LatLonPoint;
import com.amap.api.search.poisearch.PoiItem;
import com.amap.api.search.poisearch.PoiPagedResult;
import com.amap.api.search.poisearch.PoiSearch;
import com.amap.api.search.poisearch.PoiSearch.SearchBound;
import com.amap.api.search.poisearch.PoiTypeDef;
import com.amap.api.search.route.Route;
import com.example.slidingmenu.R;
import com.example.slidingmenu.map.util.AMapUtil;
import com.example.slidingmenu.map.util.Constants;
import com.example.slidingmenu.map.util.ToastUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapMainActivity extends FragmentActivity implements
        OnMarkerClickListener, OnInfoWindowClickListener, OnMarkerDragListener, InfoWindowAdapter,AMapLocationListener,LocationSource {
	
	private AMap aMap;
	private Marker mymarker;
	private Marker myLocMarker;
	private LatLng myLocation;
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyyMMddHHmmss", Locale.CHINA);
	
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	
	private PoiPagedResult result;
	private ProgressDialog progDialog = null;
	private int curpage = 1;
	private int cnt = 0;
	
	double lat=0;
	double lng=0;
	/*
	 * 路径导航变量
	 */
	private List<Route> routeResult;
	private RouteOverlay routeOverlay;
	private Route route;
	private LinearLayout routeNav;
	private ImageButton routePre, routeNext;
	private int mode = Route.BusDefault;//BusDefault 路径为公交模式。DrivingDefault 路径为自驾模式。WalkDefault 路径为步行模式。

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_main_activity);
		init();
		
		routeNav = (LinearLayout) findViewById(R.id.LinearLayoutLayout_index_bottom);
		routePre = (ImageButton) findViewById(R.id.pre_index);
		routePre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (routeOverlay != null) {
					boolean enablePre = routeOverlay.showPrePopInfo();
					if (!enablePre) {
						routePre.setBackgroundResource(R.drawable.prev_disable);
						routeNext.setBackgroundResource(R.drawable.next);
					} else {
						routePre.setBackgroundResource(R.drawable.prev);
						routeNext.setBackgroundResource(R.drawable.next);
					}
				}
			}
		});
		routeNext = (ImageButton) findViewById(R.id.next_index);
		routeNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (routeOverlay != null) {
					boolean enableNext = routeOverlay.showNextPopInfo();
					if (!enableNext) {
						routePre.setBackgroundResource(R.drawable.prev);
						routeNext.setBackgroundResource(R.drawable.next_disable);
					} else {
						routePre.setBackgroundResource(R.drawable.prev);
						routeNext.setBackgroundResource(R.drawable.next);
					}
				}
			}
		});
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	
	/*
	 * 地图初始化
	 */
	public void init(){
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.wmap)).getMap();
			if (AMapUtil.checkReady(this, aMap)) {
				setupMap();
			}
		}		
	}
	/*
	 * 地图设置
	 */
	public void setupMap(){
		UiSettings uiSettings=aMap.getUiSettings();
		uiSettings.setCompassEnabled(true);
		//uiSettings.setMyLocationButtonEnabled(true);
		uiSettings.setScaleControlsEnabled(true);
		//uiSettings.setScrollGesturesEnabled(false);默认为true
		//uiSettings.setLogoPosition(int arg0);
		//uiSettings.setRotateGesturesEnabled(false);//默认为true
		uiSettings.setTiltGesturesEnabled(true);//倾斜旋转以获取3D效果
		//指定上海经纬度
//		LatLng sh=new LatLng(31.240059,121.499614);
        //定位河北北方学院
		LatLng sh=new LatLng(40.772063,114.891850);
		//CameraUpdateFactory.newLatLng(sh);//返回一个移动目的地的屏幕中心点的经纬度的CameraUpdate 对象。
		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sh, 12));
		
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器	
		//aMap.getScalePerPixel();//返回每像素代表的距离
		//长按地图
		aMap.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),"您正在长按地图", Toast.LENGTH_SHORT).show();
			}
		});
		//-----自定义定位图标--------------------------------------
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));
		myLocationStyle.strokeColor(Color.GRAY);
		myLocationStyle.strokeWidth(3);
		aMap.setMyLocationStyle(myLocationStyle);
		mAMapLocationManager = LocationManagerProxy
				.getInstance(MapMainActivity.this);
		aMap.setLocationSource(this);
		aMap.setMyLocationEnabled(true);// 设置为true表示系统定位按钮显示并响应点击，false表示隐藏，默认是false		
	}

	@Override
	protected void onPause() {
		super.onPause();
		deactivate();
	}
	/*
	 * 菜单响应事件 
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		 case R.id.menu1:
			 aMap.getMapPrintScreen(new AMap.onMapPrintScreenListener() {
				
				@Override
				public void onMapPrint(Drawable r) {
					// TODO Auto-generated method stub
					Bitmap bitmap=((BitmapDrawable)r).getBitmap();
					try {  
			            FileOutputStream fos = new FileOutputStream(Environment  
			                    .getExternalStorageDirectory() + "/test_"  
			                    + sdf.format(new Date()) + ".png");  
			            boolean b = bitmap.compress(CompressFormat.PNG, 100, fos);  
			            try {  
			                fos.flush();  
			            } catch (IOException e) {  
			                e.printStackTrace();  
			            }  
			            try {  
			                fos.close();  
			            } catch (IOException e) {  
			                e.printStackTrace();  
			            }  
			            if (b)  
			                Toast.makeText(MapMainActivity.this, "截屏成功",
			                        Toast.LENGTH_SHORT).show();  
			        } catch (FileNotFoundException e) {  
			                    e.printStackTrace();  
			        }
				}
			});
			 break;
		 case R.id.menu2:
			 //添加Marker  东方明珠：31.23983,121.499924
			 LatLng marker = new LatLng(31.23983,121.499924);
			 mymarker=aMap.addMarker(new MarkerOptions().position(marker).title("东方明珠电视塔")
					 .snippet("上海最高的电视塔")  
			         .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
			         .draggable(true)); 			
			 break;	
		 case R.id.menu3:
			 //添加多段线
			 aMap.addPolyline((new PolylineOptions()).add(new LatLng(31.238142,121.501512),
	                 new LatLng(31.239114,121.506533),
	                 new LatLng(31.230307,121.503786))
	                 .width(5).color(Color.RED));  
			 break;		
		 case R.id.menu4:
			 //添加多边形
			 aMap.addPolygon(new PolygonOptions().addAll(createRectangle
		                (new LatLng(31.233335,121.497284),0.01, 0.01)).fillColor(Color.YELLOW)
		                .strokeColor(Color.GREEN).strokeWidth(3));
			 break;
		 case R.id.menu5:
			 aMap.addCircle(new CircleOptions().center(new LatLng(31.232546,121.473328))
                     .radius(1000).strokeColor(Color.BLUE).strokeWidth(3).fillColor(Color.TRANSPARENT).visible(true));  
			 break;
		 case R.id.menu6:
			 //Toast.makeText(getApplicationContext(),"高德地图遥感影像", Toast.LENGTH_SHORT).show();
			 aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
			 aMap.setTrafficEnabled(false);
			 break;	
		 case R.id.menu7:
			 aMap.setMapType(AMap.MAP_TYPE_NORMAL);
			 aMap.setTrafficEnabled(false);
			 break;
		 case R.id.menu8:
			 aMap.setTrafficEnabled(true);
			 break;
		 case R.id.menu9:
			 //mymarker.remove();
			 aMap.clear();//清除所有覆盖物
			 break;
		 case R.id.menu10:
			 //位置跳转，可以从当前视图跳到定位的位置
			 //动画放大效果的代码
			 /*CameraUpdate cu=CameraUpdateFactory.zoomIn();
			 aMap.animateCamera(cu);
			 //aMap.moveCamera(cu);//无动画效果*/
			 //………………………………………………………………………………
			 CameraUpdate newLoc= CameraUpdateFactory.newLatLngZoom(new LatLng(31.209333, 121.62659), 12);//可以改为GPS获取的位置
			 aMap.animateCamera(newLoc);
			 break;
		 case R.id.menu11:
			//POI搜索
			 doSearchQuery("KTV");
			 break;
		 case R.id.menu12:
			 //测试查询个人位置到东方明珠(31.240655,121.499727)的路线，给定两个经纬度点：LatLngPoint
			 routeSearch(new LatLonPoint(lat,lng),new LatLonPoint(31.240655,121.499727));
		}
		return super.onMenuItemSelected(featureId, item);
	}
	private List<LatLng> createRectangle(LatLng center, double halfWidth,
			double halfHeight) {
		return Arrays.asList(new LatLng(center.latitude - halfHeight,
				center.longitude - halfWidth), new LatLng(center.latitude
				- halfHeight, center.longitude + halfWidth), new LatLng(
				center.latitude + halfHeight, center.longitude + halfWidth),
				new LatLng(center.latitude + halfHeight, center.longitude
						- halfWidth));
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub
		/*String curDes = "Draged current position:(lat,lng)\n("
			+ arg0.getPosition().latitude + ","
			+ arg0.getPosition().longitude + ")";
	    Toast.makeText(this,arg0.getTitle() + curDes , Toast.LENGTH_SHORT).show();*/
	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub
		String curDes = "Draged current position:(lat,lng)\n("
			+ arg0.getPosition().latitude + ","
			+ arg0.getPosition().longitude + ")";
	    Toast.makeText(this,arg0.getTitle() + curDes , Toast.LENGTH_SHORT).show();
		Toast.makeText(this,arg0.getTitle() +"拖动完毕" , Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub
		String curDes = "Draged current position:(lat,lng)\n("
			+ arg0.getPosition().latitude + ","
			+ arg0.getPosition().longitude + ")";
	    Toast.makeText(this,arg0.getTitle() + curDes , Toast.LENGTH_SHORT).show();
		Toast.makeText(this,arg0.getTitle() + "开始拖动" , Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		//必须使用equals
		if(marker.equals(mymarker)){
			Toast.makeText(this,"在这里执行点击事件" , Toast.LENGTH_SHORT).show();
		}
		return false;
	}
    //定位所需实现的方法
	//……………废弃的方法…………………………
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
    //---------------------------------------------
	@Override
	public void onLocationChanged(AMapLocation location) {
		// TODO Auto-generated method stub
		if (mListener != null) {
			mListener.onLocationChanged(location);
		}
		lat=location.getLatitude();
		lng=location.getLongitude();
		//获取位置后再加一个marker-myLoc,先清除上次的标记（这样可能会引起路线图标变成马赛克）
		/*if(myLocMarker!=null){
			myLocMarker.remove();
		}
		myLocation = new LatLng(lat,lng);  
		myLocMarker=aMap.addMarker(new MarkerOptions().position(myLocation).title("我的位置")
				 .snippet("上海最高的电视塔")  
		         .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
		         .draggable(false)); */
		         
	}
    //………………………………………………………………………………………………………………………………………………………………………………………………………………………………………………………………………………
	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
		}
		/*
		 * mAMapLocManager.setGpsEnable(false);//
		 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true
		 */
		// Location API定位采用GPS和网络混合定位方式，时间最短是5000毫秒
		mAMapLocationManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 5000, 10, this);

	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}		
	//-----------------------------------------------------------------------
	/**
	 * 一次性打印多个Marker出来
	 */
	private void addMarkers(List<PoiItem> poiItems) {
		for (int i = 0; i < poiItems.size(); i++) {
			aMap.addMarker(new MarkerOptions()
					.position(
							new LatLng(
									poiItems.get(i).getPoint().getLatitude(),
									poiItems.get(i).getPoint().getLongitude()))
					.title(poiItems.get(i).getTitle())
					.snippet(poiItems.get(i).getSnippet())
					.icon(BitmapDescriptorFactory.defaultMarker()));
		}

	}

	private LatLngBounds getLatLngBounds(List<PoiItem> poiItems) {
		LatLngBounds.Builder b = LatLngBounds.builder();
		for (int i = 0; i < poiItems.size(); i++) {
			b.include(new LatLng(poiItems.get(i).getPoint().getLatitude(),
					poiItems.get(i).getPoint().getLongitude()));
		}
		return b.build();
	}

	private void showPoiItem(List<PoiItem> poiItems) {
		if (poiItems != null && poiItems.size() > 0) {
			if (aMap == null)
				return;
			aMap.clear();
			LatLngBounds bounds = getLatLngBounds(poiItems);
			aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
			addMarkers(poiItems);
		} else {
			ToastUtil.show(getApplicationContext(), "没有搜索到数据！");
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == Constants.POISEARCH) {
				dissmissProgressDialog();// 隐藏对话框

				if (result != null) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								final List<PoiItem> poiItems = result
										.getPage(1);
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										showPoiItem(poiItems);// 每页显示10个poiitem
									}
								});

							} catch (AMapException e) {
								e.printStackTrace();

							}
						}
					}).start();
				}

			} else if (msg.what == Constants.ERROR) {
				dissmissProgressDialog();// 隐藏对话框
				ToastUtil.show(getApplicationContext(), "搜索失败,请检查网络连接！");
			} else if(msg.what == Constants.ROUTE_SEARCH_RESULT){
				progDialog.dismiss();
				if (routeResult != null && routeResult.size() > 0) {
					route = routeResult.get(0);
					Log.v("路线条数：", String.valueOf(routeResult.size()));
					if (route != null) {
						routeOverlay = new RouteOverlay(MapMainActivity.this,
								aMap, route);
						routeOverlay.removeFormMap();
						routeOverlay.addMarkerLine();
						routeNav.setVisibility(View.VISIBLE);
						routePre.setBackgroundResource(R.drawable.prev_disable);
						routeNext.setBackgroundResource(R.drawable.next);
					}
				}
			}else if (msg.what == Constants.POISEARCH_NEXT) {//POI搜索结果之翻页功能
				curpage++;
				new Thread(new Runnable() {

					@Override
					public void run() {
						final List<PoiItem> poiItems;
						try {
							poiItems = result.getPage(curpage);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									showPoiItem(poiItems);// 每页显示10个poiitem
								}
							});
						} catch (AMapException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		}
	};
	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在搜索:\n");
		progDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}
	
	public void doSearchQuery(final String searchName) {
		
		curpage = 1;
		cnt = 0;
		showProgressDialog();// 显示进度框
		new Thread(new Runnable() {
			public void run() {
				try {
					PoiSearch poiSearch = new PoiSearch(
							MapMainActivity.this, new PoiSearch.Query(
									searchName, PoiTypeDef.All, "021")); // 设置搜索字符串，poi搜索类型，poi搜索区域（空字符串代表全国）
					poiSearch.setPageSize(10);// 设置搜索每次最多返回结果数
					//以自己的定位位置为中心设置搜索 区域
					if(lat!=0&&lng!=0){
						poiSearch.setBound(new SearchBound(new LatLonPoint(lat,lng), 5000));
					}				
					result = poiSearch.searchPOI();
					if (result != null) {
						cnt = result.getPageCount();
					}
					handler.sendMessage(Message.obtain(handler,
							Constants.POISEARCH));
				} catch (AMapException e) {
					handler.sendMessage(Message
							.obtain(handler, Constants.ERROR));
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void routeSearch(LatLonPoint startPoint, LatLonPoint endPoint){
		progDialog = ProgressDialog.show(MapMainActivity.this, null,
				"正在获取线路", true, true);
		final Route.FromAndTo fromAndTo = new Route.FromAndTo(startPoint,
				endPoint);
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					routeResult = Route.calculateRoute(MapMainActivity.this,
                            fromAndTo, mode);
					if (progDialog.isShowing()) {
						if (routeResult != null || routeResult.size() > 0)
							handler.sendMessage(Message
									.obtain(handler,Constants.ROUTE_SEARCH_RESULT));
					}
				} catch (AMapException e) {
					Message msg = new Message();
					msg.what = Constants.ROUTE_SEARCH_ERROR;
					msg.obj = e.getErrorMessage();
					handler.sendMessage(msg);
				}
			}
		});
		t.start();

	}

}
