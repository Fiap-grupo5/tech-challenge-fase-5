package br.com.fiap.tech.facility.util;

import org.apache.commons.text.StringEscapeUtils;

/**
 * Utilitário para sanitização de strings para evitar ataques como XSS, SQL Injection, etc.
 */
public class StringSanitizer {
    
    /**
     * Sanitiza uma string para evitar ataques XSS.
     * Remove scripts e tags HTML.
     * 
     * @param input a string a ser sanitizada
     * @return a string sanitizada
     */
    public static String sanitizeForXss(String input) {
        if (input == null) {
            return null;
        }
        
        // Escapar HTML
        String escaped = StringEscapeUtils.escapeHtml4(input);
        
        // Remover scripts
        escaped = escaped.replaceAll("(?i)<script.*?>.*?</script.*?>", "");
        escaped = escaped.replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "");
        escaped = escaped.replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "");
        
        return escaped;
    }
    
    /**
     * Sanitiza uma string para evitar SQL Injection.
     * Remove caracteres potencialmente perigosos para SQL.
     * 
     * @param input a string a ser sanitizada
     * @return a string sanitizada
     */
    public static String sanitizeForSql(String input) {
        if (input == null) {
            return null;
        }
        
        // Remover caracteres potencialmente perigosos para SQL
        return input.replaceAll("'", "''")
                .replaceAll(";", "")
                .replaceAll("--", "")
                .replaceAll("/\\*", "")
                .replaceAll("\\*/", "");
    }
    
    /**
     * Sanitiza uma string para uso geral.
     * Combina sanitização XSS e SQL.
     * 
     * @param input a string a ser sanitizada
     * @return a string sanitizada
     */
    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        
        return sanitizeForSql(sanitizeForXss(input));
    }
} 