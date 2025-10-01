package com.pinguela.rentexpres.util;

/**
 * Utilidades para enriquecer los mensajes de log con información de contexto.
 */
public final class LogUtils {
        private LogUtils() {
        }

        public static String buildMessage(Class<?> clazz, String message) {
                return buildMessage(clazz, resolveMethodName(clazz), message);
        }

        public static String buildMessage(Class<?> clazz, String methodName, String message) {
                StringBuilder builder = new StringBuilder();
                builder.append('[')
                                .append(clazz.getSimpleName())
                                .append('#')
                                .append(methodName)
                                .append("] ")
                                .append(message);
                return builder.toString();
        }

        private static String resolveMethodName(Class<?> clazz) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                for (StackTraceElement element : stackTrace) {
                        if (clazz.getName().equals(element.getClassName())) {
                                return element.getMethodName();
                        }
                }
                return "unknown";
        }
}
