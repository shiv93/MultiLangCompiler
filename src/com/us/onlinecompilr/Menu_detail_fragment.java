package com.us.onlinecompilr;

import android.app.AlertDialog;     
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Menu_detail_fragment extends Fragment {
	
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}


    static String ext[]={"txt","c","cpp","java","py","pl","php","rb","sql","dbf","clj","sh","erl","lisp","lua"};
	TextView filename,text;
	static int size=18;
	String languages[]={"Languages","C                  ","C++              ","JAVA             ","PYTHON      ","PERL            ","PHP           ","RUBY         ","MYSQL     ","ORACLE    ","CLOJURE    ","BASH          ","erLANG       ","CLISP          ","LUA             "};
	String result;
	Spinner lang;
	String testcases;
	ImageView add,sub;
	EditText editor;
	Button bcompile,input_t;
	final int ACTIVITY_CHOOSE_FILE = 1;
	final int REQUEST_SAVE = 2;
	MainActivity obj;
	static int curr=0; int flag=0; int lang_above=0;  int counter=280;static int prev=0;int refresh=0;
	String language_hint[]={"","#include <stdio.h>"+"\n"+"#include <string.h>"+"\n"+"#include <math.h>"+"\n"+"#include <stdlib.h>"+"\n"+"int main(){"+"\n"+"/* Enter your code here. Read input from STDIN. Print output to STDOUT */"
	+"\n"+"return 0;"+"\n"+"};","#include <cmath>"+"\n"+"#include <cstdio>"+"\n"+"#include <vector>"+"\n"+"#include <iostream>"+"\n"+"#include <algorithm>"+"\n"+"using namespace std;"+"\n"+"int main(){"+"\n"+"/* Enter your code here. Read input from STDIN. Print output to STDOUT */"
	+"\n"+"return 0;"+"\n"+"}","import java.io.*;\n"+"import java.util.*;\n"+"public class Solution{\n"+"public static void main(String[] args){\n"+"/* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution.*/\n"+"}\n"+"}",
	 "","","<?php\n\n// your code goes here","","","","; your code goes here","#!/bin/bash\n# your code goes here","","","-- your code goes here\n"};

	
	
  
	@Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        final View view = inflater.inflate(R.layout.activity_menu_detail_fragment, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        String menu = getArguments().getString("Menu");
        InputMethodManager im = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        
    	

    	 		lang=(Spinner)view.findViewById(R.id.spinner_lang);
    		ArrayAdapter<String> adp= new ArrayAdapter<String>(getActivity(),R.layout.forscrollview, languages);
    		
    	    lang.setAdapter(adp);
    	    add=(ImageView)view.findViewById(R.id.image_add);
    	    sub=(ImageView)view.findViewById(R.id.image_subtract);
    	    editor=(EditText)view.findViewById(R.id.editor);
    	     bcompile=(Button)view.findViewById(R.id.bcompile);
    	    input_t=(Button)view.findViewById(R.id.bInput);
    	    filename=(TextView)view.findViewById(R.id.file_name);
    	    
    	   editor.setOnKeyListener(new OnKeyListener() {
    		
    		@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
    			
    			
    			//for enter key pressed down this will occur
    			//if arg2.getAction()==KeyEvent.ACTION_DOWN wasnt written then two times executed as for action down of enter key and for action up
    			if(arg2.getAction()==KeyEvent.ACTION_DOWN&&arg1==KeyEvent.KEYCODE_ENTER){
    				TextView text=(TextView)view.findViewById(R.id.textnumber);
    				text.setText(text.getText().toString().concat("\n"+(++counter)+"."));
    				int curr_line=editor.getLayout().getLineForOffset(editor.getSelectionStart());
    				int currpos=editor.getSelectionStart();
    				int startpos=editor.getLayout().getLineStart(0);
    				editor.setSelection(editor.getText().length());
    				int lastpos=editor.getSelectionStart();
    				String lower=editor.getText().subSequence(currpos,lastpos).toString();
    				String upper=editor.getText().subSequence(startpos,currpos).toString();
    				editor.setText(upper+"\n");
    				editor.append(lower);
    				editor.setSelection(editor.getLayout().getLineStart(curr_line+1)); 
    				
    			return true;	
    				
    			}
				return false;
			}
    	}) ;  
    	input_t.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LayoutInflater li=LayoutInflater.from(getActivity());
    			View dialog_view=li.inflate(R.layout.taking_input, null);
    			AlertDialog.Builder input=new AlertDialog.Builder(getActivity());
    			input.setView(dialog_view);
    			final EditText input_test=(EditText)dialog_view.findViewById(R.id.editTextDialogUserInput);
    			input.setPositiveButton("Run", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						
						if(curr==0){
							Toast.makeText(getActivity(), "Please select a language!!",Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}
						else{
							ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		    			    NetworkInfo netInfo = cm.getActiveNetworkInfo();
		    			    if (netInfo != null && netInfo.isConnectedOrConnecting()){
						dialog.dismiss();
						testcases=input_test.getText().toString();
						Intent httpcon=new Intent(getActivity(),AndroidHTTPRequestsActivity.class);
		    			
		    			    				
	    			httpcon.putExtra("test", testcases);
	    			testcases="";
	    			httpcon.putExtra("code", editor.getText().toString());
	    			httpcon.putExtra("language",String.valueOf(curr+lang_above));
	    			startActivity(httpcon);
		    			    }
		    			   else Toast.makeText(getActivity(), "No Internet Connection",Toast.LENGTH_SHORT).show();
					}
			}
		});
    			input.setNegativeButton("Back",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						testcases="";
					
						
						  
					}
				});
    			AlertDialog aler=input.create();
    			aler.show();
			}
		});
    	
    	add.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			// TODO Auto-generated method stub
    			TextView text=(TextView)view.findViewById(R.id.textnumber);
    			if(size>60)
    			Toast.makeText(getActivity(),"Size cannot be increased beyond this limit",Toast.LENGTH_SHORT ).show();	
    			else{
    		     editor.setTextSize(TypedValue.COMPLEX_UNIT_SP,++size);
    			  text.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
    			}
    		}
    	});    
    	sub.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			// TODO Auto-generated method stub
    			TextView text=(TextView)view.findViewById(R.id.textnumber);
    			size--;
    			if(size<12){
    			Toast.makeText(getActivity(), "Size cannot be reduced beyond this limit",Toast.LENGTH_SHORT).show();		
    			size=12;
    			}
    			else{
    			editor.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);;
    			text.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);;
    			}
       		}
    	});

    	    
    	   lang.setOnItemSelectedListener(new OnItemSelectedListener() {

    		@Override
    		public void onItemSelected(AdapterView<?> arg0, View arg1, final int arg2,
    				long arg3) {
    			// TODO Auto-generated method stub
    			        if(prev!=0){   			        	
    			        	Log.e("inside prev outside flag",Integer.toString(prev));
    			        	if(flag!=0){
    			        		Log.e("inside flag",Integer.toString(prev) );
    					AlertDialog.Builder prompt=new AlertDialog.Builder(getActivity());
    					prompt.setTitle("Alert!!");
    					prompt.setMessage("Would you like to change the language\nWarning! You may lose your current code.");
    					prompt.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
    						
    						@Override
    						public void onClick(DialogInterface dialog, int which) {
    							// TODO Auto-generated method stub
    				            dialog.dismiss();
    							prev=curr;
    				            curr=arg2;
    				            flag=1;
    				            filename.setText("**Untitled");
			            		filename.setTextColor(Color.BLUE);
								filename.setTypeface(null,Typeface.BOLD_ITALIC);
    				            Log.e("inside yes", Integer.toString(prev));
    				            editor.setText(language_hint[curr]);
    				            if(MainActivity.open==1){filename.setText("**Untitled");
    				            		filename.setTextColor(Color.BLUE);
    									filename.setTypeface(null,Typeface.BOLD_ITALIC);
    									MainActivity.open=0;
    				            }
    				 		   if(arg2>=12)lang_above=4;
    				 		   else if(arg2>=10)lang_above=3;
    				 		   else if(arg2>=8)lang_above=2;
    				 		   else if(arg2>=4)lang_above=1;
    				 			 else lang_above=0;
    				          
    						}
    					});
    			        prompt.setNegativeButton("No",new DialogInterface.OnClickListener() {
    						
    						@Override
    						public void onClick(DialogInterface dialog, int which) {
    							// TODO Auto-generated method stub
    							dialog.dismiss();
    						
    							flag=0;
    							Log.e("inside no",Integer.toString(prev));
    							editor.setText(language_hint[prev]);
    							lang.setSelection(prev);
    							
    							  
    									}
    					});	
    			        AlertDialog alert=prompt.create();
    					alert.show();
    			        }else flag=1;
    			        	
    			           	}
    			        else {
    			        	curr=arg2;
    			        	prev=curr;
    			        	flag=1;
    			        	editor.setText(language_hint[curr]);
    			        	if(arg2>=12)lang_above=4;
 				 		   else if(arg2>=10)lang_above=3;
 				 		   else if(arg2>=8)lang_above=2;
 				 		   else if(arg2>=4)lang_above=1;
 				 			 else lang_above=0;
    			        	lang.setSelection(curr);
			              	}
 				 		   
 				 		 

    	  }
    		@Override
    		public void onNothingSelected(AdapterView<?> arg0) {
    			// TODO Auto-generated method stub
    			
    		}
    	});	
    	  
    	 bcompile.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) { 
    			// TODO Auto-generated method stub
    			if(curr==0)
					Toast.makeText(getActivity(), "Please select a language!!",Toast.LENGTH_SHORT).show();
    			else{
    				ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    			    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    			    if (netInfo != null && netInfo.isConnectedOrConnecting()){
    			Intent httpcon=new Intent(getActivity(),AndroidHTTPRequestsActivity.class);
    			httpcon.putExtra("test", "1\n");
    			testcases="";
    			httpcon.putExtra("code", editor.getText().toString());
    			httpcon.putExtra("language",String.valueOf(curr+lang_above));
    			Log.d("value:",Integer.toString(curr+lang_above));
    			startActivity(httpcon);
    			    }else Toast.makeText(getActivity(), "No Internet Connection",Toast.LENGTH_SHORT).show();
    			}
    		}
    	});  
    	 
    	 if(menu.equalsIgnoreCase("Load")){
     		menu = "Home";
     		Intent chooseFile;
 			Intent intent;
 			chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
 			chooseFile.setType("file/*");
 			intent = Intent.createChooser(chooseFile, "Choose a file");
 			getActivity().startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
     	}
     	if(menu.equalsIgnoreCase("Save")){
     		menu = "Home";
     		((MainActivity)getActivity()).save();
     		
     	}
     	if(menu.equalsIgnoreCase("Refresh")){
     		menu="Home";
     		text=(TextView)view.findViewById(R.id.textnumber);
     		editor.setText("");
     		size=18;
     		curr=0;
     		prev=0;
     		MainActivity.open=0;
     		editor.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_medium));
     		text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_medium));
     		filename.setText("**Untitled");
     		filename.setTypeface(null,Typeface.BOLD_ITALIC);
     		filename.setTextColor(Color.BLUE);
     	}
      if(menu.equalsIgnoreCase("Contact us")){
    	  menu="Home";
   Intent abc=new Intent(getActivity(), About_us.class);
   startActivity(abc);
   
      }
    		 	   
    	return view;
        
    }


    
}
