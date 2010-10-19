package fedreg.core

import fedreg.workflow.ProcessPriority

class BootstrapController {
	
	def IDPSSODescriptorService
	def SPSSODescriptorService
	def organizationService
	def grailsApplication
	
	static allowedMethods = [saveidp: "POST", savesp: "POST", saveorganization: "POST"]
	
	def idp = {
		def identityProvider = new IDPSSODescriptor()
		[identityProvider: identityProvider, organizationList: Organization.findAllWhere(active:true, approved:true), attributeList: AttributeBase.list(), nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)]
	}
	
	def saveidp = {
		def (created, ret) = IDPSSODescriptorService.create(params)
		
		if(created) {			
			redirect (action: "idpregistered", id: ret.identityProvider.id)
		}
		else {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.idpssoroledescriptor.register.validation.error')
			render (view:'idp', model:ret + [organizationList: Organization.findAllWhere(active:true, approved:true), attributeList: AttributeBase.list(), nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)])
		}
	}
	
	def idpregistered = {
		if(!params.id) {
			log.warn "IDPSSODescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect uri:"/"
			return
		}
		
		def identityProvider = IDPSSODescriptor.get(params.id)
		if (!identityProvider) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.idpssoroledescriptor.nonexistant')
			redirect uri:"/"
			return
		}

		[identityProvider: identityProvider]
	}
	
	def sp = {
		def serviceProvider = new SPSSODescriptor()
		[serviceProvider: serviceProvider, organizationList: Organization.findAllWhere(active:true, approved:true), attributeList: AttributeBase.list(), nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)]
	}
	
	def savesp = {
		def (created, ret) = SPSSODescriptorService.create(params)
		
		if(created) {
			redirect (action: "spregistered", id: ret.serviceProvider.id)
		}
		else {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.spssoroledescriptor.save.validation.error')
			render (view:'sp', model:ret + [organizationList: Organization.findAllWhere(active:true, approved:true), attributeList: AttributeBase.list(), nameIDFormatList: SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)])
		}
	}
	
	def spregistered = {
		if(!params.id) {
			log.warn "SPSSODescriptor ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect uri:"/"
			return
		}
		
		def serviceProvider = SPSSODescriptor.get(params.id)
		if (!serviceProvider) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.spssoroledescriptor.nonexistant')
			redirect uri: "/"
			return
		}

		[serviceProvider: serviceProvider]
	}
	
	def organization = {
		def organization = new Organization()
		[organization:organization, organizationTypes: OrganizationType.list()]
	}
	
	def saveorganization = {
		def (created, organization, contact) = organizationService.create(params)
		
		if(created) {
			redirect (action: "organizationregistered", id: organization.id)
		}
		else {
			flash.message = message(code: 'fedreg.core.organization.register.validation.error')
			render (view:'organization', model:[organization:organization, contact:contact, organizationTypes: OrganizationType.list()])
		}
	}
	
	def organizationregistered = {
		if(!params.id) {
			log.warn "Organization ID was not present"
			flash.type="error"
			flash.message = message(code: 'fedreg.controllers.namevalue.missing')
			redirect uri: "/"
			return
		}
		
		def organization = Organization.get(params.id)
		if (!organization) {
			flash.type="error"
			flash.message = message(code: 'fedreg.core.organization.nonexistant')
			redirect uri: "/"
			return
		}

		[organization: organization]
	}
}