package getless.sveri.de.getless.rest;

import android.content.Context;
import android.util.Log;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;

import getless.sveri.de.getless.pojo.Weight;

//import cz.msebera.android.httpclient.Header;

/**
 * Created by sveri on 19.09.16.
 */
public class GetlessUsage {

    public void addWeight(final String token, final RestResult result, Context context, Date date, float weight) throws JSONException, UnsupportedEncodingException {
        Log.d(getClass().getName(), "Start calling add weight");

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("date", date.getTime());
        jsonParams.put("weight", weight);
        StringEntity entity = new StringEntity(jsonParams.toString());


        GetlessClient.postWithToken(context, "weight", entity, token, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                result.setResult(true);
                result.setStatusCode(statusCode);
                Log.d(getClass().getName(), "Added weight successfully");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                result.setResult(false);
                result.setStatusCode(statusCode);
                Log.d(getClass().getName(), "Error adding weight: " + errorResponse);
            }

        });

    }

    public void login(String username, String password, final LoginRestResult result, Context context) throws UnsupportedEncodingException, JSONException {

        Log.d(getClass().getName(), "Start calling login");

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("username", username);
        jsonParams.put("password", password);
        StringEntity entity = new StringEntity(jsonParams.toString());

        GetlessClient.post(context, "login", entity, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                result.setResult(true);
                try {
                    Log.d(getClass().getName(), "Logged in ");
                    result.setToken(response.getString("token"));
                } catch (JSONException e) {
                    result.setResult(false);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(getClass().getName(), "Error logging in: " + errorResponse);
                result.setResult(false);
            }
        });

    }

    public void getWeights(final WeightsRestResult restResult, String getlessToken) {
        Log.d(getClass().getName(), "Start calling get weight");

        GetlessClient.get("weight", getlessToken, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    final Gson gson = new GsonBuilder().setFieldNamingStrategy(new WeightedAtNamingStrategy()).create();
                    Weight[] weights = gson.fromJson(response.toString(), Weight[].class);
                    restResult.setWeights(Arrays.asList(weights));
                    Log.d(getClass().getName(), "Get weight successfully");
                } catch (Exception e) {
                    Log.d(getClass().getName(), "Error getting weight");
                    System.out.println(e);
                    restResult.setResult(false);
                }
                restResult.setStatusCode(statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                restResult.setResult(false);
                restResult.setStatusCode(statusCode);
                Log.d(getClass().getName(), "Error getting weight: " + errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                restResult.setResult(false);
                restResult.setErrorMessage(responseString);
                restResult.setStatusCode(statusCode);
                Log.d(getClass().getName(), "Error getting weight: " + responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                restResult.setResult(false);
                restResult.setErrorMessage(errorResponse.toString());
                restResult.setStatusCode(statusCode);
            }
        });
    }


    private class WeightedAtNamingStrategy implements FieldNamingStrategy {

        @Override
        public String translateName(Field field) {
            if (field.getName().equals("weightedAt")) {
                return "weighted_at";
            }
            return field.getName();
        }


    }
}
