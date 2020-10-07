package resource.utility.webdrivers;

/**
 * Created by vinh.ly on 2/14/2019.
 */

import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class PerfectoLabUtils {

    private static final String HTTPS = "https://";
    private static final String MEDIA_REPOSITORY = "/services/repositories/media/";
    private static final String UPLOAD_OPERATION = "operation=upload&overwrite=true";
    private static final String UTF_8 = "UTF-8";

    /**
     * Uploads a file to the media repository.
     * Example:
     * uploadFileToRepo("demo.perfectomobile.com", "john@perfectomobile.com", "123456", "C:\\test\\ApiDemos.apk", "PRIVATE:apps/ApiDemos.apk");
     */
    public static void uploadFileToRepo(String host, String securityToken, String path, String repositoryKey) throws IOException {
        try {
            File file = new File(path);
            byte[] content = readFile(file);
            uploadFile(host, securityToken, content, repositoryKey);
        } catch (RuntimeException ex) {

        }
    }

    /**
     * Uploads content to the media repository.
     * Example:
     * uploadFile("demo.perfectomobile.com", "john@perfectomobile.com", "123456", content, "PRIVATE:apps/ApiDemos.apk");
     */
    public static void uploadFile(String host, String securityToken, byte[] content, String repositoryKey) throws IOException {
        if (content != null) {
            String encodedSecurityToken = URLEncoder.encode(securityToken, "UTF-8");
            String urlStr = HTTPS + host + MEDIA_REPOSITORY + repositoryKey + "?" + UPLOAD_OPERATION + "&securityToken=" + encodedSecurityToken;
            URL url = new URL(urlStr);

            sendRequest(content, url);
        }
    }

    public static void deleteFileOnMobileDevice(RemoteWebDriver webDriver, String handsetFile) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("handsetFile", handsetFile);
        webDriver.executeScript("mobile:media:delete", params);
    }

    public static void copyFileToMobileDevice(RemoteWebDriver webDriver, String repositoryFile, String handsetFile) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("repositoryFile", repositoryFile);
        params.put("handsetFile", handsetFile);
        webDriver.executeScript("mobile:media:put", params);
    }

    private static void sendRequest(byte[] content, URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        connection.connect();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(content);
        outStream.writeTo(connection.getOutputStream());
        outStream.close();
        int code = connection.getResponseCode();
        if (code > HttpURLConnection.HTTP_OK) {
            handleError(connection);
        }
    }

    private static void handleError(HttpURLConnection connection) throws IOException {
        String msg = "Failed to upload media.";
        InputStream errorStream = connection.getErrorStream();
        if (errorStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(errorStream, UTF_8);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            try {
                StringBuilder builder = new StringBuilder();
                String outputString;
                while ((outputString = bufferReader.readLine()) != null) {
                    if (builder.length() != 0) {
                        builder.append("\n");
                    }
                    builder.append(outputString);
                }
                String response = builder.toString();
                msg += "Response: " + response;
            } finally {
                bufferReader.close();
            }
        }
        throw new RuntimeException(msg);
    }

    private static byte[] readFile(File path) throws IOException {
        int length = (int) path.length();
        byte[] content = new byte[length];
        InputStream inStream = new FileInputStream(path);
        try {
            inStream.read(content);
        } finally {
            inStream.close();
        }
        return content;
    }
}
