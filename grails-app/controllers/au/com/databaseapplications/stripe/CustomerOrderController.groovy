package au.com.databaseapplications.stripe

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class CustomerOrderController {

    CustomerOrderService customerOrderService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond customerOrderService.list(params), model:[customerOrderCount: customerOrderService.count()]
    }

    def show(Long id) {
        respond customerOrderService.get(id)
    }

    def create() {
        respond new CustomerOrder(params)
    }

    def save(CustomerOrder customerOrder) {
        if (customerOrder == null) {
            notFound()
            return
        }

        try {
            customerOrderService.save(customerOrder)
        } catch (ValidationException e) {
            respond customerOrder.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'customerOrder.label', default: 'CustomerOrder'), customerOrder.id])
                redirect customerOrder
            }
            '*' { respond customerOrder, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond customerOrderService.get(id)
    }

    def update(CustomerOrder customerOrder) {
        if (customerOrder == null) {
            notFound()
            return
        }

        try {
            customerOrderService.save(customerOrder)
        } catch (ValidationException e) {
            respond customerOrder.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'customerOrder.label', default: 'CustomerOrder'), customerOrder.id])
                redirect customerOrder
            }
            '*'{ respond customerOrder, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        customerOrderService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'customerOrder.label', default: 'CustomerOrder'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'customerOrder.label', default: 'CustomerOrder'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
