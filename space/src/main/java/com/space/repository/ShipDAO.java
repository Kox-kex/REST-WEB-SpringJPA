package com.space.repository;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public interface ShipDAO extends JpaRepository<Ship, Long>, JpaSpecificationExecutor<Ship> {

    //List<Ship> findByName(String name);

    //List<Ship> findByIsUsed(Boolean isUsed);

    //List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);

    /*List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);

    List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);

    List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);

    List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);

    List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);

    List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);

    List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);

    List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);

    List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);

    List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);

    List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);

    List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);

    List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);*/



}
