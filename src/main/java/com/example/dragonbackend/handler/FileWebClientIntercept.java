package com.example.dragonbackend.handler;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.UUID;

public class FileWebClientIntercept implements WebClientIntercept {

    //private static final Logger logger = LoggerFactory.getLogger(FileWebClientIntercept.class);

    PrintStream printStream;

    public FileWebClientIntercept() {
        try {
            var file = Path.of(String.format("testData/%s.json", UUID.randomUUID())).toFile();
            Path.of("testData").toFile().mkdirs();
            printStream = new PrintStream(file);
        } catch (FileNotFoundException ignored) {
        }
    }

    @Override
    public void write(MethodName methodName, String req, String resp) {
        if (printStream != null) {
            printStream.printf("{ \"event\": \"%s\", \"request\": %s, \"response\": %s }, %n",
                    methodName, req, resp);
        }
    }
}
