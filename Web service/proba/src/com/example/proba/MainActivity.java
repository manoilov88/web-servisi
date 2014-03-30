package com.example.proba;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	
	public static final String NAMESPACE = "http://tempuri.org/";
//	public static final String URL = "http://192.168.20.103:8080/HelloService.svc?wsdl";  
	public static final String URL = "http://192.168.0.100:8080/HelloService.svc?wsdl"; 
	public static String SOAP_ACTION; 
	public static String METHOD_NAME;
	 String obj;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.layout_camera);
        ll.setBackgroundResource(R.drawable.narandzasto);
		
		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
		    	SOAP_ACTION = "http://tempuri.org/IHelloService/Zbir";
		    	METHOD_NAME = "Zbir";
		    	AsyncCallWS task = new AsyncCallWS();
			    task.execute("zbir"); 
//		    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//		    	startActivity(browserIntent);
		    	
		        } 
		    });
		
		Button b2 = (Button) findViewById(R.id.button2);
		b2.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
		    	SOAP_ACTION = "http://tempuri.org/IHelloService/Razlika";
		    	METHOD_NAME = "Razlika";
		    	AsyncCallWS task = new AsyncCallWS();
			    task.execute("razlika"); 
		    	
//		    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//		    	startActivity(browserIntent);
		    	
		        } 
		    });
		
		Button b3 = (Button) findViewById(R.id.button3);
		b3.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
		    	SOAP_ACTION = "http://tempuri.org/IHelloService/Proizvod";
		    	METHOD_NAME = "Proizvod";
		    	AsyncCallWS task = new AsyncCallWS();
			    task.execute("proizvod"); 
		    	
//		    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//		    	startActivity(browserIntent);
		    	
		        } 
		    });
		
		Button b4 = (Button) findViewById(R.id.button4);
		b4.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
		    	
		    	GPSTracker gpst = new GPSTracker(getApplicationContext());
		    	//Location l = gpst.getLocation();
//		    	String add = gpst.getAddress(l);
		    //	Toast.makeText(getApplicationContext(), String.valueOf(l.getLatitude()), Toast.LENGTH_SHORT).show();
//		    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//		    	startActivity(browserIntent);
		    	
		        } 
		    });
		
		Button b5 = (Button) findViewById(R.id.button5);
		b5.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
		    	
		    	SOAP_ACTION = "http://tempuri.org/IHelloService/Kolicnik";
		    	METHOD_NAME = "Kolicnik";
		    	AsyncCallWS task = new AsyncCallWS();
			    task.execute("Kolicnik"); 
//		    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//		    	startActivity(browserIntent);
		    	
		        } 
		    });
		
		Button b6 = (Button) findViewById(R.id.button6);
		b6.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
		    	
		    	SOAP_ACTION = "http://tempuri.org/IHelloService/AllArchParks";
		    	METHOD_NAME = "AllArchParks";
		    	AsyncObject task = new AsyncObject();
			    task.execute("AllArchParks"); 
//		    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//		    	startActivity(browserIntent);
		    	
		        } 
		    });
		
		 View btn=findViewById(R.id.layout_map);
	     btn.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
				
		case R.id.layout_map:
			i = new Intent(getApplicationContext(), ARGoogleMapsActivity.class);
			startActivity(i);
			break;
		

		default:
			break;			
		}
		
	}
	
	private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
        	Calculate();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        	Toast.makeText(getApplicationContext(), obj, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }
	
	private class AsyncObject extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
        	GetObject();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        	Toast.makeText(getApplicationContext(), obj, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }
	
	
	public void GetObject()
	{
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); 
        
		 SoapSerializationEnvelope envelope = 
	                new SoapSerializationEnvelope(SoapEnvelope.VER11); 

	        envelope .dotNet = true;

	        envelope.setOutputSoapObject(request);
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        try {
	            androidHttpTransport.call(SOAP_ACTION, envelope); 
	           obj = envelope.getResponse().toString();
	          
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
		
	}
	
	
	public void Calculate()
	{
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); 
		request.addProperty("a", 10);
        request.addProperty("b", 5);
        
		 SoapSerializationEnvelope envelope = 
	                new SoapSerializationEnvelope(SoapEnvelope.VER11); 

	        envelope .dotNet = true;

	        envelope.setOutputSoapObject(request);
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        try {
	            androidHttpTransport.call(SOAP_ACTION, envelope); 
	           obj = envelope.getResponse().toString();
	          
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
//	public void onButtonClick(View v)
//	{
//		Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
//	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
