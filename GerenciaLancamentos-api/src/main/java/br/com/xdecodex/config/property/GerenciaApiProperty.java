package br.com.xdecodex.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "gerencia")
public class GerenciaApiProperty {

    private String originPermitida = "http://localhost:8000";

    private final S3 s3 = new S3();
    private final Mail mail = new Mail();
    private final Seguranca seguranca = new Seguranca();
    private final Logging logging = new Logging();
    private final Security security = new Security();

    public S3 getS3() {
        return s3;
    }

    public Mail getMail() {
        return mail;
    }

    public Seguranca getSeguranca() {
        return seguranca;
    }

    public Logging getLogging() {
        return logging;
    }

    public Security getSecurity() {
        return security;
    }

    public String getOriginPermitida() {
        return originPermitida;
    }

    public void setOriginPermitida(String originPermitida) {
        this.originPermitida = originPermitida;
    }

    // ======== CLASSE S3 ========
    public static class S3 {
        private String accessKeyId;
        private String secretAccessKey;
        private String region;
        private String bucket;

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getSecretAccessKey() {
            return secretAccessKey;
        }

        public void setSecretAccessKey(String secretAccessKey) {
            this.secretAccessKey = secretAccessKey;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }
    }

    // ======== CLASSE SEGURANCA ========
    public static class Seguranca {
        private List<String> redirectsPermitidos;
        private String authServerUrl;

        public List<String> getRedirectsPermitidos() {
            return redirectsPermitidos;
        }

        public void setRedirectsPermitidos(List<String> redirectsPermitidos) {
            this.redirectsPermitidos = redirectsPermitidos;
        }

        public String getAuthServerUrl() {
            return authServerUrl;
        }

        public void setAuthServerUrl(String authServerUrl) {
            this.authServerUrl = authServerUrl;
        }
    }

    // ======== CLASSE MAIL ========
    public static class Mail {
        private String host;
        private int port;
        private String username;
        private String password;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
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
    }

    // ======== CLASSE LOGGING ========
    public static class Logging {
        private final Level level = new Level();

        public Level getLevel() {
            return level;
        }

        public static class Level {
            private String brComXdecodex = "DEBUG";

            public String getBrComXdecodex() {
                return brComXdecodex;
            }

            public void setBrComXdecodex(String brComXdecodex) {
                this.brComXdecodex = brComXdecodex;
            }
        }
    }

    // ======== CLASSE SECURITY ========
    public static class Security {
        private final Clients clients = new Clients();
        private final Keystore keystore = new Keystore();

        public Clients getClients() {
            return clients;
        }

        public Keystore getKeystore() {
            return keystore;
        }

        public static class Clients {
            private final Client angular = new Client();
            private final Client mobile = new Client();

            public Client getAngular() {
                return angular;
            }

            public Client getMobile() {
                return mobile;
            }

            public static class Client {
                private String clientSecret;

                public String getClientSecret() {
                    return clientSecret;
                }

                public void setClientSecret(String clientSecret) {
                    this.clientSecret = clientSecret;
                }
            }
        }

        public static class Keystore {
            private String password;
            private String keyPassword;

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getKeyPassword() {
                return keyPassword;
            }

            public void setKeyPassword(String keyPassword) {
                this.keyPassword = keyPassword;
            }
        }
    }
}
