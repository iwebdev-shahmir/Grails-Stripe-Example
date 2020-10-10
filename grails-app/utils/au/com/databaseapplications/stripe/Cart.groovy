package au.com.databaseapplications.stripe

/**
 *
 * @author chris
 */
class Cart {

    List<au.com.databaseapplications.stripe.LineItem> items

    Cart(){
        items = new ArrayList<au.com.databaseapplications.stripe.LineItem>()
    }

    Double getTotal(){
        Double total = 0.00
        items.each{itm ->
            total += itm.price*itm.quantity
        }
        return total.round(2)
    }

    ArrayList<au.com.databaseapplications.stripe.LineItem> getItems(){
        return items
    }

    // Add is from the cart index view - all items for sale
    void add(au.com.databaseapplications.stripe.LineItem boughtItem){
        if(items?.contains(boughtItem)){
            items.each{ itm ->
                if(itm.item.equals(boughtItem.item)){
                    itm.quantity = itm.quantity + boughtItem.quantity
                }
            }
        }
        else{
            items.add(boughtItem)
        }
    }

    // Methods below from the showCart view - what actually is in the cart
    void remove(String ad){
        int idx = 0
        items.eachWithIndex{ itm, index ->
            if(itm.added.toString() == ad){
                idx = index
            }
        }
        items.remove(idx)
    }

    void update(String added, String newQuantity){
        items.each{itm ->
            if(itm.added.toString() == added){
                itm.quantity = new Integer(newQuantity)
            }
        }
    }
}
