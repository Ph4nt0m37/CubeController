package com.pakn.dev;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;


@Controller
public class PresetHandler {
    HashMap<Integer, Preset> presetList = new HashMap<>();
    ObjectMapper presetMapper = new ObjectMapper();
    File presetFile = new File(System.getProperty("user.dir")+"\\presets.json".replace("\\", File.separator));

    //on start, read presets from presetFile
    @PostConstruct
    public void init() {
        try {
            presetFile.createNewFile();
            JsonNode presetsNode = presetMapper.readTree(presetFile);
            for (JsonNode presetNode:presetsNode) {
                Preset preset = presetMapper.readValue(presetNode.toString(), Preset.class);
                preset.setMoveMap(readActionMap(presetNode)); //has to use readActionMap since the hashmap doesn't serialize nicely due to polymorphism
                presetList.put(preset.getId(), preset);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/save-preset")
    public ResponseEntity<?> savePreset(@RequestBody Preset preset) {
        try {
            preset.setMoveMap(WindowHandler.getInstance().getActionMap());
            presetList.put(preset.getId(), preset);
            presetMapper.writeValue(presetFile, presetList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/save-preset")
    public ResponseEntity<?> deletePreset(@RequestBody Preset preset) {
        try {
            presetList.remove(preset.getId());
            presetMapper.writeValue(presetFile, presetList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get-presets")
    public ResponseEntity<String> getPresets() {
        try {
            return new ResponseEntity<>(Files.readString(presetFile.toPath()), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private HashMap<String, Action> readActionMap(JsonNode presetNode) {
        HashMap<String, Action> actionMap = new HashMap<>();

        JsonNode moveMapNode = presetNode.get("moveMap");
        Iterator<String> moveMapNodeIterator = moveMapNode.fieldNames();
        while (moveMapNodeIterator.hasNext()) {
            String moveKey = moveMapNodeIterator.next();
            JsonNode moveNode = moveMapNode.get(moveKey);
            if (moveNode.get("key")!=null) {
                actionMap.put(moveKey, new KeyClick(moveNode.get("actionString").asText(), moveNode.get("timeMs").asLong()));
            }else if (moveNode.get("mouse")!=null) {
                actionMap.put(moveKey, new MouseClick(moveNode.get("actionString").asText(), moveNode.get("timeMs").asLong()));
            }
        }
        return actionMap;
    }
    
    private static class Preset {
        private int id;
        private String name;
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private HashMap<String, Action> moveMap = new HashMap<>();

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public HashMap<String, Action> getMoveMap() {
            return moveMap;
        }

        public void setMoveMap(HashMap<String, Action> moveMap) {
            this.moveMap = moveMap;
        }

        @Override
        public String toString() {
            return "Preset [id=" + id + ", name=" + name + ", moveMap=" + Arrays.asList(moveMap) + "]";
        }
    }
}
