package locations

import org.apache.ivy.plugins.conflict.ConflictManager
import api.locations.exceptions.BadRequestException
import api.locations.exceptions.ConflictException
import api.locations.exceptions.NotFoundException
import java.text.MessageFormat
import grails.converters.*
import locations.Zipcodes
import locations.Location

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import api.locations.ValidAccess

class LocationService {

    static transactional = "mongo"
    def validAccess = new ValidAccess()
	//static transactional = true
	def getLocation(def locationId){

		Map jsonResult = [:]
		def jsonChildren = []
		Map resultParentLocation = [:]
        Map arrayLocation = [:]
        def zipCode = null

		if (!locationId){
            throw new NotFoundException("You must provider locationId")
		}

		def location = Location.findByLocationID(locationId, [ sort: "name", order: "asc"])

		if (!location){
            throw new NotFoundException("The locationId not found")
		}

        //Traeme a los hijos que me tengan como padre (darth vader)
		def childrenLocations = Location.findAllByParentLocationId(locationId, [ sort: "name", order: "asc"])
		if(childrenLocations){
            childrenLocations.each{
                jsonChildren.add(
                    locationId : it.locationID,
                    name : it.name
                )
            }
        //Si no tengo hijos entonces traeme mi codigo postal correspondiente
        }else{
            childrenLocations = Zipcodes.findByColonId(locationId, [ sort: "zipcode", order: "asc"])
            childrenLocations.each{
                zipCode = it.zipcode
            }
        }

        def parentLocation
        //Si tengo padre entonces busca a los papas de mi papa
		if(location.parentLocationId){
            //Traeme a mi padre incluyendo a mis antecesores
			parentLocation = getParentLocation(location.parentLocationId)
            //Location.findByLocationID(location.parentLocationId)
			//resultParentLocation.id = parentLocation.locationID
			//resultParentLocation.name = parentLocation.name
		}

		if(zipCode != null){
            parentLocation.add(
                location_id:location.locationID,
                name: location.name
                )
            arrayLocation.neighborhood = parentLocation[3]
            arrayLocation.city = parentLocation[0]
            arrayLocation.state = parentLocation[1]
            arrayLocation.country = parentLocation[2]
            
            jsonResult.zipcode = zipCode
    		jsonResult.location = arrayLocation
        }else{
            jsonResult.id = location.locationID
            jsonResult.name = location.name
            jsonResult.parent_location = parentLocation
            jsonResult.children_locations = jsonChildren
        }
		jsonResult

	}

    //Recive como parametro el id de mi papa
    def getParentLocation(def parentLocationId){

        def resultParentsLocations = []

        //si el dato no el vacio o null entonces entra al ciclo
        while (parentLocationId){
            
            //Realiza la busqueda de todos mis antecesores (papa, abuelo, y bisabuelo)
            def parent = getParent(parentLocationId)

            //Se agregan los datos a un array para crear el json de antecesores
            resultParentsLocations.add(
                location_id : parent.location_id,
                name : parent.name
            )
            //se agrega el id del siguiente antecesor a la variable de control del 
            //while, si ya no encuentra ningun antecesor entonces el ciclo se rompe
            //de esta manera se buscan los padres
            parentLocationId = parent.parent_location_id
        }
        
        resultParentsLocations
    }

    //Busca el padre correspondiente del id que se le este mandando a evaluar
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

    //Metodo para crear una locacion que tenga padre, la variable de params es para poder
    //usar la seguridad del acces token
	def createLocation(def parentlocationId, def jsonLocation, def params){

        Map jsonResult = [:]
        def responseMenssage = ''

        if (!params.access_token){
            throw new BadRequestException ("You must provider de access_token")
        }

        def access_token = validAccess.validAccessToken(params.access_token)
        def user_id = params.access_token.split('_')[2]

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

    //Metodo para crear una locacion que sea padre o raiz, la variable de params es para poder
    //usar la seguridad del acces token
    def createLocation(def jsonLocation, def params){

        Map jsonResult = [:]
        def responseMenssage = ''

        if (!params.access_token){
            throw new BadRequestException ("You must provider de access_token")
        }

        def access_token = validAccess.validAccessToken(params.access_token)
        def user_id = params.access_token.split('_')[2]

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

    def modifyLocation(def locationId, def jsonLocation, def params){

        Map jsonResult = [:]
        def responseMessage = ''

        if (!params.access_token){
            throw new BadRequestException ("You must provider de access_token")
        }

        def access_token = validAccess.validAccessToken(params.access_token)
        def user_id = params.access_token.split('_')[2]

        if (!locationId){
            throw  new NotFoundException("You must provider locationId")
        }

        def obteinedLocation = Location.findByLocationID(locationId, [ sort: "name", order: "asc"])

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
