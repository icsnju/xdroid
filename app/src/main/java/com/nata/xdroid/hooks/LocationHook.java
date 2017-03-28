package com.nata.xdroid.hooks;

import android.content.Context;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.telephony.CellLocation;

import com.nata.xdroid.notifier.CommonNotice;
import com.nata.xdroid.notifier.Notifier;
import com.nata.xdroid.notifier.ToastNotifier;
import com.nata.xdroid.utils.XPreferencesUtils;

import java.lang.reflect.Method;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/11/21.
 */

public class LocationHook implements Hook {
    Context context;
    public LocationHook(Context context) {
        this.context = context;
    }
    public void hook(ClassLoader loader) {
        findAndHookMethod("android.net.wifi.WifiManager", loader, "getScanResults", new XC_MethodHook() {
            /**
             *
             If there is a remote exception (e.g., either a communication problem with the system service or an exception within the framework) an empty list will be returned
             */
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("afterHookedMethod: " + "getScanResults");
                List<ScanResult> scanResults = (List<ScanResult>)param.getResult();
                if(scanResults.size() == 0) {
                    Notifier.notice(context, CommonNotice.WIFI_LOCATION);
                }
            }
        });

        findAndHookMethod("android.telephony.TelephonyManager", loader, "getCellLocation", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("afterHookedMethod: " + "getCellLocation");
                CellLocation cl = (CellLocation) param.getResult();
                if(cl == null) {
                    ToastNotifier.makeToast(context,CommonNotice.LTE_LOCATION);
                }
            }
        });

//        findAndHookMethod("android.telephony.TelephonyManager", loader, "getNeighboringCellInfo", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                XposedBridge.log("afterHookedMethod: " + "getNeighboringCellInfo");
//                List<NeighboringCellInfo> list = (List<NeighboringCellInfo>)param.getResult();
//                if(list.size() == 0) {
//                    ToastNotifier.makeToast(context,CommonNotice.CELL_LOCATION);
//                }
////                param.setResult(null);
//            }
//        });

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
                        LocationManager lm = (LocationManager) param.thisObject;
                        String provider = (String)param.args[0];


                        if(provider.equals(LocationManager.GPS_PROVIDER) &&  !lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Notifier.notice(context,CommonNotice.NO_GPS_PROVIDER);
                        }
                        else if(provider.equals(LocationManager.NETWORK_PROVIDER) &&  !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                            Notifier.notice(context,CommonNotice.NO_NETWORK_PROVIDER);
                        } else if(provider.equals(LocationManager.PASSIVE_PROVIDER) &&  !lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
                            Notifier.notice(context,CommonNotice.NO_PASSIVE_PROVIDER);
                        } else {
                            if(XPreferencesUtils.isFakeGps()) {
                                LocationListener ll = (LocationListener) param.args[3];
                                fackGPSLocation(ll);
                            }
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

                        if (XPreferencesUtils.isFakeGps()) {
                            GpsStatus gss = (GpsStatus) param.getResult();
                            setGPSStatus(gss,param);
                        }
                    }
                });

    }

    private void fackGPSLocation(LocationListener ll) {
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

    private void setGPSStatus(GpsStatus gss, XC_MethodHook.MethodHookParam param){
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
}
