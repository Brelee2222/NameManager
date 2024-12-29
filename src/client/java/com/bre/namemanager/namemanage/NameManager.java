package com.bre.namemanager.namemanage;

import java.io.*;
import java.util.*;

public class NameManager {
    private static String setName;

    private static final Hashtable<UUID, String> nameLogs = new Hashtable<>();

    private static String ENTITY_NAMES_PATH = "entityNames.txt";

    public static void loadNameLogs() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ENTITY_NAMES_PATH));

            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" - ");

                nameLogs.put(UUID.fromString(data[1]), data[0]);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static void saveNameLogs() {
        try {
            FileWriter fileWriter = new FileWriter(ENTITY_NAMES_PATH);

            Iterator iterator = nameLogs.entrySet().stream().iterator();

            while(iterator.hasNext()) {
                Map.Entry uuidStringEntry = (Map.Entry) iterator.next();
                fileWriter.write(uuidStringEntry.getValue() + " - " + uuidStringEntry.getKey() + "\n");
            }

            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean addName(String name, UUID uuid) {
        NameManager.nameLogs.put(uuid, name);
        saveNameLogs();
        return true;
    }

    public static String getName(UUID uuid) {
        return nameLogs.get(uuid);
    }

    public static void queueName(String name) {
        NameManager.setName = name;
    }

    public static String nextName() {
        String name = NameManager.setName;
        NameManager.setName = null;

        return name;
    }

    public static void removeName(UUID uuid) {
        nameLogs.remove(uuid);
        saveNameLogs();
    }

    public static Enumeration<UUID> getUUIDs() {
        return NameManager.nameLogs.keys();
    }
}
