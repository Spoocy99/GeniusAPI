/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

module GeniusAPI {
    requires org.jetbrains.annotations;
    requires dev.spoocy.utils.common;
    requires dev.spoocy.utils.config;
    requires reactor.core;
    requires reactor.netty.http;
    requires reactor.netty.core;
    requires org.json;
    requires org.reactivestreams;
    requires java.desktop;
    requires org.jsoup;
    requires io.netty.transport;
    requires io.netty.codec.http;
    requires io.netty.codec.http2;
    requires io.netty.buffer;

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
    exports dev.spoocy.genius.core.http.internal;
}