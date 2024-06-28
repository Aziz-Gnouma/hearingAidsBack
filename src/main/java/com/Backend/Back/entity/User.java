package com.Backend.Back.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity(name = "UserInfo")
public class User {


    private String email;


    private String userFirstName;
    private String userLastName;

    @Column(name = "CIN")
    private int cin;
    private String userPassword;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @JsonFormat(pattern="dd/MM/yyyy")

    private Date dateOfBirth;
    private Integer phoneNumber;
    private String address;
    private String pays;
    private  String CodePostal;
    private  String state;

    private  String staus;
    private String Privateemail;

    private  Date CreatedDate;

    public String getStaus() {
        return staus;
    }

    public void setStaus(String staus) {
        this.staus = staus;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public String getPrivateemail() {
        return Privateemail;
    }

    public void setPrivateemail(String privateemail) {
        Privateemail = privateemail;
    }

    public void setCreatedDate(Date createdDate) {
        CreatedDate = createdDate;
    }

    public String getCodePostal() {
        return CodePostal;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCodePostal(String codePostal) {
        CodePostal = codePostal;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID")
            }
    )
    private Set<Role> role;



    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    @JsonFormat(pattern="dd-MM-yyyy")
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }


    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    // Getter and setter for email field
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    // Getter and setter for role field
    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }


}
