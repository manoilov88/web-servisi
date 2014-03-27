package com.example.proba;

import java.util.ArrayList;
import java.util.List;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ARGoogleMapsActivity extends FragmentActivity implements OnMarkerClickListener, OnClickListener, OnMyLocationChangeListener{

	public static final String NAMESPACE = "http://tempuri.org/";
	//	public static final String URL = "http://192.168.20.103:8080/HelloService.svc?wsdl";  
	public static final String URL = "http://192.168.0.100:8080/HelloService.svc?wsdl"; 
	public static String SOAP_ACTION; 
	public static String METHOD_NAME;
	public String all_pins_info = null;
	public String all_distances = null;
	public List<Marker> markers;
  private GoogleMap map;
  String path;
  String  address;
  String  date;
  
  //XML node keys
  static final String KEY_ITEM = "object"; // parent node
  static final String KEY_NAME= "name";
  static final String KEY_LATITUDE = "latitude";
  static final String KEY_LONGITUDE = "longitude";
  
 //Distance XML node keys
  static final String KEY_ITEM_DISTANCE = "distance"; // parent node
  static final String KEY_NAME_DISTANCE = "name";
  static final String KEY_METERS = "meters";
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.map);
	
	SOAP_ACTION = "http://tempuri.org/IHelloService/AllParkObjects";
	METHOD_NAME = "AllParkObjects";
	AsyncCall task = new AsyncCall();
	task.execute();
	
	
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
 
    
    //AddLocation(lat, lon);
    GPSTracker gpst = new GPSTracker(getApplicationContext());
	Location l = gpst.getLocation();
    LatLng user_position = new LatLng(l.getLatitude(), l.getLongitude());
   
 //   LatLng user_position = new LatLng( 43.323752, 21.895280);

    // Move the camera instantly to hamburg with a zoom of 15.
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(user_position, 3));

    // Zoom in, animating the camera.
    map.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
    map.setMyLocationEnabled(true);
    map.setOnMyLocationChangeListener(this);
    View btn=findViewById(R.id.layout_camera);
    btn.setOnClickListener(this);
    
  }
  

  @Override
  public void onMyLocationChange(Location lastKnownLocation) {
      CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(
              new CameraPosition.Builder().target(new LatLng(lastKnownLocation.getLatitude(),
                      lastKnownLocation.getLongitude())).zoom(18).build());
      map.moveCamera(myLoc);
      map.setOnMyLocationChangeListener(null);
      Toast.makeText(getApplicationContext(), "promena", Toast.LENGTH_SHORT).show();
      setDistanceToMarker(lastKnownLocation);
  }
 
  public static boolean isTablet(Context context) {
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
  
 
	private void AddLocations() {
		// TODO Auto-generated method stub
		
		XMLParser parser = new XMLParser();
		String xml = all_pins_info.toString();
		
		Document doc =  parser.getDomElement(xml);
		NodeList nl = doc.getElementsByTagName(KEY_ITEM);
		
		List<ArchObject> objects = new ArrayList<ArchObject>();
		Marker marker;
		markers = new ArrayList<Marker>();
		for(int i=0; i<nl.getLength(); i++)
		{
			 Element e = (Element) nl.item(i);
			 String name = parser.getValue(e, KEY_NAME); // name child value
		     double lat = Double.valueOf(parser.getValue(e, KEY_LATITUDE).replace(",", ".")); // cost child value
		     double lon = Double.valueOf(parser.getValue(e, KEY_LONGITUDE).replace(",", ".")); // description child value
			 ArchObject arc_object = new ArchObject(name, lat, lon);
			 objects.add(arc_object);
			 LatLng position =  new LatLng(lat, lon);
			 marker = map.addMarker(new MarkerOptions()
		        .position(position)
		        .snippet("50m")
		        .title(name));
			 markers.add(marker);
		}
		
	
	}
	
	private Marker getMarkerWithTitle(String title)
	{
		Marker marker = null;
		
		for(int i=0; i<markers.size(); i++)
		{
			if(markers.get(i).getTitle().equals(title))
			{
				marker = markers.get(i);
				break;
			}
		}
		
		return marker;
	}
	
	private void setDistanceToMarker(Location lastKnownLocation)
	{
		SOAP_ACTION = "http://tempuri.org/IHelloService/distances";
		METHOD_NAME = "distances";
		AsyncCallDistance task = new AsyncCallDistance();
		task.execute(lastKnownLocation);
		
	}
	
	private void calculateDistanceToMarker(Location lastKnownLocation)
	{
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); 
		request.addProperty("lat1", String.valueOf(lastKnownLocation.getLatitude()));
		request.addProperty("lon1", String.valueOf(lastKnownLocation.getLongitude()));
		request.addProperty("park_name", "Tvrðava");
		 SoapSerializationEnvelope envelope = 
	                new SoapSerializationEnvelope(SoapEnvelope.VER11); 

	        envelope .dotNet = true;

	        envelope.setOutputSoapObject(request);
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        try {
	            androidHttpTransport.call(SOAP_ACTION, envelope); 
	           all_distances = envelope.getResponse().toString();
	          
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	private void setCalculatedDistance()
	{
		XMLParser parser = new XMLParser();
		String xml = all_distances.toString();
		Toast.makeText(getApplicationContext(), xml, Toast.LENGTH_LONG).show();
		
		Document doc =  parser.getDomElement(xml);
		NodeList nl = doc.getElementsByTagName(KEY_ITEM_DISTANCE);
		Marker marker;
		for(int i=0; i<nl.getLength(); i++)
		{
			 Element e = (Element) nl.item(i);
			 String name = parser.getValue(e, KEY_NAME_DISTANCE); // name child value
		     String meters =parser.getValue(e, KEY_METERS).split(",")[0] + " m"; // cost child value
			 
		     marker = getMarkerWithTitle(name);
		     marker.setSnippet(String.valueOf(meters));
			 
		}
		
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
	
	private class AsyncCallDistance extends AsyncTask<Location, Void, Void> {
		
		
		
        @Override
        protected Void doInBackground(Location... params) {
        	calculateDistanceToMarker(params[0]);
        	return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        	setCalculatedDistance();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }
	
	private class AsyncCall extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
        	getAllPins();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        	AddLocations();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }
	
	public void getAllPins()
	{
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); 
		request.addProperty("park_name", "Tvrðava");
		 SoapSerializationEnvelope envelope = 
	                new SoapSerializationEnvelope(SoapEnvelope.VER11); 

	        envelope .dotNet = true;

	        envelope.setOutputSoapObject(request);
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        try {
	            androidHttpTransport.call(SOAP_ACTION, envelope); 
	           all_pins_info = envelope.getResponse().toString();
	          
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	

} 