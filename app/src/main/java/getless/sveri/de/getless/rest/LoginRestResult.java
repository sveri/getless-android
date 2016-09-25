package getless.sveri.de.getless.rest;

/**
 * Created by sveri on 21.09.16.
 */

public class LoginRestResult extends RestResult{

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
