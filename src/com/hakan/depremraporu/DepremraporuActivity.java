package com.hakan.depremraporu;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DepremraporuActivity extends Activity
		implements
			OnClickListener,
			OnItemClickListener {
	ProgressDialog progressDialog;
	ReportAdapter reportAdapter;
	ListView reportList;
	ArrayList<String> rcArrList = new ArrayList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		try {
			// putDataList();
			if (isNetworkAvailable()){
				getConnectionData();
			}
			else{
				AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
	            alertbox.setMessage("Network baðlantýnýz yok.\n Lütfen kontrol ediniz.");
	            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface arg0, int arg1) {
	                   finish();
	                }
	            });
	            alertbox.show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getConnectionData() {
		progressDialog = ProgressDialog.show(this, "veriler alýnýyor",
				"lütfen bekleyiniz");
		try {
			ConnectionListener<String, String> listener = new ConnectionListener<String, String>() {
				@Override
				public void actionPerformed(String actionCommand,
						boolean success, String[] data, String data2) {
					progressDialog.dismiss();
					if (success) {
						ApplicationData.reportList = data;
						setContentView(R.layout.report_main);
						try {
							putDataList();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
					}
				}
			};
			DepremConnection dp = new DepremConnection();
			dp.getTradeMasterNews(listener);
		} catch (Exception e) {
			System.out.println("1 " + e.toString());
		}
	}

	public void getMapLocation() {
		startActivity(new Intent(DepremraporuActivity.this, MapAct.class));
	}

	public void putDataList() throws IOException {
		reportList = (ListView) findViewById(R.id.listReport);
		reportList.setCacheColorHint(Color.TRANSPARENT);
		reportList.setOnItemClickListener(this);

		String[] tmpData = ApplicationData.reportList;

		rcArrList.clear();
		for (int i = 0; i < tmpData.length; i++) {
			rcArrList.add(tmpData[i]);
		}
		ApplicationData.reportList = tmpData;
		tmpData = rcArrList.toArray(new String[rcArrList.size()]);
		reportAdapter = new ReportAdapter(this, tmpData);
		reportList.setAdapter(reportAdapter);
		reportAdapter.notifyDataSetChanged();
	}

	public static String[] parseMock(String str) {
		return str.split("[#]");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == reportList) {
			ApplicationData.xCord = parseMock(ApplicationData.reportList[position])[1];
			ApplicationData.yCord = parseMock(ApplicationData.reportList[position])[2];
			ApplicationData.gDate = parseMock(ApplicationData.reportList[position])[0];
			ApplicationData.gWhere = parseMock(ApplicationData.reportList[position])[6];
			for (int i = 0; i < ApplicationData.reportList.length; i++) {
				System.out.println(ApplicationData.reportList[i]);
			}
			System.out.println("ApplicationData.xCord " + ApplicationData.xCord
					+ " --- " + "ApplicationData.yCord "
					+ ApplicationData.yCord);
			getMapLocation();
		}
	}

	@Override
	public void onClick(View arg0) {
	}

	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Çýkmak istediðinizden emin misiniz?")
				.setCancelable(false)
				.setPositiveButton("Evet",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try {
									finish();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						})
				.setNegativeButton("Hayýr", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
}
