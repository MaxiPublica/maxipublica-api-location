import locations.Location

class BootStrap {
	def init = { servletContext ->
		test{}
		
		development{
			/*if (Location.count() == 0){
				def location01 = new Location(
					locationID:"MX",
					name:"México"
				)
				location01.save()

				def location02 = new Location(
					locationID:"DF",
					parentLocationId:"MX",
					name:"Distrito Federal"
				)

				location02.save()

				def location03 = new Location(
					locationID:"EDO",
					parentLocationId:"MX",
					name:"Estado de México"
				)

				location03.save()
			}*/
		}
		
		production{}
	}

	def destroy = {}
}
