package au.com.databaseapplications.stripe

class Customer {

    static hasMany = [orders: CustomerOrder]

    String name, surname, emailAddress

    static constraints = {
        name blank: false
        surname blank: false
        emailAddress blank: false, email: true, unique: true
    }

    String toString(){
        name + " " + surname
    }
}
