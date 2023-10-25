package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class TokenObject {

    @SerializedName("Value")
    private String access_token;

    /*@SerializedName("token_type")
    private String token_type;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("grant_type")
    private String grant_type;
*/

    public TokenObject(String access_token) {
        this.access_token = access_token;
    }

    /* public TokenObject(String username, String password, String grant_type) {
            this.username = username;
            this.password = password;
            this.grant_type = grant_type;
        }
    */
    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    /*public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }*/
}
