package com.mycompany.visitormanagementsystem.config;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class AppConfig {
    private static final Map<String, String> DOT_ENV = loadDotEnv();

    private AppConfig() {
    }

    public static String databaseUrl() {
        return required("DB_URL");
    }

    public static String databaseUsername() {
        return required("DB_USERNAME");
    }

    public static String databasePassword() {
        return required("DB_PASSWORD");
    }

    public static String adminUsername() {
        return required("ADMIN_USERNAME");
    }

    public static String adminPassword() {
        return required("ADMIN_PASSWORD");
    }

    private static String required(String key) {
        String value = System.getenv(key);
        if (value == null) {
            value = DOT_ENV.get(key);
        }
        if (value == null || value.isBlank()) {
            if ("DB_PASSWORD".equals(key)) {
                return "";
            }
            throw new IllegalStateException(
                    "Missing required configuration " + key + ". Set it in the project .env file or environment.");
        }
        return value;
    }

    private static Map<String, String> loadDotEnv() {
        Map<String, String> values = new HashMap<>();
        Path configuredFile = configuredEnvFile();
        if (configuredFile != null) {
            return readDotEnv(configuredFile, values);
        }

        Path envFile = findDotEnv();
        return envFile == null ? values : readDotEnv(envFile, values);
    }

    private static Path configuredEnvFile() {
        String configuredPath = System.getProperty("visitor.management.env");
        if (configuredPath == null || configuredPath.isBlank()) {
            return null;
        }
        Path envFile = Path.of(configuredPath).toAbsolutePath().normalize();
        if (!Files.isRegularFile(envFile)) {
            throw new IllegalStateException("Configured .env file does not exist: " + envFile);
        }
        return envFile;
    }

    private static Path findDotEnv() {
        try {
            Path classesDirectory = Path.of(
                    AppConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            Path envFile = findDotEnvFrom(classesDirectory);
            if (envFile != null) {
                return envFile;
            }
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Could not locate the application classes to find .env.", e);
        }
        return findDotEnvFrom(Path.of(System.getProperty("user.dir")));
    }

    private static Path findDotEnvFrom(Path location) {
        Path directory = Files.isDirectory(location) ? location : location.getParent();
        while (directory != null) {
            Path candidate = directory.resolve(".env");
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
            directory = directory.getParent();
        }
        return null;
    }

    private static Map<String, String> readDotEnv(Path envFile, Map<String, String> values) {
        try {
            for (String line : Files.readAllLines(envFile)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                int separator = trimmed.indexOf('=');
                if (separator < 1) {
                    throw new IllegalStateException("Invalid .env entry in " + envFile + ": " + trimmed);
                }

                String key = trimmed.substring(0, separator).trim();
                String value = trimmed.substring(separator + 1).trim();
                if (value.length() >= 2
                        && ((value.startsWith("\"") && value.endsWith("\""))
                        || (value.startsWith("'") && value.endsWith("'")))) {
                    value = value.substring(1, value.length() - 1);
                }
                values.put(key, value);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read .env file at " + envFile, e);
        }
        return values;
    }
}
