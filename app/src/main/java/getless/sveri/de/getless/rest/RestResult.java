package getless.sveri.de.getless.rest;

/**
 * Created by sveri on 20.09.16.
 */
public class RestResult {
    private boolean result;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private String errorMessage;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

}
