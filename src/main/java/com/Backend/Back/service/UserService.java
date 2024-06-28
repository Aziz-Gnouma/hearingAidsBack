package com.Backend.Back.service;


import com.Backend.Back.dao.RoleDao;
import com.Backend.Back.dao.UserDao;
import com.Backend.Back.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;



    @Autowired
    private PasswordEncoder passwordEncoder;

    public void initRoleAndUser() {

        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin role of platform");
        roleDao.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("Default role for newly created record");
        roleDao.save(userRole);



        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setCin(14659847);
        adminUser.setEmail("aziz@aziz.com");
        adminUser.setUserPassword(getEncodedPassword("aziz@123"));
        adminUser.setUserFirstName("test");
        adminUser.setUserLastName("test2");
        adminUser.setDateOfBirth(new Date(100, 4, 25));

        adminUser.setPhoneNumber(23679560);
        adminUser.setAddress("nabeul");
        adminUser.setPays("Nabeul");
        Set<Role> AdminRole = new HashSet<>();
        AdminRole.add(adminRole);
        adminUser.setRole(AdminRole);

        userDao.save(adminUser);
     //   Employe ok = adminUser ;
       // EmployeDao.save(ok);


//        User user = new User();
//        user.setUserName("raj123");
//        user.setUserPassword(getEncodedPassword("raj@123"));
//        user.setUserFirstName("raj");
//        user.setUserLastName("sharma");
//        Set<Role> userRoles = new HashSet<>();
//        userRoles.add(userRole);
//        user.setRole(userRoles);
//        userDao.save(user);
    }

    public Role createNewRole(Role role) {
        return roleDao.save(role);
    }
    public User registerNewUser(User user) {
        if ( userDao.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User already exists");
        }




        Role role = roleDao.findById("User").orElseThrow(() -> new EntityNotFoundException("Role 'User' not found"));

        // Associate the role with the user
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        user.setRole(userRoles);

        user.setUserPassword(getEncodedPassword(user.getUserPassword()));



        return userDao.save(user);
    }




    public User AddNewUser(User user  ) {

        try {
            if (userDao.existsByEmail(user.getEmail())) {
                throw new RuntimeException("User already exist with email");
            }

            Role role = roleDao.findById("User").orElseThrow(() -> new RuntimeException("Role not found"));
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(role);
            user.setRole(userRoles);
            user.setCreatedDate(new Date());
            user.setStaus("activate");
            user.setPrivateemail(user.getEmail());

            user.setUserPassword(getEncodedPassword(user.getUserPassword()));


            userDao.save(user);
            return user;
        } catch (Exception e) {
            e.printStackTrace(); // Or log the error
            throw new RuntimeException("An error occurred while adding the user");
        }
    }



    public ResponseEntity<String>  updateUserById(Long Id, User updatedUser) {
        // Fetch the existing user by ID
        Optional<User> userOptional = userDao.findById(Id);


        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (updatedUser.getUserFirstName() != null && !updatedUser.getUserFirstName().isEmpty()) {
            user.setUserFirstName(updatedUser.getUserFirstName());
            }
            if (updatedUser.getUserLastName() != null && !updatedUser.getUserLastName().isEmpty()) {
            user.setUserLastName(updatedUser.getUserLastName());
        }
            if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                user.setEmail(updatedUser.getEmail());
            }

            if (updatedUser.getDateOfBirth() != null) {
                user.setDateOfBirth(updatedUser.getDateOfBirth());
            }

            if (updatedUser.getPhoneNumber() != null) {
                user.setPhoneNumber(updatedUser.getPhoneNumber());
            }
            if (updatedUser.getAddress() != null) {
                user.setAddress(updatedUser.getAddress());

            }
            if (updatedUser.getPays() != null) {
                user.setPays(updatedUser.getPays());
            }
            if (updatedUser.getCodePostal() != null) {
                user.setCodePostal(updatedUser.getCodePostal());
            }

            // Check if the password is provided before updating
            if (updatedUser.getUserPassword() != null && !updatedUser.getUserPassword().isEmpty()) {
                user.setUserPassword(passwordEncoder.encode(updatedUser.getUserPassword())); // Encrypt the password
            }



        userDao.save(user);

            return ResponseEntity.ok("User information updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<String> updateUser(Long Id, User updatedUser) {
        // Fetch the existing user by ID
        Optional<User> userOptional = userDao.findById(Id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Update user information based on the fields you want to allow the user to update
            user.setUserFirstName(updatedUser.getUserFirstName());
            user.setUserLastName(updatedUser.getUserLastName());
            user.setEmail(updatedUser.getEmail());

            if (updatedUser.getUserPassword() != null && !updatedUser.getUserPassword().isEmpty()) {
                user.setUserPassword(passwordEncoder.encode(updatedUser.getUserPassword()));
            }


            userDao.save(user);
            return ResponseEntity.ok("User information updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }


    public long getTotalUsers() {
        return userDao.countAllUsers();
    }

}
