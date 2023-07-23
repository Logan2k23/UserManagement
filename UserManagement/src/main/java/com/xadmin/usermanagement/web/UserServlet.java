package com.xadmin.usermanagement.web;

import java.io.IOException;
import java.sql.SQLException;
//import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.xadmin.usermanagement.bean.User;
import com.xadmin.usermanagement.dao.UserDao;
/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       private UserDao userDao;
   

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init( ) throws ServletException {
		 userDao=new UserDao();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action =request.getServletPath();
		switch(action) {
		case "/new/":
			showNewForm(request,response);
				break;
		case "/insert":
			insertUser(request,response);
			break;
		case "/delete":
			deleteUser(request,response);
			break;
		case "/edit":
			showEditForm(request,response);
			break;
		case "/update":
			try {
				updateUser(request,response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			listUser(request,response);
			break;
		}
		
	}
	
	private  void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		RequestDispatcher dispatcher=request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request, response);
	}

	private  void insertUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String name=request.getParameter("name");
		String email=request.getParameter("email");
		String country=request.getParameter("country");
		User newUser=new User(name,email,country);

		userDao.insertUser(newUser);
		response.sendRedirect("list");
	}
	
	private void deleteUser(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		int id=Integer.parseInt(request.getParameter("id"));
		try {
			userDao.deleteUser(id);
		}catch(Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("list");
	}
	
	private void showEditForm(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		int id=Integer.parseInt(request.getParameter("id"));
		User extinguisher;
		try {
			extinguisher=userDao.selectUser(id);
			RequestDispatcher dispatcher=request.getRequestDispatcher("user-form.jsp");
			request.setAttribute("user", extinguisher);
			dispatcher.forward(request,response);
		}catch(Exception e) {
			e.printStackTrace();
		}
			}

	private void updateUser(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException, SQLException
	{
		// TODO Auto-generated method stub
		int id=Integer.parseInt(request.getParameter("id"));
		String name=request.getParameter("name");
		String email=request.getParameter("email");
		String country=request.getParameter("country");
		
		User user=new User(id,name,email,country);
		userDao.updateUser(user);
		response.sendRedirect("list");
	}
	
	//default
	private void listUser(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
	{
		try {
			List<User> listUser=userDao.selectAllUsers();
			request.setAttribute("listUser", listUser);
			RequestDispatcher dispatcher=request.getRequestDispatcher("user-list.jsp");
			dispatcher.forward(request, response);
		}catch(Exception e) {
			e.printStackTrace();
	}
	}
	
}
