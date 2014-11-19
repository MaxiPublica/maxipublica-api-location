import locations.Location
import locations.Zipcodes

class BootStrap {
	def init = { servletContext ->
		test{}
		
		development{
			/*if (Location.count() == 0){
				//Pais
				def location01 = new Location(
					locationID:"MX",
					name:"México"
				)
				location01.save()

				//Estados
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

				def location04 = new Location(
					locationID:"QRO",
					parentLocationId:"MX",
					name:"Queretaro"
				)
				location04.save()

				//Municipios
				def municipios = new Location(
					locationID:"MUN1",
					parentLocationId:"DF",
					name:"Gustabo a madero"
				)
				municipios.save()

				def municipios2 = new Location(
					locationID:"MUN2",
					parentLocationId:"DF",
					name:"Venustiano Carranza"
				)
				municipios2.save()

				def municipios3 = new Location(
					locationID:"MUN3",
					parentLocationId:"DF",
					name:"Azcapotzalco"
				)
				municipios3.save()

				def municipios4 = new Location(
					locationID:"MUN4",
					parentLocationId:"EDO",
					name:"La Paz"
				)
				municipios4.save()

				def municipios5 = new Location(
					locationID:"MUN5",
					parentLocationId:"EDO",
					name:"Tecamac"
				)
				municipios5.save()

				def municipios6 = new Location(
					locationID:"MUN6",
					parentLocationId:"EDO",
					name:"Temascalapa"
				)
				municipios6.save()

				def municipios7 = new Location(
					locationID:"MUN7",
					parentLocationId:"QRO",
					name:"Tequisquiapan"
				)
				municipios7.save()

				def municipios8 = new Location(
					locationID:"MUN8",
					parentLocationId:"QRO",
					name:"Toliman"
				)
				municipios8.save()
				
				//Colonias
				def colonias = new Location(
					locationID:"COL1",
					parentLocationId:"MUN1",
					name:"Colonia 1"
				)
				colonias.save()

				def colonias2 = new Location(
					locationID:"COL2",
					parentLocationId:"MUN1",
					name:"Colonia 2"
				)
				colonias2.save()

				def colonias3 = new Location(
					locationID:"COL3",
					parentLocationId:"MUN1",
					name:"Colonia 3"
				)
				colonias3.save()

				def colonias4 = new Location(
					locationID:"COL4",
					parentLocationId:"MUN2",
					name:"Colonia 4"
				)
				colonias4.save()

				def colonias5 = new Location(
					locationID:"COL5",
					parentLocationId:"MUN2",
					name:"Colonia 5"
				)
				colonias5.save()

				def colonias6 = new Location(
					locationID:"COL6",
					parentLocationId:"MUN2",
					name:"Colonia 6"
				)
				colonias6.save()

				def colonias7 = new Location(
					locationID:"COL7",
					parentLocationId:"MUN3",
					name:"Colonia 7"
				)
				colonias7.save()

				def colonias8 = new Location(
					locationID:"COL8",
					parentLocationId:"MUN3",
					name:"Colonia 8"
				)
				colonias8.save()

				def colonias9 = new Location(
					locationID:"COL9",
					parentLocationId:"MUN3",
					name:"Colonia 9"
				)
				colonias9.save()

				def colonias10 = new Location(
					locationID:"COL10",
					parentLocationId:"MUN4",
					name:"Colonia 10"
				)
				colonias10.save()

				def colonias11 = new Location(
					locationID:"COL11",
					parentLocationId:"MUN4",
					name:"Colonia 11"
				)
				colonias11.save()

				def colonias12 = new Location(
					locationID:"COL12",
					parentLocationId:"MUN4",
					name:"Colonia 12"
				)
				colonias12.save()

				def colonias13 = new Location(
					locationID:"COL13",
					parentLocationId:"MUN5",
					name:"Colonia 13"
				)
				colonias13.save()

				def colonias14 = new Location(
					locationID:"COL14",
					parentLocationId:"MUN5",
					name:"Colonia 14"
				)
				colonias14.save()

				def colonias15 = new Location(
					locationID:"COL15",
					parentLocationId:"MUN5",
					name:"Colonia 15"
				)
				colonias15.save()

				def colonias16 = new Location(
					locationID:"COL16",
					parentLocationId:"MUN6",
					name:"Colonia 16"
				)
				colonias16.save()

				def colonias17 = new Location(
					locationID:"COL17",
					parentLocationId:"MUN6",
					name:"Colonia 17"
				)
				colonias17.save()

				def colonias18 = new Location(
					locationID:"COL18",
					parentLocationId:"MUN6",
					name:"Colonia 18"
				)
				colonias18.save()

				def colonias19 = new Location(
					locationID:"COL19",
					parentLocationId:"MUN7",
					name:"Colonia 19"
				)
				colonias19.save()

				def colonias20 = new Location(
					locationID:"COL20",
					parentLocationId:"MUN7",
					name:"Colonia 20"
				)
				colonias20.save()

				def colonias21 = new Location(
					locationID:"COL21",
					parentLocationId:"MUN7",
					name:"Colonia 21"
				)
				colonias21.save()

				def colonias22 = new Location(
					locationID:"COL22",
					parentLocationId:"MUN8",
					name:"Colonia 22"
				)
				colonias22.save()

				def colonias23 = new Location(
					locationID:"COL23",
					parentLocationId:"MUN8",
					name:"Colonia 23"
				)
				colonias23.save()

				def colonias24 = new Location(
					locationID:"COL24",
					parentLocationId:"MUN8",
					name:"Colonia 24"
				)
				colonias24.save()
			}

			//CPs
			if(Zipcodes.count() == 0){
				def cp1 = new Zipcodes(
					zipcode:"11111",
					colonId:"COL1"
				)
				cp1.save()

				def cp2 = new Zipcodes(
					zipcode:"11111",
					colonId:"COL2"
				)
				cp2.save()

				def cp3 = new Zipcodes(
					zipcode:"22222",
					colonId:"COL3"
				)
				cp3.save()

				def cp4 = new Zipcodes(
					zipcode:"33333",
					colonId:"COL4"
				)
				cp4.save()

				def cp5 = new Zipcodes(
					zipcode:"44444",
					colonId:"COL5"
				)
				cp5.save()

				def cp6 = new Zipcodes(
					zipcode:"44444",
					colonId:"COL6"
				)
				cp6.save()

				def cp7 = new Zipcodes(
					zipcode:"55555",
					colonId:"COL7"
				)
				cp7.save()

				def cp8 = new Zipcodes(
					zipcode:"66666",
					colonId:"COL8"
				)
				cp8.save()

				def cp9 = new Zipcodes(
					zipcode:"66666",
					colonId:"COL9"
				)
				cp9.save()

				def cp10 = new Zipcodes(
					zipcode:"77777",
					colonId:"COL10"
				)
				cp10.save()

				def cp11 = new Zipcodes(
					zipcode:"77777",
					colonId:"COL11"
				)
				cp11.save()

				def cp12 = new Zipcodes(
					zipcode:"77777",
					colonId:"COL12"
				)
				cp12.save()

				def cp13 = new Zipcodes(
					zipcode:"88888",
					colonId:"COL13"
				)
				cp13.save()

				def cp14 = new Zipcodes(
					zipcode:"88888",
					colonId:"COL14"
				)
				cp14.save()

				def cp15 = new Zipcodes(
					zipcode:"88888",
					colonId:"COL15"
				)
				cp15.save()

				def cp16 = new Zipcodes(
					zipcode:"99999",
					colonId:"COL16"
				)
				cp16.save()

				def cp17 = new Zipcodes(
					zipcode:"99999",
					colonId:"COL17"
				)
				cp17.save()

				def cp18 = new Zipcodes(
					zipcode:"10101",
					colonId:"COL18"
				)
				cp18.save()

				def cp19 = new Zipcodes(
					zipcode:"11100",
					colonId:"COL19"
				)
				cp19.save()

				def cp20 = new Zipcodes(
					zipcode:"00111",
					colonId:"COL20"
				)
				cp20.save()

				def cp21 = new Zipcodes(
					zipcode:"00111",
					colonId:"COL21"
				)
				cp21.save()
				def cp22 = new Zipcodes(
					zipcode:"33301",
					colonId:"COL22"
				)
				cp22.save()

				def cp23 = new Zipcodes(
					zipcode:"33301",
					colonId:"COL23"
				)
				cp23.save()

				def cp24 = new Zipcodes(
					zipcode:"33301",
					colonId:"COL24"
				)
				cp24.save()
			}*/
		}
		
		production{}
	}

	def destroy = {}
}
