package com.example.space_colony.utils;

import android.content.Context;

import com.example.space_colony.logic.MissionControl;
import com.example.space_colony.logic.Storage;
import com.example.space_colony.model.CrewMember;
import com.example.space_colony.model.Engineer;
import com.example.space_colony.model.Location;
import com.example.space_colony.model.Medic;
import com.example.space_colony.model.Pilot;
import com.example.space_colony.model.Scientist;
import com.example.space_colony.model.Soldier;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Handles JSON-based persistence for crew state and mission progress.
 * <p>
 * The save file ({@code crew_save.json}) is stored in the app's private internal storage
 * and contains the full crew roster plus the current mission count. It is written on
 * explicit user request and read automatically at app startup.
 */
public class FileManager {
    private static final String FILE_NAME = "crew_save.json";

    /**
     * Serializes all crew members and the mission counter to {@code crew_save.json}.
     * Each crew member is stored as a JSON object containing id, type, name,
     * experience, current energy, and location.
     *
     * @param ctx Android context used to open the private output stream
     */
    public static void save(Context ctx) {
        try {
            Storage storage = Storage.getInstance();
            JSONObject root = new JSONObject();
            JSONArray array = new JSONArray();

            for (CrewMember m : storage.getAll()) {
                JSONObject obj = new JSONObject();
                obj.put("id", m.getId());
                obj.put("type", m.getSpecializationName());
                obj.put("name", m.getName());
                obj.put("experience", m.getExperience());
                obj.put("energy", m.getEnergy());
                obj.put("location", m.getLocation().name());
                array.put(obj);
            }

            root.put("crew", array);
            root.put("missionCount", MissionControl.getMissionCount());
            root.put("nextId", storage.getNextId());

            try (FileOutputStream fos = ctx.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
                fos.write(root.toString().getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads crew state from {@code crew_save.json} if the file exists.
     * Clears the current {@link Storage} contents before populating from the file,
     * so this method replaces — rather than merges — the in-memory roster.
     * Silently returns if no save file is found.
     *
     * @param ctx Android context used to resolve the private files directory
     */
    public static void tryLoad(Context ctx) {
        File file = new File(ctx.getFilesDir(), FILE_NAME);
        if (!file.exists()) return;

        try {
            byte[] data = new byte[(int) file.length()];
            try (FileInputStream fis = ctx.openFileInput(FILE_NAME)) {
                fis.read(data);
            }

            JSONObject root = new JSONObject(new String(data));
            Storage storage = Storage.getInstance();
            storage.clear();

            JSONArray array = root.getJSONArray("crew");
            int maxId = 0;

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String type = obj.getString("type");
                String name = obj.getString("name");

                CrewMember member = createMember(type, name);
                if (member == null) continue;

                int id = obj.getInt("id");
                member.setId(id);
                member.setExperience(obj.getInt("experience"));
                member.setEnergy(obj.getInt("energy"));
                member.setLocation(Location.valueOf(obj.getString("location")));
                storage.getCrew().put(id, member);
                if (id > maxId) maxId = id;
            }

            storage.setNextId(maxId + 1);

            if (root.has("missionCount")) {
                MissionControl.setMissionCount(root.getInt("missionCount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static CrewMember createMember(String type, String name) {
        switch (type) {
            case "Pilot":     return new Pilot(name);
            case "Engineer":  return new Engineer(name);
            case "Medic":     return new Medic(name);
            case "Scientist": return new Scientist(name);
            case "Soldier":   return new Soldier(name);
            default:          return null;
        }
    }
}
