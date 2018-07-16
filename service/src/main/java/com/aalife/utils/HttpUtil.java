package com.aalife.utils;

import com.aalife.exception.BizException;
import com.aalife.service.UserActionLogService;
import com.aalife.service.UserLoginService;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @auther brother lu
 * @date 2018-06-06
 */
public class HttpUtil {

    private HttpUtil(){}
    private static Logger logger = Logger.getLogger(HttpUtil.class);
    /**
     * 通用get请求
     * @param url
     * @return
     * @throws IOException
     */
    public static String doGet(String url){
        try {
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            // 读取服务器返回过来的json字符串数据
            String body = EntityUtils.toString(response.getEntity());
            logger.info(url+" response:{status:"+response.getStatusLine().getStatusCode()+", body:"+body+"}");
            return body;
        } catch (IOException e){
            logger.error("request "+url+" failed", e);
            throw new BizException(e);
        }
    }

    /**
     * post请求(用于key-value格式的参数) 
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, Map params){
        try {
            // 定义HttpClient
            HttpClient client = new DefaultHttpClient();
            // 实例化HTTP方法
            HttpPost request = new HttpPost();
            request.setURI(new URI(url));
            //设置参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String value = String.valueOf(params.get(name));
                nvps.add(new BasicNameValuePair(name, value));
            }
            request.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity);
            logger.info(url+" response:{status:"+code+", body:"+body+"}");
            return body;
        } catch (IOException e){
            logger.error("request "+url+" failed", e);
            throw new BizException(e);
        } catch (URISyntaxException e) {
            logger.error("request " + url + " failed", e);
            throw new BizException(e);
        }
    }

    /**
     * post请求（用于请求json格式的参数） 
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, String params){
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            // 创建httpPost
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Type", "application/json");
            String charSet = "UTF-8";
            StringEntity entity = new StringEntity(params, charSet);
            httpPost.setEntity(entity);
            CloseableHttpResponse response =  httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int code = status.getStatusCode();
            HttpEntity responseEntity = response.getEntity();
            String body = EntityUtils.toString(responseEntity);
            logger.info(url+" response:{status:"+code+", body:"+body+"}");
            return body;
        } catch (ClientProtocolException e) {
            logger.error("request " + url + " failed", e);
            throw new BizException(e);
        } catch (IOException e) {
            logger.error("request " + url + " failed", e);
            throw new BizException(e);
        }
    }
}
