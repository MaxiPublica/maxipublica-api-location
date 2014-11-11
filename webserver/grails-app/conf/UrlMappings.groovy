class UrlMappings {
	static mappings = {
		"/$locationId?" {
			controller = "Location"
			action = [GET: 'getLocation', POST:'createLocation',PUT:'modifyLocation' ,DELETE: 'notAllowed']
		}
	}
}
