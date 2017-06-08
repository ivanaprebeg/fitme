package hr.fitme.models;

/**
 * Created by ivanc on 22/05/2017.
 */

public class JwtAuthenticationResponse {

    private String token;

    public JwtAuthenticationResponse() {
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
