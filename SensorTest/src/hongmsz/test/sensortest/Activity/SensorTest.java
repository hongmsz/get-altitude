package hongmsz.test.sensortest.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.TimeZone;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import hongmsz.test.sensortest.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SensorTest extends Activity implements SensorEventListener {
    /** Called when the activity is first created. */
	private TextView _TextView = null;
    private Button _btInit = null;
    private EditText _et, _et2;
    private Button setB, setB2;
    
    long _timeMillis;
    String loc = 572+"";
    
	
	float wd;
	String weather="";
	
//    private int cnt_filter;
//*
    private static final int SIZE_FILTER = 9;
    private float[] tmp_x = {0,0,0,0,0,0,0,0,0};
    private float[] tmp_y = {0,0,0,0,0,0,0,0,0};
    private float[] tmp_z = {0,0,0,0,0,0,0,0,0};
//*/
/*
    private static final int SIZE_FILTER = 5;
    private float[] tmp_x = {0,0,0,0,0};
    private float[] tmp_y = {0,0,0,0,0};
    private float[] tmp_z = {0,0,0,0,0};
//*/
    private float tmp_sort;
/*    
    private float[] raw_x;
    private float[] raw_y;
    private float[] raw_z;
//*/    
	private long lastTime;
	private float angle;
    private float speed;
    private float altitude, c_altitude=0.0f;
    private float lastX; 
    private float lastY; 
    private float lastZ; 
    private float dX;
    private float dY;
    private float dZ;
    double lati, longi;
   boolean isGPS = false, isWifi=false;

    private float x, y, z; 
    private float gx, gy, gz;
    private float gx2, gy2, gz2;
    private float px, pc, p0;
    private static final int SHAKE_THRESHOLD = 800; 
   

    private static final int DATA_X = SensorManager.DATA_X; 
    private static final int DATA_Y = SensorManager.DATA_Y; 
    private static final int DATA_Z = SensorManager.DATA_Z; 
   

    private SensorManager sensorManager; 
    private SensorManager sensorManager2;
    private SensorManager sensorManager3;
    private Sensor accelerormeterSensor;
    private Sensor Gyro;
    private Sensor RGyro;
    private Sensor Tmp11;
    
//    private Activity mainA;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        LocationManager locMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        TimeZone timezone = TimeZone.getTimeZone("Etc/GMT-9");
        TimeZone.setDefault(timezone);
        
        _timeMillis = System.currentTimeMillis();
        
        p0 = Float.parseFloat(getPageSource(loc)+"f");
        
 //       _btInit = (Button) findViewById(R.id.btInit);
 //       _btInit.setOnClickListener(on_Init);
        
        _et = (EditText) findViewById(R.id.editText1);
        _et.setText("지역명(예: 서울, 성남...)");
        _et.setOnClickListener(on_ETinit);
        
        _et2 = (EditText) findViewById(R.id.editText2);
        _et2.setText("현재 고도");
        _et2.setOnClickListener(on_ETinit);
        
        setB = (Button)findViewById(R.id.setB);
        setB.setOnClickListener(on_Set);
        setB2 = (Button)findViewById(R.id.setB2);
        setB2.setOnClickListener(on_Set);
        
        _TextView = (TextView) findViewById(R.id.TextView);
       
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        sensorManager2 = (SensorManager) getSystemService(SENSOR_SERVICE);
//        sensorManager3 = (SensorManager) getSystemService(SENSOR_SERVICE); 
//        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        Gyro = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
//        RGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Tmp11 = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        
        isGPS = locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isWifi = locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        
        if(!isGPS){
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setCancelable(true);
        	builder.setMessage("GPS가 사용가능하도록 설정되어있지 않습니다.")
        	.setCancelable(false)
        	.setPositiveButton("확인", 
        			new DialogInterface.OnClickListener() {
        		
				@Override
				public void onClick(DialogInterface dialog, int id) {
					// TODO Auto-generated method stub
					try{
						dialog.dismiss();
					}catch(IllegalArgumentException e){
						
					}
					startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				}
			}).setNegativeButton("닫기", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int id) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
        	AlertDialog alert = builder.create();
        	alert.show();
        }else if(!isWifi){
        	Toast.makeText(this, "GPS 사용", Toast.LENGTH_LONG);
        }
        
        LocationListener locListener = new LocationListener()
        {
        	public void onLocationChanged(Location location){
        		if(location != null){
        			lati = location.getLatitude();
        			longi = location.getLongitude();
//        			Toast.makeText(getBaseContext(), "lat: "+location.getLatitude()+"long: "+location.getLongitude(), Toast.LENGTH_SHORT).show();
        		}
        	}
        	
        	public void onProviderDisabled(String provider){
        		
        	}
        	
        	public void onProviderEnabled(String provider){
        		
        	}
        	
        	public void onStatusChanged(String provider, int status, Bundle extras){
        		
        	}
        };
        
        if(isWifi){
        	locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 120000L, 10, locListener);
        }
         
        if(isGPS){
        	locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000L, 5, locListener);
        }
        
        
    }
    
    private View.OnClickListener on_ETinit = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.editText1)
				_et.setText("");
			else if(v.getId()==R.id.editText2)
				_et2.setText("");
		}
	};
    
	
    public void onStart(){
    	super.onStart();
/*    	
    	if (accelerormeterSensor != null) 
            sensorManager.registerListener(this, accelerormeterSensor, 
//                   SensorManager.SENSOR_DELAY_GAME);
            		SensorManager.SENSOR_DELAY_NORMAL);
    	if (Gyro != null) 
            sensorManager.registerListener(this, Gyro, 
//                    SensorManager.SENSOR_DELAY_GAME);
            		SensorManager.SENSOR_DELAY_NORMAL);
    	if (RGyro != null) 
            sensorManager.registerListener(this, RGyro, 
//                    SensorManager.SENSOR_DELAY_GAME);
            		SensorManager.SENSOR_DELAY_NORMAL);
//*/
    	if (Tmp11 != null) 
            sensorManager.registerListener(this, Tmp11, 
//                    SensorManager.SENSOR_DELAY_GAME);
            		SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    public void onStop(){
    	super.onStop();
    	
    	if (sensorManager != null) 
            sensorManager.unregisterListener(this); 
//    	if (sensorManager2 != null) 
//            sensorManager2.unregisterListener(this);
//    	if (sensorManager3 != null) 
//            sensorManager3.unregisterListener(this);
    }
    
    public void onAccuracyChanged(Sensor sensor, int accuracy) { 
    } 

    public void onSensorChanged(SensorEvent event) { 
/*
    	if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) { 
            long currentTime = System.currentTimeMillis(); 
            long gabOfTime = (currentTime - lastTime); 
   

            if (gabOfTime > 100) { 
                lastTime = currentTime; 
   
                for(int si = 0; si < SIZE_FILTER; si++)
                {
                	tmp_x[si]=event.values[SensorManager.DATA_X];
                	tmp_y[si]=event.values[SensorManager.DATA_Y];
                	tmp_z[si]=event.values[SensorManager.DATA_Z];
                }
                
                fSort(tmp_x, 0, SIZE_FILTER);
                fSort(tmp_y, 0, SIZE_FILTER);
                fSort(tmp_z, 0, SIZE_FILTER);
                
                x = tmp_x[SIZE_FILTER/2];
                y = tmp_y[SIZE_FILTER/2];
                z = tmp_z[SIZE_FILTER/2];      
                

//                x = event.values[SensorManager.DATA_X]; 
//                y = event.values[SensorManager.DATA_Y]; 
//                z = event.values[SensorManager.DATA_Z];
 
                dX = x - lastX;
                dY = y - lastY;
                dZ = z - lastZ;
                
                if(Math.abs(dX) < 0.05)
                	dX = 0;
                if(Math.abs(dY) < 0.05)
                	dY = 0;
                if(Math.abs(dZ) < 0.05)
                	dZ = 0;

                speed = Math.abs(dX + dY + dZ) / gabOfTime * 10000;   
//                speed = (float) Math.sqrt(x*x + y*y + z*z);
                angle = (float)Math.atan(y/z)*180 / (float)Math.PI;

//                if (speed > SHAKE_THRESHOLD) { 
                    // 이벤트 발생!! 
//                } 
                
//                lastX = event.values[DATA_X]; 
//                lastY = event.values[DATA_Y]; 
//                lastZ = event.values[DATA_Z]; 

                for(int si = 0; si < SIZE_FILTER; si++)
                {
                	tmp_x[si]=event.values[SensorManager.DATA_X];
                	tmp_y[si]=event.values[SensorManager.DATA_Y];
                	tmp_z[si]=event.values[SensorManager.DATA_Z];
                }
                
                fSort(tmp_x, 0, SIZE_FILTER);
                fSort(tmp_y, 0, SIZE_FILTER);
                fSort(tmp_z, 0, SIZE_FILTER);
                
                lastX = tmp_x[SIZE_FILTER/2];
                lastY = tmp_y[SIZE_FILTER/2];
                lastZ = tmp_z[SIZE_FILTER/2];  
               
                
            } 
        } 
    	
    	if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                gx = event.values[SensorManager.DATA_X]; 
                gy = event.values[SensorManager.DATA_Y]; 
                gz = event.values[SensorManager.DATA_Z];  
                
        } 
    	
    	if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gx2 = event.values[0]; 
            gy2 = event.values[1]; 
            gz2 = event.values[2];  
            
    	}
//*/
    	
    	if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            px = event.values[0];
    	}
    	
    	
    	//Float.parseFloat(_et.getText().toString()+"f");
    	
    	altitude = (float) (44330 * (1 - Math.pow((px/p0), 0.190295)));
    	
    	pc = (float) (p0*Math.pow((1-c_altitude/44330.0d), 5.255));

    	wd = px - pc;
    
    	if(wd>2.5f)
    		weather = "Sunny";
    	else if(wd >= -2.5f && wd<= 2.5f)
    		weather = "Cloudy";
    	else if(wd<-2.5f)
    		weather = "Rain";
    	
//    	_TextView.setText(weather);
//*    	
        _TextView.setText( 
    			String.format(
//    					"Current\nx: %f\ny: %f\nz: %f\n\n" +
//    					"Difference\nx: %f\ny: %f\nz: %f\n\n" +
//    					"speed: %f\n\n" +
//    					"angle: %f\n\n" +
//    					"Direction\nx: %f\ny: %f\nz: %f\n\n" +
//    					"Gyro\nx: %f\ny: %f\nz: %f\n\n" +
//    					"ex) 서울: 1021.8 성남: 1022.2\n\n"+
    					"%s\n\n"+
    					"Pressure\n%.3f (hPa)\n\n" +
    					"e-Pressure\n%.3f (hPa)\n\n" +
    					"Latitude: %f, Longitude: %f, " +
    					"Altitude\n%.2f (m)\n\n", 
//    					x, y, z, 
//    					dX, dY, dZ, 
//    					speed, 
//    					angle, 
//    					gx, gy, gz, 
//    					gx2, gy2, gz2,
    					weather,
    					px,
    					pc,
    					lati, longi,
    					altitude
    			)
    	);
//*/
    } 
    
    public void fSort(float[] in, int beg_num, int end_num){
    	end_num = SIZE_FILTER;
    	for(int x=0;x<end_num-1; x++)
		for(int i = 0; i < end_num-2; i++)
			for(int j = i; j< end_num- 1;j++)
				if(in[j]>in[j+1])
					fSwap(in[j], in[j+1], tmp_sort);
    }
    
    public void fSwap(float a, float b, float tmp){
			tmp = a;
			a = b;
			b = tmp;
    }
    
    private View.OnClickListener on_Init = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
//			cnt_filter = 0;
		}
	};
	
	private View.OnClickListener on_Set = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.setB){
				if(_et.getText().toString().matches("서울")){
					loc = 108+"";
				}else if(_et.getText().toString().matches("성남")){
					loc = 572+"";
				}
			}else if(v.getId()==R.id.setB2){
				c_altitude = Float.parseFloat(_et2.getText().toString());
			}			
			p0 = Float.parseFloat(getPageSource(loc)+"f");
		}
	};
	
	private String getPageSource(String aaa ){
    	String retVal = "";
    	
    	String resVal="";
 //   	String eUrl="";
    	String urlString;
    	String Sdate = DateFormat.format("yyyyMMddkkmm", _timeMillis).toString();	;
    	int SI, EI;
    	
    	try {
    		
//    		String sUrl="";
    		
//    		sUrl = urlString.substring(0, urlString.lastIndexOf("/")+1);
//    		eUrl = urlString.substring(urlString.lastIndexOf("/")+1, urlString.length()); // 한글과 공백을 포함한 부분
//    		eUrl = URLEncoder.encode(nameString,"UTF-8").replace("+", "%20");
//    		urlString = "http://search.naver.com/search.naver?where=nexearch&sm=ies_hty&query=" + eUrl;
//    		urlString = "http://hongmsz.raonnet.com/datacode/kname.xml";
    		urlString = "http://203.247.66.10/cgi-bin/aws/nph-aws_txt_min?"+Sdate+"&0&MINDB_01M&"+aaa+"&a";
    		
    		URL url = new URL( urlString );
    		URLConnection connection = url.openConnection();
    		InputStream is = connection.getInputStream();
    		BufferedReader br = new BufferedReader( new InputStreamReader( is, "UTF-8" ), 8192 );

    		char recv[] = new char[ 8192 ];
    		while( br.read( recv ) > 0 )
    			retVal += new String( recv );
    		br.close();
    		is.close();
    	} catch (MalformedURLException e) {
    		e.printStackTrace();
    		return null;
    	} catch (IOException e) {
    		e.printStackTrace();
    		return null;
    	}
/*    	
    	EI = retVal.indexOf(MyApp.getGlobalString());
		SI = retVal.indexOf("<span>(", EI);
		if(SI > 0){
			EI = retVal.indexOf(")</span>", SI);
			SI= SI+7;
//*/		
//		EI = retVal.indexOf(nameString);
		SI = retVal.indexOf("class=textb>");
		if(SI > 0){
			SI = retVal.indexOf("class=textb>", SI+12);
			SI = retVal.indexOf("class=textb>", SI+12);
			SI = retVal.indexOf("<td>", SI+12);
			SI = retVal.indexOf("<td>", SI+4);
			EI = retVal.indexOf("</td>", SI);
			SI= SI+4;
			resVal = retVal.substring(SI, EI);
			retVal = "";
			retVal = resVal;
			
			return retVal;
		}else
			return null;
    }

}