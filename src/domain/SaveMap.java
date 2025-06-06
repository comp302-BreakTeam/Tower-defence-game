package domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveMap {
	public static void saveMap(Maps map, String filename) throws IOException {
		File dir = new File("maps");
        if (!dir.exists()) {
            dir.mkdir();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(dir, filename)))) {
            oos.writeObject(map);
        }
        
	}
	public static Maps loadMap(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("maps", filename)))) {
            return (Maps) ois.readObject();
        }
    }

}
