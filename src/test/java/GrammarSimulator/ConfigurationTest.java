package GrammarSimulator;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by isabel on 19.01.17.
 */
public class ConfigurationTest {
    @Test
    public void getChildren() throws Exception {
        Grammar grammar = GrammarUtil.parse(getResourceAsFile("/Grammar/test3.gr"));
        Configuration config = GrammarUtil.getStartConfiguration(grammar);
        config.getChildren().forEach(child -> child.getChildren().forEach(con -> {
            System.out.println(con.getConfig().stream().map(sym -> sym.getName()).collect(Collectors.joining(" ")));
        }));


    }

    @Ignore
    public File getResourceAsFile(String path) {

        try {
            InputStream input = getClass().getResourceAsStream(path);
            if (input == null) {
                return null;
            }

            File res = File.createTempFile(String.valueOf(input.hashCode()), ".tmp");
            res.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(res)) {
                //copy stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}