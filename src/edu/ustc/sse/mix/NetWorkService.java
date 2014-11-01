package edu.ustc.sse.mix;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetWorkService {
	
	public static int NET_OK = 1;				// ��������������״̬
    public static int NET_TIMEOUT = 2;			// �޷�����������״̬
    public static int NET_NOT_PREPARED = 3;		// ����δ׼����
    public static int NET_ERROR = 4;			// �����쳣
    private static int TIMEOUT = 1000*5;		// ��ʱʱ��
    
    /**
     * ���ص�ǰ����״̬
     * 
     * @param context		�����Ļ���
     * @return
     */
    public static int getNetState(Context context) {
	    
    	try {
	        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        if (connectivity != null) {
		        NetworkInfo networkinfo = connectivity.getActiveNetworkInfo();
		        if (networkinfo != null) {
		            if (networkinfo.isAvailable() && networkinfo.isConnected()) {
			            if (!connectionNetwork()) {
			                return NET_TIMEOUT;
			            } else {
			                return NET_OK;
			            }
		            } else {
		            	return NET_NOT_PREPARED;
		            }
		        }
	        }
	    } catch (Exception e) {
	    	//TODO
	    }
    	
    	return NET_ERROR;
    }
    
    /**
     * ping�ٶȵ�ַ
     * 
     * @return
     */
    private static boolean connectionNetwork() {
		
    	boolean result = false;
    	
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL("http://www.baidu.com").openConnection();
			connection.setConnectTimeout(TIMEOUT);
			connection.connect();
			result = true;
		} catch (IOException e) {
			//TODO
		} finally {
			if (null != connection) {
				connection.disconnect();
			}
			connection = null;
		}
		
		return result; 
    }
    
    /**
	 * �жϵ�ǰ�����Ƿ���2G����
	 * 
	 * @param context		�����Ļ���
	 * @return boolean
	 */
	public static boolean is2G(Context context) {
		
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && (activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE
						|| activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS 
						|| activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * �жϵ�ǰ�����Ƿ���3G����
	 * 
	 * @param context		�����Ļ���
	 * @return boolean
	 */
	public static boolean is3G(Context context) {
		
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * �жϵ�ǰ�����Ƿ���WIFI����
	 * 
	 * @param context		�����Ļ���
	 * @return boolean
	 */
	public static boolean isWifi(Context context) {
		
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * WIFI�Ƿ��
	 * 
	 * @param context		�����Ļ���
	 * @return
	 */
	public static boolean isWifiEnabled(Context context) {
		
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
		return ((connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) 
				|| telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}
	
	/**
	 * ��ñ���IP��ַ
	 * 
	 * @return
	 */
	public static String getLocalHostIp() {
		
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr.hasMoreElements();) {
					InetAddress inetAddress = ipAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
			//TODO
		} catch (Exception e) {
			//TODO
		}
		
		return null;
	}
	
	/**
	 * ��ȡ��������IMEI
	 * 
	 * @param context		�����Ļ���
	 * @return
	 */
	public static String getIMEI(Context context) {
		
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
		return telephonyManager.getDeviceId();
	}
}
