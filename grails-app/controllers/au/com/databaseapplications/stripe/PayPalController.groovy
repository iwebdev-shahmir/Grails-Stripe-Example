package au.com.databaseapplications.stripe

import groovy.json.*

import com.paypal.api.payments.Amount
import com.paypal.api.payments.Links
import com.paypal.api.payments.Payer
import com.paypal.api.payments.PayerInfo
import com.paypal.api.payments.Payment
import com.paypal.api.payments.PaymentExecution
import com.paypal.api.payments.RedirectUrls
import com.paypal.api.payments.Transaction
import com.paypal.api.payments.Item
import com.paypal.api.payments.ItemList
import com.paypal.base.rest.APIContext
import com.paypal.base.rest.PayPalRESTException

class PayPalController {

    //def springSecurityService

    private final String clientId = "your-client-id"
    private final String clientSecret = "your-client-secret"

    private final String payPalTesting = "sandbox"
    private final String payPalLive = "live"
    private final String mode = payPalTesting  // change as needed
    private Map<String, String> map = new HashMap<String, String>()
    private StringBuilder builder = new StringBuilder()

    def index() {
    }

    // This method will be called twice.  The first time the else block will execute, the second
    // time the if block.
    Payment createPayment() {
        Payment payment = new Payment()
        Payment createdPayment
        APIContext apiContext = new APIContext(clientId, clientSecret, mode)
        if(params['PayerID']) {
            if (params["guid"]) {
                payment.setId(map.get(params["guid"]))
            }

            PaymentExecution paymentExecution = new PaymentExecution()
            paymentExecution.setPayerId(params["PayerID"])
            try {
                createdPayment = payment.execute(apiContext, paymentExecution)
                clearBuilder()
                builder.append("PayPalController completePayment no exception\n")
                String lr = Payment.getLastResponse()
                int index = lr.indexOf('receipt_id')
                String receiptId = lr.getAt(index+13..index+28)
                builder.append(lr + "\n")
                builder.append("*Receipt Id*: ${receiptId}")
                print(builder.toString())
            }
            catch (PayPalRESTException e) {
                clearBuilder()
                builder.append("PayPalController completePayment PayPalRESTException\n")
                builder.append(Payment.getLastRequest() + "\n")
                builder.append(e.getMessage() + "\n")
                print(builder.toString())
            }
            session.removeAttribute('CART')
            render(getPayerDetails(createdPayment))
        }
        else{
            Cart cart = session.CART
            // The payer who bought something
            Payer payer = new Payer()
            payer.setPaymentMethod("paypal")
            /*
            def user = springSecurityService.isLoggedIn() ? springSecurityService.getCurrentUser() : null
            if(user) {
                PayerInfo payerInfo = new PayerInfo()
                payerInfo.setEmail(user?.emailAddress)
                payer.setPayerInfo(payerInfo)
            }
            */
            // If the total amount is made up of subamounts like tax, shipping, etc. one should
            // have a com.paypal.api.payments.Details to take care of that.
            /*
            Details details = new Details();
            details.setShipping("1");
            details.setSubtotal("5");
            details.setTax("1");
            */

            // The total amount of the purchase
            Amount amount = new Amount();
            amount.setCurrency("AUD");
            amount.setTotal(cart.getTotal().toString());
            // amount.setDetails(details)

            // Now the purchase transaction
            Transaction transaction = new Transaction()
            transaction.setAmount(amount)
            transaction.setDescription("Online purchase from Jack on ${new Date().format('dd MMM yyyy - HH:mm')}")

            // Now to take care of all the items purchased
            List<com.paypal.api.payments.Item> items = new ArrayList<com.paypal.api.payments.Item>()
            ItemList itemList = new ItemList()
            cart.items.each { li ->  // li for line item
                com.paypal.api.payments.Item item = new Item()
                item.setName(li?.item?.name).setQuantity(li.quantity?.toString()).setCurrency("AUD").setPrice((li?.price?.round(2)).toString())
                items.add(item)
            }
            itemList.setItems(items)
            transaction.setItemList(itemList)

            // PayPal wants the transactions in a list, even if there is only one
            List<Transaction> transactions = new ArrayList<Transaction>()
            transactions.add(transaction)

            // Tell PayPal where to send the results of the transaction payment
            String guid = UUID.randomUUID().toString().replaceAll("-", "") // to uniquely tag a response
            RedirectUrls redirectUrls = new RedirectUrls()
            redirectUrls.setCancelUrl("http://localhost:8080/payPal/cancel?guid=${guid}")
            redirectUrls.setReturnUrl("http://localhost:8080/payPal/createPayment?guid=${guid}")

            // Now create a payment that will tie all the above together
            payment.setIntent("sale")
            payment.setPayer(payer)
            payment.setTransactions(transactions)
            payment.setRedirectUrls(redirectUrls)

            // Create a payment by posting to the APIService
            // using a valid AccessToken
            // The return object contains the status;
            try {
                createdPayment = payment.create(apiContext)
                println("************Created payment with id = "
                        + createdPayment.getId() + " and status = "
                        + createdPayment.getState())
                map.put(guid, createdPayment.getId())
                def json = Payment.getLastResponse()
                def slurper = new JsonSlurper()
                println("Start of Slurper in createPayment-----")
                println(slurper.parseText(json))
                println("End of Slurper-------------")
                // Find the Payment Approval Url for this client and mode
                Iterator<Links> links = createdPayment.getLinks().iterator()
                while (links.hasNext()) {
                    Links link = links.next()
                    if (link.getRel().equalsIgnoreCase("approval_url")) {
                        redirect(url: link.getHref())
                    }
                }
            }
            catch (PayPalRESTException e) {
                clearBuilder()
                builder.append("PayPalController createPayment PayPalRESTException\n") builder.append(Payment.getLastRequest() + "\n")
                builder.append(e.getMessage() + "\n")
                render(builder.toString())
            }
        }
        return createdPayment
    }

    def cancel() {
        render("<p style='text-align: center;'><b>You cancelled, bozo</b></p>")
    }

    def clearBuilder() {
        if(builder.length() > 0) {
            builder.delete(0, builder.length())
        }
    }

    private String getPayerDetails(Payment payment) {
        clearBuilder()
        def payer = payment.getPayer()
        def payerInfo = payer.getPayerInfo()
        builder.append("First name: " + payerInfo.getFirstName() + ". Surname: " + payerInfo.getLastName() + "\n")
        builder.append("Email address: " + payerInfo.getEmail())
        return builder.toString()
    }
}
