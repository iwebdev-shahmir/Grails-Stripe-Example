package au.com.databaseapplications.stripe

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class CustomerOrderServiceSpec extends Specification {

    CustomerOrderService customerOrderService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new CustomerOrder(...).save(flush: true, failOnError: true)
        //new CustomerOrder(...).save(flush: true, failOnError: true)
        //CustomerOrder customerOrder = new CustomerOrder(...).save(flush: true, failOnError: true)
        //new CustomerOrder(...).save(flush: true, failOnError: true)
        //new CustomerOrder(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //customerOrder.id
    }

    void "test get"() {
        setupData()

        expect:
        customerOrderService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<CustomerOrder> customerOrderList = customerOrderService.list(max: 2, offset: 2)

        then:
        customerOrderList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        customerOrderService.count() == 5
    }

    void "test delete"() {
        Long customerOrderId = setupData()

        expect:
        customerOrderService.count() == 5

        when:
        customerOrderService.delete(customerOrderId)
        sessionFactory.currentSession.flush()

        then:
        customerOrderService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        CustomerOrder customerOrder = new CustomerOrder()
        customerOrderService.save(customerOrder)

        then:
        customerOrder.id != null
    }
}
