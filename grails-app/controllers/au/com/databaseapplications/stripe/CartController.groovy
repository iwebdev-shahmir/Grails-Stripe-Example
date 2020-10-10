package au.com.databaseapplications.stripe

class CartController {

    def itemService

    def index() {
        [items: Item.list(sort: 'name', order: 'asc')]
    }

    //Called by Ajax
    def addToCart() {
        Item itm = itemService.get(Long.parseLong(params['id']))
        LineItem li = new LineItem(item: itm, quantity: params['quantity'], price: itm.price, added: this.getTime())
        Cart cart
        if(session.CART){
            cart = session.CART
        }
        else{
            cart = new Cart()
        }
        cart.add(li)
        session.CART = cart
        render("<strong>You've added - ${li.toString()}</strong>")
    }

    def showCart() {
        Cart cart = session.CART
        if(cart.items.size() > 0){
            [items: cart.getItems(), total: cart.getTotal()]
        }
        else{
            render("<strong>There is nothing in your cart</strong>")
        }
    }

    def remove() {
        Cart cart = session.CART
        if(cart.items.size() > 0){
            cart.remove(params.added)
            session.CART = cart
            redirect(contoller:'cart', action:'showCart')
        }
        else{
            render("<strong>There is nothing in your cart</strong>")
        }
    }

    def update() {
        Cart cart = session.CART
        if(cart.items.size() > 0){
            cart.update(params.added, params.qty)
            session.CART = cart
            redirect(contoller:'cart', action:'showCart')
        }
        else{
            render("<strong>There is nothing in your cart</strong>")
        }
    }

    long getTime(){
        return Calendar.getInstance().getTimeInMillis()
    }
}
