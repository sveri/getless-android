package getless.sveri.de.getless.rest;

import android.content.Context;

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

import getless.sveri.de.getless.pojo.Weight;

//import cz.msebera.android.httpclient.Header;

/**
 * Created by sveri on 19.09.16.
 */
public class GetlessUsage {

    public void addWeight(final String token, final RestResult result, Context context) {

    }

    public void login(String username, String password, final LoginRestResult result, Context context) throws UnsupportedEncodingException, JSONException {
        final RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", password);

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("username", username);
        jsonParams.put("password", password);
        StringEntity entity = new StringEntity(jsonParams.toString());

        GetlessClient.post(context, "login", entity, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                result.setResult(true);
                try {
                    result.setToken(response.getString("token"));
                } catch (JSONException e) {
                    result.setResult(false);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                result.setResult(false);
            }
        });

    }

    public void getWeights(final WeightsRestResult restResult, String getlessToken) {

        GetlessClient.get("weight", getlessToken, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    final Gson gson = new GsonBuilder().setFieldNamingStrategy(new WeightedAtNamingStrategy()).create();
                    Weight[] weights = gson.fromJson(response.toString(), Weight[].class);
                    restResult.setWeights(Arrays.asList(weights));
                } catch (Exception e) {
                    System.out.println(e);
                    restResult.setResult(false);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                restResult.setResult(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                restResult.setResult(false);
                restResult.setErrorMessage(responseString);
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
