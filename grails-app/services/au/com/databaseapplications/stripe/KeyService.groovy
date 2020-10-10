package au.com.databaseapplications.stripe

import grails.gorm.transactions.Transactional

@Transactional
class KeyService {

    private char[] goodChars = [ 'a', 'b', 'c', 'd', 'e', 'f', 'g',
'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
'2', '3', '4', '5', '6', '7', '8', '9']


    String getRandomKey(Integer length) {
        Random random = new Random()
        StringBuffer sb = new StringBuffer()
        for (int i = 0 ; i < length ; i++) {
            sb.append(goodChars[random.nextInt(goodChars.length)])
        }
        return sb.toString()
    }
}
