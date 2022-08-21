package dev.cheerfun.deepix.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.concurrent.ExecutorService;

@Configuration
public class HttpClientConfig {

    @Bean
    public TrustManager[] trustAllCertificates() {
        return new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null; // Not relevant.
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                // TODO Auto-generated method stub
            }
        }};
    }

    @Bean
    @Primary
    @Autowired
    public HttpClient httpClientWithOutProxy(TrustManager[] trustAllCertificates, ExecutorService httpclientExecutorService) throws NoSuchAlgorithmException, KeyManagementException {
        SSLParameters sslParams = new SSLParameters();
        sslParams.setEndpointIdentificationAlgorithm("");
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCertificates, new SecureRandom());
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(30))
                .executor(httpclientExecutorService)
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
    }

    @Autowired
    @Bean(name = "httpClientWithProxy")
    public HttpClient httpClient(TrustManager[] trustAllCertificates, ExecutorService httpclientExecutorService) throws NoSuchAlgorithmException, KeyManagementException {
        SSLParameters sslParams = new SSLParameters();
        sslParams.setEndpointIdentificationAlgorithm("");
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCertificates, new SecureRandom());
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .sslParameters(sslParams)
                .sslContext(sc)
                .executor(httpclientExecutorService)
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
    }

}
