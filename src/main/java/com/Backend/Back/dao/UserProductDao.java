package com.Backend.Back.dao;

import com.Backend.Back.entity.User;
import com.Backend.Back.entity.UserProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProductDao extends JpaRepository <UserProduct, Long> {

    @Query("SELECT COUNT(DISTINCT CONCAT(up.email, up.userFirstName, up.userLastName)) " +
            "FROM UserProduct up " +
            "WHERE EXISTS (SELECT 1 FROM com.Backend.Back.entity.User u " +
            "WHERE u.email = up.email AND u.userFirstName = up.userFirstName AND u.userLastName = up.userLastName)")
    long countDistinctUsersWithOrders();



    @Query("SELECT COUNT(up) FROM UserProduct up WHERE up.product.id = :productId")
    long countByProductId(@Param("productId") Long productId);



}
