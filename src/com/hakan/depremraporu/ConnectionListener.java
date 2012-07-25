package com.hakan.depremraporu;
public interface ConnectionListener<T, V> {
	public void actionPerformed(String actionCommand, boolean sucsessfull,
			T[] data, V data2);
}