package com.nata.xdroid.Hooks;

import android.app.PendingIntent;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/11/21.
 */

public class GPSLocationHook implements Hook {
    public void hook(ClassLoader loader) {
        findAndHookMethod("android.net.wifi.WifiManager", loader, "getScanResults", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("afterHookedMethod: " + "getScanResults");
                param.setResult(null);
            }
        });

        findAndHookMethod("android.telephony.TelephonyManager", loader, "getCellLocation", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("afterHookedMethod: " + "getCellLocation");
                param.setResult(null);
            }
        });

        findAndHookMethod("android.telephony.TelephonyManager", loader, "getNeighboringCellInfo", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("afterHookedMethod: " + "getNeighboringCellInfo");
                param.setResult(null);
            }
        });

        findAndHookMethod("android.location.LocationManager", loader, "requestLocationUpdates", String.class, long.class, float.class, LocationListener.class,Looper.class,
                new XC_MethodHook() {
                    /**
                     * android.location.LocationManager类的requestLocationUpdates方法
                     * 其参数有4个：
                     * String provider, long minTime, float minDistance,LocationListener listener
                     * Register for location updates using the named provider, and a pending intent
                     */
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("beforeHookedMethod: " + "requestLocationUpdates");
                        //位置监听器,当位置改变时会触发onLocationChanged方法
                        LocationListener ll = (LocationListener) param.args[3];

                        Class<?> clazz = LocationListener.class;
                        Method m = null;
                        for (Method method : clazz.getDeclaredMethods()) {
                            if (method.getName().equals("onLocationChanged")) {
                                m = method;
                                break;
                            }
                        }

                        try {
                            if (m != null) {
                                Object[] args = new Object[1];
                                Location l = new Location(LocationManager.GPS_PROVIDER);
                                //台北经纬度:121.53407,25.077796
                                double la = 121.53407;
                                double lo = 25.077796;
                                l.setLatitude(la);
                                l.setLongitude(lo);
                                args[0] = l;
                                m.invoke(ll, args);
                                XposedBridge.log("fake location: " + la + ", " + lo);
                            }
                        } catch (Exception e) {
                            XposedBridge.log(e);
                        }
                    }
                });

        findAndHookMethod("android.location.LocationManager", loader, "requestLocationUpdates", String.class, long.class, float.class, LocationListener.class,
                new XC_MethodHook() {
                    /**
                     * android.location.LocationManager类的requestLocationUpdates方法
                     * 其参数有4个：
                     * String provider, long minTime, float minDistance,LocationListener listener
                     * Register for location updates using the named provider, and a pending intent
                     */
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("beforeHookedMethod: " + "requestLocationUpdates");

                        //位置监听器,当位置改变时会触发onLocationChanged方法
                        LocationListener ll = (LocationListener) param.args[3];

                        Class<?> clazz = LocationListener.class;
                        Method m = null;
                        for (Method method : clazz.getDeclaredMethods()) {
                            if (method.getName().equals("onLocationChanged")) {
                                m = method;
                                break;
                            }
                        }

                        try {
                            if (m != null) {
                                Object[] args = new Object[1];
                                Location l = new Location(LocationManager.GPS_PROVIDER);
                                //台北经纬度:121.53407,25.077796
                                double la = 121.53407;
                                double lo = 25.077796;
                                l.setLatitude(la);
                                l.setLongitude(lo);
                                args[0] = l;
                                m.invoke(ll, args);
                                XposedBridge.log("fake location: " + la + ", " + lo);
                            }
                        } catch (Exception e) {
                            XposedBridge.log(e);
                        }
                    }
                });

        findAndHookMethod("android.location.LocationManager", loader, "getGpsStatus", GpsStatus.class,
                new XC_MethodHook() {
                    /**
                     * android.location.LocationManager类的getGpsStatus方法
                     * 其参数只有1个：GpsStatus status
                     * Retrieves information about the current status of the GPS engine.
                     * This should only be called from the {@link GpsStatus.Listener#onGpsStatusChanged}
                     * callback to ensure that the data is copied atomically.
                     *
                     */
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("afterHookedMethod: " + "getGpsStatus");
                        GpsStatus gss = (GpsStatus) param.getResult();
                        if (gss == null)
                            return;

                        Class<?> clazz = GpsStatus.class;
                        Method m = null;
                        for (Method method : clazz.getDeclaredMethods()) {
                            if (method.getName().equals("setStatus")) {
                                if (method.getParameterTypes().length > 1) {
                                    m = method;
                                    break;
                                }
                            }
                        }
                        m.setAccessible(true);
                        //make the apps belive GPS works fine now
                        int svCount = 5;
                        int[] prns = {1, 2, 3, 4, 5};
                        float[] snrs = {0, 0, 0, 0, 0};
                        float[] elevations = {0, 0, 0, 0, 0};
                        float[] azimuths = {0, 0, 0, 0, 0};
                        int ephemerisMask = 0x1f;
                        int almanacMask = 0x1f;
                        //5 satellites are fixed
                        int usedInFixMask = 0x1f;
                        try {
                            if (m != null) {
                                m.invoke(gss, svCount, prns, snrs, elevations, azimuths, ephemerisMask, almanacMask, usedInFixMask);
                                param.setResult(gss);
                            }
                        } catch (Exception e) {
                            XposedBridge.log(e);
                        }
                    }
                });


    }
}
