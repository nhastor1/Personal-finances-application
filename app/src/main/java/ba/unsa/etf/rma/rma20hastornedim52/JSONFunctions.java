package ba.unsa.etf.rma.rma20hastornedim52;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONFunctions {

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            //
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                //
            }
        }
        return sb.toString();
    }
}
