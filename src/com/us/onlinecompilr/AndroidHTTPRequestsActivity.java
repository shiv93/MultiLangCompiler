package com.us.onlinecompilr;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class AndroidHTTPRequestsActivity extends Activity {
	
	TextView status,time,memory,output;
	String lang,program,testcases;
	RelativeLayout l1;
	private ActionBar actionBar;
	ArrayList<String>info=null;
	int flag=0;
	TableLayout t_l;
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_httprequests);
		actionBar = getActionBar();
		actionBar.setDisplayUseLogoEnabled(true);
		Intent getintent=getIntent();
		info=new ArrayList<String>();
		lang=getintent.getStringExtra("language");
		info.add(lang);
		program=getintent.getStringExtra("code");
		info.add(program);
		testcases=getIntent().getStringExtra("test");
		info.add(testcases);
		status = (TextView)findViewById(R.id.editstatus);
		time = (TextView)findViewById(R.id.edittime);
		memory = (TextView)findViewById(R.id.editmemory);
		output = (TextView)findViewById(R.id.editoutput);
		l1=(RelativeLayout)findViewById(R.id.loadingPanel);
		t_l=(TableLayout)findViewById(R.id.tlayout);

		try{
			new get_data().execute(info);
			
		} catch(Exception e){ 
			Log.e("error", "yo") ;
			e.printStackTrace(); 
		}
		
	}
	
	
	class get_data extends AsyncTask<ArrayList<String>, Void, String>{
      
		String ret=null; 
      
		@Override
		protected String doInBackground(ArrayList<String>... arg0) {
			// TODO Auto-generated method stub
			ArrayList<String> get_details=arg0[0];
			String prog=get_details.get(1);
			String langg=get_details.get(0);
			String testcase = get_details.get(2);  
			testcase = testcase.replace("\n", "\\n");
			testcases ="[\""+testcase+"\"]";


			    	try{ 
			    		
			    		HttpClient client = new DefaultHttpClient();
						HttpPost request = new HttpPost("http://api.hackerrank.com/checker/submission.json");
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
						nameValuePairs.add(new BasicNameValuePair("source",prog));
				        nameValuePairs.add(new BasicNameValuePair("lang",Integer.toString(Integer.parseInt(langg))));
				        nameValuePairs.add(new BasicNameValuePair("testcases", testcases));
				        nameValuePairs.add(new BasicNameValuePair("api_key", "hackerrank|282807-132|8d62bbbdf90d6a790747561f031a017b7f6cbbeb"));
				         request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				      
						HttpResponse response = client.execute(request);
						ret = EntityUtils.toString(response.getEntity());
						Log.e("language", langg);
						}catch (Exception e){
						
						e.printStackTrace();
					}
			  
			  return ret;
			  
					
	}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			try{
				l1.setVisibility(View.INVISIBLE);
				t_l.setVisibility(View.VISIBLE);
				
				JSONObject obj = new JSONObject(result);
				JSONObject sys  = obj.getJSONObject("result");
				String compile = sys.getString("compilemessage");
				String mem = sys.getString("memory");
				String message = sys.getString("message");
				String comp_err = sys.getString("censored_stderr");
				String comp_err1 = comp_err.substring(2,comp_err.length()-2 );
				String out = sys.getString("stdout");
				String out1 = out.replace("\\n", "\n").replace("\\b", "\b").replace("\\t", "\t");
				String time1 = sys.getString("time"); 
				if(message.subSequence(2, message.length()-2).equals("Success")){
				time.setText(time1.substring(1,Math.min(time1.length(),4))+" sec");
				memory.setText(mem.substring(1,mem.length()-1)+" Kb");
				status.setText(message.substring(2,message.length()-2));
				status.setTextColor(Color.GREEN);
				output.setText(out1.substring(2,out1.length()-2));
			}
			else if(compile.length()>0 && time1.equalsIgnoreCase("null")){
				status.setText("Compilation error \n"+compile);
				status.setTextColor(Color.RED);
				time.setText("0 sec");
				memory.setText("0 Kb");
				
			}
			else if(compile.length()>0 || comp_err1.length()>0){
				status.setText("Runtime error \n"+compile);
				status.setTextColor(Color.RED);
				time.setText(time1.substring(1,Math.min(time1.length(),4))+" sec");
				memory.setText(mem.substring(1,mem.length()-1)+" KB");
				output.setText(out1.substring(2,out1.length()-2));
			}
							} catch(Exception e){ 
				e.printStackTrace();
			}
		
		   
		
		}		
	}	
	
	
}