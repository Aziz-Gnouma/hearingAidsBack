package com.Backend.Back.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PURCHASE_HISTORY")
public class UserProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userFirstName;
    private String userLastName;
    private String address;
    private String pays;
    private  String CodePostal;
    private  String state;
    private String email;
    private Integer phoneNumber;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @Temporal(TemporalType.TIMESTAMP)
    private Date purchaseDate;

    @Column(name = "PAYMENT_REFERENCE") // Store payment reference or token
    private String paymentReference;
    private String encryptedCardNumber;
    private String CardDate;
    private String CSV;



    // Constructors, getters, and setters

    public UserProduct() {
    }

    public String getEncryptedCardNumber() {
        return encryptedCardNumber;
    }

    public String getCardDate() {
        return CardDate;
    }

    public void setCardDate(String cardDate) {
        CardDate = cardDate;
    }

    public String getCSV() {
        return CSV;
    }

    public void setCSV(String CSV) {
        this.CSV = CSV;
    }

    public void setEncryptedCardNumber(String encryptedCardNumber) {
        this.encryptedCardNumber = encryptedCardNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPays(String pays) {
        return this.pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getCodePostal() {
        return CodePostal;
    }

    public void setCodePostal(String codePostal) {
        CodePostal = codePostal;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }
}
