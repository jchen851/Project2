package com.example.itech3860_project1;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@RestController
public class VehicleController {


    private final ObjectMapper mapper = new ObjectMapper();
    private ApplicationHome home = new ApplicationHome(itech3860Project1Application.class);


    @RequestMapping(value = "/addVehicle", method = RequestMethod.POST)
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) throws IOException {
        // we will keep track of vehicles in the file "vehicles.txt"

        //serialize
        String json = mapper.writeValueAsString(vehicle);
        File file = new File(home.getDir() + "/vehicles.txt");
        FileUtils.writeStringToFile(new File("./vehicles.txt"), json + "\n", "UTF-8", true);
        return vehicle;

    }

    @RequestMapping(value = "/getVehicle/{id}", method = RequestMethod.GET)
    public Vehicle getVehicle(@PathVariable int id) throws IOException {
        List<String> lines = FileUtils.readLines(new File("./vehicles.txt"), "UTF-8");


        for (String line : lines) {
            Vehicle v = mapper.readValue(line, Vehicle.class);
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }


    @RequestMapping(value = "/updateVehicle", method = RequestMethod.PUT)
    public Vehicle updateVehicle(@RequestBody Vehicle vehicle) throws IOException {

        File file = new File(home.getDir() + "/vehicles.txt");
        List<String> lines = FileUtils.readLines(file, "UTF-8");
        String output = "";
        for (String line : lines) {
            Vehicle v = mapper.readValue(line, Vehicle.class);
            if (v.getId() == vehicle.getId()) {
                v.setMakeModel(vehicle.getMakeModel());
                v.setYear(vehicle.getYear());
                v.setRetailPrice(vehicle.getRetailPrice());
                String newLine = mapper.writeValueAsString(v);
                output = output + newLine + "\n";

            } else {
                output = output + line + "\n";
        }
            }


        FileUtils.writeStringToFile(file, output, "UTF-8");
        return vehicle;

    }

    @RequestMapping(value = "/deleteVehicle/{id}", method = RequestMethod.DELETE)
    public String deleteVehicle(@PathVariable int id) throws IOException {
        File file = new File(home.getDir() + "/vehicles.txt");
        List<String> lines = FileUtils.readLines(file, "UTF-8");

        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            Vehicle v = mapper.readValue(line, Vehicle.class);
            if (v.getId() != id) {
                updatedLines.add(line);
            } else {
                found = true;
            }
        }

        if (found) {
            FileUtils.writeLines(file, updatedLines, "UTF-8");
            return "Vehicle with ID " + id + " has been deleted.";
        } else {
            return "Vehicle with ID " + id + " not found.";
        }
    }
}






















