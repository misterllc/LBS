package rx.com;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class webfilter implements Filter {
	private FilterConfig fc;
	
	public void destroy() {
		this.fc=null;
	}

	public void doFilter(ServletRequest sRequest, ServletResponse sResponse,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest)sRequest;
//		HttpSession session=request.getSession();
//		//判断当前session是否过期
//		Boolean mark=(Boolean)session.getAttribute("activation");
//		if(mark!=null&&mark)
//			chain.doFilter(sRequest,sResponse);
//		else{
//			String message="<li>对不起，您已经与服务器断开！请重新访问！</li>";
//			request.setAttribute("message",message);
			
			RequestDispatcher rd=request.getRequestDispatcher("/i.jsp");
			rd.forward(sRequest,sResponse);
//		}
	}

	public void init(FilterConfig fc) throws ServletException {
		this.fc=fc;
	}
}