package au.com.databaseapplications.stripe

import com.stripe.Stripe as Stripe
import com.stripe.exception.*
import com.stripe.model.*
import com.stripe.net.*

import groovy.json.JsonSlurper

class StripeController {

    def keyService

    def index() {
        Cart cart = session.CART
        if(cart.items.size() > 0){
            [total: cart.getTotal()]
        }
        else{
            flash.message = "There is nothing in your cart to pay for"
            [total: '0.00']
        }
    }

    def pay() {
        String token = request.getParameter("stripeToken")
        // Set your secret key: remember to change this to your live secret key in production
        // See your keys here: https://dashboard.stripe.com/account/apikeys
        Stripe.apiKey = "your-private-key"
        Integer amount = new Double(params['amount'])*100 as Integer // Stripe wants the amount in cents
        String stripeChargeId // used for refunds
        // Create a charge: this will charge the user's card
        try {
            Map<String, Object> chargeParams = new HashMap<String, Object>()
            chargeParams.put("amount", amount); // Amount in cents
            chargeParams.put("currency", "aud")
            chargeParams.put("source", token)
            chargeParams.put("description", "Order from Joe's Shop by ${params['name']} ${params['surname']} on ${new Date().format('dd MMM yyyy')}")
            /**
            Map<String, String> initialMetadata = new HashMap<String, String>()
            initialMetadata.put("order_id", "6735")
            chargeParams.put("metadata", initialMetadata)
             **/

            RequestOptions options = RequestOptions.builder().setIdempotencyKey(keyService.getRandomKey(22)).build()
            Charge charge = Charge.create(chargeParams, options)
            def x = charge.toString() =~ /((ch_\w+?)>)/
            stripeChargeId= x[0][2]
            def parser = new JsonSlurper()
            String json = charge.toString() - ~/(<.+\:\s)?/
            def obj = parser.parseText(json)
            println "*&^&*Getting the Stripe Charge Id from the parsed object: ${obj.id}"

        }
        catch (CardException e) {
            // Since it's a decline, CardException will be caught
            flash.message = "Payment failed: ${e.getMessage()}.  Code: ${e.getCode()}"
            redirect(action: "failedPayment")
            return
        }
        catch (RateLimitException e) {
            // Too many requests made to the API too quickly
            flash.message = "Rate limit exception: ${e.getMessage()}."
            redirect(action: "failedPayment")
            return
        }
        catch (InvalidRequestException e) {
            // Invalid parameters were supplied to Stripe's API
            flash.message = "Invalid parameters supplied: ${e.getMessage()}."
            redirect(action: "failedPayment")
            return
        }
        catch (AuthenticationException e) {
            // Authentication with Stripe's API failed
            // (maybe you changed API keys recently)
            flash.message = "Authentication exception: ${e.getMessage()}."
            redirect(action: "failedPayment")
            return
        }
        catch (ApiConnectionException e) {
            // Network communication with Stripe failed
            flash.message = "Network communication failed: ${e.getMessage()}."
            redirect(action: "failedPayment")
            return
        }
        catch (StripeException e) {
            // Display a very generic error to the user, and maybe send
            // yourself an email
            flash.message = "Stripe exception: ${e.getMessage()}."
            redirect(action: "failedPayment")
            return
        }
        catch (Exception e) {
            // Something else happened, completely unrelated to Stripe
            flash.message = "Generic exception: ${e.getMessage()}."
            redirect(action: "failedPayment")
            return
        }

        Customer customer = Customer.findByEmailAddress(params['emailAddress'])
        if(!customer){
            customer = new Customer(name: params['name'], surname: params['surname'], emailAddress: params['emailAddress'])
        }
        println "The customer: ${customer.toString()}"
        CustomerOrder order = new CustomerOrder(token: token, stripeChargeId: stripeChargeId)
        customer.addToOrders(order).save()
        def cart = session.CART
        cart.items.each{
            order.addToLineItems(it).save(flush: true)
        }
        flash.message = "Your order succeeded"
        session.removeAttribute('CART')
        redirect(controller: 'customerOrder', action: 'show', id: order.id)
    }

    def failedPayment() {

    }
}
