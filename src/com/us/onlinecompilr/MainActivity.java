package com.us.onlinecompilr;

import java.io.File;  
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends Activity {
	
	String[] menu;
	DrawerLayout dLayout;
	ListView dList;
	ArrayAdapter<String> adapter;
	private ActionBar actionBar;
	private MenuItem refreshMenuItem;
	Menu_detail_fragment fragment;
	final int ACTIVITY_CHOOSE_FILE = 1;
	final int REQUEST_SAVE = 2;
	EditText editor;
	TextView filename;
	String fname="";
	Spinner lang;
	static int open=0;
	
	public void save()
	{
		LayoutInflater li = LayoutInflater.from(MainActivity.this);
		View dialog_view = li.inflate(R.layout.file_input, null);
		AlertDialog.Builder input = new AlertDialog.Builder(MainActivity.this);
		input.setView(dialog_view);
		final EditText input_test = (EditText)dialog_view.findViewById(R.id.editTextDialogUserInput1);
		input.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				String filename = input_test.getText().toString();
				if(filename.equals("")){
					Toast.makeText(getApplicationContext(), "Please Enter a filename", Toast.LENGTH_LONG).show();
				}
				else{
					arg0.dismiss();
					fname = new String(input_test.getText().toString());
					Intent intent1 = new Intent(getApplicationContext(),DirectoryPicker.class);
					// optionally set options here
					startActivityForResult(intent1, REQUEST_SAVE);
				}
			}
		});
		input.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.dismiss();
			}
		});
		AlertDialog aler = input.create();
		aler.show();
	}
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			//Toast.makeText(getApplicationContext(), requestCode, Toast.LENGTH_LONG).;
	    	switch (requestCode) {
			case ACTIVITY_CHOOSE_FILE: {
				if (resultCode == RESULT_OK) {
					Uri uri = data.getData();
					String filePath = uri.getPath();
					try {
						File file = new File(filePath);
						FileInputStream fis = new FileInputStream(file);
						byte[] data1 = new byte[(int) file.length()];
						fis.read(data1);
						fis.close();
						editor = (EditText) findViewById(R.id.editor);
						filename = (TextView)findViewById(R.id.file_name);
						String str = new String(data1, "UTF-8");
						editor.setText(str);
						filename.setText(file.getName());
						filename.setTextColor(Color.BLUE);
						filename.setTypeface(null,Typeface.BOLD_ITALIC);
						open=1;
						Toast.makeText(MainActivity.this,"If not selected, please select the language before submission",Toast.LENGTH_LONG).show();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			break;
			case REQUEST_SAVE:
				lang=(Spinner)findViewById(R.id.spinner_lang);
				Bundle extras = data.getExtras();
				String path = (String) extras.get(DirectoryPicker.CHOSEN_DIRECTORY);
				try {
					OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(new File(path+File.separator+fname+"."+Menu_detail_fragment.ext[lang.getSelectedItemPosition()])));
					EditText editor = (EditText)findViewById(R.id.editor);
					fout.write(editor.getText().toString());
					fout.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();
				
			break;
			
			}
		}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		actionBar = getActionBar();
		actionBar.setTitle("");
		actionBar.setIcon(R.drawable.ic_launcher);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		// Hide the action bar title
		menu = new String[]{"Home","Load","Save","Refresh","Contact us"};
		dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		dList = (ListView) findViewById(R.id.left_drawer);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menu);
		dList.setAdapter(adapter);
		dList.setSelector(android.R.color.holo_blue_dark);
		
		Bundle args = new Bundle();
		args.putString("Menu", menu[0]);
		Fragment detail = new Menu_detail_fragment();
		detail.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager();
		fragment = new Menu_detail_fragment();
		fragmentManager.beginTransaction()
		.replace(R.id.content_frame, detail).commit();
		dList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				dLayout.closeDrawers();
				Bundle args = new Bundle();
				args.putString("Menu", menu[position]);
				Fragment detail = new Menu_detail_fragment();
				detail.setArguments(args);
				FragmentManager fragmentManager = getFragmentManager();
				
				fragmentManager.beginTransaction().replace(R.id.content_frame, detail).commit();
			}
		});
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		
		// Associate searchable configuration with the SearchView
//		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
//				.getActionView();
//		searchView.setSearchableIn fo(searchManager
//				.getSearchableInfo(getComponentName()));
		
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		
//		case R.id.action_search:
//			// search action
//			search.setOnQueryTextListener(new OnQueryTextListener() {
//				
//				@Override
//				public boolean onQueryTextSubmit(String arg0) {
//					// TODO Auto-generated method stub
//					Toast.makeText(getApplicationContext(), arg0, Toast.LENGTH_LONG).show();
//					return false;
//				}
//
//				@Override
//				public boolean onQueryTextChange(String arg0) {
//					// TODO Auto-generated method stub
//					return false;
//				}
//				
//			});
//			return true;
		case R.id.action_refresh:
			editor = (EditText) findViewById(R.id.editor);
			filename=(TextView)findViewById(R.id.file_name);
			TextView  text=(TextView)findViewById(R.id.textnumber);
			refreshMenuItem = item;
			Menu_detail_fragment.size=18;
			// load the data from server
			new SyncData().execute();
			filename.setText("**Untitled");
			editor.setText("");
			Menu_detail_fragment.prev=0;
			Menu_detail_fragment.curr=0;
			open=0;
			lang=(Spinner)findViewById(R.id.spinner_lang);
			lang.setSelection(0);
			filename.setTypeface(null,Typeface.BOLD_ITALIC);
			editor.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_medium));
     		text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_medium));
			return true;
		case R.id.action_open:
			Intent chooseFile;
			Intent intent;
			chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
			chooseFile.setType("file/*");
			intent = Intent.createChooser(chooseFile, "Choose a file");
			startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
			return true;
        case R.id.action_save:
        	lang=(Spinner)findViewById(R.id.spinner_lang);
        	if(lang.getSelectedItemPosition()==0)
        	{
        		Toast.makeText(getApplicationContext(), "Please select the language before submission" , Toast.LENGTH_SHORT).show();
        		return true;
        		}
        	LayoutInflater li = LayoutInflater.from(MainActivity.this);
			View dialog_view = li.inflate(R.layout.file_input, null);
			AlertDialog.Builder input = new AlertDialog.Builder(MainActivity.this);
			input.setView(dialog_view);
			final EditText input_test = (EditText)dialog_view.findViewById(R.id.editTextDialogUserInput1);
			input.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					String filename_a = input_test.getText().toString();
					if(filename_a.equals("")){
						Toast.makeText(getApplicationContext(), "Please Enter a filename", Toast.LENGTH_LONG).show();
					}
					else{
						filename=(TextView)findViewById(R.id.file_name);
						filename.setText(filename_a+"."+Menu_detail_fragment.ext[lang.getSelectedItemPosition()]);
						filename.setTextColor(Color.BLUE);
						filename.setTypeface(null,Typeface.BOLD_ITALIC);
						arg0.dismiss();
						fname = new String(input_test.getText().toString());
						Intent intent1 = new Intent(getApplicationContext(),DirectoryPicker.class);
						// optionally set options here
						startActivityForResult(intent1, REQUEST_SAVE);
					}
				}
			});
			input.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					arg0.dismiss();
				}
			});
			AlertDialog aler = input.create();
			aler.show();
        	
			return true;	
        case android.R.id.home:
        	if (!dLayout.isDrawerOpen(Gravity.LEFT)) {
				dLayout.openDrawer(Gravity.LEFT);
			} else {
				dLayout.closeDrawers();
			}
        	return false;
		
		default:
			return false;
		}
		
	}
	

	/**
	 * Launching new activity
	 * */
	

	/*
	 * Actionbar navigation item select listener
	 */
	

	/**
	 * Async task to load the data from server
	 * **/
	private class SyncData extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			// set the progress bar view
			refreshMenuItem.setActionView(R.layout.action_progressbar);

			refreshMenuItem.expandActionView();
		}

		@Override
		protected String doInBackground(String... params) {
			// not making real request in this demo
			// for now we use a timer to wait for sometime
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			refreshMenuItem.collapseActionView();
			// remove the progress bar view
			refreshMenuItem.setActionView(null);
		}
	}

	public boolean onNavigationItemSelected(int arg0, long arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}

 
	   
		   
	
