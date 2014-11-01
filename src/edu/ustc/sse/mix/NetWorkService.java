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
	
	public static int NET_OK = 1;				// 正常访问因特网状态
    public static int NET_TIMEOUT = 2;			// 无法访问因特网状态
    public static int NET_NOT_PREPARED = 3;		// 网络未准备好
    public static int NET_ERROR = 4;			// 网络异常
    private static int TIMEOUT = 1000*5;		// 超时时间
    
    /**
     * 返回当前网络状态
     * 
     * @param context		上下文环境
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
     * ping百度地址
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
	 * 判断当前网络是否是2G网络
	 * 
	 * @param context		上下文环境
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
	 * 判断当前网络是否是3G网络
	 * 
	 * @param context		上下文环境
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
	 * 判断当前网络是否是WIFI网络
	 * 
	 * @param context		上下文环境
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
	 * WIFI是否打开
	 * 
	 * @param context		上下文环境
	 * @return
	 */
	public static boolean isWifiEnabled(Context context) {
		
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
		return ((connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) 
				|| telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}
	
	/**
	 * 获得本机IP地址
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
	 * 获取本机串号IMEI
	 * 
	 * @param context		上下文环境
	 * @return
	 */
	public static String getIMEI(Context context) {
		
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
		return telephonyManager.getDeviceId();
	}
}
