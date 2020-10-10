package au.com.databaseapplications.stripe

class Item {

    static hasMany = [lineItems: LineItem]

    String name
    String description
    Double price

    static constraints = {
        name(blank: false, unique: true)
        description(nullable: true)
        price(blank: false, matches: /\d+\.\d{2}/)
    }

    String toString(){
        name
    }
}
