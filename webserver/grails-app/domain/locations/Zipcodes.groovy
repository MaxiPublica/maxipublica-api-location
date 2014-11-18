package locations

class Zipcodes {

    String zipcode
	String colonId //Funciona como el parentID
	Date dateRegistration = new Date()
	Date dateUpdate = new Date()
	String siteId = 'MP'
	
    static constraints = {
    	zipcode nullable:false, blank:false
    	colonId nullable:true, maxSize: 20
		dateRegistration nullable:false
		dateUpdate nullable:false
		siteId nullable:false
	}
}
