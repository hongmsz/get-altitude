package hongmsz.test.sensortest.Activity;


//import android.app.Activity;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import hongmsz.test.sensortest.R;

public class MapLoc extends MapActivity {
	List<Overlay> mapOverlays;
	MapView mapView; 

    int lati, longi;
	
	 protected boolean isRouteDisplayed() {
	        return false;
	    }
	 
	/** Called when the activity is first created. */
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.locmap);
	        
	        Intent i0 = getIntent();
	        Bundle b = i0.getExtras();
	        lati = b.getInt("lati");
	        longi = b.getInt("longi");
	        
	        mapView = (MapView) findViewById(R.id.mapview);
	        mapView.setBuiltInZoomControls(true);
	        mapView.setSatellite(false);
	        
	        GeoPoint p=new GeoPoint(lati, longi);
	        MapController mc=mapView.getController();        
	        mc.animateTo(p);
	        mc.setZoom(18); 
	        
	        mapOverlays = mapView.getOverlays(); 
	        
	        OverlayItem overlayitem = new OverlayItem(p, "", "");
	    }        
}
