package getless.sveri.de.getless.rest;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.entity.StringEntity;

/**
 * Created by sveri on 19.09.16.
 */
public class GetlessClient {

//    private final static String GETLESS_URI = "http://10.0.2.2:3000/api/";
    private final static String GETLESS_URI = "http://sveri.de:3123/api/";

    private static SyncHttpClient client = new SyncHttpClient();


    public static void get(String url, String token, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", "Token " + token);
        client.get(getAbsoluteUrl(url), null, responseHandler);
    }

    public static void post(Context context, String url, StringEntity params, AsyncHttpResponseHandler responseHandler) {
        client.post(context, getAbsoluteUrl(url), params, "application/json", responseHandler);
    }

    public static void postWithToken(Context context, String url, StringEntity params, String token, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", "Token " + token);
        client.post(context, getAbsoluteUrl(url), params, "application/json", responseHandler);
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        return GETLESS_URI + relativeUrl;
    }

}
