package com.service.marketplace.service.serviceImpl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.service.marketplace.dto.request.Checkout;
import com.service.marketplace.dto.request.OfferPaymentRequest;
import com.service.marketplace.dto.request.StripeAccountRequest;
import com.service.marketplace.persistence.entity.Offer;
import com.service.marketplace.persistence.entity.Role;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.service.EmailSenderService;
import com.service.marketplace.persistence.enums.OfferStatus;
import com.service.marketplace.persistence.repository.*;
import com.service.marketplace.service.ServiceService;
import com.service.marketplace.service.StripeService;
import com.service.marketplace.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.*;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeServiceImpl implements StripeService {
    private static final Gson gson = new Gson();
    private final UserService userService;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final OfferRepository offerRepository;
    private final ServiceRepository serviceRepository;
    private final VipServiceRepository vipServiceRepository;
    private final EmailSenderService emailSenderService;
    private final ServiceService serviceService;

    @Value("${STRIPE_PRIVATE_KEY}")
    private String stripeApiKey;

    @Override
    public String createStripeAccount(StripeAccountRequest stripeAccountRequest) {
        String[] data = stripeAccountRequest.getDateOfBirth().split("-");

        try {
            HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            Stripe.apiKey = stripeApiKey;
            long currentUnixTimestamp = (int) (System.currentTimeMillis() / 1000);

            Map<String, Object> externalAccountParams = new HashMap<>();
            externalAccountParams.put("object", "bank_account");
            externalAccountParams.put("country", "BG");
            externalAccountParams.put("currency", "BGN");
            externalAccountParams.put("account_holder_name", "Account Holder");
            externalAccountParams.put("account_holder_type", "individual");
            externalAccountParams.put("account_number", stripeAccountRequest.getIban());
            externalAccountParams.put("default_for_currency", true);

            Map<String, Object> tokenParams = new HashMap<>();
            tokenParams.put("bank_account", externalAccountParams);
            Token token = Token.create(tokenParams);
            String bankAccountToken = token.getId();

            AccountCreateParams accountCreateParams = AccountCreateParams.builder()
                    .setType(AccountCreateParams.Type.CUSTOM)
                    .setCountry("BG")
                    .setEmail(stripeAccountRequest.getEmail())
                    .setCapabilities(AccountCreateParams.Capabilities.builder()
                            .setCardPayments(AccountCreateParams.Capabilities.CardPayments.builder().setRequested(true).build())
                            .setTransfers(AccountCreateParams.Capabilities.Transfers.builder().setRequested(true).build())
                            .build())
                    .setBusinessType(AccountCreateParams.BusinessType.INDIVIDUAL)
                    .setTosAcceptance(AccountCreateParams.TosAcceptance.builder()
                            .setDate(currentUnixTimestamp)
                            .setIp(ClientIp.getClientIp(httpRequest))
                            .build())
                    .setBusinessProfile(AccountCreateParams.BusinessProfile.builder()
                            .setMcc("7277")
                            .setProductDescription("Service")
                            .build())
                    .setIndividual(AccountCreateParams.Individual.builder()
                            .setFirstName(stripeAccountRequest.getFirstMiddleName())
                            .setLastName(stripeAccountRequest.getLastName())
                            .setEmail(stripeAccountRequest.getEmail())
                            .setDob(AccountCreateParams.Individual.Dob.builder()
                                    .setDay(Long.parseLong(data[2]))
                                    .setMonth(Long.parseLong(data[1]))
                                    .setYear(Long.parseLong(data[0]))
                                    .build())
                            .setAddress(AccountCreateParams.Individual.Address.builder()
                                    .setLine1(stripeAccountRequest.getAddress())
                                    .setCity(stripeAccountRequest.getCity())
                                    .setState("BG")
                                    .setPostalCode(stripeAccountRequest.getPostalCode())
                                    .setCountry("BG")
                                    .build())
                            .setPhone(stripeAccountRequest.getPhoneNumber())
                            .build())
                    .setExternalAccount(bankAccountToken)
                    .build();

            Account account = Account.create(accountCreateParams);
            account.setPayoutsEnabled(true);

            User user = userRepository.findByEmail(stripeAccountRequest.getEmail()).orElse(null);

            if (user != null) {
                user.setStripeAccountId(account.getId());
            } else {
                throw new RuntimeException("Failed to create Stripe account.");
            }
            userRepository.save(user);

            return account.getId();
        } catch (StripeException e) {
            log.error("Failed to create Stripe account: {}", e.getMessage());
            throw new RuntimeException("Failed to create Stripe account.", e);
        }
    }

    @Override
    public String subscriptionWithCheckoutPage(Checkout checkout) {
        Stripe.apiKey = stripeApiKey;

        SessionCreateParams params = new SessionCreateParams.Builder()
                .setSuccessUrl(checkout.getSuccessUrl())
                .setCancelUrl(checkout.getCancelUrl())
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .addLineItem(new SessionCreateParams.LineItem.Builder()
                        .setQuantity(1L)
                        .setPrice(checkout.getPriceId())
                        .build())
                .setCustomerEmail(checkout.getEmail())
                .build();

        try {
            Session session = Session.create(params);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sessionId", session.getId());
            return gson.toJson(responseData);
        } catch (Exception e) {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("message", e.getMessage());
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("error", messageData);
            return gson.toJson(responseData);
        }
    }

    @Override
    public String getProductPrice(String priceId) {
        Stripe.apiKey = stripeApiKey;

        try {
            Price price = Price.retrieve(priceId);

            return gson.toJson(price);
        } catch (StripeException e) {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("message", e.getMessage());
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("error", messageData);
            return gson.toJson(responseData);
        }
    }

    public Map<String, String> getMetadataFromPaymentIntent(String sessionId) {
        Stripe.apiKey = stripeApiKey;
        try {
            // Retrieve the session
            Session session = Session.retrieve(sessionId);

            // Get the PaymentIntent ID from the session
            String paymentIntentId = session.getPaymentIntent();

            if (paymentIntentId == null) {
                // Handle the case where there is no PaymentIntent associated with the session
                log.error("No PaymentIntent associated with the session: {}", sessionId);
                return Collections.emptyMap();
            }
            // Access the metadata
            Map<String, String> metadata = session.getMetadata();
            return metadata;
        } catch (StripeException e) {
            log.error("Failed to retrieve PaymentIntent or Session: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }


    @Override
    public ResponseEntity<String> handleStripeWebhook(String payload, String sigHeader) {
        String endpointSecret = "whsec_cc0df325d4cf2f830514ec91c6e23dde3a856062b86ec456d8aa4791581aa91d";

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            System.out.println("Failed signature verification");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error handling webhook event");
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        }

        switch (event.getType()) {
            case "checkout.session.completed": {
                String userEmail = extractUserEmailFromPayload(payload);

                String sessionId = null;
                Session session = (Session) stripeObject;
                sessionId = session.getId();
                Map<String, String> metadata = getMetadataFromPaymentIntent(sessionId);

                if (!metadata.isEmpty() && metadata.containsKey("PAYMENT_TYPE") && "PAYMENT".equals(metadata.get("PAYMENT_TYPE"))) {
                    String offerIdString = metadata.get("offerId");
                    Integer offerId = Integer.parseInt(offerIdString);
                    Offer offer = offerRepository.findById(offerId).orElseThrow();
                    offer.setOfferStatus(OfferStatus.ACCEPTED);
                    offerRepository.save(offer);
                } else if (!metadata.isEmpty() && metadata.containsKey("serviceType") && "VIP".equals(metadata.get("serviceType"))) {
                    try {
                        Customer customer = Customer.list(CustomerListParams.builder().setEmail(userEmail).build()).getData().get(0);
                        String customerId = customer.getId();
                        PaymentIntentListParams paymentIntentParams = PaymentIntentListParams.builder()
                                .setCustomer(customerId)
                                .build();
                        PaymentIntentCollection paymentIntents = PaymentIntent.list(paymentIntentParams);

                        PaymentIntent latestPaymentIntent = paymentIntents.getData().stream()
                                .max(Comparator.comparing(PaymentIntent::getCreated))
                                .orElse(null);

                        String serviceIdString = metadata.get("serviceId");
                        Integer serviceId = Integer.parseInt(serviceIdString);

                        Optional<com.service.marketplace.persistence.entity.Service> serviceOptional = serviceRepository.findById(serviceId);

                        Optional.ofNullable(latestPaymentIntent)
                                .filter(paymentIntent -> serviceOptional.isPresent())
                                .ifPresent(s -> updateVipService(serviceOptional.get(), s));

                        User user = userRepository.findByEmail(userEmail).orElseThrow();

                        String emailSubject = "Congratulations on upgrading your service to VIP!";
                        String emailBody = String.format("Dear %s %s,\n" +
                                "\n" +
                                "Congratulations on successfully upgrading your service to VIP with Service Marketplace!\n" +
                                "\n" +
                                "We're delighted to inform you that your service is now part of our exclusive VIP services. As having a VIP service, you'll enjoy premium features and benefits tailored to enhance your experience.\n" +
                                "\n" +
                                "Thank you for choosing to upgrade your service with us. If you have any questions or need assistance, feel free to reach out to our support team.\n" +
                                "\n" +
                                "Best regards,\n" +
                                "\n" +
                                "Service Marketplace Team", user.getFirstName(), user.getLastName());

                        emailSenderService.sendSimpleEmail(user.getEmail(), emailSubject, emailBody);


                        return ResponseEntity.status(HttpStatus.OK).body("VIP service created");
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error handling webhook event");
                    }
                } else {
                    try {
                        Customer customer = Customer.list(CustomerListParams.builder().setEmail(userEmail).build()).getData().get(0);
                        String customerId = customer.getId();

                        List<Subscription> subscriptions;
                        try {
                            subscriptions = Subscription.list(SubscriptionListParams.builder().setCustomer(customerId).build()).getData();
                        } catch (StripeException e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error handling webhook event");
                        }

                        boolean hasActiveSubscription = subscriptions.stream().anyMatch(subscription -> "active".equals(subscription.getStatus()));
                        User userToBeUpdated = userRepository.findByEmail(userEmail).orElseThrow();
                        if (hasActiveSubscription) {
                            userService.updateUserRoleToProvider(userToBeUpdated.getId());
                        }

                        long currentPeriodStart = subscriptions.get(0).getCurrentPeriodStart();
                        long currentPeriodEnd = subscriptions.get(0).getCurrentPeriodEnd();

                        java.util.Date startDate = new java.util.Date(currentPeriodStart * 1000);
                        java.util.Date endDate = new java.util.Date(currentPeriodEnd * 1000);

                        com.service.marketplace.persistence.entity.Subscription subscription = new com.service.marketplace.persistence.entity.Subscription();
                        subscription.setStripeId(subscriptions.get(0).getId());
                        subscription.setStartDate(startDate);
                        subscription.setEndDate(endDate);
                        subscription.setUser(userToBeUpdated);
                        subscription.setActive("active".equals(subscriptions.get(0).getStatus()));

                        subscriptionRepository.save(subscription);

                        List<com.service.marketplace.persistence.entity.Service> services = serviceRepository.findByProvider(userToBeUpdated);
                        if (!services.isEmpty()) {
                            serviceService.makeServicesActive(userToBeUpdated.getId());
                        }

                        String emailSubject = "Thank You for Subscribing!";
                        String emailBody = String.format("Dear %s %s,\n" +
                                "\n" +
                                "Congratulations! Your subscription to Service Marketplace is now active. \uD83C\uDF89\n" +
                                "\n" +
                                "Thank you for choosing us! We appreciate your trust and look forward to providing you with a seamless experience. As a subscriber, you gain access to exclusive features and updates.\n" +
                                "\n" +
                                "If you have any questions or need assistance, feel free to reach out. We're here to help!\n" +
                                "\n" +
                                "Best regards,\n" +
                                "\n" +
                                "Service Marketplace Team", userToBeUpdated.getFirstName(), userToBeUpdated.getLastName());

                        emailSenderService.sendSimpleEmail(userEmail, emailSubject, emailBody);
                    } catch (StripeException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error handling webhook event");

                    }
                }
            }
            break;

            case "customer.subscription.created": {
                break;
            }
            case "customer.subscription.deleted": {
                break;
            }
            case "customer.subscription.updated": {
                System.out.println("Webhook for updated subscription");
                break;
            }
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

        return ResponseEntity.ok().build();
    }


    public ResponseEntity<String> cancelSubscription(String stripeId) {
        Stripe.apiKey = stripeApiKey;

        try {
            Subscription subscription = Subscription.retrieve(stripeId);

            if ("active".equals(subscription.getStatus())) {
                SubscriptionUpdateParams params =
                        SubscriptionUpdateParams.builder().setCancelAtPeriodEnd(true).build();

                Subscription canceledSubscription = subscription.update(params);
                com.service.marketplace.persistence.entity.Subscription existingSubscription = subscriptionRepository.findByStripeId(stripeId);
                existingSubscription.setCancelled(true);
                subscriptionRepository.save(existingSubscription);

                User user = userRepository.findById(existingSubscription.getUser().getId()).orElseThrow();

                String emailSubject = "We're Sorry to See You Go";
                String emailBody = String.format("Dear %s %s,\n" +
                        "\n" +
                        "We're sorry to inform you that your subscription with Service Marketplace has been canceled.\n" +
                        "\n" +
                        "We value your past support and hope to serve you again in the future. If you have any feedback on how we can improve, please don't hesitate to share it with us.\n" +
                        "\n" +
                        "Should you have any questions or require further assistance, please feel free to reach out to our support team.\n" +
                        "\n" +
                        "Best regards,\n" +
                        "\n" +
                        "Service Marketplace Team", user.getFirstName(), user.getLastName());

                emailSenderService.sendSimpleEmail(user.getEmail(), emailSubject, emailBody);

                if (canceledSubscription.getCancelAtPeriodEnd()) {
                    return ResponseEntity.ok("Subscription is successfully canceled.");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The subscription cancellation was unsuccessful.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The subscription is not active and it cannot be canceled.");
            }
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error with stripe interaction: " + e.getMessage());
        }
    }

    private String extractUserEmailFromPayload(String payload) {
        try {
            JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();

            if (jsonObject.has("data") && !jsonObject.get("data").isJsonNull()) {
                JsonObject data = jsonObject.getAsJsonObject("data").getAsJsonObject("object").getAsJsonObject("customer_details");
                if (data.has("email")) {
                    String email = data.get("email").getAsString();
                    return email;
                } else {
                    System.err.println("Missing email field in payload");
                    return null;
                }
            } else {
                System.err.println("Missing or null 'data' field in payload");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error extracting user email from payload: " + e.getMessage());
            return null;
        }
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void checkSubscriptionsStatus() {
        Stripe.apiKey = stripeApiKey;

        List<com.service.marketplace.persistence.entity.Subscription> subscriptions = subscriptionRepository.findByIsActiveTrue();

        for (com.service.marketplace.persistence.entity.Subscription subscription : subscriptions) {
            try {
                Subscription stripeSubscription = Subscription.retrieve(subscription.getStripeId());

                if ("canceled".equals(stripeSubscription.getStatus())) {
                    User user = userRepository.findById(subscription.getUser().getId()).orElse(null);

                    subscription.setActive(false);
                    subscriptionRepository.save(subscription);

                    Set<Role> userRoles = user.getRoles();
                    Role role = new Role("PROVIDER");
                    userRoles.remove(role);
                    user.setRoles(userRoles);
                    userRepository.save(user);

                    serviceService.makeServicesInactive(user.getId());
                }
            } catch (StripeException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    public String createProduct(OfferPaymentRequest offerPaymentRequest) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        ProductCreateParams params =
                ProductCreateParams.builder()
                        .setName(offerPaymentRequest.getDescription())
                        .setDefaultPriceData(ProductCreateParams.DefaultPriceData.builder()
                                .setUnitAmount(offerPaymentRequest.getPrice().longValueExact() * 100).setCurrency("usd").build()).build();

        Product product = Product.create(params);
        Offer existingOffer = offerRepository.findById(offerPaymentRequest.getOfferId()).orElse(null);
        existingOffer.setProductId(product.getId());
        offerRepository.save(existingOffer);
        return product.getId();
    }

    public String payCheckout(OfferPaymentRequest offerPaymentRequest) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        Product product = Product.retrieve(createProduct(offerPaymentRequest));
        User user = userRepository.findById(Integer.valueOf(offerPaymentRequest.getUserId())).orElse(null);

        SessionCreateParams params = new SessionCreateParams.Builder()
                .setSuccessUrl(offerPaymentRequest.getSuccessUrl())
                .setCancelUrl(offerPaymentRequest.getCancelUrl())
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addLineItem(new SessionCreateParams.LineItem.Builder()
                        .setQuantity(1L)
                        .setPrice(product.getDefaultPrice())
                        .build()).setPaymentIntentData(SessionCreateParams.PaymentIntentData.builder()
                        .setTransferData(SessionCreateParams.PaymentIntentData.TransferData.builder()
                                .setDestination(user.getStripeAccountId()).build()).build()
                )
                .setCustomerEmail(user.getEmail())
                .putMetadata("PAYMENT_TYPE", "PAYMENT")
                .putMetadata("offerId", String.valueOf(offerPaymentRequest.getOfferId()))
                .build();

        try {
            Session session = Session.create(params);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sessionId", session.getId());
            return gson.toJson(responseData);
        } catch (Exception e) {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("message", e.getMessage());
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("error", messageData);
            return gson.toJson(responseData);
        }
    }

    @Override
    public String vipWithCheckoutPage(Checkout checkout) {
        Stripe.apiKey = stripeApiKey;

        SessionCreateParams params = new SessionCreateParams.Builder()
                .setSuccessUrl(checkout.getSuccessUrl())
                .setCancelUrl(checkout.getCancelUrl())
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addLineItem(new SessionCreateParams.LineItem.Builder()
                        .setQuantity(1L)
                        .setPrice(checkout.getPriceId())
                        .build())
                .putMetadata("serviceType", "VIP")
                .putMetadata("serviceId", checkout.getServiceId())
                .setCustomerEmail(checkout.getEmail())
                .build();


        try {
            Session session = Session.create(params);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sessionId", session.getId());
            return gson.toJson(responseData);
        } catch (Exception e) {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("message", e.getMessage());
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("error", messageData);
            return gson.toJson(responseData);
        }
    }


    private void updateVipService(com.service.marketplace.persistence.entity.Service service, PaymentIntent latestPaymentIntent) {
        com.service.marketplace.persistence.entity.VipService vipService = new com.service.marketplace.persistence.entity.VipService();
        vipService.setStripeId(latestPaymentIntent.getId());
        vipService.setStartDate(new Date(latestPaymentIntent.getCreated() * 1000L));
        vipService.setEndDate(new Date((latestPaymentIntent.getCreated() + 14L * 24 * 60 * 60) * 1000L));
        vipService.setActive(true);
        vipService.setService(service);

        service.setVip(true);
        serviceRepository.save(service);
        vipServiceRepository.save(vipService);
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void checkVipServiceStatus() {
        Stripe.apiKey = stripeApiKey;


        List<com.service.marketplace.persistence.entity.VipService> vipServices = vipServiceRepository.findAll();

        for (com.service.marketplace.persistence.entity.VipService vipService : vipServices) {
            try {
                Date endDate = vipService.getEndDate();
                Instant instant = endDate.toInstant();
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime endDateAsLocalDateTime = instant.atZone(zoneId).toLocalDateTime();
                PaymentIntent stripeVipService = PaymentIntent.retrieve(vipService.getStripeId());
                if (LocalDateTime.now().isAfter(endDateAsLocalDateTime)) {
                    com.service.marketplace.persistence.entity.Service service = serviceRepository.findById(vipService.getService().getId()).orElse(null);
                    service.setVip(false);
                    serviceRepository.save(service);
                    vipService.setActive(false);
                    vipServiceRepository.save(vipService);
                }
            } catch (StripeException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

}
