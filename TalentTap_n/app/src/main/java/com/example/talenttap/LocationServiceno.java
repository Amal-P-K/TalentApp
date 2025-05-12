package com.example.talenttap;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;

import android.speech.tts.TextToSpeech;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.core.app.NotificationCompat;
public class LocationServiceno extends Service implements TextToSpeech.OnInitListener{
	 private LocationManager locationManager;
	    private Boolean locationChanged;
	    private Handler handler = new Handler();
	    public static boolean isService = true;
	    private File root;
	    private ArrayList<String> fileList = new ArrayList<String>();
	    String ip="";
	    String[] zone;
	    String pc="",url="";
	    SQLiteDatabase db;
	    String datemsg = "";
	    String imei="";
	    String encodedImage = null;
		String table,req;
	    ArrayList<String> name,date,Time;
	TelephonyManager telemanager;
	    SharedPreferences sh;
	int NOTIFICATION_ID = 234;
	NotificationManager notificationManager;

	private TextToSpeech tts;


	private static String speed="Normal";

	@Override
	public void onCreate() {
		   super.onCreate();
		notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

	        isService =  true;
	    }

	    final String TAG="LocationService";
	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
	    	return super.onStartCommand(intent, flags, startId);
	   }
	   @Override
	   
	   public void onLowMemory() {
	       super.onLowMemory();

	   }
	@Override
	public void onStart(Intent intent, int startId) {
		tts = new TextToSpeech(this, this);
		//  Toast.makeText(this, "Start services", Toast.LENGTH_SHORT).show();
		  telemanager  = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

	        sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		  String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

//		  if(!provider.contains("gps"))
//	        { //if gps is disabled
//	        	final Intent poke = new Intent();
//	        	poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
//	        	poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
//	        	poke.setData(Uri.parse("3"));
//	        	sendBroadcast(poke);
//	        }
		  
//		  SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//	      URL=preferences.getString("url", "");
//	      
	      handler.postDelayed(GpsFinder,10000);
	}
	
	@Override
	public void onDestroy() {
		 String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

//		   if(provider.contains("gps"))
//		   { //if gps is enabled
//		   final Intent poke = new Intent();
//		   poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
//		   poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
//		   poke.setData(Uri.parse("3"));
//		   sendBroadcast(poke);
//		   }
		   
		   handler.removeCallbacks(GpsFinder);
	       handler = null;
	       Toast.makeText(this, "Service Stopped..!!", Toast.LENGTH_SHORT).show();
	       isService = false;
	   }

	  
	  public Runnable GpsFinder = new Runnable(){
		  
		 
	    public void run(){

	    	String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//
//	  	  if(!provider.contains("gps"))
//	          { //if gps is disabled
//	          	final Intent poke = new Intent();
//	          	poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
//	          	poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
//	          	poke.setData(Uri.parse("3"));
//	          	sendBroadcast(poke);
//	          }


	        	
	    		//Toast.makeText(getApplicationContext(), phoneid, Toast.LENGTH_LONG).show();
	    
	           // Log.d("MyService", String.valueOf("latitude"+curLocation.getLatitude()));
	            

	            getnotify();


//	            
//	            db=new completedboperation(getApplicationContext());
//	            db.location(lati, logi);
	            
	            
	            
	            
	           // Toast.makeText(getApplicationContext(),URL+" received", Toast.LENGTH_SHORT).show();

      
		    	        



      handler.postDelayed(GpsFinder,20000);// register again to start after 20 seconds...
	    }


	  };












	private  void getnotify() {




		RequestQueue queue3 = Volley.newRequestQueue(LocationServiceno.this);
		String url3 = "http://" + sh.getString("ip", "") + ":8000/noti";

		// Request a string response from the provided URL.
		StringRequest stringRequest3 = new StringRequest(Request.Method.POST, url3, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// Display the response string.
				Log.d("+++++++++++++++++", response);
				try {

					JSONObject json = new JSONObject(response);
					if(json.getString("task").equalsIgnoreCase("ok"))
					{
						notificationChecknew("New Request Accepted");
					}

					if(json.getString("task").equalsIgnoreCase("rec"))
					{
						notificationChecknew("New Recomendation available");
					}


//					for(int i=0;i<json.length();i++)
//					{
//						JSONObject jo=json.getJSONObject(i);
//						msg.add(jo.getString("task"));
//
//						notificationChecknew(jo.getString("msg"));
//						speakOut(jo.getString("msg"));
//
////                        details.add(jo.getString("details"));
////                        date.add(jo.getString("date"));
////                        status.add(jo.getString("status"));
//
//
//
//					}
//					String res = json.getString("task");
//
//
//
////                            pDialog.hide();
//					if (res.equalsIgnoreCase("yes")) {
//						 table = json.getString("table");
//						 req = json.getString("req");
//						notificationChecknew();
//
//
//						Toast.makeText(getApplicationContext(), "Request found", Toast.LENGTH_LONG).show();
//
//
//					} else {
////						Toast.makeText(getApplicationContext(), "Route is clear", Toast.LENGTH_LONG).show();
//
//
//					}

				} catch (JSONException e) {
					e.printStackTrace();
				}


			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

//                        pDialog.hide();
				Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("lid",sh.getString("lid",""));


				return params;
			}
		};
		// Add the request to the RequestQueue.
		queue3.add(stringRequest3);





	}


	  	
	  	
	  	
		
		
		


	  	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private void notificationCheck() {
		// TODO Auto-generated method stub
		try {


//				String msg="Have "+medname+" \n on   "+time+" ";
			String msg="Carefull some distruptions are there ";


				notification_popup(msg);
			}


		catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}

	}


	private void notificationChecknew(String msgg) {
		// TODO Auto-generated method stub
		try {


				String msg=msgg;
//			String msg="Be carefull You are in danger";


			notification_popup(msg);
		}


		catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}

	}
	public void notification_popup(String msg) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			String CHANNEL_ID = "my_channel_01";
			CharSequence name = "my_channel";
			String Description = "This is my channel";
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
			mChannel.setDescription(Description);
			mChannel.enableLights(true);
			mChannel.setLightColor(Color.RED);
			mChannel.enableVibration(true);
			mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//       mChannel.setVibrationPattern(new long[]{0, 800, 200, 1200, 300, 2000, 400, 4000});
			mChannel.setShowBadge(false);
			notificationManager.createNotificationChannel(mChannel);
		}
		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "my_channel_01")
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle("Notification")
				.setContentText(msg);
//    Intent resultIntent = new Intent(getApplicationContext(), view_helprequest.class);
//    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//    stackBuilder.addParentStack(MainActivity.class);
//    stackBuilder.addNextIntent(resultIntent);
//    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//    builder.setContentIntent(resultPendingIntent);
		notificationManager.notify(NOTIFICATION_ID, builder.build());
	}
	private void speakOut(String text ) {

		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void onInit(int i) {
		if (i == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} else {


			}

		} else { Log.e("TTS", "Initilization Failed!");}

	}
}
