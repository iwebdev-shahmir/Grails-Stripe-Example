package au.com.databaseapplications.stripe

class LineItem {

    static belongsTo = [item: Item, customerOder: CustomerOrder]

    static transients = ['total']

    long added
    Double price
    Integer quantity = 1

    static constraints = {
        price blank: false, matches: /\d+\.\d{2}/
        quantity range: 1..10
    }

    String toString(){
        def name = item?.name ?: 'undefined'
        quantity + " " + name + " at \$" +price + " each"
    }

    Double getTotal(){
        return quantity*price
    }
}
