package com.pakn.dev;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;


@Controller
public class PresetHandler {
    HashMap<Integer, Preset> presetList = new HashMap<>();
    ObjectMapper presetMapper = new ObjectMapper();
    File presetFile = new File("cube_controller\\src\\main\\resources\\static\\presets.json".replace("\\", File.separator));

    //on start, read presets from presetFile
    @PostConstruct
    public void init() {
        try {
            presetFile.createNewFile();
            JsonNode presetsNode = presetMapper.readTree(presetFile);
            if (presetsNode.isArray()) {
                for (JsonNode presetNode:presetsNode) {
                    Preset preset = presetMapper.readValue(presetNode.toString(), Preset.class);
                    presetList.put(preset.getId(), preset);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/save-preset")
    public ResponseEntity<?> savePreset(@RequestBody Preset preset) {
        presetList.put(preset.getId(), preset);
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
    
    private static class Preset {
        private int id;
        private String name;
        private HashMap<String, Action> moveMap = new HashMap<>();

        public Preset() {}

        public Preset(int id, String name) {
            this.id = id;
            this.name = name;
        }

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
    }
}
