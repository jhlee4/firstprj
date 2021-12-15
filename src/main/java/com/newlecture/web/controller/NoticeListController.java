package com.newlecture.web.controller;

import java.io.IOException;
import java.util.List;

import javax.lang.model.element.ModuleElement.RequiresDirective;
import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.service.NoticeService;


@WebServlet("/notice/list")
public class NoticeListController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String field_ = request.getParameter("f");
		String query_ = request.getParameter("q");
		String page_ = request.getParameter("p");
		
		String field = "title";
		if(field_ != null && !field_.equals(""))
			field = field_;
		
		String query = "";
		if(query_ != null && !query_.equals(""))
			query = query_;
		
		int page = 1;
		if(page_ != null && !page_.equals(""))
			page = Integer.valueOf(page_);
		
		NoticeService service = new NoticeService();
		
		List<Notice> list = service.getNoticeList(field, query, page);
		//DB에 존재하는 데이터 개수 : count
		int count = service.getNoticeCount(field, query);
		
		
		//notice를 담은 배열 request에 담아주기
		request.setAttribute("list", list);
		request.setAttribute("count", count);
		
		//notice/list.jsp에 넘겨주기
		request.getRequestDispatcher("/WEB-INF/view/notice/list.jsp")
		.forward(request, response); //데이터를 가지고 이어서 작성
	}
}
