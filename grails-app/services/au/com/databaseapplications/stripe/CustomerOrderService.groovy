package au.com.databaseapplications.stripe

import grails.gorm.services.Service

@Service(CustomerOrder)
interface CustomerOrderService {

    CustomerOrder get(Serializable id)

    List<CustomerOrder> list(Map args)

    Long count()

    void delete(Serializable id)

    CustomerOrder save(CustomerOrder customerOrder)

}