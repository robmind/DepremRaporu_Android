package com.hakan.depremraporu;

import java.io.Serializable;

public class ApplicationData implements Serializable {
	private static final long serialVersionUID = 9045332374558591947L;
	public static String[] reportList;
	public static String xCord;
	public static String yCord;
	public static String gDate;
	public static String gWhere;
	public static boolean isClose = false;
}
