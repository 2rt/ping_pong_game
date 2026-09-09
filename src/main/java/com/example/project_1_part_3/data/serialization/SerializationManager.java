package com.example.project_1_part_3.data.serialization;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
public class SerializationManager {

    private static final String FILE_NAME = "pongGame.ser";
    private static SerializationManager instance;

    private SerializationManager() {}

    public static SerializationManager getInstance() {
        if (instance == null) {
            instance = new SerializationManager();
        }
        return instance;
    }

    public void saveData(List<Object> dataList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Object> loadData() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Object>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}