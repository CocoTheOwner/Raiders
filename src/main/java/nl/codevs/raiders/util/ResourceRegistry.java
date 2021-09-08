package nl.codevs.raiders.util;

import com.google.gson.Gson;
import nl.codevs.raiders.Raiders;
import nl.codevs.raiders.decree.util.KList;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public interface ResourceRegistry<T> {

    String dir();

    Class<T> type();

    String nameFrom(T object);

    default String path() {
        return Raiders.instance.getDataFolder() + "/" + dir() + "/";
    }

    default boolean saveResource(T object, String fileName) {
        File file = new File(path() + fileName + ".json");
        file.getParentFile().mkdirs();
        try {
            new FileWriter(file).write(new Gson().toJson(object));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    default T loadResource(String name, T ifNotExists) {
        File file = new File(path() + name + ".json");
        if (!file.exists()) {
            return ifNotExists;
        }
        try {
            return new Gson().fromJson(new FileReader(file), type());
        } catch (IOException e){
            return null;
        }
    }

    default KList<T> loadAllResources() {
        Gson g = new Gson();
        KList<T> resources = new KList<>();
        File dir = new File(path());
        if (dir.mkdirs() || !dir.isDirectory()) {
            return new KList<>();
        }
        for (File file : dir.listFiles()){
            try {
                if (file.isFile() && file.getPath().endsWith(".json")) {
                    resources.add(g.fromJson(new FileReader(file), type()));
                }
            } catch (FileNotFoundException ignored) {

            }
        }
        return resources;
    }

    default void saveAll(KList<T> elements, Function<T, String> toName) {
        elements.forEach(e -> saveResource(e, toName.apply(e)));
    }
}
