package com.example.agagneja.newgiftingapp;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;

/**
 * Created by aagnihotri on 12/09/14.
 */
public class ConnectionUtils {

    private static final String API_HOST_STAGE = "https://stage2c7161.qa.paypal.com";

    private static final String API_PORT_STAGE = ":14262";


    private static final String X_PAYPAL_SECURITY_CONTEXT = "{\"scopes\":[\"*\"],\"subjects\":[{\"subject\":{\"id\":\"0\",\"auth_state\":\"LOGGEDIN\",\"account_number\":\"1453948006982829156\",\"auth_claims\":[\"USERNAME\",\"PASSWORD\"]}}],\"actor\":{\"id\":\"0\",\"account_number\":\"1453948006982829156\",\"auth_claims\":[\"USERNAME\",\"PASSWORD\"]}, \"global_session_id\": \"3ilsi34jsi32300Zsdkk23sdlfjlkjsd\"}";

    private final static HttpClient httpClient = getNewHttpClient();

    public static JSONObject createGift(String input) {

        String postPath = buildPathForCreateGift();
        HttpPost httpPost = new HttpPost(postPath);
        Log.d("PATH", postPath);
        HttpResponse httpResponse;
        JSONObject response = null;
        try {
            httpPost.setEntity(new StringEntity(input));
            Log.d("INPUT", input);
            httpPost.setHeader("X-PAYPAL-SECURITY-CONTEXT", X_PAYPAL_SECURITY_CONTEXT);
            Log.d("SECURITY_CONTEXT", X_PAYPAL_SECURITY_CONTEXT);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Type", "application/json");
            try {
                httpResponse = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200) {

            try {
                response = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        } else {
            try {
                Log.e("FAILED RESPONSE", EntityUtils.toString(httpResponse.getEntity()));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return response;
    }

    private static String buildPathForCreateGift() {
        StringBuilder stringBuilder = new StringBuilder(API_HOST_STAGE);
        stringBuilder.append(API_PORT_STAGE);
        stringBuilder.append(Endpoints.ENDPOINT_GIFT_CREATE);
        return stringBuilder.toString();
    }

    private static String buildPathForFundingOptions(String giftID) {
        StringBuilder stringBuilder = new StringBuilder(API_HOST_STAGE);
        stringBuilder.append(API_PORT_STAGE);
        stringBuilder.append(Endpoints.ENDPOINT_GIFT_CREATE);
        stringBuilder.append(giftID);
        stringBuilder.append(Endpoints.SUFFIX_PLAN);
        return stringBuilder.toString();
    }

    public static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            return new DefaultHttpClient(ccm, params);

        } catch (Exception e) {
            return new DefaultHttpClient();
        }

    }

    public static JSONObject fundingOptionsRequest(String id) {
        String postPath = buildPathForFundingOptions(id);
        HttpPost httpPost = new HttpPost(postPath);
        Log.d("PATH", postPath);
        HttpResponse httpResponse;
        JSONObject response = null;
        try {
            httpPost.setEntity(new StringEntity(new JSONObject().toString()));
            httpPost.setHeader("X-PAYPAL-SECURITY-CONTEXT", X_PAYPAL_SECURITY_CONTEXT);
            Log.d("SECURITY_CONTEXT", X_PAYPAL_SECURITY_CONTEXT);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Type", "application/json");
            try {
                httpResponse = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200) {

            try {
                response = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        } else {
            try {
                Log.e("FAILED RESPONSE", EntityUtils.toString(httpResponse.getEntity()));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return response;
    }


    public static JSONObject fulfillmentOptionsRequest(String id, String body) {
        String postPath = buildPathForFundingOptions(id);
        HttpPost httpPost = new HttpPost(postPath);
        Log.d("PATH", postPath);
        HttpResponse httpResponse;
        JSONObject response = null;
        try {
            httpPost.setEntity(new StringEntity(body));
            httpPost.setHeader("X-PAYPAL-SECURITY-CONTEXT", X_PAYPAL_SECURITY_CONTEXT);
            Log.d("SECURITY_CONTEXT", X_PAYPAL_SECURITY_CONTEXT);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Type", "application/json");
            try {
                httpResponse = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200) {

            try {
                response = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        } else {
            try {
                Log.e("FAILED RESPONSE", EntityUtils.toString(httpResponse.getEntity()));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return response;
    }


}
