class UrlMappings {
	static mappings = {
		"/$locationId?" {
			controller = "Location"
			action = [GET: 'getLocation', POST:'createLocation',PUT:'modifyLocation' ,DELETE: 'notAllowed']
		}

		"/cp/$zipId?" {
			controller = "Zipcodes"
			action = [GET: 'getZip', POST:'createZip',PUT:'modifyZip' ,DELETE: 'notAllowed']
		}
	}
}
