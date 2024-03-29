package it.univaq.f4i.iw.framework.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SecurityHelpers {

    // --------- SESSION SECURITY ------------
    // questa funzione esegue una serie di controlli di sicurezza
    // sulla sessione corrente. Se la sessione non è valida, la cancella
    // e ritorna null, altrimenti la aggiorna e la restituisce
    public static HttpSession checkSession(HttpServletRequest r) {
        return checkSession(r, false);
    }

    public static HttpSession checkSession(HttpServletRequest r, boolean loginAgeCheck) {
        boolean check = true;

        HttpSession s = r.getSession(false);
        // per prima cosa vediamo se la sessione è attiva
        if (s == null) {
            return null;
        }

        LocalDateTime now_ts = LocalDateTime.now();
        // inizio sessione
        LocalDateTime start_ts = (LocalDateTime) s.getAttribute("session-start-ts");
        // ultima azione
        LocalDateTime action_ts = (LocalDateTime) s.getAttribute("last-action-ts");
        if (action_ts == null) {
            action_ts = now_ts;
        }
        // ultima rigenerazione dell'ID
        LocalDateTime refresh_ts = (LocalDateTime) s.getAttribute("session-refresh-ts");
        if (refresh_ts == null) {
            refresh_ts = start_ts;
        }
        // secondi trascorsi dall'inizio della sessione
        long seconds_from_start = start_ts != null ? Duration.between(start_ts, now_ts).abs().getSeconds() : 0;
        // secondi trascorsi dall'ultima azione
        long seconds_from_action = Duration.between(action_ts, now_ts).abs().getSeconds();
        // secondi trascorsi dall'ultimo refresh della sessione
        long seconds_from_refresh = Duration.between(refresh_ts, now_ts).abs().getSeconds();
        if (s.getAttribute("userid") == null || start_ts == null) {
            // check sulla validità della sessione
            // nota: oltre a controllare se la sessione contiene un userid,
            // dovremmo anche controllere che lo userid sia valido, probabilmente
            // consultando il database utenti
            check = false;
        } else if ((s.getAttribute("ip") == null) || !((String) s.getAttribute("ip")).equals(r.getRemoteHost())) {
            // check sull'ip del client
            check = false;
        } else if (seconds_from_start > 3 * 60 * 60) {
            // dopo tre ore la sessione scade
            check = false;
        } else if (seconds_from_action > 30 * 60) {
            // dopo trenta minuti dall'ultima operazione la sessione è invalidata
            check = false;
        }
        if (!check) {
            s.invalidate();
            return null;
        } else {
            // ogni 120 secondi, rigeneriamo la sessione per cambiarne l'ID
            if (seconds_from_refresh >= 120) {
                s = regenerateSession(r);
                s.setAttribute("session-refresh-ts", now_ts);
            }
            // reimpostiamo la data/ora dell'ultima azione
            s.setAttribute("last-action-ts", now_ts);
            return s;
        }
    }

    public static HttpSession createSession(HttpServletRequest request, String username, int userid) {
        // se una sessione è già attiva, rimuoviamola e creiamone una nuova
        disposeSession(request);
        HttpSession s = request.getSession(true);
        s.setAttribute("username", username);
        s.setAttribute("userid", userid);
        s.setAttribute("ip", request.getRemoteHost());
        s.setAttribute("session-start-ts", LocalDateTime.now());
        return s;
    }

    public static void disposeSession(HttpServletRequest request) {
        HttpSession s = request.getSession(false);
        if (s != null) {
            s.invalidate();
        }
    }

    // questo metodo rigenera la sessione invalidando quella corrente e
    // creandone una nuova con gli stessi attributi. Può essere utile per
    // prevenire il session hijacking, perchè modifica il session identifier
    public static HttpSession regenerateSession(HttpServletRequest request) {
        Map<String, Object> attributes = new HashMap<>();
        HttpSession s = request.getSession(false);
        if (s != null) {
            Enumeration<String> attributeNames = s.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String key = attributeNames.nextElement();
                Object value = s.getAttribute(key);
                attributes.put(key, value);
            }
            s.invalidate();
        }
        s = request.getSession(true);
        for (String key : attributes.keySet()) {
            Object value = attributes.get(key);
            s.setAttribute(key, value);
        }
        return s;
    }

    // --------- CONNECTION SECURITY ------------
    public static String checkHttps(HttpServletRequest request) {
        // possiamo usare questa tecnica per controllare se la richiesta è
        // stata effettuata in https e, in caso contrario, costruire la URL
        // necessaria a ridirezionare il browser verso l'https
        // we can use this technique to check if the request was made in https
        // and, if not, build the URL needed to redirect the browser to https
        if (request.getScheme().equals("http")) {
            String url = "https://" + request.getServerName()
                    + ":" + request.getServerPort()
                    + request.getRequestURI() // request.getContextPath() + request.getServletPath() +
                                              // (request.getPathInfo() != null) ? request.getPathInfo() : ""
                    + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            return url;
        } else {
            return null;
        }
    }

    // --------- DATA SECURITY ------------
    // questa funzione aggiunge un backslash davanti a
    // tutti i caratteri "pericolosi", usati per eseguire
    // SQL injection attraverso i parametri delle form
    public static String addSlashes(String s) {
        return s.replaceAll("(['\"\\\\])", "\\\\$1");
    }

    // questa funzione rimuove gli slash aggiunti da addSlashes
    public static String stripSlashes(String s) {
        return s.replaceAll("\\\\(['\"\\\\])", "$1");
    }

    public static int checkNumeric(String s) throws NumberFormatException {
        // convertiamo la stringa in numero, ma assicuriamoci prima che sia valida
        if (s != null) {
            // se la conversione fallisce, viene generata un'eccezione
            return Integer.parseInt(s);
        } else {
            throw new NumberFormatException("String argument is null");
        }
    }

    public static String sanitizeFilename(String name) {
        return name.replaceAll("[^a-zA-Z0-9_.-]", "_");
    }

    // --------- PASSWORD SECURITY ------------
    // support functions for the password hashing functions
    private static String bytesToHexString(byte[] byteArray) {
        StringBuilder hexStringBuffer = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            char[] hexDigits = new char[2];
            hexDigits[0] = Character.forDigit((byteArray[i] >> 4) & 0xF, 16);
            hexDigits[1] = Character.forDigit((byteArray[i] & 0xF), 16);
            hexStringBuffer.append(new String(hexDigits));
        }
        return hexStringBuffer.toString();
    }

    private static byte[] hexStringToBytes(String hexString) {
        byte[] byteArray = new byte[hexString.length() / 2];
        for (int i = 0; i < byteArray.length; i++) {
            int val = Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16);
            byteArray[i] = (byte) val;
        }
        return byteArray;
    }

    // password hashing with SHA-512 + salt
    private static String getPasswordHashSHA(String password, byte[] salt) throws NoSuchAlgorithmException {
        if (salt.length != 16) {
            throw new IllegalArgumentException("Salt must be 16 bytes");
        }
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return bytesToHexString(salt) + bytesToHexString(digest);
    }

    // check password hashed by getPasswordHashSHA
    public static boolean checkPasswordHashSHA(String password, String passwordhash) throws NoSuchAlgorithmException {
        byte[] salt = new byte[16];
        System.arraycopy(hexStringToBytes(passwordhash), 0, salt, 0, 16);
        return getPasswordHashSHA(password, salt).equals(passwordhash);
    }

    // password hashing with SHA-512 without salt
    public static String getPasswordHashSHA(String password) throws NoSuchAlgorithmException {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return getPasswordHashSHA(password, salt);
    }

    // check the input hash with the password hash in the MySQL DB
    public static boolean checkHashSHA(String input, String hash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext.equals(hash);
    }

    // password hashing with PBKDF2 + salt
    private static String getPasswordHashPBKDF2(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (salt.length != 16) {
            throw new IllegalArgumentException("Salt must be 16 bytes");
        }
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        byte[] digest = factory.generateSecret(spec).getEncoded();
        return bytesToHexString(salt) + bytesToHexString(digest);
    }

    public static String getPasswordHashPBKDF2(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return getPasswordHashPBKDF2(password, salt);
    }

    // check password hashed by getPasswordHashPBKDF2
    public static boolean checkPasswordHashPBKDF2(String password, String passwordhash)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = new byte[16];
        System.arraycopy(hexStringToBytes(passwordhash), 0, salt, 0, 16);
        return (getPasswordHashPBKDF2(password, salt)).equals(passwordhash);
    }

}
