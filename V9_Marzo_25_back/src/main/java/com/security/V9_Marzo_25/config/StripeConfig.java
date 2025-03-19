package com.security.V9_Marzo_25.config;
import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;



@Configuration
public class StripeConfig {

    @PostConstruct
    public void init() {
        Stripe.apiKey = "sk_test_51QXJFaFSJlNz9yxYdc0CcCtBPfPYca8fxhV3h4YNr5DCyePwxV9WgBSHlGJvoPJaUTuClgykGSBuB2Ewj0gPFJ5E00hIjO9auq"; // Clave secreta desde tu dashboard de Stripe
    }
}
