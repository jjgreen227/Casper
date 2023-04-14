package dev.casperbot.util.file;

import dev.casperbot.util.*;

import java.io.*;

public class CasperFile implements Serializable {

    public CasperFile(String name) {
        File file = new File(name);
        if (file.exists()) {
            CasperConstants.fine("File " + name + " has been loaded.");
            return;
        }
        CasperConstants.warning("File " + name + " does not exist. Creating file...");
        try {
            file.createNewFile();
            CasperConstants.fine("Created file");
        } catch (IOException e) {
            e.printStackTrace();
            CasperConstants.severe("Failed to create file");
        }
    }

}
