package com.newlecture.web.controller.admin.notice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.ModuleElement.RequiresDirective;
import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.entity.NoticeView;
import com.newlecture.web.service.NoticeService;


@WebServlet("/admin/board/notice/list")
public class ListController extends HttpServlet{
	// 404 url 없는 경우
	// 405 url은 있는데 처리하는 코드 없는 경우
	// 403 보안오류
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] openIds = request.getParameterValues("open-id"); //3, 5 , 8 
		String[] delIds = request.getParameterValues("del-id");
		String cmd = request.getParameter("cmd");
		String ids_ = request.getParameter("ids");
		
		String[] ids = ids_.trim().split(" ");//1,2,3,4,5,6,7,8,9,10
		
		NoticeService service = new NoticeService();
		
		switch(cmd) {
		case "일괄공개":
			for(String openId : openIds)
				System.out.printf("open id : %s\n", openId);
		
			List<String> oids = Arrays.asList(openIds);
			
			List<String> cids = new ArrayList(Arrays.asList(ids));
			cids.removeAll(oids);
			System.out.println(Arrays.asList(ids));
			System.out.println(oids);
			System.out.println(cids);
			
			service.pubNoticeAll(oids, cids);
			break;
			
		case "일괄삭제":
			
			int[] ids1 = new int[delIds.length];
			for(int i=0;i<delIds.length ; i++)
				ids1[i] = Integer.parseInt(delIds[i]);
			
			int result = service.deleteNoticeAll(ids1);
			break;
		}
		
		response.sendRedirect("list");
	}
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
		
		List<NoticeView> list = service.getNoticeList(field, query, page);
		//DB에 존재하는 데이터 개수 : count
		int count = service.getNoticeCount(field, query);
		
		
		//notice를 담은 배열 request에 담아주기
		request.setAttribute("list", list);
		request.setAttribute("count", count);
		
		//notice/list.jsp에 넘겨주기
		request.getRequestDispatcher("/WEB-INF/view/admin/board/notice/list.jsp")
		.forward(request, response); //데이터를 가지고 이어서 작성
	}
	
	
}
