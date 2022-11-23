import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	
	@Resource(name = "jdbc/student_tracker")
	private DataSource dataSource;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		

		// TESTER LA CONNECTION ICI
		
		Connection connection = null;
		Statement stmt = null;
		ResultSet res = null;
		
		try {
			
			connection = dataSource.getConnection();
			String sql = "select * from student;";
			stmt = connection.createStatement();
			
			res = stmt.executeQuery(sql);
			
			while(res.next()) {
				String email = res.getString("email");
				out.print(email); // affiche sur la page web
				System.out.println(email); // affiche en console
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
			out.print(e.getMessage());
		}
		
	}

}
