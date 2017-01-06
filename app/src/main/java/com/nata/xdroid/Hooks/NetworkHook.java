package com.nata.xdroid.hooks;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nata.xdroid.notifier.Notifier;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.net.HttpURLConnection;

import de.robv.android.xposed.XC_MethodHook;

import static com.nata.xdroid.utils.XPreferencesUtils.isNetWorkConnected;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/11/21.
 */

public class NetworkHook implements Hook{
    private final int HTTP_400 = 400;
    private Context context;

    public NetworkHook(Context context) {
        this.context = context;
    }


    @Override
    public void hook(ClassLoader loader) {
        findAndHookConstructor("java.net.URL", loader, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String url = (String) param.args[0];
                log("URL construct " + url);
                if(!isNetWorkConnected()) {
                    Notifier.notice(context, "App visit" + url + ", but there is not network");
                }
            }
        });

        findAndHookMethod("java.net.HttpURLConnection", loader, "getResponseCode", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                log("afterHookedMethod getResponseCode");
                HttpURLConnection urlConn = (HttpURLConnection)param.thisObject;
                int code = (int) param.getResult();
                if(code == HTTP_400) {
                    Notifier.notice(context, "App send a request for " + urlConn.getURL() + ", but failed");
                }
            }
        });

        // API23后被废弃
        if(android.os.Build.VERSION.SDK_INT <23 ) {
            findAndHookMethod("android.net.http.AndroidHttpClient", loader, "execute",HttpUriRequest.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    log("afterHookedMethod execute");
                    HttpResponse httpResponse =(HttpResponse) param.getResult();
                    HttpUriRequest request = (HttpUriRequest) param.args[0];

                    if(httpResponse.getStatusLine().getStatusCode() == HTTP_400) {
                        Notifier.notice(context, "App send a request for " + request.getURI() + ", but failed");
                    }
                }
            });
        }

        findAndHookMethod("android.webkit.WebView", loader, "loadUrl",String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                log("afterHookedMethod loadUrl");
                final WebView webView = (WebView)param.thisObject;
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onReceivedError(WebView view, int errorCode,
                                                String description, String failingUrl) {
// TODO Auto-generated method stub
                        super.onReceivedError(view, errorCode, description, failingUrl);
                        Notifier.notice(context, "App open a webpage for" + view.getUrl() + ", but failed");
                    }
                });
            }
        });


    }
}
