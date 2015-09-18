package cn.ryon.manager.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2015/9/15.
 */
public class AsyncHttp {
    private int connectTimeOut = 60000;
    private int readTimeOut = 60000;
    public void get(String url){
        get(url,null);
    }
    public void get(String strUrl , AsyncHttpHandleListener listener){
        execute(strUrl,null,listener,"GET");
    }
    public void post(String url){
        post(url, null, null);
    }
    public void post(String url , Map<Object,Object> map){
        post(url,map,null);
    }
    public void post(String strUrl , Map<Object,Object> map , AsyncHttpHandleListener listener){
        execute(strUrl,map,listener,"POST");
    }
    public void execute(String strUrl , Map<Object,Object> map,AsyncHttpHandleListener listener,String method) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(connectTimeOut);
            connection.setReadTimeout(readTimeOut);
            connection.connect();
            int resultCode = connection.getResponseCode();
            if (resultCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
