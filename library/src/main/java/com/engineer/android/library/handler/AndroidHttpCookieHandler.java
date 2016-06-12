package com.engineer.android.library.handler;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by L.J on 2016/6/4.
 */
public final class AndroidHttpCookieHandler {
    private static AndroidHttpCookieHandler instance = new AndroidHttpCookieHandler();

    public static AndroidHttpCookieHandler getInstance(){
        return instance;
    }

    private CookieManager cookieManager;
    private AndroidHttpCookieHandler(){
        this.cookieManager = new CookieManager();
    }

    public String loadCookie(String uri){
        StringBuilder cookieBuilder = new StringBuilder();
        List<HttpCookie> httpCookieList = this.cookieManager.getCookieStore().get(URI.create(uri));
        for(HttpCookie httpCookie:httpCookieList){
            cookieBuilder.append(httpCookie.getName()).append("=").append(httpCookie.getValue()).append(";");
        }
        return cookieBuilder.deleteCharAt(cookieBuilder.length() - 1).toString();
    }

    public boolean saveCookie(String uri, Map<String,List<String>> responseHeaders){
        try {
            this.cookieManager.put(URI.create(uri),responseHeaders);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
