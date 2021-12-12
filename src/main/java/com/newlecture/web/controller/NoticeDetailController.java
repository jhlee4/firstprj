package com.newlecture.web.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.newlecture.web.entity.Notice;

@WebServlet("/notice/detail")
public class NoticeDetailController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));

		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String uid = "newlec"; // oracle id
		String pwd = "1115"; //oracle password
		String driver = "oracle.jdbc.driver.OracleDriver";
		String sql = "select * FROM NOTICE where id=?";

		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,uid,pwd);
			//Statement st = con.createStatement();
			PreparedStatement st = con.prepareStatement(sql); 
			st.setInt(1, id);//%"+query+"%
			//st.setInt(2, start);
			//st.setInt(3, end);
			
			ResultSet rs = st.executeQuery();
			//id에 해당하는 쿼리 결과 불러오기
			rs.next();
			//쿼리 결과를 변수에 저장하기
			String title = rs.getString("TITLE");
			Date regdate = rs.getDate("REGDATE");
			String  writerId = rs.getString("WRITER_ID");
			String hit = rs.getString("HIT");
			String files = rs.getString("FILES");
			String content = rs.getString("CONTENT");
			
			Notice notice = new Notice(
					id, 
					title, 
					regdate, 
					writerId, 
					hit, 
					files, 
					content
					);
					
			
			request.setAttribute("n", notice);

			/*
			//request에 데이터 심기
			request.setAttribute("title", title);
			request.setAttribute("regdate", regdate);
			request.setAttribute("writerId", writerId);
			request.setAttribute("hit", hit);
			request.setAttribute("files", files);
			request.setAttribute("content", content);
			*/

			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		request.getRequestDispatcher("/WEB-INF/view/notice/detail.jsp")
		.forward(request, response); //데이터를 가지고 이어서 작성
	}
}
