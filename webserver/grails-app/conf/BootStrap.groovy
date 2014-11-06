class BootStrap {
	def init = { servletContext ->
		test{}
		
		development{}
		
		production{}
	}

	def destroy = {}
}
