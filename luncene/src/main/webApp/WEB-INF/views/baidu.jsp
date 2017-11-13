<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

                                  
  


      
           <form action="${pageContext.request.contextPath}/lucene/find?nowPage=1">
          
			
			百度唐诗:<input type="text" name="keyWord">
                     <input type="submit" name="搜索">



           </form>

         <c:forEach var="i" items="${requestScope.poetries}">
               ${i.title}<br/>
               ${i.name}<br/>
              ${i.content}<br/>
             <hr/>

         </c:forEach>

        <a href="${pageContext.request.contextPath}/lucene/find?nowPage=${requestScope.page-1}">上一夜</a>
        <c:forEach var="i" begin="1" end="10" >
          <a href="${pageContext.request.contextPath}/lucene/find?nowPage=${i}">${i}</a>

         </c:forEach>
        <a href="${pageContext.request.contextPath}/lucene/find?nowPage=${requestScope.page+1}">下一夜</a>
     
      

