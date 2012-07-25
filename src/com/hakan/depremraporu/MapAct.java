package com.hakan.depremraporu;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import java.util.List;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
  
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

public class MapAct extends MapActivity {
	
	private MapView mapView;
	
	static final private int MENU_Search = Menu.FIRST;
	static final private int MENU_Directions = Menu.FIRST + 1;
	static final private int MENU_Point = Menu.FIRST + 2;
	static final private int MENU_Location = Menu.FIRST + 3;
	static final private int MENU_Layers = Menu.FIRST + 4;
	static final private int MENU_Track = Menu.FIRST + 5;
	static final private int MENU_Exit = Menu.FIRST + 6;
	
	 int latitudeE6 =  Integer.parseInt(ApplicationData.xCord.replace(".", "")+ "00");
	 int longitudeE6 = Integer.parseInt(ApplicationData.yCord.replace(".", "")+ "00");
	

		List<Overlay> mapOverlays;
		Drawable drawable;
		Drawable drawable2;
		MyItemizedOverlay itemizedOverlay;
		MyItemizedOverlay itemizedOverlay2;
		
		@Override
	    public void onCreate(Bundle savedInstanceState) {
			
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.deprem_map);
	        
	        mapView = (MapView) findViewById(R.id.myGMap);  
			mapView.setBuiltInZoomControls(true);
			
			mapOverlays = mapView.getOverlays();
			
			// first overlay
			drawable = getResources().getDrawable(R.drawable.marker);
			itemizedOverlay = new MyItemizedOverlay(drawable, mapView);
			
			GeoPoint point = new GeoPoint(latitudeE6,longitudeE6);
			OverlayItem overlayItem = new OverlayItem(point, ApplicationData.gWhere, 
					ApplicationData.gDate);
			itemizedOverlay.addOverlay(overlayItem);

			mapOverlays.add(itemizedOverlay);

			final MapController mc = mapView.getController();
			mc.animateTo(point);
			mc.setZoom(11);
			
	    }
		
		@Override
		protected boolean isRouteDisplayed() {
			return false;
		}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_Layers, 0, "Harita Seçimi").setIcon(
				android.R.drawable.ic_menu_view);
		menu.add(0, MENU_Exit, 0, "Geri").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case MENU_Layers:
			selectViewMode();
			break;
		case MENU_Exit:
			MapAct.this.finish();
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	private void selectViewMode() {
		// TODO Auto-generated method stub
		OnClickListener listener = new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				switch (which) {
				case 0:
					mapView.setTraffic(false);
					mapView.setSatellite(false);
					mapView.setStreetView(true);
					break;
				case 1:
					mapView.setSatellite(false);
					mapView.setStreetView(false);
					mapView.setTraffic(true);
					break;
				case 2:
					mapView.setStreetView(false);
					mapView.setTraffic(false);
					mapView.setSatellite(true);
					break;
				}

			}
		};

		String[] menu = { "Trafik", "Uydu", "Sokak Görünümü" };
		new AlertDialog.Builder(MapAct.this).setTitle("Harita Görünümünü Seçiniz").setItems(menu,
				listener).show();
	}
}