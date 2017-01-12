package cz.zcu.qwerty2.daburujanpu.net;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class Logging {
    private static String FILE_NAME = "log.txt";
    private static FileWriter fw;
    private static SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss.SSS");
    static {
        init_file();
    }

    private static void init_file() {
        try {
            fw= new FileWriter(new File(FILE_NAME));
        } catch (IOException e) {
            System.out.println("Can't init logging to file "+FILE_NAME);
        }
    }

    public synchronized static void writeline(String s) {
        try {
            String time = df.format(new Date());
            fw.append(time + " " + s + "\n");
            fw.flush();
        } catch (IOException e) {
            System.out.println("Error writing to file "+FILE_NAME);
        }
    }
}
