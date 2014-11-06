class UrlMappings {
	static mappings = {
		"/$location?" {
			controller = "Location"
			action = [GET: 'getLocation', POST:'createLocation',PUT:'modifyLocation' ,DELETE: 'notAllowed']
		}
	}
}
