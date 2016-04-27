package aeneas.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Joseph Martin
 */
public class LevelIndex {
  HashMap<Integer, Level> levels;

  LevelIndex() {
    levels = new HashMap<>();
    reindex();
  }

  public void reindex() {
    levels.clear();

    ArrayList<Level> defaultLevels = LevelGenerator.generateDefaultLevels();
    for (int i = 0; i < defaultLevels.size(); i++) {
      Level l = defaultLevels.get(i);
      l.levelNumber = i + 1;
      levels.put(i, l);
    }

    // honestly we need to change this path.
    String homeDir = System.getenv("HOME");
    String defaultLevelPath = homeDir + "/.aeneas-kabasuji";
    Path defaultDirectory = (new File(defaultLevelPath)).toPath();

    if (!Files.exists(defaultDirectory)) {
      System.err.println("Error loading levels. Directory '" + defaultDirectory + "' does not exist.");
      return;
    }

    try {
      Files.list(defaultDirectory).filter(file -> file.getFileName().toString().endsWith(".kbs")).forEach((file) -> {
        ObjectInputStream ois = null;
        try {
          ois = new ObjectInputStream(new FileInputStream(file.toFile()));
        } catch (IOException e) {
          System.err.println("Error reading file '" + file + "'");
        }

        try {
          int n = Integer.parseInt(file.getFileName().toString().replaceFirst("[.].*$", ""));
          Level l = (Level) ois.readObject();
          l.levelNumber = n;
          levels.put(l.levelNumber, l);
        } catch (NumberFormatException e) {
          // Do something here to load custom levels
        } catch (ClassNotFoundException e) {
          // Improperly formatted file. Ignore it and move on
          e.printStackTrace();
          System.err.println("Error reading from file '" + file + "'");
        } catch (IOException e) {
          // Something went wrong reading the file. Ignore this file and move
          // on.
          e.printStackTrace();
          System.err.println("Error reading from file '" + file + "'");
        }

        // Try to close the stream
        if (ois != null) {
          try {
            ois.close();
          } catch (IOException e) {
            // Oh well. We tried.
          }
        }
      });
    } catch (NotDirectoryException e) {
      System.err.println("Error loading levels. '" + defaultDirectory + "' is not a directory");
    } catch (IOException e) {
      System.err.println("Error reading from directory '" + defaultDirectory + "'");
    }
  }

  Collection<Level> getLevels() {
    return levels.values();
  }

  Level getLevel(int i) {
    return levels.get(i);
  }
}
