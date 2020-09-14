package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/rest/ships")
public class ShipController {

    @Autowired
    private ShipService shipService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Ship>> getShipsList(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "planet", required = false) String planet,
                                                  @RequestParam(value = "shipType", required = false) ShipType shipType,
                                                  @RequestParam(value = "after", required = false) Long after,
                                                  @RequestParam(value = "before", required = false) Long before,
                                                  @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                                  @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                                  @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                                  @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                                  @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                                  @RequestParam(value = "minRating", required = false) Double minRating,
                                                  @RequestParam(value = "maxRating", required = false) Double maxRating,
                                                  @RequestParam(value = "order", required = false) ShipOrder order,
                                                  @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                  @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Date dateAfter = null;
        Date dateBefore = null;
        if (Objects.nonNull(after)) dateAfter = new Date(after);
        if (Objects.nonNull(before)) dateBefore = new Date(before);

        return new ResponseEntity<>(shipService.filter(name, planet, shipType, dateAfter, dateBefore, isUsed,
                                                        minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating,
                                                        maxRating, order, pageNumber, pageSize), HttpStatus.OK);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Integer> getShipsCount(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "planet", required = false) String planet,
                                                  @RequestParam(value = "shipType", required = false) ShipType shipType,
                                                  @RequestParam(value = "after", required = false) Long after,
                                                  @RequestParam(value = "before", required = false) Long before,
                                                  @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                                  @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                                  @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                                  @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                                  @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                                  @RequestParam(value = "minRating", required = false) Double minRating,
                                                  @RequestParam(value = "maxRating", required = false) Double maxRating) {
        Date dateAfter = null;
        Date dateBefore = null;
        if (Objects.nonNull(after)) dateAfter = new Date(after);
        if (Objects.nonNull(before)) dateBefore = new Date(before);

        return new ResponseEntity<>(shipService.shipCountFilter(name, planet, shipType, dateAfter, dateBefore, isUsed,
                minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating,
                maxRating), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {

        if (ship.getUsed() == null) ship.setUsed(false);
        ship.setRating(); //installation rating

        //identifying the year
        long date  = 0;
        if (ship.getProdDate() != null) {
            SimpleDateFormat dateformatYYYY = new SimpleDateFormat("yyyy");
            date = Long.parseLong(dateformatYYYY.format(ship.getProdDate()));
        }

            if (ship.getSpeed() != null && ship.getCrewSize() != null
                    && ship.getPlanet() != null && ship.getName() != null
                    && ship.getName().length() < 50 && ship.getPlanet().length() < 50
                    && !ship.getName().isEmpty() && !ship.getPlanet().isEmpty()
                    && ship.getProdDate().getTime() >= 0 && date >= 2800 && date <= 3019
                    && ship.getSpeed() >= 0.01 && ship.getSpeed() <= 0.99
                    && ship.getCrewSize() >= 1 && ship.getCrewSize() <= 9999) {

                this.shipService.create(ship);

                return new ResponseEntity<>(ship, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }


    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> getShip(@PathVariable(name = "id") String id) {

        long idN = parseId(id);

        if (idN == -1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Ship ship = null;
        /*if (shipService.getById(id).isPresent()) {
            ship = shipService.getById(id).get();
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);*/
        if (shipService.getById(idN) != null) {
            ship = shipService.getById(idN);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    /*@RequestMapping(value = "{id}", method = POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> updateShip(@PathVariable("id") Long id,
                                           @RequestBody Ship ship){
        Ship update = null;
        if (ship.getName() != null && !(ship.getName().length() < 50 && !ship.getName().isEmpty())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (ship.getPlanet() != null && !(ship.getPlanet().length() < 50 && !ship.getPlanet().isEmpty())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (ship.getSpeed() != null && !(ship.getSpeed() >= 0.01 && ship.getSpeed() <= 0.99)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (ship.getCrewSize() != null && !(ship.getCrewSize() >= 1 && ship.getCrewSize() <= 9999)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (ship.getProdDate() != null) {
            if (!(ship.getProdDate().getTime() >= 33103209600000L && ship.getProdDate().getTime() <=  26192246400000L))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        update = shipService.update(id, ship);

        if (update == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(update, HttpStatus.OK);
    }*/
    private long parseId(String str) {
        double numId;
        try {
            numId = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return -1;
        }
        if (numId <= 0 || (long)numId != numId) return -1;
        return (long)numId;
    }

    @RequestMapping(value = "{id}", method = POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> updateShip(@PathVariable(name = "id") String id,
                                           @RequestBody Ship ship){
        String name = ship.getName();
        String planet = ship.getPlanet();
        Date prodDate = ship.getProdDate();
        Double speed = ship.getSpeed();
        Integer crewSize = ship.getCrewSize();
        final long beforeDate = 26192246400000L;
        final long afterDate = 33103209600000L;
        Ship shipUpdate = null;

        long idN = parseId(id);

        if (idN == -1 ||
                name != null && (name.length() > 50 || name.equals("")) ||
                planet != null && planet.length() > 50 ||
                prodDate != null && (prodDate.getTime() < beforeDate || prodDate.getTime() > afterDate) ||
                speed != null && (speed < 0.01 || speed > 0.99) ||
                crewSize != null && (crewSize < 1 || crewSize > 9999)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
                shipUpdate = shipService.update(idN, ship);
        }
        if (shipUpdate == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(shipUpdate, HttpStatus.OK);
    }


    /*@RequestMapping(value = "{id}", method = POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> updateShip(@PathVariable("id") Long id,
                                           @RequestBody Ship ship) {

        Ship update = null;
        if (id == 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        *//*if (ship == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }*//*
        if (ship.getId() == null && ship.getName() == null && ship.getPlanet() == null && ship.getShipType() == null
                && ship.getProdDate() == null && ship.getUsed() == null
                && ship.getSpeed() == null && ship.getCrewSize() == null
                && ship.getRating() == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            if (ship.getName() != null && !(ship.getName().length() < 50 && !ship.getName().isEmpty())){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (ship.getSpeed() != null && !(ship.getSpeed() >= 0.01 && ship.getSpeed() <= 0.99)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (ship.getCrewSize() != null && !(ship.getCrewSize() >= 1 && ship.getCrewSize() <= 9999)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            long date = 0;
            if (ship.getProdDate() != null) {
                SimpleDateFormat dateformatYYYY = new SimpleDateFormat("yyyy");
                date = Long.parseLong(dateformatYYYY.format(ship.getProdDate()));
                if (!(ship.getProdDate().getTime() >= 0 && date >= 2800 && date <= 3019))
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
                update = shipService.update222(id, ship);
        }

        if (update == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }*/

    @RequestMapping(value = "{id}", method = DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> deleteShip(@PathVariable(name = "id") String id) {
        Ship ship = null;

        long idN = parseId(id);
        if (idN == -1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        //if (id != 0) {

            if (shipService.getById(idN) != null) {
                ship = shipService.getById(idN);
                shipService.delete(ship);
            } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        //} else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
