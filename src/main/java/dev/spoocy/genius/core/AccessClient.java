package dev.spoocy.genius.core;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class AccessClient extends Client {

    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final String USER_AGENT;
    private final String CALLBACK_URI;

    private OkHttpClient httpClient;

    public AccessClient(GeniusClientBuilder builder) {
        this.status = ClientStatus.STARTING;
        this.CLIENT_ID = builder.getClientId();
        this.CLIENT_SECRET = builder.getClientSecret();
        this.USER_AGENT = builder.getUserAgent();
        this.CALLBACK_URI = builder.getCallbackUrl();

        if(CLIENT_ID == null || CLIENT_SECRET == null) {
            this.status = ClientStatus.CLOSED;
            throw new IllegalArgumentException("Client ID and Client Secret must be provided in the GenuisClientBuilder.");
        }

        this.status = ClientStatus.WAITING;
    }

    @Nullable
    @Override
    public InputStream execute(@NotNull String apiEndpoint) throws IOException {
        checkReady(true);
        Request request = new Request.Builder()
                .url(API_BASE + apiEndpoint)
                .get()
                .build();

        Response response = httpClient.newCall(request).execute();

        if(response.body() == null) {
            return null;
        }

        return response.body().byteStream();
    }

    @Override
    public String getClientId() {
        return this.CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return this.CLIENT_SECRET;
    }

    @Override
    public String getCallbackUrl() {
        return this.CALLBACK_URI;
    }

    @Override
    public String getUserAgent() {
        return this.USER_AGENT;
    }

    @Override
    public String buildAuthorizationUrl(@NotNull String secretState) {
        checkReady(false);
        // not needed
        return "null";
    }

    @Override
    public void setAuthorizationCode(@NotNull String code) {
        checkReady(false);
        this.httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .addHeader("User-Agent", this.USER_AGENT)
                            .addHeader("Authorization", "Bearer " + code);
                    return chain.proceed(requestBuilder.build());
                }).build();
        this.status = ClientStatus.READY;
    }

    @Override
    public void close() {
        this.status = ClientStatus.CLOSED;
        httpClient = null;
    }
}
