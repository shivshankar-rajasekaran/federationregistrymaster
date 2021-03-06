package fedreg.workflow

class ProcessInstance {

	String description
	ProcessStatus status
    ProcessPriority priority

	Date dateCreated
	Date lastUpdated
	
	boolean completionAcknowlegded = false
	
	List taskInstances
	Map params

	static hasMany = [taskInstances: TaskInstance, messages: WorkflowMessage]
    static belongsTo = [process: Process]

	static constraints = {
		dateCreated(nullable: true)
		lastUpdated(nullable: true)
	}
	
	public String toString() {
		"processinstance:[id:$id, description:$description] of $process"
	}
}
