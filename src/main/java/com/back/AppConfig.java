package com.back;

public final class AppConfig {

    private static final String KEY = "app.mode";

    public static String getMode() {
        return System.getProperty(KEY, "dev"); // 기본 dev
    }

    public static void setMode(String mode) {
        System.setProperty(KEY, mode);
    }

    public static void setTestMode() { setMode("test"); }

    public static void setDevMode()  { setMode("dev"); }
}
