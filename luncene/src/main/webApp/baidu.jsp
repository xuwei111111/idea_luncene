<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

                                  
  


      
           <form action="${pageContext.request.contextPath}/lucene/find?nowPage=1">

			<div style="text-align: center">
                <img hidefocus="true" src="//www.baidu.com/img/bd_logo1.png" width="270" height="129"><br/>
               <div style="height: 20px">

                <input style="height: 30px" type="text" name="keyWord"><input type="submit" name="百度唐诗">
               </div>
            </div>

           </form><hr/>

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
     
      

