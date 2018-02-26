package l2.poc.service.discovery;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 * 
 * @author Eldho Mathulla
 *
 */
@JsonRootName("instance_details")
public class InstanceDetails {
	private int workLoad = 0;
	private int maxWorkLoad;

	public InstanceDetails(@JsonProperty("max_work_load") int maxWorkLoad) {
		this.maxWorkLoad = maxWorkLoad;
	}

	public int getWorkLoad() {
		return workLoad;
	}

	public void setWorkLoad(int workLoad) {
		if (workLoad > getMaxWorkLoad() || workLoad < 0) {
			throw new RuntimeException("Invalid workload :" + workLoad);
		}
		this.workLoad = workLoad;
	}

	@JsonProperty("max_work_load")
	public int getMaxWorkLoad() {
		return maxWorkLoad;
	}

	public void updateWorkLoad(int workLoadChange) {
		this.workLoad = this.workLoad + workLoadChange;
	}

}