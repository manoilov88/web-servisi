package com.example.proba;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class ARGoogleMapsActivity extends FragmentActivity implements OnMarkerClickListener, OnClickListener{
  private GoogleMap map;
  String path;
  String  address;
  String  date;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.map);
	
	LinearLayout ll = (LinearLayout) findViewById(R.id.layout_map);
    ll.setBackgroundResource(R.drawable.narandzasto);
    
	Intent intent = getIntent();
    date =  intent.getStringExtra("date");
    address =  intent.getStringExtra("address");
    double lat = intent.getDoubleExtra("lat",0);
    double lon = intent.getDoubleExtra("lon",0);
    path = intent.getStringExtra("path");
    
    map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
        .getMap();
    
    map.setOnMarkerClickListener(this);
 
    
    AddLocation(lat, lon);
    GPSTracker gpst = new GPSTracker(getApplicationContext());
	Location l = gpst.getLocation();
    LatLng user_position = new LatLng(l.getLatitude(), l.getLongitude());
   
 //   LatLng user_position = new LatLng( 43.323752, 21.895280);

    // Move the camera instantly to hamburg with a zoom of 15.
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(user_position, 3));

    // Zoom in, animating the camera.
    map.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
    map.setMyLocationEnabled(true);
    View btn=findViewById(R.id.layout_camera);
    btn.setOnClickListener(this);
    
  }
  

 
  public static boolean isTablet(Context context) {
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
  
 
	private void AddLocation(double lat, double lon) {
		// TODO Auto-generated method stub
		
//     	LatLng position =  new LatLng(43.324083, 21.901384);
//		Marker marker = map.addMarker(new MarkerOptions()
//        .position(position)
//        .title("Marker"));
//		BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
//		LatLng position =  new LatLng(lat, lon);
//		
//		MarkerOptions markerOptions = new MarkerOptions().position(position)
//                .title(address == null ? "Unknown address" : address.toString())
//                .icon(icon);
//		
//		map.addMarker(markerOptions);
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i;
		switch (v.getId()) {
				
		case R.id.layout_camera:
			i = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(i);
			finish();
			break;
		

		default:
			break;			
		}
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void SendMail(View v)
	{
		 Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
         String[] recipients = new String[]{"e-mail address"};
//         emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
         emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Dent-Cam");
         
         String output = "Address: "+ address+"\nDate: "+date+ "\n";

         emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,output.toString());
         
         emailIntent.setType("plain/text"); // This is incorrect MIME, but Gmail is one of the only apps that responds to it - this might need to be replaced with text/plain for Facebook
        
         emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + path));
         emailIntent.setType("image/jpg");
         
         final PackageManager pm = getPackageManager();
         final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
         ResolveInfo best = null;
         for (final ResolveInfo info : matches)
             if (info.activityInfo.packageName.endsWith(".gm") ||
                     info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
                 if (best != null)
                     emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                 startActivity(emailIntent);
		
	
	}
 

} 