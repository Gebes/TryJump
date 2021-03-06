package eu.gebes.tryjump.desktop.map;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;


@FieldDefaults(level = AccessLevel.PRIVATE)
public class MapManagment {
    String[] maps;
    final String home = System.getProperty("user.home");
    final File FILE_DIRECTORY = new File( home + "\\AppData\\Roaming\\.tryjump\\");
    final File FILE_NAME = new File(FILE_DIRECTORY + "\\maps.txt");

    @SneakyThrows
    @PostConstruct
    public String[] load() {

        try {
            BufferedReader in  = new BufferedReader(new FileReader(FILE_NAME));

            String[] tmp = new String[100];
            int count=0;
            while ((tmp[count] = in.readLine()) != null) {
                count++;
            }

            maps = new String[count];
            for(int i =0;i<count;i++){
                maps[i]=tmp[i];
            }
            in.close();
        } catch (FileNotFoundException e) {
            new File( home + "\\AppData\\Roaming\\.tryjump\\maps\\").mkdirs();
            List<String> lines = Arrays.asList("Jump1:10000", "Jump2:10000", "Jump3:10000");
            Files.write(Paths.get(String.valueOf(FILE_NAME)),
                    lines
                    ,StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            load();
        } catch (IOException e) {
            throw new RuntimeException("Error initializing stream", e);
        }

        return maps;
    }
}