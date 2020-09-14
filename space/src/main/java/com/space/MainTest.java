package com.space;

import com.space.service.ShipService;

public class MainTest {

    public static void main(String[] args) {
        ShipService shipService = new ShipService();

        //System.out.println(shipService.getById(100000L).get());
    }
}
/*if (ship.getName() == null && ship.getPlanet() == null && ship.getShipType() == null
                && ship.getProdDate() == null && ship.getUsed() == null
             && ship.getSpeed() == null && ship.getCrewSize() == null)
            return new ResponseEntity<>(HttpStatus.OK);*/

   /* @RequestMapping(
            value = "",
            params = { "name", "planet", "shipType", "prodDate", "isUsed", "speed", "crewSize" },
            method = POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> createShip(@RequestParam(value = "name") String name,
                                           @RequestParam(value = "planet") String planet,
                                           @RequestParam(value = "shipType") ShipType shipType,
                                           @RequestParam(value = "prodDate")  Long prodDate,
                                           @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                           @RequestParam(value = "speed") Double speed,
                                           @RequestParam(value = "crewSize") Integer crewSize) {
        Ship ship = null;
        SimpleDateFormat dateformatYYYY = new SimpleDateFormat("yyyy");
        Date dateDate = new Date(prodDate);
        long date  = Long.parseLong(dateformatYYYY.format(dateDate));

        if (name.length() < 50 && planet.length() < 50
                && !name.isEmpty() && !planet.isEmpty()
                && prodDate >= 0 && date >= 2800 && date <= 3019
                && speed >= 0.01 && speed <= 0.99
                && crewSize >= 1 && crewSize <= 9999) {

            if (isUsed) {
                ship = new Ship(name, planet, shipType, dateDate, true, speed, crewSize);
                this.shipService.create(ship);
                return new ResponseEntity<>(ship, HttpStatus.OK);
            } else {
                ship = new Ship(name, planet, shipType, dateDate, false, speed, crewSize);
                this.shipService.create(ship);
                return new ResponseEntity<>(ship, HttpStatus.OK);
            }
        } else { return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }
    }*/