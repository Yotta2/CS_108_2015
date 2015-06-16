package servlets;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.AccountManager;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		printHttpBody(request);
		AccountManager accountMgr = (AccountManager) getServletContext().getAttribute(AccountManager.class.getName());
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		if (accountMgr.matched(name, password)) {
			request.setAttribute("name", name);
			RequestDispatcher dispatcher = request.getRequestDispatcher("welcome.jsp");
			dispatcher.forward(request, response);
		} else {
			RequestDispatcher dispatcher = request.getRequestDispatcher("illegalLogin.html");
			dispatcher.forward(request, response);
		}
	}

	private void printHttpBody(HttpServletRequest request) throws IOException {
		BufferedReader br = request.getReader();
		System.out.println("Whole http body");
		while (true) {
			String line = br.readLine();
			if (line == null)
				break;
			System.out.println(line);
		}
	}

}
