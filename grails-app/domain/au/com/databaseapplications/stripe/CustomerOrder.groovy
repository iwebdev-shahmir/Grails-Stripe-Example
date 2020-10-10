package au.com.databaseapplications.stripe

class CustomerOrder {

    static belongsTo = [customer: Customer]
    static hasMany = [lineItems: LineItem]
    static transients = ['total']

    String token
    String payPalSaleId  // used for refunds
    String stripeChargeId // used for refunds
    Date dateCreated
    boolean refunded = false

    static constraints = {
        token blank: false
        payPalSaleId nullable: true
        stripeChargeId nullable: true
    }

    String toString(){
        def dte = dateCreated.format('dd MMM yyyy') ?: 'no date'
        def cst = customer.toString() ?: 'no customer'
        cst + " " + dte
    }

    Double getTotal(){
        def ttl = 0.0
        lineItems.each{
            ttl += it.getTotal()
        }
        return ttl
    }
}
