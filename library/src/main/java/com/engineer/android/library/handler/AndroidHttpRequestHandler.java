package com.engineer.android.library.handler;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * Created by L.J on 2016/6/10.
 */
public final class AndroidHttpRequestHandler {
    private static AndroidHttpRequestHandler instance = new AndroidHttpRequestHandler();

    public static AndroidHttpRequestHandler getInstance() {
        return instance;
    }

    private ConcurrentHashMap<String,OnHttpRequestResponseListener> listenerMap;
    private RequestProcessThread requestProcessThread;
    private AndroidHttpRequestHandler() {
        this.listenerMap = new ConcurrentHashMap<>();
        this.requestProcessThread = new RequestProcessThread();
        this.requestProcessThread.start();
    }

    public void shutDown() {
        if(this.requestProcessThread != null) {
            this.requestProcessThread.stop = true;
            this.requestProcessThread.addRequest(new HttpRequest());
            this.requestProcessThread.executorService.shutdown();
        }
    }

    public boolean addListener(String listenerKey,OnHttpRequestResponseListener listener) {
        return this.listenerMap.put(listenerKey, listener) == null;
    }

    public void removeListener(String listenerKey){
        this.listenerMap.remove(listenerKey);
    }

    public void useMultiThreadProcessing(boolean aBoolean) {
        this.requestProcessThread.isProcessWithMultiThread = aBoolean;
    }

    public void httpGet(String listenerKey,String url,Map<String,String> param,Map<String,String> requestProperty) {
        this.requestProcessThread.addRequest(new HttpRequest(listenerKey,url,"GET",false,param,requestProperty,null));
    }

    public void httpsGet(String listenerKey, String url, InputStream certificate, Map<String,String> param, Map<String,String> requestProperty){
        this.requestProcessThread.addRequest(new HttpRequest(listenerKey,url,"GET",false,param,requestProperty,certificate));
    }

    public void httpPost(String listenerKey,String url, Map<String,String> param,Map<String,String> requestProperty) {
        this.requestProcessThread.addRequest(new HttpRequest(listenerKey,url,"POST",true,param,requestProperty,null));
    }

    public void httpsPost(String listenerKey,String url,InputStream certificate, Map<String,String> param,Map<String,String> requestProperty) {
        this.requestProcessThread.addRequest(new HttpRequest(listenerKey,url,"GET",true,param,requestProperty,certificate));
    }

    private void notifyResponse(HttpResponse response) {
        OnHttpRequestResponseListener listener = this.listenerMap.get(response.listenerKey);
        if(listener != null){
            if(response.result){
                listener.onSuccess(response.url,response.content);
            }else{
                listener.onFailure(response.url,response.error);
            }
        }
    }

    private class HttpRequest {
        private String listenerKey;
        private String url;
        private String method;
        private boolean doOutput;
        private Map<String,String> param;
        private Map<String,String> requestProperty;
        private InputStream certificate;

        public HttpRequest() {

        }

        public HttpRequest(String listenerKey, String url, String method, boolean doOutput, Map<String, String> param, Map<String, String> requestProperty, InputStream certificate) {
            this.listenerKey = listenerKey;
            this.url = url;
            this.method = method;
            this.doOutput = doOutput;
            this.param = param;
            this.requestProperty = requestProperty;
            this.certificate = certificate;
        }

        public String getSpec() throws UnsupportedEncodingException {
            if(this.method.equals("GET")){
                String param = getParamString();
                return TextUtils.isEmpty(param) ? this.url : this.url + "?" + param;
            }else{
                return this.url;
            }
        }

        public String getParamString() throws UnsupportedEncodingException {
            if(param == null || param.isEmpty()){
                return "";
            }else {
                StringBuilder paramBuilder = new StringBuilder();
                for (Map.Entry<String,String> entry:param.entrySet()) {
                    paramBuilder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(),"UTF-8")).append("&");
                }
                return paramBuilder.deleteCharAt(paramBuilder.length() - 1).toString();
            }
        }
    }

    private class HttpResponse {
        private boolean result;
        private String listenerKey;
        private String url;
        private String content;
        private String error;

        public HttpResponse(String listenerKey, String url) {
            this.listenerKey = listenerKey;
            this.url = url;
        }
    }

    public interface OnHttpRequestResponseListener {
        void onSuccess(String url,String content);
        void onFailure(String url,String error);
    }

    private class RequestProcessThread extends Thread {
        private boolean isProcessWithMultiThread;
        private boolean stop;
        private LinkedBlockingQueue<HttpRequest> requestQueue;
        private ExecutorService executorService;
        public RequestProcessThread(){
            this.requestQueue = new LinkedBlockingQueue<>();
            this.executorService = Executors.newCachedThreadPool();
        }

        public void addRequest(HttpRequest request){
            this.requestQueue.add(request);
        }

        private SSLSocketFactory getSSLSocketFactory(InputStream certificate) throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, KeyManagementException {
            if(certificate == null) {
                return HttpsURLConnection.getDefaultSSLSocketFactory();
            } else {
                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null,null);
                keyStore.setCertificateEntry("ca", CertificateFactory.getInstance("X.509").generateCertificate(certificate));
                certificate.close();

                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);

                sslContext.init(null,trustManagerFactory.getTrustManagers(),null);
                return sslContext.getSocketFactory();
            }
        }

        private HostnameVerifier getHostnameVerifier(final String host){
            return new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return hostname.equals(host) || HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname,session);
                }
            };
        }

        private HttpURLConnection getHttpUrlConnection(HttpRequest request) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
            URL url = new URL(request.getSpec());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if(url.getProtocol().equalsIgnoreCase("https")){
                ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(getSSLSocketFactory(request.certificate));
                ((HttpsURLConnection) httpURLConnection).setHostnameVerifier(getHostnameVerifier(url.getHost()));
            }
            httpURLConnection.setRequestMethod(request.method);
            httpURLConnection.setDoOutput(request.doOutput);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(15000);
            if(request.requestProperty != null && !request.requestProperty.isEmpty()){
                for (Map.Entry<String,String> entry : request.requestProperty.entrySet()){
                    httpURLConnection.addRequestProperty(entry.getKey(),entry.getValue());
                }
            }
            return httpURLConnection;
        }

        private void write(HttpURLConnection httpUrlConnection,HttpRequest request) throws IOException {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(httpUrlConnection.getOutputStream()));
            writer.println(request.getParamString());
            writer.close();
        }

        private String read(HttpURLConnection httpUrlConnection) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
            StringBuilder builder = new StringBuilder(httpUrlConnection.getContentLength());
            String line;
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
            reader.close();
            return builder.toString();
        }

        private Callable<HttpResponse> createCallable(final HttpRequest request){
            return new Callable<HttpResponse>() {
                @Override
                public HttpResponse call() throws Exception {
                    HttpResponse response = new HttpResponse(request.listenerKey,request.url);
                    try {
                        HttpURLConnection httpURLConnection = getHttpUrlConnection(request);
                        if(request.method.equals("POST")){
                            write(httpURLConnection,request);
                        }else{
                            httpURLConnection.connect();
                        }
                        if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                            response.content = read(httpURLConnection);
                            response.result = true;
                        }else{
                            response.error = httpURLConnection.getResponseMessage();
                            response.result = false;
                        }
                        httpURLConnection.disconnect();
                    } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                        e.printStackTrace();
                        response.result = false;
                        response.error = e.getLocalizedMessage();
                    }
                    return response;
                }
            };
        }

        private void processWithMultiThread() throws InterruptedException, ExecutionException {
            HttpRequest request = this.requestQueue.take();
            if(request.url != null) {
                Future<HttpResponse> httpResponseFuture = this.executorService.submit(createCallable(request));
                notifyResponse(httpResponseFuture.get());
            }
        }

        private void processWithSingleThread() throws InterruptedException {
            HttpRequest request = this.requestQueue.take();
            if(request.url != null) {
                HttpResponse response = new HttpResponse(request.listenerKey,request.url);
                try {
                    HttpURLConnection httpURLConnection = getHttpUrlConnection(request);
                    if(request.method.equals("POST")){
                        write(httpURLConnection,request);
                    }else{
                        httpURLConnection.connect();
                    }
                    if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        response.content = read(httpURLConnection);
                        response.result = true;
                    }else{
                        response.error = httpURLConnection.getResponseMessage();
                        response.result = false;
                    }
                    httpURLConnection.disconnect();
                } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                    e.printStackTrace();
                    response.result = false;
                    response.error = e.getLocalizedMessage();
                }
                notifyResponse(response);
            }
        }

        @Override
        public void run() {
            super.run();
            while (!stop) {
                try {
                    if(this.isProcessWithMultiThread){
                        processWithMultiThread();
                    }else {
                        processWithSingleThread();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
