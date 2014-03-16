package com.example.proba;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static final String NAMESPACE = "http://tempuri.org/";
	//http://localhost:8080/HelloService.svc?wsdl
//	public static final String URL = "http://10.0.2.2:80/MyFirstPublishedWebService/WebServiceImpl.svc?wsdl";
//	public static final String URL = "http://10.0.2.2:8080/HelloService.svc?wsdl";  /
	public static final String URL = "http://192.168.20.245:8080/HelloService.svc?wsdl";  
	public static String SOAP_ACTION; 
	public static String METHOD_NAME;
	 String obj;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
	
	
	
	public void Calculate()
	{
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); 
		request.addProperty("a", 5);
        request.addProperty("b", 9);
        
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
