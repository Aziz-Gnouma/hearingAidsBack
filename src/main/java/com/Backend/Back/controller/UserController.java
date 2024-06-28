package com.Backend.Back.controller;

import com.Backend.Back.dao.*;
import com.Backend.Back.entity.*;
import com.Backend.Back.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserDTO UserDTO;

    @PostConstruct
    public void initRoleAndUser() {
        userService.initRoleAndUser();
    }


    // Add another method with a different name or parameters
    @PostMapping("/registerNewUser")
    public ResponseEntity<?> registerNewUserV2(@RequestBody RegistrationRequest request) {
        try {
            User registeredUser = userService.registerNewUser(request.getUser());
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/AddNewUser")
    public ResponseEntity<User> registerNewUser(@RequestBody RegistrationRequest request) {
        String password = request.getUser().getUserPassword();

        try {
            // Register the new user
            User newUser = userService.AddNewUser(request.getUser());

            String candidateEmail = newUser.getEmail();

            senderService.sendSimpleEmail(candidateEmail,
                    "Account Activation",
                    "Hello " + newUser.getUserFirstName() + " " + newUser.getUserLastName() + ",\n\n" +
                            "Welcome to our platform!\n\n" +
                            "We are delighted to have you join us. We hope you find your experience with us to be highly rewarding.\n" +
                            "Your registration has been successfully confirmed, and we are excited to collaborate with you.\n\n" +
                            "Here are your credentials to access our platform:\n\n" +
                            "Email: " + newUser.getEmail() + "\n" +
                            "Password: " + password + "\n\n" +
                            "Thank you for your trust, and we look forward to seeing you on our platform soon!\n\n" +
                            "Best regards,\n" +
                            "The Platform Team ");

            // Return the newly registered user with HTTP status 200 OK
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            // Log the exception or return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PutMapping("/updateUser/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUserById(id, updatedUser);

    }

    @PostMapping({"/createNewRole"})
    public Role createNewRole(@RequestBody Role role) {
        return userService.createNewRole(role);
    }

    @GetMapping("/AllUsers")
    public List<User> getAllUsers() {
        return userDao.findAll();
    }


    @GetMapping("/AllUsers/Admin")
    public List<User> getAllAdminUsers() {
        return userDao.findAll().stream()
                .filter(user -> user.getRole().stream()
                        .anyMatch(role -> "Gerant".equals(role.getRoleName())))
                .collect(Collectors.toList());
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userDao.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping({"/forAdmin"})
    @PreAuthorize("hasRole('Admin')")
    public String forAdmin() {
        return "This URL is only accessible to the admin";
    }

    @GetMapping({"/forUser"})
    @PreAuthorize("hasRole('User')")
    public String forUser() {
        return "This URL is only accessible to the user";
    }


    @PutMapping("/archiveUser/{id}")
    public ResponseEntity<String> archiveUserById(@PathVariable Long id) {
        // Fetch the existing user by ID
        Optional<User> userOptional = userDao.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Role role = roleDao.findById("Archiver").orElseThrow(() -> new EntityNotFoundException("Role 'User' not found"));

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(role);
            user.setRole(userRoles);
            user.setEmail(null);

            userDao.save(user);
            return ResponseEntity.ok("User archived successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/desarchiveUser/{id}")
    public ResponseEntity<String> DarchiveUserById(@PathVariable Long id) {
        // Fetch the existing user by ID
        Optional<User> userOptional = userDao.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Role role = roleDao.findById("User").orElseThrow(() -> new EntityNotFoundException("Role 'User' not found"));

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(role);
            user.setRole(userRoles);
            user.setEmail(user.getPrivateemail());


            userDao.save(user);
            return ResponseEntity.ok("User desarchived successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Autowired
    private EmailSenderService senderService;


    @PostMapping("/SendConfirmation/{id}")
    public ResponseEntity<String> SendConfirmation(@PathVariable Long id) {
        try {
            // Check if the user already exists
            Optional<User> optionalUser = userDao.findById(id);


            if (optionalUser.isPresent()) {
                User User = optionalUser.get();

                String candidateEmail = User.getEmail();
                senderService.sendSimpleEmail(candidateEmail,
                        "Confirmation and Activation of Your Account",
                        "Dear " + User.getUserFirstName() + " " + User.getUserLastName() + ",\n\n" +
                                "Welcome to our platform! We are excited to have you as a new member of our community.\n\n" +
                                "Your account has been successfully created and activated. Below are your login credentials:\n" +

                                "Thank you for joining us. We look forward to your active participation and contribution.\n\n" +
                                "Best regards,\n" +
                                "The Platform Team");

                return ResponseEntity.ok("Email sent successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found ");
            }
        } catch (Exception e) {
            // Log the exception or return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email");
        }

    }


    @GetMapping("/totalUsers")
    public long getTotalUsers() {
        return userService.getTotalUsers();
    }

    @Autowired
    private ProductDao productRepository;

    @Autowired
    private UserProductDao UserProductDao;
    @Autowired
    private ProductService ProductService;

    @PostMapping("/ajouterProduit")
    public ResponseEntity<?> ajouterProduit(@RequestParam("file") MultipartFile multipartFile,
                                            @RequestParam("category") String category,
                                            @RequestParam("stock") int stock,
                                            @RequestParam("price") String price,
                                            @RequestParam("productName") String productName,
                                            @RequestParam("description") String description
    ) {
        try {
            String fileName = multipartFile.getOriginalFilename();
            byte[] fileContent = multipartFile.getBytes();
            Long productId = ProductService.saveProduct(productName, fileName, fileContent, category, stock, price, description);

            // Construct response
            FileUploadResponse response = new FileUploadResponse();
            response.setFileName(fileName);
            response.setSize(multipartFile.getSize());
            response.setDownloadUri("/api/products/downloadFile/" + productId);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload product.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("Allproducts")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @PutMapping("products/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return ProductService.updateProductById(id, updatedProduct);

    }

    @Autowired
    private ProductDao productDao;

    @PutMapping("Updateproduct/{id}")
    public ResponseEntity<?> updateProductById(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile multipartFile,
            @RequestParam(value = "productName", required = false) String productName,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "stock", required = false) Integer stock,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "description", required = false) String description) {

        Optional<Product> productOptional = productDao.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            try {
                if (multipartFile != null) {
                    String fileName = multipartFile.getOriginalFilename();
                    byte[] fileContent = multipartFile.getBytes();
                    product.setFileName(fileName);
                    product.setContent(fileContent);
                }
                if (productName != null && !productName.isEmpty()) {
                    product.setProductName(productName);
                }
                if (category != null && !category.isEmpty()) {
                    product.setCategory(category);
                }
                if (stock != null) {
                    product.setStock(stock);
                }
                if (price != null && !price.isEmpty()) {
                    product.setPrice(price);
                }
                if (description != null && !description.isEmpty()) {
                    product.setDescription(description);
                }

                productDao.save(product);

                // Construct response if needed
                return ResponseEntity.ok("Product updated successfully");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating product");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private PaymentGateway paymentGateway;

    @PostMapping("/{productId}")
    public ResponseEntity<String> addUserToProduct(@PathVariable Long productId,
                                                   @RequestBody CreditCardInfo creditCardInfo
                                                  ) {
        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }

        String encryptedCardNumber;
        try {
            encryptedCardNumber = EncryptionUtil.maskAndEncryptCreditCard(creditCardInfo.getCardNumber());
        } catch (Exception e) {
            return new ResponseEntity<>("Error encrypting credit card number", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Integrate with a payment gateway to charge the card securely
        PaymentGatewayResponse paymentResponse = paymentGateway.charge(encryptedCardNumber,
                creditCardInfo.getExpirationDate(), creditCardInfo.getCvc());

        if (!paymentResponse.isSuccess()) {
            return new ResponseEntity<>("Payment failed", HttpStatus.BAD_REQUEST);
        }

        UserProduct userProduct = new UserProduct();
        userProduct.setProduct(product);
        userProduct.setUserFirstName(creditCardInfo.getUserFirstName());
        userProduct.setUserLastName(creditCardInfo.getUserLastName());
        userProduct.setPays(creditCardInfo.getPays());
        userProduct.setState(creditCardInfo.getState());
        userProduct.setAddress(creditCardInfo.getAddress());
        userProduct.setCodePostal(creditCardInfo.getCodePostal());
        userProduct.setEmail(creditCardInfo.getEmail());
        userProduct.setPhoneNumber(creditCardInfo.getPhoneNumber());
        userProduct.setPurchaseDate(new Date());
        userProduct.setPaymentReference(paymentResponse.getTransactionId());
        userProduct.setEncryptedCardNumber(EncryptionUtil.maskCreditCard(creditCardInfo.getCardNumber()));
        userProduct.setCardDate(creditCardInfo.getExpirationDate());
        userProduct.setCSV(creditCardInfo.getCvc());

        UserProductDao.save(userProduct);

        try {
            String candidateEmail = creditCardInfo.getEmail();
            senderService.sendSimpleEmail(candidateEmail,
                    "Order Confirmation",
                    "Dear " + creditCardInfo.getUserFirstName() + " " + creditCardInfo.getUserLastName() + ",\n\n" +
                            "Thank you for your purchase from our platform! We are excited to confirm your order.\n\n" +
                            "Below are the details of your order:\n" +
                            "Product Name: " + product.getProductName() + "\n" +
                            "Category: " + product.getCategory() + "\n" +
                            "Price: $" + product.getPrice() + "\n\n" +
                            "Your order will be processed shortly and you will receive further updates via email.\n\n" +
                            "Thank you for shopping with us. We look forward to serving you again.\n\n" +
                            "Best regards,\n" +
                            "The Platform Team");

            return new ResponseEntity<>("User added to product and purchase history recorded successfully", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email");
        }
    }

    @GetMapping("/AllOrders")
    public ResponseEntity<List<OrderDetails>> getAllOrdersWithDetails() {
        List<UserProduct> purchaseHistories = UserProductDao.findAll();

        List<OrderDetails> orderDetailsList = purchaseHistories.stream()
                .map(this::mapToOrderDetails)
                .collect(Collectors.toList());

        return new ResponseEntity<>(orderDetailsList, HttpStatus.OK);
    }

    @GetMapping("/allorders/{id}")
    public List<OrderDetails> getAllOrdersUser(@PathVariable("id") Long userId) {
        User user = userDao.findById(userId).orElse(null);
        if (user == null) {
            return Collections.emptyList();
        }
        String userEmail = user.getEmail();
        String userFirstName = user.getUserFirstName();
        String userLastName = user.getUserLastName();

        return userProductDao.findAll().stream()
                .filter(userProduct ->
                        userEmail.equals(userProduct.getEmail()) &&
                                userFirstName.equals(userProduct.getUserFirstName()) &&
                                userLastName.equals(userProduct.getUserLastName()))
                .map(this::mapToOrderDetails)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    @GetMapping("/countProducts")
    public ResponseEntity<Long> countProducts() {
        Long count = productService.countProducts();
        return ResponseEntity.ok().body(count);
    }
    private OrderDetails mapToOrderDetails(UserProduct purchaseHistory) {


        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderId(purchaseHistory.getId());
        orderDetails.setProductName(purchaseHistory.getProduct().getProductName());
        orderDetails.setPurchaseDate(purchaseHistory.getPurchaseDate());
        orderDetails.setPurchaseDate(purchaseHistory.getPurchaseDate());
        orderDetails.setCard(purchaseHistory.getEncryptedCardNumber());
        orderDetails.setPaymentRef(purchaseHistory.getPaymentReference());
        orderDetails.setDateCard(purchaseHistory.getCardDate());
        orderDetails.setPrice(purchaseHistory.getProduct().getPrice());

        orderDetails.setUserId(purchaseHistory.getId());
        orderDetails.setUserFirstName(purchaseHistory.getUserFirstName());
        orderDetails.setUserLastName(purchaseHistory.getUserLastName());
        orderDetails.setUserEmail(purchaseHistory.getEmail());
        orderDetails.setPhoneNumber(purchaseHistory.getPhoneNumber());

        return orderDetails;
    }

    private static class OrderDetails {
        private Long orderId;
        private String productName;
        private Date purchaseDate;
        private Long userId;
        private String userFirstName;
        private String userLastName;
        private String userEmail;
        private String paymentRef;

        private String card;
        private String Price;

        private String DateCard;

        public String getPrice() {
            return Price;
        }

        public void setPrice(String price) {
            Price = price;
        }

        private Integer phoneNumber;

        public String getPaymentRef() {
            return paymentRef;
        }

        public String getDateCard() {
            return DateCard;
        }

        public void setDateCard(String dateCard) {
            DateCard = dateCard;
        }

        public void setPaymentRef(String paymentRef) {
            this.paymentRef = paymentRef;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public Integer getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(Integer phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Date getPurchaseDate() {
            return purchaseDate;
        }

        public void setPurchaseDate(Date purchaseDate) {
            this.purchaseDate = purchaseDate;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
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

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

    }

    @Autowired
    private UserProductDao userProductDao;

    @GetMapping("/distinctUserCount")
    public long getDistinctUserCount() {
        return userProductDao.countDistinctUsersWithOrders();
    }

    @PostMapping("/desactiveProduct/{id}")
    public ResponseEntity<String> deactivateProduct(@PathVariable Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setStatus("desactivate");
            productRepository.save(product);
            return new ResponseEntity<>("Product deactivated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/activeProduct/{id}")
    public ResponseEntity<String> activateProduct(@PathVariable Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setStatus("activate");
            productRepository.save(product);
            return new ResponseEntity<>("Product activated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id, @RequestParam String roleName) {
        Optional<User> userOptional = userDao.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        boolean hasRole = user.getRole().stream().anyMatch(role -> role.getRoleName().equals(roleName));
        if (!hasRole) {
            return ResponseEntity.badRequest().body("User does not have role: " + roleName);
        }

        userDao.deleteById(id);
        return ResponseEntity.ok("User with ID " + id + " deleted successfully.");
    }

@Autowired
    private ProductService productService ;
    @GetMapping("/countByProductId/{productId}")
    public ResponseEntity<Long> countProductsById(@PathVariable Long productId) {
        long count = productService.countProductsById(productId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

}
