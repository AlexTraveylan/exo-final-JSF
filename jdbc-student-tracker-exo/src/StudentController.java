
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
public class StudentController {
	
	private StudentDbUtil studentDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());
	private List<Student> students;
	
	public StudentController() throws Exception {
		students = new ArrayList<>();
		studentDbUtil = StudentDbUtil.getInstance();
		students = studentDbUtil.getStudents();
	}
	
	public List<Student> getStudents() throws Exception {
		loadStudents();
		return students;
	}
	
	public void loadStudents() {
		logger.info("Loading students...");
		students.clear();
		
		try {
			students = studentDbUtil.getStudents();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error loading students", e);
			addErrorMessage(e);
			e.printStackTrace();
		}
	}
	
	
	public String loadStudent(int studentId) {
		
		logger.info("loading student: " + studentId);
		
		try {
			Student theStudent = studentDbUtil.getStudent(studentId);
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("student", theStudent);
			
			System.out.println("student Loaded");
			
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error loading student id:" + studentId, exc);
			addErrorMessage(exc);
			return "";
		}
		
		return "modify";	
		
	}
	
	
	public String addStudent(Student student) {
		
		try {
			studentDbUtil = StudentDbUtil.getInstance();
			studentDbUtil.addElement(student);
			return "list-students";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public String updateStudent(Student student) {
		try {
			
			studentDbUtil = StudentDbUtil.getInstance();
			studentDbUtil.updateStudent(student);	
			
			System.out.println("student updated");
			
			return "list-students";
			
		} catch (Exception e) {
			e.printStackTrace();
			
			return "";
		}
	}
	
	public void deleteStudent(int id) {
		
		try {
			studentDbUtil.deleteStudent(id);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error deleting students", e);
			addErrorMessage(e);
			e.printStackTrace();
		}
		
	}
	
	
	private void addErrorMessage(Exception e) {
		FacesMessage msg = new FacesMessage("Error :" + e.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}
	
	
	
	
}
