/*
 * Copyright (c) 2021 Teodor G.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.teogor.modernnetwork.tcp;

import android.annotation.SuppressLint;

import com.koushikdutta.async.AsyncSSLSocketWrapper;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.Util;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class ClientSSL
{

    private final String host;
    private final int port;

    public ClientSSL(String host, int port)
    {
        this.host = host;
        this.port = port;
        setup();
    }

    private void setup()
    {
        AsyncServer.getDefault().connectSocket(new InetSocketAddress(host, port), (ex, socket) ->
        {
            try
            {
                handleConnectCompleted(ex, socket);
            } catch (NoSuchAlgorithmException | KeyManagementException nsae)
            {
                throw new RuntimeException(nsae);
            }
        });
    }

    private void handleConnectCompleted(Exception ex, final AsyncSocket socket) throws NoSuchAlgorithmException, KeyManagementException
    {
        if (ex != null) throw new RuntimeException(ex);

        //You would want to use a "real" trust manager, instead of one that ignores the certificates
        TrustManager[] trustManagers = new TrustManager[]{createTrustAllTrustManager()};
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustManagers, new SecureRandom());
        SSLEngine sslEngine = sslContext.createSSLEngine();

        AsyncSSLSocketWrapper.handshake(socket, host, port, sslEngine, trustManagers, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER, true,
                (ex1, socket1) ->
                {
                    if (ex1 != null) throw new RuntimeException(ex1);
                    socket1.setWriteableCallback(() -> Util.writeAll(socket1, "Hello TCPServer".getBytes(), ex11 ->
                    {
                        if (ex11 != null) throw new RuntimeException(ex11);
                        System.out.println("[TCPClient] Successfully wrote message");
                    }));

                    socket1.setDataCallback((emitter, bb) -> System.out.println("[TCPClient] Received Message " + new String(bb.getAllByteArray())));

                    socket1.setClosedCallback(ex113 ->
                    {
                        if (ex113 != null) throw new RuntimeException(ex113);
                        System.out.println("[TCPClient] Successfully closed connection");
                    });

                    socket1.setEndCallback(ex112 ->
                    {
                        if (ex112 != null) throw new RuntimeException(ex112);
                        System.out.println("[TCPClient] Successfully end connection");
                    });
                });
    }

    private TrustManager createTrustAllTrustManager()
    {
        return new X509TrustManager()
        {
            public X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }

            @SuppressLint("TrustAllX509TrustManager")
            public void checkClientTrusted(X509Certificate[] certs, String authType)
            {

            }

            @SuppressLint("TrustAllX509TrustManager")
            public void checkServerTrusted(X509Certificate[] certs, String authType)
            {
            }
        };
    }

}
