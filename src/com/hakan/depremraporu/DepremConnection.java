package com.hakan.depremraporu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.AsyncTask;

public class DepremConnection {
	@SuppressWarnings("all")
	public void getTradeMasterNews(final ConnectionListener listener)
			throws Exception {
		new AsyncTask<String[], String[], String[]>() {
			@Override
			protected String[] doInBackground(String[]... params) {
				String[] pureBigData = null;
				InputStreamReader isr = null;
				BufferedReader in = null;
				try {

					try {
						URL url = new URL(
								"http://www.koeri.boun.edu.tr/scripts/lst6.asp");
						in = new BufferedReader(new InputStreamReader(
								url.openStream(), "windows-1254"));
						String line1 = null;
						int inSize = 0;
						int inBigSize = 0;
						String[] pureData = new String[0];
						pureBigData = new String[0];
						String strLine;
						while ((line1 = in.readLine()) != null) {
							// SB.append(line1 + "\n");
							try {
								pureData = (String[]) expand(pureData);
								pureData[inSize] = line1;
								inSize++;
							} catch (Exception e) {// Catch exception if any
								System.err.println("Error: " + e.getMessage());
							}
						}

						for (int i = 0; i < pureData.length; i++) {
							Pattern patternRecordID = Pattern
									.compile("(\\d{4})\\.(\\d{2})\\.(\\d{2})\\s(\\d{2}):(\\d{2}):(\\d{2})\\s+(\\d{2}\\.\\d{4})\\s+(\\d{2}\\.\\d{4})\\s+(\\d+\\.\\d)\\s+([\\d\\.[-]\\s]+)(.+)");
							Matcher matcherRecordID = patternRecordID
									.matcher(pureData[i]);

							while (matcherRecordID.find()) {
								String pData = matcherRecordID.group(1)
										+ "."
										+ matcherRecordID.group(2)
										+ "."
										+ matcherRecordID.group(3)
										+ " "
										+ matcherRecordID.group(4)
										+ ":"
										+ matcherRecordID.group(5)
										+ ":"
										+ matcherRecordID.group(6)
										+ "#"
										+ matcherRecordID.group(7)
										+ "#"
										+ matcherRecordID.group(8)
										+ "#"
										+ matcherRecordID.group(9)
										+ "#"
										+ getParsingData(matcherRecordID
												.group(10))
										+ "#"
										+ parseMockDataCity(matcherRecordID
												.group(11))[0];
								pureBigData = (String[]) expand(pureBigData);
								pureBigData[inBigSize] = pData;
								inBigSize++;
							}
						}
					} catch (Exception e) {
						System.out.println(">" + e.toString());
					} finally {

						try {
							if (isr != null)
								isr.close();
							if (in != null)
								in.close();
						} catch (IOException e) {
							System.out.println("2 " + e.toString());
						}
					}
					return pureBigData;
				} catch (Exception e) {
					System.out.println("3 " + e.toString());
				}
				return null;
			}
			@Override
			protected void onPostExecute(String[] result) {
				try {
					if (isCancelled()) {
						return;
					}

					if (result != null) {
						listener.actionPerformed("Last_Updates_Data", true,
								result, null);
					} else {
						listener.actionPerformed("Last_Updates_Data", false,
								result, null);
					}
				} catch (Exception ex) {
					System.out.println("4 " + ex.toString());
				}
			}
		}.execute(null);
	}

	public static String[] parseMockData(String str) {
		return str.split("[-][.][-][   ]");
	}

	public static String[] parseMockDataSec(String str) {
		return str.split("(\\d+\\.\\d)");
	}

	public static String[] parseMockDataCity(String str) {
		return str.split("(\\s{6})");
	}

	public static String[] parseMock(String str) {
		return str.split("[#]");
	}

	public static Object expand(Object a) {
		Class cl = a.getClass();

		if (!cl.isArray()) {
			return null;
		}

		int length = Array.getLength(a);
		int newLength = length + 1; // 50% more
		Class componentType = a.getClass().getComponentType();
		Object newArray = Array.newInstance(componentType, newLength);

		System.arraycopy(a, 0, newArray, 0, length);
		return newArray;
	}

	// public static String getParsingData(String str) {
	// String mock = "";
	// Pattern patternRecordID = Pattern.compile("(\\d+\\.\\d)");
	// Matcher matcherRecordID = patternRecordID.matcher(str);
	// while (matcherRecordID.find()) {
	// mock = matcherRecordID.group(0);
	// }
	// return mock;
	// }

	public static String getParsingData(String str) {
		String dd = str.substring(0, 15);
		String mock = "";
		try {
			String a = parseMockData(dd)[0].trim();
			String b = parseMockData(dd)[1].trim();
			String c = parseMockData(dd)[2].trim();
			if (a.compareTo("") != 0)
				mock = a + "#MD";
			else if (b.compareTo("") != 0)
				mock = b + "#ML";
			else if (c.compareTo("") != 0)
				mock = c + "#MS";
		} catch (Exception e) {
			String a = parseMockDataSec(dd)[0].trim();
			mock = a + "#ML";
		}

		return mock;
		// 2.6 2.5 -.-
	}
}
