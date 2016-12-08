package me.vilsol.skypebot.modules.http.result;

import java.util.ArrayList;

public class Result {

    private ArrayList<String> result;
    private int               responseCode;

    public Result(ArrayList<String> result, int responseCode) {
        this.responseCode = responseCode;
        this.result = result;
    }

    /**
     * Gives information sent from server.
     *
     * @return Lines of String information returned from server.
     */
    public ArrayList<String> getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "Result{" +
                "result=" + result +
                ", responseCode=" + responseCode +
                '}';
    }

    /**
     * Gives the HTTP response code.
     *
     * @return Http response code.
     */
    public int getResponseCode() {
        return responseCode;
    }
}