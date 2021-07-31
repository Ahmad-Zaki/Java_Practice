package Main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
	
	@RequestMapping("/viewdescription")
	public String descripeData() {
		return "dataDescription";
	}
	
	@RequestMapping("/cleandata")
	public String cleanData() {
		return "cleanData";
	}
	
	@RequestMapping("/companycount")
	public String companyCount() {
		return "companyCount";
	}
	
	@RequestMapping("/jobscount")
	public String jobsCount() {
		return "jobsCount";
	}

	@RequestMapping("/areacount")
	public String areaCount() {
		return "areaCount";
	}
	
	@RequestMapping("/skillcount")
	public String skillCount() {
		return "skillCount";
	}
	
	@RequestMapping("/factorize")
	public String factorize() {
		return "factorize";
	}
	
}
