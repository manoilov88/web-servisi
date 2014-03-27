package com.example.proba;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class GPSTracker  extends Service implements LocationListener
{
 
    private final Context mContext;
    
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
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute
 
    // Declaring a Location Manager
    protected LocationManager locationManager;
    
    public GPSTracker(Context context)
    {
        this.mContext = context;
//        getLocation();
    }
    
    public Location getLocation() 
    {
        try 
        {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
 
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 10, this);
            boolean isOnline = isOnline();
            
            // TODO: Daniel - proveri da li je GPS ukljuèen i da li ima signal
            
            if (!isGPSEnabled && !isNetworkEnabled && isOnline) {
            	// povezan na internet ali nema pravo pristupa lokacijama
            	// GPS iskljuèen
            	
            	throw new GetLocationException("Allow getting location through network!");
            }
            else if (!isGPSEnabled && !isNetworkEnabled) 
            {
               Toast.makeText(this.mContext, "Please turn on GPS or Data", Toast.LENGTH_SHORT).show();
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
   
    public String getAddress(double lat, double lng) {
    	String address = "";
    	Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
    	
    	boolean isNetworkAwailable = isNetworkAvailable(mContext);
    	
        if (!isNetworkAwailable) {
        	return null;
        }
    	
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if(addresses != null)
            {
            	Address locationAddress = addresses.get(0);
            	address = locationAddress.getAddressLine(0);
            
            	String locality = locationAddress.getLocality();
            	if (locality != null) {
            		address = address + ", " + locality;
            	}
            	
            	address = address + ", " + locationAddress.getCountryName();
            	
            	return address;

            }	// TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            e.printStackTrace();
            address = "Not Availiable";
            Toast.makeText(this, "Error in getting address.", Toast.LENGTH_SHORT).show();
            return null;
        }
        return null;
    }
    
    public String getAddress(Location location) {
    	return getAddress(location.getLatitude(), location.getLongitude());
    }
    
    @Override
    public void onLocationChanged(Location location)
    {
    	Toast.makeText(getApplicationContext(), String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
    }
    
 
    @Override
    public void onProviderDisabled(String provider)
    {
    }
 
    @Override
    public void onProviderEnabled(String provider)
    {
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }
 
    @Override
    public IBinder onBind(Intent arg0) 
    {
        return null;
    }
    
    
    /**
     * Function to get latitude
     * */
    public double getLatitude()
    {
        if(location != null)
        {
            latitude = location.getLatitude();
        }
 
        // return latitude
        return latitude;
    }
 
    /**
     * Function to get longitude
     * */
    public double getLongitude()
    {
        if(location != null)
        {
            longitude = location.getLongitude();
        }
 
        // return longitude
        return longitude;
    }
    
    
    /**
     * Function to check if best network provider
     * @return boolean
     * */
    public boolean canGetLocation() 
    {
        return this.canGetLocation;
    }
 
    /**
     * Function to show settings alert dialog
     * */
    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
 
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
 
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
 
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);
 
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog,int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
 
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which) 
            {
            	dialog.cancel();
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
    }
    
    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS()
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }
    
    
    
    public String getAddressFromGoogle(double latitude, double longitude) 
    {
        String address = "";
        try {
            String URL = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&sensor=false";
            Log.d("Activity", "Address:" + URL);
            String device_address = Get(URL,"address");
            String output = device_address;

            try {
                address = output;
            } catch (Exception e) {
            	Log.d("Activity", "error:" + e.getMessage());
            }
            Log.d("Activity", "Address:" + address);
        } catch (Exception e) {
        }
        return address;

    }
    
    public Integer getDistance(double olat, double olong, double dlat, double dlong) {
    	try {
    		String url = "http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + olat + "," + olong +
    					 "&destinations=" + dlat + "," + dlong + "&mode=driving&sensor=false";
    		String dist = Get(url, "distance");
            int output = Integer.parseInt(dist);
            return output;
    	}
    	catch(NumberFormatException e) {
    		return null;
    	}
    	catch(Exception e) {
    		Log.d("Distance error",e.getMessage());
    		return -1;
    	}
    }

    public static String Get(String URLStr, String action) throws Exception {
        String resultStr = "";
        BufferedReader in = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(URLStr);
            HttpResponse response = client.execute(request);
            in = new BufferedReader
            (new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            
            in.close();
            resultStr = sb.toString();
            
            JSONObject mainObj= new JSONObject(resultStr);
            Log.d("saas",mainObj.toString());
            
            if(action == "address") {
            	JSONArray jArray = mainObj.optJSONArray("results");
                JSONObject temp = jArray.optJSONObject(0);
                Log.d("saas",temp.toString());
            	resultStr = temp.getString("formatted_address");
            }
            else {
            	try {
            		JSONArray jArray = mainObj.optJSONArray("rows");
            	
            		JSONObject temp = jArray.optJSONObject(0);
            		Log.d("saas",temp.toString());
            		JSONArray  dist = temp.optJSONArray("elements");
            		JSONObject dd = dist.optJSONObject(0);
            		Log.d("saas",dd.toString());
            		JSONObject Str = dd.getJSONObject("distance");
            		Log.d("saas",Str.toString());
            		resultStr =  Str.getString("value");
            		
        			return resultStr;
            	}
            	catch (Exception j) {
            		resultStr = "0";
            	}
            }
        }
        catch(Exception ee) {
            ee.printStackTrace();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (Exception e) {
                }
            }
        }
        return resultStr;
    }
    
    
    
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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