package com.ss.schedulesys.web.rest.util;

import org.springframework.http.HttpHeaders;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for HTTP headers creation.
 */
@Slf4j
public final class HeaderUtil {


    private HeaderUtil() {}

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-schedulesys-alert", message);
        headers.add("X-schedulesys-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("A new " + entityName + " is created with identifier " + param, param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("A " + entityName + " is updated with identifier " + param, param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("A " + entityName + " is deleted with identifier " + param, param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        log.error("Entity creation failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-schedulesys-error", defaultMessage);
        headers.add("X-schedulesys-params", entityName);
        return headers;
    }
}