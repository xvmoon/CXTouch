/**
 * The code is written by ytx, and is confidential.
 * Anybody must not broadcast these files without authorization.
 */
package com.cxplan.projection.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.plugin2.util.SystemUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2017/4/16.
 *
 * @author kenny
 */
public class CommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    public static final String TOUCH_INPUTER = "com.cxplan.mediate/.inputer.CXTouchIME";

    public static int resolveProcessID(String content, String processName) {
        if (StringUtil.isEmpty(content)) {
            return -1;
        }
        StringTokenizer st = new StringTokenizer(content);
        int index = 0;
        String processIdString = null;
        while(st.hasMoreTokens()) {
            index++;
            String token = st.nextToken();
            if (index == 2) {
                processIdString = token;
            } else if (index == 9) {
                if (!token.equals(processName)) {
                    return -1;
                } else {
                    break;
                }
            }

        }
        if (index < 9 || processIdString == null) {
            return -1;
        }
        try {
            return Integer.parseInt(processIdString);
        } catch (Exception e) {
            logger.error("Returned string is not pid: " + processIdString);
            return -1;
        }
    }

    public static Properties loadPropertyFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        FileInputStream inputStream = null;
        try {
            Properties properties = new Properties();
            inputStream = new FileInputStream(file);
            properties.load(inputStream);

            return properties;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static byte[] int2LowEndianBytes(int value) {
        byte[] ret = new byte[4];
        ret[0] =(byte) (value & 0xFF);
        ret[1] =(byte) ((value >> 8) & 0xFF);
        ret[2] =(byte) ((value >> 16) & 0xFF);
        ret[3] =(byte) ((value >> 24) & 0xFF);

        return ret;
    }

    public static void writeIntLowEndian(int value, OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new RuntimeException("The output stream object is empty!");
        }

        byte[] data = int2LowEndianBytes(value);

        outputStream.write(data);

    }

    public static int readIntLowEndian(InputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();

        return ((ch1 << 0) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
    }

    public static int readIntUpEndian(InputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();

        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

}
