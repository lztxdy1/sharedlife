package com.wang.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * response工具类
* @ClassName: ResponseUtil
*
 */
public class ResponseUtil {

	public static void write(Object o,HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
			out = response.getWriter();
			out.println(o.toString());
			out.flush();
			out.close();
	}
}
