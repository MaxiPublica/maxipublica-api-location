package locations

import javax.servlet.http.HttpServletResponse
import grails.converters.*
import grails.transaction.*
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.HttpMethod.*
import grails.plugin.gson.converters.GSON
import api.locations.exceptions.BadRequestException
import api.locations.exceptions.ConflictException
import api.locations.exceptions.NotFoundException
import locations.Zipcodes
import locations.Location

class ZipcodesService {

    //static transactional = "mongo"
	static transactional = true
	def jsonCol = []
	def jsonMun = []
	def jsonEst = []
	def jsonCountry = []

	def getZip(){
		Map jsonResult = [:]
		def jasonGeneral = []

		def zip = Zipcodes.getAll()

		if (!zip){
            throw new NotFoundException("The zips not found")
		}

		zip.each{
			jasonGeneral.add(
				zip_code : it.zipcode,
				colon_Id : it.colonId
			)
		}

		jsonResult.zips = jasonGeneral

		jsonResult
	}

	def getZip(def zipId){

		Map jsonResult = [:]
		Map resultParentLocation = [:]
		def colonia = ""
		jsonCol = []

		if (!zipId){
            throw new NotFoundException("You must provider zipId")
		}

		def zip = Zipcodes.findAllByZipcode(zipId)

		if (!zip){
            throw new NotFoundException("The zipId not found")
		}
		
		for(temporal in zip.colonId){
			def lo = Location.findByLocationID(temporal)
			jsonMun = getParentLocation(lo.parentLocationId)
			/*jsonMun = findMun(lo.parentLocationId)*/
			lo.each{
				jsonCol.add(
					locationID:it.locationID,
					name:it.name
				)
			}
		}

		jsonResult.zip_code = zipId
		jsonResult.col = jsonCol
		jsonResult.mun = jsonMun
		/*jsonResult.est = jsonEst
		jsonResult.country = jsonCountry*/

		jsonResult

	}

	def findMun(def parentId){
		def arreglo = []
		def mun
		def munA = ""
		mun = Location.findByLocationID(parentId)
		jsonEst = findEst(mun.parentLocationId)
		mun.each{
			if (munA != it.locationID) {
				arreglo.add(
					locationID:it.locationID,
					name:it.name
				)
				munA = it.locationID
			}
		}

		arreglo
	}

	def findEst(def parentId){
		def arreglo = []
		def estate
		def estateA = ""
		estate = Location.findByLocationID(parentId)
		jsonCountry = findCountry(estate.parentLocationId)
		estate.each{
			if (estateA != it.locationID) {
				arreglo.add(
					locationID:it.locationID,
					name:it.name
				)
				estateA = it.locationID
			}
		}
		arreglo
	}
	
	def findCountry(def parentId){
		def arreglo = []
		def country
		def countryA = ""
		country = Location.findByLocationID(parentId)
		country.each{
			if (countryA != it.locationID) {
				arreglo.add(
					locationID:it.locationID,
					name:it.name
				)
				countryA = it.locationID
			}
		}
		arreglo
	}

	//--------------------------------------------

	def getParentLocation(def parentLocationId){

        def resultParentsLocations = []

        while (parentLocationId){
            
            def parent = getParent(parentLocationId)

            resultParentsLocations.add(
                location_id : parent.location_id,
                name : parent.name
            )
            
            parentLocationId = parent.parent_location_id
        }
        
        resultParentsLocations
    }

    def getParent(def parentLocationId){

        def jsonParent = [:]

        if (parentLocationId){

            def parentLocation = Location.findByLocationID(parentLocationId)

            jsonParent.location_id = parentLocation.locationID
            jsonParent.name = parentLocation.name
            jsonParent.parent_location_id = parentLocation.parentLocationId
        }

        jsonParent
    }
}
