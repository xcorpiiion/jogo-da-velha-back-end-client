package br.com.unip.jogodavelha.constante;

public final class Constantes {

    private Constantes() {
    }

    private static final String HEADER = "access-control-expose-headers";

    private static final String AUTHORIZATION = "Authorization";

    private static final String BEARER = "Bearer ";

    private static final String LOCATION = "location";

    public static String getHEADER() {
        return HEADER;
    }

    public static String getAUTHORIZATION() {
        return AUTHORIZATION;
    }

    public static String getBEARER() {
        return BEARER;
    }

    public static String getLOCATION() {
        return LOCATION;
    }
}
