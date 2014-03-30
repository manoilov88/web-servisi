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
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.LocationListener;

public class ARGoogleMapsActivity extends FragmentActivity implements OnMarkerClickListener, OnClickListener, OnMyLocationChangeListener, LocationListener{

  public static final String NAMESPACE = "http://tempuri.org/";
  //	public static final String URL = "http://192.168.20.103:8080/HelloService.svc?wsdl";  
  public static final String URL = "http://192.168.0.100:8080/HelloService.svc?wsdl"; 
  public static String SOAP_ACTION; 
  public static String METHOD_NAME;
  public String all_pins_info = null;
  public String all_distances = null;
  public List<Marker> markers;
  public boolean isWifiConn, isMobileConn;
  private GoogleMap map;
  String path;
  String  address;
  String  date;
  
  // flag for GPS status
  boolean isGPSEnabled = false;

  // flag for network status
  boolean isNetworkEnabled = false;

  boolean canGetLocation = false;

  Location location; // location
  double latitude; // latitude
  double longitude; // longitude

  // The minimum distance to change Updates in meters
  private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

  // The minimum time between updates in milliseconds
  private static final long MIN_TIME_BW_UPDATES = 1000;//1000 * 60 * 1; // 1 minute

  // Declaring a Location Manager
  protected LocationManager locationManager;
  
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
	
	locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
	
	ConnectivityManager connMgr = (ConnectivityManager) 
            getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
    isWifiConn = networkInfo.isConnected();
    networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    isMobileConn = networkInfo.isConnected();
	
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
  //  GPSTracker gpst = new GPSTracker(getApplicationContext());
	Location l = getLocation();
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
		if(all_pins_info!=null)
		{
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
		if(isWifiConn || isMobileConn)
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
	}
	
	private void setCalculatedDistance()
	{
		XMLParser parser = new XMLParser();
		if(all_distances!=null)
		{
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
		if(isWifiConn || isMobileConn)
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


	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "promena2", Toast.LENGTH_SHORT).show();
		setDistanceToMarker(location);
		 CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(
	              new CameraPosition.Builder().target(new LatLng(location.getLatitude(),
	            		  location.getLongitude())).zoom(18).build());
		 map.moveCamera(myLoc);
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
	
	public Location getLocation() 
	    {
	        try 
	        {
	            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
	 
	            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	//	            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 10, this);
	            boolean isOnline = isOnline();
	            
	            // TODO: Daniel - proveri da li je GPS ukljuèen i da li ima signal
	            
	            if (!isGPSEnabled && !isNetworkEnabled && isOnline) {
	            	// povezan na internet ali nema pravo pristupa lokacijama
	            	// GPS iskljuèen
	            	
	            	throw new GetLocationException("Allow getting location through network!");
	            }
	            else if (!isGPSEnabled && !isNetworkEnabled) 
	            {
	               Toast.makeText(this.getApplicationContext(), "Please turn on GPS or Data", Toast.LENGTH_SHORT).show();
	            }
	            else
	            {
	                this.canGetLocation = true;
	
	                // if GPS Enabled get lat/long using GPS Services first
	                if (isGPSEnabled) {
	                		locationManager.requestLocationUpdates(
	                				LocationManager.GPS_PROVIDER,
	                				MIN_TIME_BW_UPDATES,
	                				MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	                		
	                		if (locationManager != null) {
	                			location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	                			if (location != null) 
	                			{
	                				latitude = location.getLatitude();
	                				longitude = location.getLongitude();
	                			}
	                		}
	                }
	                
	                // Get location from Network Provider if GPS is not available
	                if (isNetworkEnabled)
	                {
	                	if (location == null) 
	                	{
		                    locationManager.requestLocationUpdates(
		                            LocationManager.NETWORK_PROVIDER,
		                            MIN_TIME_BW_UPDATES,
		                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		                    
		                    if (locationManager != null) 
		                    {
		                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		                        if (location != null) 
		                        {
		                            latitude = location.getLatitude();
		                            longitude = location.getLongitude();
		                        }
		                    }
	                	}
	                }
	            }
	            return location;
	        }
	        catch (GetLocationException e) {
	        	Toast.makeText(this, "Allow getting location through network!", Toast.LENGTH_SHORT).show();
	        	e.printStackTrace();
	        }
	        catch (Exception e)
	        {
	        	Toast.makeText(this, "Error in getting location." + e.getMessage(), Toast.LENGTH_SHORT).show();
	            e.printStackTrace();
	        }
	        
	        return null;
	    }
	 private boolean isOnline() {
	        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo netInfo = cm.getActiveNetworkInfo();
	        return netInfo != null && netInfo.isConnectedOrConnecting();
	    }
	    
		
		public boolean isNetworkAvailable(Context context) {
			try {
				ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo netInfo = cm.getActiveNetworkInfo();
				if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				    return true;
				}
				return false;
			} catch (Exception e) {
				return false;
			}
		}

} 