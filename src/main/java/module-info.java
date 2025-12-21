/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

module GeniusAPI {
    requires static lombok;

    requires org.jetbrains.annotations;
    requires dev.spoocy.utils.common;
    requires dev.spoocy.utils.config;
    requires reactor.core;
    requires org.json;
    requires org.reactivestreams;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.client5.httpclient5.cache;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires java.desktop;
    requires org.jsoup;

    exports dev.spoocy.genius;
    exports dev.spoocy.genius.core;
    exports dev.spoocy.genius.core.auth;
    exports dev.spoocy.genius.core.data;
    exports dev.spoocy.genius.core.http;
    exports dev.spoocy.genius.core.http.requests;
    exports dev.spoocy.genius.core.http.u_requests;
    exports dev.spoocy.genius.core.lyrics;
    exports dev.spoocy.genius.exception;
    exports dev.spoocy.genius.model;
}