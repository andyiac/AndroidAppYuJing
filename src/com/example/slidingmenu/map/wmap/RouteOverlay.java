package com.example.slidingmenu.map.wmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.search.core.LatLonPoint;
import com.amap.api.search.route.BusSegment;
import com.amap.api.search.route.DriveSegment;
import com.amap.api.search.route.WalkSegment;
import com.amap.api.search.route.Route;
import com.amap.api.search.route.Segment;
import com.example.slidingmenu.R;
import com.example.slidingmenu.map.util.AMapUtil;
import com.example.slidingmenu.map.util.ChString;


public class RouteOverlay implements OnMarkerClickListener, InfoWindowAdapter {

	private AMap mMap;
	private Route mRoute;
	private LatLng startPoint;
	private LatLng endPoint;
	private Context mContext;
	private Map<Integer, Marker> markerMap;
	private int currentIndex = 0;
	private static int zoomLevel = 13;

	public RouteOverlay(Context context, AMap map, Route route) {
		mContext = context;
		mMap = map;
		mRoute = route;
		mMap.setOnMarkerClickListener(this);
		mMap.setInfoWindowAdapter(this);
		startPoint = SearchPointConvert(route.getStartPos());
		endPoint = SearchPointConvert(route.getTargetPos());
		markerMap = new HashMap<Integer, Marker>();
	}

	/**
	 * ���������ص㼰�����ڵ�ͼ�����·
	 */
	public void addMarkerLine() {
		Marker startMarker = mMap.addMarker((new MarkerOptions())
				.position(startPoint)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.start))
				.title("0"));
		startMarker.showInfoWindow();
		mMap.moveCamera(CameraUpdateFactory
				.newLatLngZoom(startPoint, zoomLevel));
		markerMap.put(0, startMarker);
		for (int i = 0; i < mRoute.getStepCount(); i++) {
			if (i != 0) {
				Segment segment = mRoute.getStep(i);
				BitmapDescriptor icon = null;

				if (segment instanceof BusSegment) {
					icon = BitmapDescriptorFactory.fromResource(R.drawable.bus);
				} else if (segment instanceof WalkSegment) {
					icon = BitmapDescriptorFactory.fromResource(R.drawable.man);
				} else if (segment instanceof DriveSegment) {
					icon = BitmapDescriptorFactory.fromResource(R.drawable.car);
				}
				Marker tempMarker = mMap.addMarker((new MarkerOptions())
						.position(
								SearchPointConvert(mRoute.getStep(i)
										.getFirstPoint())).icon(icon)
						.anchor(0.5f, 0.5f).title("" + i));
				markerMap.put(i, tempMarker);

			}
			mMap.addPolyline((new PolylineOptions())
					.addAll(convertArrList(mRoute.getStep(i).getShapes()))
					.color(Color.argb(180, 54, 114, 227)).width(10.9F));
		}
		Marker targerMarker = mMap.addMarker((new MarkerOptions())
				.position(endPoint)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.end))
				.title("" + (mRoute.getStepCount())));
		markerMap.put(mRoute.getStepCount(), targerMarker);

	}

	/**
	 * �������
	 */
	public void removeFormMap() {
		currentIndex = 0;
		mMap.clear();
	}

	public boolean showPrePopInfo() {
		if (currentIndex > 0) {
			currentIndex--;
			Marker merker = markerMap.get(currentIndex);
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    merker.getPosition(), zoomLevel));
			merker.showInfoWindow();
		}
		if (currentIndex == 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean showNextPopInfo() {
		if (currentIndex < mRoute.getStepCount()) {
			currentIndex++;
			Marker merker = markerMap.get(currentIndex);
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    merker.getPosition(), zoomLevel));
			merker.showInfoWindow();
		}
		if(currentIndex == mRoute.getStepCount()){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		marker.showInfoWindow();
		currentIndex = Integer.parseInt(marker.getTitle());
		return false;
	}

	@Override
	public View getInfoContents(Marker arg0) {
		int index = Integer.parseInt(arg0.getTitle());
		return getInfoView(mContext, index);
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

	/**
	 * ����Segment index ����popInfo view
	 * 
	 * @param index
	 * @return
	 */
	public View getInfoView(Context cnt, int index) {
		if (index < 0 || index > mRoute.getStepCount()) {//Route.getStepCount()����·���е�·����Ŀ��
			return null;
		}

		LinearLayout ll_parents = new LinearLayout(cnt);
		ll_parents.setOrientation(LinearLayout.VERTICAL);
		ll_parents.setBackgroundResource(R.drawable.custom_info_bubble);

		LinearLayout ll_child1 = new LinearLayout(cnt);
		ll_child1.setOrientation(LinearLayout.HORIZONTAL);
		ll_child1.setGravity(Gravity.AXIS_PULL_BEFORE);
		TextView titleVw = new TextView(cnt);
		String spannedInfos[] = getSpannedInfo(index).toString()
				.split("\\n", 2);
		titleVw.setTextColor(Color.BLACK);
		titleVw.setText(AMapUtil.stringToSpan(spannedInfos[0]));
		titleVw.setPadding(3, 0, 0, 3);
		ll_child1.addView(titleVw, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		TextView backgroud = new TextView(cnt);
		backgroud.setBackgroundColor(Color.rgb(165, 166, 165));
		backgroud.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 1));

		LinearLayout ll_child3 = new LinearLayout(cnt);
		ll_child3.setOrientation(LinearLayout.VERTICAL);

		TextView titleVwdown = new TextView(cnt);
		if (spannedInfos.length == 2) {
			ll_child3.addView(backgroud, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 1));

			titleVwdown.setText(AMapUtil.stringToSpan(spannedInfos[1]));
			titleVwdown.setTextColor(Color.rgb(82, 85, 82));
			ll_child3.addView(titleVwdown, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
		}

		LinearLayout ll_child2 = new LinearLayout(cnt);
		ll_child2.setOrientation(LinearLayout.HORIZONTAL);
		ll_child2.setGravity(Gravity.CENTER_HORIZONTAL);

		ll_parents.addView(ll_child1, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		ll_parents.addView(ll_child3, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 1));

		ll_parents.addView(ll_child2, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		return ll_parents;
	}

	/**
	 * ��ȡpopInfo����
	 * 
	 * @param index
	 * @return
	 */
	public Spanned getSpannedInfo(int index) {
		if (index == mRoute.getStepCount()) {
			StringBuffer buf = new StringBuffer();
			buf.append(ChString.Arrive);
			buf.append(mRoute.getTargetPlace());
			return AMapUtil.stringToSpan(buf.toString());
		}
		if (mRoute.getStep(index) instanceof BusSegment) {
			return getBusSpan(index);
		} else if (mRoute.getStep(index) instanceof DriveSegment) {
			return getCarInfo(index);
		}

		return getFootSpan(index);
	}

	/**
	 * ��ȡ����popInfo����
	 * 
	 * @param index
	 * @return
	 */
	private Spanned getBusSpan(int index) {
		BusSegment seg = (BusSegment) mRoute.getStep(index);
		StringBuffer buf = new StringBuffer();

		buf.append(AMapUtil.colorFont(seg.getLineName(), AMapUtil.HtmlBlack));
		buf.append(AMapUtil.makeHtmlSpace(3));
		buf.append(AMapUtil.colorFont(seg.getLastStationName(),
				AMapUtil.HtmlBlack));
		buf.append(ChString.Direction);
		buf.append(AMapUtil.makeHtmlNewLine());

		buf.append(ChString.GetOn + " : ");
		buf.append(AMapUtil.colorFont(seg.getOnStationName(),
				AMapUtil.HtmlBlack));
		buf.append(AMapUtil.makeHtmlSpace(3));
		buf.append(AMapUtil.makeHtmlNewLine());

		buf.append(ChString.GetOff + " : ");
		buf.append(AMapUtil.colorFont(seg.getOffStationName(),
				AMapUtil.HtmlBlack));
		buf.append(AMapUtil.makeHtmlNewLine());
		buf.append(String.format("%s%d%s , ", ChString.Gong,
				seg.getStopNumber() - 1, ChString.Zhan));

		buf.append(ChString.About + AMapUtil.getFriendlyLength(seg.getLength()));

		return AMapUtil.stringToSpan(buf.toString());
	}

	/**
	 * ��ȡ�ݳ�popInfo����
	 * 
	 * @param index
	 * @return
	 */
	public Spanned getCarInfo(int index) {
		String content = "";
		DriveSegment seg = (DriveSegment) mRoute.getStep(index);
		if (!AMapUtil.IsEmptyOrNullString(seg.getRoadName())
				&& !AMapUtil.IsEmptyOrNullString(seg.getActionDescription())) {
			content = seg.getActionDescription() + " --> " + seg.getRoadName();
		} else {
			content = seg.getActionDescription() + seg.getRoadName();
		}

		content = AMapUtil.colorFont(content, AMapUtil.HtmlGray);
		content += AMapUtil.makeHtmlNewLine();
		content += String.format("%s%s", ChString.About,
				AMapUtil.getFriendlyLength(seg.getLength()));

		return AMapUtil.stringToSpan(content);
	}

	/**
	 * ��ȡ����popInfo����
	 * 
	 * @param index
	 * @return
	 */
	private Spanned getFootSpan(int index) {
		if (mRoute.getMode() == Route.WalkDefault) {
			String content = "";
			WalkSegment seg = (WalkSegment) mRoute.getStep(index);
			if (!AMapUtil.IsEmptyOrNullString(seg.getRoadName())
					&& !AMapUtil
							.IsEmptyOrNullString(seg.getActionDescription())) {
				content = seg.getActionDescription() + " --> "
						+ seg.getRoadName();
			} else {
				content = seg.getActionDescription() + seg.getRoadName();
			}

			content = AMapUtil.colorFont(content, AMapUtil.HtmlGray);
			content += AMapUtil.makeHtmlNewLine();
			content += String.format("%s%s", ChString.About,
					AMapUtil.getFriendlyLength(seg.getLength()));

			return AMapUtil.stringToSpan(content);
		} else {
			StringBuilder result = new StringBuilder();
			result.append(ChString.ByFoot).append(ChString.To);

			if (index == mRoute.getStepCount() - 1) {
				result.append(AMapUtil.colorFont(ChString.TargetPlace,
						AMapUtil.HtmlGray));
			} else {
				result.append(AMapUtil.colorFont(
						((BusSegment) mRoute.getStep(index + 1)).getLineName()
								+ ChString.Station, AMapUtil.HtmlGray));
			}

			result.append(AMapUtil.makeHtmlNewLine());
			result.append(ChString.About
					+ AMapUtil.getFriendlyLength(mRoute.getStep(index)
							.getLength()));

			return AMapUtil.stringToSpan(result.toString());
		}
	}

	/**
	 * ���߷����� ��һ��segment shaps ת����map��LatLng list ������ӵ���ͼ
	 * 
	 * @param shapes
	 * @return
	 */
	private ArrayList<LatLng> convertArrList(LatLonPoint[] shapes) {
		ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
		for (LatLonPoint point : shapes) {
			LatLng latLngTemp = SearchPointConvert(point);
			lineShapes.add(latLngTemp);
		}
		return lineShapes;
	}

	/**
	 * ���߷������������õ���LatLonPointת��latLng ������ӵ���ͼ��
	 * 
	 * @param latLonPoint
	 * @return
	 */
	private LatLng SearchPointConvert(LatLonPoint latLonPoint) {
		return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
	}
}
