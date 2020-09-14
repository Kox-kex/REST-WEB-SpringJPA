package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ShipService {

    @Autowired
    private ShipDAO shipDAO;


    public List<Ship> getAllUsers() {
        return shipDAO.findAll();
    }

    public Ship create(Ship ship) {
        if (ship == null) return null;
        return shipDAO.save(ship);
    }

    public Ship update(Long id, Ship ship) {
        Ship shipUpdate = null;
        //if (getById(id).isPresent()) {
            shipUpdate = getById(id);//.get();
        if (shipUpdate != null) {
            if (ship.getName() != null) shipUpdate.setName(ship.getName());
            if (ship.getPlanet() != null) shipUpdate.setPlanet(ship.getPlanet());
            if (ship.getShipType() != null) shipUpdate.setShipType(ship.getShipType());
            if (ship.getProdDate() != null) shipUpdate.setProdDate(ship.getProdDate());
            if (ship.getUsed() != null) shipUpdate.setUsed(ship.getUsed());
            if (ship.getSpeed() != null) shipUpdate.setSpeed(ship.getSpeed());
            if (ship.getCrewSize() != null) shipUpdate.setCrewSize(ship.getCrewSize());
            shipUpdate.setRating();
            shipDAO.save(shipUpdate);
        }
        //} else return null;
        return shipUpdate;
    }

    public Ship update222(Long id, Ship ship) {
        Ship shipUpdate = null;
        //if (getById(id).isPresent()) {
            shipUpdate = getById(id);//.get();
            if (ship.getName() != null && ship.getName().length() < 50 && !ship.getName().isEmpty())
                shipUpdate.setName(ship.getName());
            if (ship.getPlanet() != null && ship.getPlanet().length() < 50 && !ship.getPlanet().isEmpty())
                shipUpdate.setPlanet(ship.getPlanet());
            if (ship.getShipType() != null) shipUpdate.setShipType(ship.getShipType());

            //identifying the year
            long date = 0;
            if (ship.getProdDate() != null) {
                SimpleDateFormat dateformatYYYY = new SimpleDateFormat("yyyy");
                date = Long.parseLong(dateformatYYYY.format(ship.getProdDate()));
                if (ship.getProdDate().getTime() >= 0 && date >= 2800 && date <= 3019)
                    shipUpdate.setProdDate(ship.getProdDate());
            }
            if (ship.getUsed() != null) shipUpdate.setUsed(ship.getUsed());
            if (ship.getSpeed() != null && ship.getSpeed() >= 0.01 && ship.getSpeed() <= 0.99)
                shipUpdate.setSpeed(ship.getSpeed());
            if (ship.getCrewSize() != null && ship.getCrewSize() >= 1 && ship.getCrewSize() <= 9999)
                shipUpdate.setCrewSize(ship.getCrewSize());
            shipUpdate.setRating();
            shipDAO.save(shipUpdate);
        //} else return null;

        return shipUpdate;
    }

    public void delete(Ship ship) {
        shipDAO.delete(ship);
    }

    /*public Optional<Ship> getById(Long id) {
        return shipDAO.findById(id);
    }*/

    public Ship getById(Long id) {

        return shipDAO.findById(id).orElse(null);
    }

    public List<Ship> filter(String name, String planet, ShipType shipType, Date after,
                             Date before, Boolean isUsed, Double minSpeed, Double maxSpeed,
                             Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating,
                             ShipOrder order, Integer pageNumber, Integer pageSize) {

        /*Page<Ship> ff = ShipsHelper.filterHelper(name, planet, shipType, after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating,
                order, pageNumber, pageSize, shipDAO);
        return ff.getContent();*/

        Pageable firstPageWithTwoElements = PageRequest.of(0, 3);
        if (pageNumber == null && pageSize != null) {
            firstPageWithTwoElements = PageRequest.of(0, pageSize);
        }
        if (pageNumber != null && pageSize == null) {
            firstPageWithTwoElements = PageRequest.of(pageNumber, 3);
        }
        return shipDAO.findAll(ShipsHelper.filterHelper(name, planet, shipType, after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating,
                order, pageNumber, pageSize, shipDAO), firstPageWithTwoElements).getContent();
    }

    public Integer shipCountFilter(String name, String planet, ShipType shipType, Date after,
                                   Date before, Boolean isUsed, Double minSpeed, Double maxSpeed,
                                   Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating) {
        /*Integer count = filter(name, planet, shipType, after, before, isUsed, minSpeed,
                                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating,
                                null, null, null).size();*/
        /*Page<Ship> ff = ShipsHelper.filterHelper(name, planet, shipType, after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating,
                null, null, null, shipDAO);
        int count = 0;
        if (ff.hasNext()) {
            count = +ff.getSize();
        }*/
        return shipDAO.findAll(ShipsHelper.filterHelper(name, planet, shipType, after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating,
                null, null, null, shipDAO)).size();
    }


    //======================================ClassHelper========================================================//


    private static class ShipsHelper {

        private static Specification<Ship> filterHelper(String name, String planet, ShipType shipType, Date after,
                                                        Date before, Boolean isUsed, Double minSpeed, Double maxSpeed,
                                                        Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating,
                                                        ShipOrder order, Integer pageNumber, Integer pageSize, ShipDAO shipDAO) {

            Specification<Ship> ss = (Specification<Ship>) (root, query, criteriaBuilder) -> {
                Predicate predicate = criteriaBuilder.conjunction();
                if (Objects.nonNull(name)) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                }
                if (Objects.nonNull(planet)) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(root.get("planet"), "%" + planet + "%"));
                }
                if (Objects.nonNull(shipType)) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.equal(root.get("shipType"), shipType));
                }
                if (Objects.nonNull(after)) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), after));
                }
                if (Objects.nonNull(before)) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), before));
                }
                if (Objects.nonNull(isUsed)) {
                    if (!isUsed) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.isFalse(root.get("isUsed")));
                    }
                    if (isUsed) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.isTrue(root.get("isUsed")));
                    }
                }
                if (Objects.nonNull(minSpeed) || Objects.nonNull(maxSpeed)) {
                    if (minSpeed == null) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("speed"), 0.01, maxSpeed));
                    } else if (maxSpeed == null) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("speed"), minSpeed, 0.99));
                    } else {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("speed"), minSpeed, maxSpeed));
                    }
                }
                if (Objects.nonNull(minCrewSize) || Objects.nonNull(maxCrewSize)) {
                    if (minCrewSize == null) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("crewSize"), 1, maxCrewSize));
                    } else if (maxCrewSize == null) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("crewSize"), minCrewSize, 9999));
                    } else {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("crewSize"), minCrewSize, maxCrewSize));
                    }
                }
                if (Objects.nonNull(minRating) || Objects.nonNull(maxRating)) {
                    if (minRating == null) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("rating"), 0.01, maxRating));
                    } else if (maxRating == null) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("rating"), minRating, 1000.00));
                    } else {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("rating"), minRating, maxRating));
                    }
                }
                if (Objects.nonNull(order)) {
                    query.orderBy(criteriaBuilder.asc(root.get(order.getFieldName())));
                }

                return predicate;
            };

            return ss;
        }
    }
}

            /*Page<Ship> ff = shipDAO.findAll((Specification<Ship>) (root, query, criteriaBuilder) -> {
                Predicate predicate = criteriaBuilder.conjunction();
                if (Objects.nonNull(name)) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                }
                if (Objects.nonNull(planet)) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(root.get("planet"), "%" + planet + "%"));
                }
                if (Objects.nonNull(shipType)) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.equal(root.get("shipType"), shipType));
                }
                if (Objects.nonNull(after)) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), after));
                }
                if (Objects.nonNull(before)) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), before));
                }
                if (Objects.nonNull(isUsed)) {
                    if (!isUsed) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.isFalse(root.get("isUsed")));
                    }
                    if (isUsed) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.isTrue(root.get("isUsed")));
                    }
                }
                if (Objects.nonNull(minSpeed) || Objects.nonNull(maxSpeed)) {
                    if (minSpeed == null) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("speed"), 0.01, maxSpeed));
                    } else if (maxSpeed == null) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("speed"), minSpeed, 0.99));
                    } else {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("speed"), minSpeed, maxSpeed));
                    }
                }
                if (Objects.nonNull(minCrewSize) || Objects.nonNull(maxCrewSize)) {
                    if (minCrewSize == null) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("crewSize"), 1, maxCrewSize));
                    } else if (maxCrewSize == null) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("crewSize"), minCrewSize, 9999));
                    } else {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("crewSize"), minCrewSize, maxCrewSize));
                    }
                }
                if (Objects.nonNull(minRating) || Objects.nonNull(maxRating)) {
                    if (minRating == null) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("rating"), 0.01, maxRating));
                    } else if (maxRating == null) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("rating"), minRating, 1000.00));
                    } else {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.between(root.get("rating"), minRating, maxRating));
                    }
                }
                if (Objects.nonNull(order)) {
                    query.orderBy(criteriaBuilder.asc(root.get(order.getFieldName())));
                }

            *//*if (pageNumber == null && pageSize == null) {
                Pageable firstPageWithTwoElements = PageRequest.of(0, 3);
            }*//*


             *//*if (Objects.nonNull(minSpeed) && Objects.nonNull(maxSpeed) && minSpeed > maxSpeed) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.between(root.get("speed"), minSpeed, maxSpeed));
            }*//*
             *//*if (Objects.nonNull(maxSpeed)) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.le(root.get("maxSpeed"), maxSpeed));
            }*//*
                return predicate;
            }, firstPageWithTwoElements);
            return ff;
        }*/

