
package com.security.V9_Marzo_25.controller;

import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import java.util.HashMap;
import java.util.Map;
/*Esta es la version del controlador
 * usando el formulario de stripe*/

/*
@RestController
@RequestMapping("/api/webhook")
public class WebhookController {
	
	

	private static final String ENDPOINT_SECRET =
			"whsec_4eefddb3d7a4bfa3e759824ec14b10c3d7d7edac7710c3611c4b8a99979031ca";
	


	 @PostMapping("/stripe")
	    public ResponseEntity<String> handleStripeWebhook(
	            @RequestBody String payload, // Cuerpo del evento en formato JSON
	            @RequestHeader("Stripe-Signature") String sigHeader) { // Cabecera de firma para validación
	        try {
	            // Validar la firma del webhook
	            Event event = Webhook.constructEvent(
	                    payload,    // El cuerpo de la solicitud
	                    sigHeader,  // La cabecera "Stripe-Signature"
	                    ENDPOINT_SECRET // Tu clave secreta del webhook
	            );

	            // Procesar el evento basado en su tipo
	            switch (event.getType()) {
	                case "payment_intent.succeeded": // Pago exitoso
	                    handlePaymentIntentSucceeded(event);
	                    break;
	                case "payment_intent.payment_failed": // Pago fallido
	                    handlePaymentIntentFailed(event);
	                    break;
	                case "customer.created": // Nuevo cliente creado
	                    handleCustomerCreated(event);
	                    break;
	                default:
	                    System.out.println("Evento no manejado: " + event.getType());
	            }

	            // Responder a Stripe que el evento fue procesado
	            return ResponseEntity.ok("Evento procesado exitosamente");
	        } catch (SignatureVerificationException e) {
	            // La firma no es válida, rechaza la solicitud
	            System.out.println("Firma del webhook no válida: " + e.getMessage());
	            return ResponseEntity.status(400).body("Firma no válida");
	        } catch (Exception e) {
	            // Error general al procesar el evento
	            System.out.println("Error procesando el webhook: " + e.getMessage());
	            e.printStackTrace();
	            return ResponseEntity.status(500).body("Error interno al procesar el evento");
	        }
	    }

	    private void handlePaymentIntentSucceeded(Event event) {
	        // Deserializar los datos del evento
	        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
	                .getObject()
	                .orElse(null);

	        if (paymentIntent != null) {
	            System.out.println("Pago exitoso para el PaymentIntent ID: " + paymentIntent.getId());
	            // Lógica adicional: registrar en base de datos, enviar notificación, etc.
	        }
	    }

	    private void handlePaymentIntentFailed(Event event) {
	        // Deserializar los datos del evento
	        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
	                .getObject()
	                .orElse(null);

	        if (paymentIntent != null) {
	            System.out.println("Pago fallido para el PaymentIntent ID: " + paymentIntent.getId());
	            // Lógica adicional: manejar fallos, notificar al usuario, etc.
	        }
	    }

	    private void handleCustomerCreated(Event event) {
	        // Deserializar los datos del evento
	        Customer customer = (Customer) event.getDataObjectDeserializer()
	                .getObject()
	                .orElse(null);

	        if (customer != null) {
	            System.out.println("Cliente creado con ID: " + customer.getId());
	            // Lógica adicional: registrar cliente en base de datos, etc.
	        }
	    }
}
*/