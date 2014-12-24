package org.cwhd.measure.configuration



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SourceTypeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond SourceType.list(params), model:[sourceTypeInstanceCount: SourceType.count()]
    }

    def show(SourceType sourceTypeInstance) {
        respond sourceTypeInstance
    }

    def create() {
        respond new SourceType(params)
    }

    @Transactional
    def save(SourceType sourceTypeInstance) {
        if (sourceTypeInstance == null) {
            notFound()
            return
        }

        if (sourceTypeInstance.hasErrors()) {
            respond sourceTypeInstance.errors, view:'create'
            return
        }

        sourceTypeInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'sourceType.label', default: 'SourceType'), sourceTypeInstance.id])
                redirect sourceTypeInstance
            }
            '*' { respond sourceTypeInstance, [status: CREATED] }
        }
    }

    def edit(SourceType sourceTypeInstance) {
        respond sourceTypeInstance
    }

    @Transactional
    def update(SourceType sourceTypeInstance) {
        if (sourceTypeInstance == null) {
            notFound()
            return
        }

        if (sourceTypeInstance.hasErrors()) {
            respond sourceTypeInstance.errors, view:'edit'
            return
        }

        sourceTypeInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'SourceType.label', default: 'SourceType'), sourceTypeInstance.id])
                redirect sourceTypeInstance
            }
            '*'{ respond sourceTypeInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(SourceType sourceTypeInstance) {

        if (sourceTypeInstance == null) {
            notFound()
            return
        }

        sourceTypeInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'SourceType.label', default: 'SourceType'), sourceTypeInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'sourceType.label', default: 'SourceType'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
