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
import locations.ZipcodesService

class ZipcodesController {

    def ZipcodesService

	def setHeaders(){
		response.setContentType "application/json; charset=utf-8"
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
		response.setHeader("Access-Control-Max-Age", "86400");
		response.setHeader("Access-Control-Allow-Headers", "application/json;charset=UTF-8");
	}

	def renderException(def e){

		def statusCode
		def error

		try{
			statusCode = e.status
			error = e.error
		}catch(Exception ex){
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
			error = "internal_server_error"
		}

		response.setStatus(statusCode)

		def mapExcepction = [
			message: e.getMessage(),
			status: statusCode,
			error: error
		]
		render mapExcepction as GSON
	}

	def notAllowed(){
		def method = request.method

		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
		setHeaders()

		def mapResult = [
			message: "Method $method not allowed",
			status: HttpServletResponse.SC_METHOD_NOT_ALLOWED,
			error:"not_allowed"
		]
		render mapResult as GSON
	}

	def getZip(){

		def zipId = params.zipId
		def result

		setHeaders()

		try{
			result = ZipcodesService.getZip(zipId)
			response.setStatus(HttpServletResponse.SC_OK)
			render result as GSON
		}catch (NotFoundException e){
			renderException(e)
		}catch(BadRequestException e){
			renderException(e)
		}catch (Exception e){
			renderException(e)
		}
	}

	def createZip(){
		def colonId = params.zipId
		def jsonZipcodes = request.JSON
		def result

		setHeaders()

		try{
			result = ZipcodesService.createZip(colonId, jsonZipcodes, params)
			response.setStatus(HttpServletResponse.SC_OK)
			render result as GSON
		}catch (NotFoundException e){
			renderException(e)
		}catch(BadRequestException e){
			renderException(e)
		}catch (Exception e){
			renderException(e)
		}
	}

	def modifyZip(){
		def zipId = params.zipId
		def jsonZip = request.JSON
		def result

		setHeaders()

		try{
			result = ZipcodesService.modifyZip(zipId, jsonZip, params)
			response.setStatus(HttpServletResponse.SC_OK)
			render result as GSON
		}catch (NotFoundException e){
			renderException(e)
		}catch(BadRequestException e){
			renderException(e)
		}catch (Exception e){
			renderException(e)
		}
	}
}
