package locations

import java.text.MessageFormat
import org.apache.ivy.plugins.conflict.ConflictManager
import grails.converters.*
import api.locations.exceptions.BadRequestException
import api.locations.exceptions.ConflictException
import api.locations.exceptions.NotFoundException

class LocationService {

    static transactional = "mongo"
	//static transactional = true
	def getLocation(def locationId){

		Map jsonResult = [:]
		def jsonChildren = []
		Map resultParentLocation = [:]

		if (!locationId){
            throw new NotFoundException("You must provider locationId")
		}

		def location = Location.findByLocationID(locationId)

		if (!location){
            throw new NotFoundException("The locationId not found")
		}

		def childrenLocations = Location.findAllByParentLocationId(locationId)
		childrenLocations.each{
			jsonChildren.add(
				locationId : it.locationID,
				name : it.name
			)
		}

		if(location.parentLocationId){
			def parentLocation = Location.findByLocationID(location.parentLocationId)
			resultParentLocation.id = parentLocation.locationID
			resultParentLocation.name = parentLocation.name
		}

		jsonResult.id = location.locationID
		jsonResult.name = location.name
		jsonResult.parent_location = resultParentLocation
		jsonResult.children_locations = jsonChildren

		jsonResult

	}

	def createLocation(def parentlocationId,def jsonLocation){

        Map jsonResult = [:]
        def responseMenssage = ''

        if (!Location.findByLocationID(parentlocationId)){
            throw  new NotFoundException("The locationId = "+parentlocationId+" not found")
        }

        def newLocation =  new Location(
            locationID:jsonLocation?.id,
            parentLocationId: parentlocationId,
            name:jsonLocation?.name
        )

        if(!newLocation.validate()) {
            newLocation.errors.allErrors.each {
                responseMenssage += MessageFormat.format(it.defaultMessage, it.arguments) + " "
            }
            throw new BadRequestException(responseMenssage)
        }

        newLocation.save()

        jsonResult.id                = newLocation.locationID
        jsonResult.name              = newLocation.name
        jsonResult.parent_location   = newLocation.parentLocationId

        jsonResult
    }

    def createLocation(def jsonLocation){

        Map jsonResult = [:]
        def responseMenssage = ''

        def newLocation = new Location(
            locationID:jsonLocation?.id,
            name:jsonLocation?.name
        )

        if(!newLocation.validate()){
            newLocation.errors.allErrors.each{
                responseMenssage+=MessageFormat.format(it.defaultMessage,it.arguments) + " "
            }
            throw new BadRequestException(responseMenssage)
        }

        newLocation.save()

        jsonResult.id                = newLocation.locationID
        jsonResult.name              = newLocation.name
        jsonResult.parent_location   = newLocation.parentLocationId

        jsonResult
    }

    def modifyLocation(def locationId, def jsonLocation){

        Map jsonResult = [:]
        def responseMessage = ''

        if (!locationId){
            throw  new NotFoundException("You must provider locationId")
        }

        def obteinedLocation = Location.findByLocationID(locationId)

        if (!obteinedLocation){
            throw new  NotFoundException("The Location with Id="+locationId+" not found")
        }

        obteinedLocation.name                = jsonLocation?.name
        obteinedLocation.parentLocationId    = jsonLocation?.parent_location_id
        obteinedLocation.status              = jsonLocation?.status
        obteinedLocation.dateUpdate          = new Date()

        if(!obteinedLocation.validate()){
            obteinedLocation.errors.allErrors.each {
                responseMessage += MessageFormat.format(it.defaultMessage, it.arguments) + " "
            }
            throw new BadRequestException(responseMessage)
        }

        obteinedLocation.save()

        jsonResult = getLocation(locationId)

        jsonResult
    }
}
