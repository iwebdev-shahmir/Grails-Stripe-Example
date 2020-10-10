package au.com.databaseapplications.stripe

import grails.gorm.transactions.Transactional
import java.text.DecimalFormat

@Transactional
class NumberFormattingService {

    String formatMoney(Double amount) {
        def pattern = "\$##,###.##"
        def moneyform = new DecimalFormat(pattern)
        return moneyform.format(amount)
    }

    String formatPercentage(Double percentage){
        def pattern1 = "###.##%"
        def percentform = new DecimalFormat(pattern1)
        return percentform.format(percentage)
    }
}
