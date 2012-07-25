package com.hakan.depremraporu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReportAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private String[] gRecommendation;
	Context context;

	@SuppressWarnings("unused")
	public String[] getgRecommendation() {
		return gRecommendation;
	}

	@SuppressWarnings("unused")
	public void setgRecommendation(String[] gCategory) {
		this.gRecommendation = gCategory;
		this.notifyDataSetChanged();
	}

	@SuppressWarnings("unused")
	public ReportAdapter(Context context, String[] gCategory) {
		mInflater = LayoutInflater.from(context);
		this.context = context;
		this.gRecommendation = gCategory;
	}

	@Override
	public int getCount() {
		return gRecommendation.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.report_main_list, null);

			holder = new ViewHolder();
			holder.rcmCityText = (TextView) convertView
					.findViewById(R.id.cityNameTV);
			holder.rcmDateText = (TextView) convertView
					.findViewById(R.id.dateTV);
			holder.rcmDepthText = (TextView) convertView
					.findViewById(R.id.depthTV);
			holder.rcmLevelText = (TextView) convertView
					.findViewById(R.id.levelTV);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Integer[] Imgid = {R.drawable.red, R.drawable.orange, R.drawable.blue};
		 
		if (gRecommendation != null || gRecommendation.length > 0
				|| gRecommendation[position] != "") {
			ImageView imgView = (ImageView) convertView
					.findViewById(R.id.iconSignal);
//
//			if (langOfComment == 0) {
//				if (XUtil.parseMockData(gRecommendation[position])[3]
//						.compareTo("") != 0) {
//					suggestionIcon.setVisibility(View.VISIBLE);
//				} else {
//					suggestionIcon.setVisibility(View.INVISIBLE);
//				}
//			} else {
//				if (XUtil.parseMockData(gRecommendation[position])[4]
//						.compareTo("") != 0) {
//					suggestionIcon.setVisibility(View.VISIBLE);
//				} else {
//					suggestionIcon.setVisibility(View.INVISIBLE);
//				}
//			}

			holder.rcmDateText.setText(parseMock(gRecommendation[position])[0]);
			holder.rcmCityText.setText(parseMock(gRecommendation[position])[6]);
			holder.rcmDepthText.setText(parseMock(gRecommendation[position])[3] + " km");
			String stateStyle = (parseMock(gRecommendation[position])[5]);
			if (stateStyle.compareTo("ML") == 0)
				imgView.setImageResource(Imgid[2]);
			else if(stateStyle.compareTo("MD") == 0)
				imgView.setImageResource(Imgid[1]);
			else
				imgView.setImageResource(Imgid[0]);
//			holder.rcmStateText
//					.setTextAppearance(
//							context,
//							((stateStyle.compareTo("AL") == 0)
//									? R.style.mypage_suggestions_state_green_textstyle
//									: (stateStyle.compareTo("TUT") == 0)
//											? R.style.mypage_suggestions_state_orange_textstyle
//											: (stateStyle.compareTo("SAT") == 0)
//													? R.style.mypage_suggestions_state_red_textstyle
//													: R.style.mypage_suggestions_state_white_textstyle));

			holder.rcmLevelText.setText(parseMock(gRecommendation[position])[4]);
		}
		return convertView;
	}

	public static String dateConverter(String data) {
		return data.subSequence(8, 10) + "." + data.subSequence(5, 7) + "."
				+ data.subSequence(0, 4);
	}

	public static String convertValidPrice(String strIn) {
		String strOut = "";
		String[] data = strIn.split("[.]");
		if (strIn.contains(".")) {
			if (data[1].length() > 2) {
				strOut += data[0] + "." + data[1].substring(0, 2);
			} else if (data[1].length() == 1) {
				strOut += data[0] + "." + data[1] + "0";
			} else {
				strOut += data[0] + "." + data[1];
			}
		} else {
			strOut += strIn + ".0";
		}
		return strOut;
	}

	static class ViewHolder {
		TextView rcmCityText;
		TextView rcmDateText;
		TextView rcmDepthText;
		TextView rcmLevelText;
	}
	
	public static String[] parseMock(String str) {
		return str.split("[#]");
	}
}
