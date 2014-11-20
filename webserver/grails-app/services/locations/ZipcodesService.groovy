package locations

import static org.springframework.http.HttpStatus.*
import static org.springframework.http.HttpMethod.*
import api.locations.exceptions.BadRequestException
import api.locations.exceptions.ConflictException
import api.locations.exceptions.NotFoundException
import javax.servlet.http.HttpServletResponse
import grails.plugin.gson.converters.GSON
import grails.transaction.*
import grails.converters.*
import locations.Zipcodes
import locations.Location

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import api.locations.ValidAccess
import org.joda.time.LocalTime;

class ZipcodesService {

    static transactional = "mongo"
    def validAccess = new ValidAccess()
	//static transactional = true
	/*def jsonCol = []
	def jsonEst = []
	def jsonCountry = []*/

	def getZip(def zipId){

		Map jsonResult = [:]
		Map resultParentLocation = [:]
		def colonia = ""
		def jsonData = []
		def jsonData2 = []
		def temporalParent
		def lo

		if (!zipId){
            throw new NotFoundException("You must provider zipId")
		}

		if(zipId.length() != 5){
			throw new ConflictException("Zip does not meet the criteria")
		}

		def zip = Zipcodes.findAllByZipcode(zipId, [ sort: "zipcode", order: "asc"])

		if (!zip){
            throw new NotFoundException("The zipId not found")
		}

		for(temporal in zip.colonId){
			lo = Location.findByLocationID(temporal, [ sort: "name", order: "asc"])
			/*jsonData2 = findMun(lo.parentLocationId)*/
			lo.each{
				jsonData.add(
					locationID:it.locationID,
					name:it.name
				)
			}
			temporalParent = lo.parentLocationId
		}

		jsonData2 = getParentLocation(lo.parentLocationId)
		temporalParent = null

		jsonResult.zipcode = zipId
		jsonResult.parent_location = jsonData2
		jsonResult.children_locations = jsonData
		/*jsonResult.est = jsonEst
		jsonResult.country = jsonCountry*/
		jsonResult

	}

	//Metodos de Prueba para los datos de colonia
	def findMun(def parentId){
		def arreglo = []
		def mun
		def munA = ""
		mun = Location.findByLocationID(parentId, [ sort: "name", order: "asc"])
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
		estate = Location.findByLocationID(parentId, [ sort: "name", order: "asc"])
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
		country = Location.findByLocationID(parentId, [ sort: "name", order: "asc"])
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

	//Metodos Finales para los datos de colonia
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

            def parentLocation = Location.findByLocationID(parentLocationId, [ sort: "name", order: "asc"])

            jsonParent.location_id = parentLocation.locationID
            jsonParent.name = parentLocation.name
            jsonParent.parent_location_id = parentLocation.parentLocationId
        }

        jsonParent
    }

    def createZip(def colon_Id, def jsonZipcodes, def params){
    	Map jsonResult = [:]
        def responseMenssage = ''

        /*if (!params.access_token){
            throw new BadRequestException ("You must provider de access_token")
        }

        def access_token = validAccess.validAccessToken(params.access_token)
        def user_id = params.access_token.split('_')[2]*/
        
        if (!Location.findByLocationID(colon_Id)){
            throw  new NotFoundException("The colonId = " + colon_Id + " not found")
        }

        def newZipcode =  new Zipcodes(
            zipcode:jsonZipcodes?.zip_code,
            colonId:colon_Id
        )

        if(!newZipcode.validate()) {
            newZipcode.errors.allErrors.each {
                responseMenssage += MessageFormat.format(it.defaultMessage, it.arguments) + " "
            }
            throw new BadRequestException(responseMenssage)
        }

        newZipcode.save()

        jsonResult.id                = newZipcode.zipcode
        jsonResult.parent_location   = newZipcode.colonId

        jsonResult
    }

    def modifyZip(def zipId, def jsonZipcode, def params){
    	Map jsonResult = [:]
        def responseMessage = ''

        if (!params.access_token){
            throw new BadRequestException ("You must provider de access_token")
        }

        def access_token = validAccess.validAccessToken(params.access_token)
        def user_id = params.access_token.split('_')[2]

        if (!zipId){
            throw  new NotFoundException("You must provider zipId")
        }

        def obteinedZipcode = Zipcodes.findByZipcodeAndColonId(zipId, jsonZipcode?.lastColonId, [ sort: "zipcode", order: "asc"])

        if (!obteinedZipcode){
            throw new  NotFoundException("The Zip with Id="+zipId+" not found")
        }

        obteinedZipcode.colonId = jsonZipcode?.newColonId
        obteinedZipcode.dateUpdate = new Date()

        if(!obteinedZipcode.validate()){
            obteinedZipcode.errors.allErrors.each {
                responseMessage += MessageFormat.format(it.defaultMessage, it.arguments) + " "
            }
            throw new BadRequestException(responseMessage)
        }

        obteinedZipcode.save()

        jsonResult = getZip(zipId)

        jsonResult
    }
}
