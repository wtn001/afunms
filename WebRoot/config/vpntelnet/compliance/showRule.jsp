<%@page language="java" contentType="text/html;charset=gb2312" %>

<%@page import="java.util.*"%>
<%@page import="com.afunms.config.model.CompRule"%>
<%@page import="com.afunms.config.dao.DetailCompRuleDao"%>
<%@page import="com.afunms.config.model.DetailCompRule"%>
<%@page import="com.afunms.config.model.CompGroupRule"%>
<%@page import="com.afunms.config.dao.CompRuleDao"%>
<%@page import="com.afunms.config.model.CompCheckRule"%>


<% 

	String rootPath = request.getContextPath(); 
	
    String id=(String)request.getAttribute("ruleId");
    List list=(List)request.getAttribute("list");
    String isVor=(String)request.getAttribute("isVor");
    CompRuleDao ruleDao=new CompRuleDao();
   CompRule compRule= (CompRule)ruleDao.findByID(id);
   StringBuffer sb=new StringBuffer();
   CompCheckRule vo=null;
  if(list!=null&&list.size()>0){
  for(int i=0;i<list.size();i++){
   vo=(CompCheckRule)list.get(i);
   sb.append(vo.getContent()+"\r\n");
  }
 
  }
   String error="<img src='/afunms/img/error.gif'>";
  String correct="<img src='/afunms/img/correct.gif'>";
  String status="";//�Ƿ�Υ������
  String statusImg="";
  if(isVor.equals("0")){
  status="Υ��";
  statusImg="<img src='/afunms/img/error.gif'>";
  }else if(isVor.equals("1")){
  status="�Ϲ�";
  statusImg="<img src='/afunms/img/correct.gif'>";
  }
  String level="";
  String levelImg="";
  if(compRule.getViolation_severity()==0){
  level="��ͨ";
  levelImg="<img src='/afunms/img/common.gif'>";
  }else if(compRule.getViolation_severity()==1){
  level="��Ҫ";
  levelImg="<img src='/afunms/img/serious.gif'>";
  }else if(compRule.getViolation_severity()==2){
  level="����";
  levelImg="<img src='/afunms/img/urgency.gif'>";
  }
  
  String contain="";
  String type="";
  if(compRule.getSelect_type()==0){
  type="�򵥱�׼";
  if(vo.getIsContain()==0){
  contain="����������";
  }else if(vo.getIsContain()==1){
   contain="�������κ���";
  }else if(vo.getIsContain()==2){
   contain="Ӧ�ð�������";
  }else if(vo.getIsContain()==3){
   contain="��Ӧ�ð�������";
  }
  }else if(compRule.getSelect_type()==1){
  type="�߼���׼";
  }else if(compRule.getSelect_type()==2){
  type="�߼��Զ����׼";
  }
 
%>

<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>



<script language="JavaScript" type="text/javascript">



function CreateWindow(url)
{
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no");
}    


</script>


</head>
<body id="body" >


	<!-- �Ҽ��˵�����-->
   <form name="mainForm" method="post">
		<table id="body-container" class="body-container">
			<tr>
				<td class="td-container-main" bgcolor="#FFFFFF">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-add">
								<table id="container-main-add" class="container-main-add">
									<tr>
										<td bgcolor="#FFFFFF">
											<table id="add-content" class="add-content" border=1>
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header" >
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title">&nbsp; �Զ���>> �����ļ�����>> �Ϲ��Թ���>>������ϸ</td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body">
				        									<tr>
				        										<td>
				        										
				        												<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
																			<tr>
																			    <td nowrap align="left" height="30" width="20%">��������&nbsp;&nbsp;��</td>
																			    <td width="80%" height="30" align="left"><%=compRule.getComprule_name() %></td>
													                  		</tr>
																			<tr>
																			    <td nowrap align="left" height="30" width="20%">������&nbsp;&nbsp;��</td>
																			    <td width="80%" height="30" align="left"><%=statusImg%> <%=status %></td>
													                  		</tr>
																			<tr>
																			    <td nowrap align="left" height="30" width="20%">Υ�����ضȣ�&nbsp;&nbsp;</td>
																			    <td width="80%"  height="30" align="left" valign=middle><%=levelImg %> <%=level %></td>
													                  		</tr>
																			<tr>
																			    <td nowrap align="left" height="30" width="20%">��׼����&nbsp;&nbsp;��</td>
																			    <td width="80%"  height="30" align="left" valign=middle><%=type %></td>
													                  		</tr>
																			<tr>
																			     <td nowrap align="left" height="30" width="20%" valign=top> ��&nbsp;&nbsp;��&nbsp;&nbsp;&nbsp;&nbsp;��</td>
																			     <td width="80%" height="30"><%=compRule.getDescription() %></td>
																		   </tr>
																		   <% if(compRule.getSelect_type()==0){
																		   %>
																		   <tr>
																			     <td nowrap align="left" height="30" width="20%" valign=top> �����׼&nbsp;&nbsp;��</td>
																			     <td width="80%" height="30"><%=contain %></td>
																		    </tr>
																		    <tr>
																			     <td nowrap align="left"  width="20%" valign=top> ��������&nbsp;&nbsp;��</td>
																			     <td width="80%" >
																			     
																			          <div >
																			              <table bgcolor="#F8F8FF" >
																			              <%
																			             
																			               if(list!=null&&list.size()>0){
                                                                                                for(int i=0;i<list.size();i++){
                                                                                                   CompCheckRule checkRule=(CompCheckRule)list.get(i);
                                                                                                   // sb.append(vo.getContent()+"\r\n"+"sss");
                                                                                                    String content=checkRule.getContent();
                                                                                                    
                                                                                                    if(checkRule.getIsViolation()==0){
                                                                                                    %>
                                                                                                    <tr>
																			                        <td><font color="red"><%=content%></font></td>
																			                         </tr>
                                                                                                    <%
                                                                                                    }else if(checkRule.getIsViolation()==1){
                                                                                                     %>
                                                                                                    <tr>
																			                        <td><font color="black"><%=content %></font></td>
																			                         </tr>
                                                                                                    <%
                                                                                                    }
                                                                                             }
 
                                                                                           } 
                                                                                           
																			               %>
																			                     
																			              </table>
																			          </div>
                                                                                 </td>
																		   </tr>
																		   <%
																		   }else if(compRule.getSelect_type()==1||compRule.getSelect_type()==2){
																		    CompCheckRule checkRule=null;
																		    int isExist=0;
																		   if(compRule.getSelect_type()==1){
																		    %>
																		   <tr>
																			     <td nowrap align="left" height="30"  colspan=2> �߼���׼&nbsp;&nbsp;��</td>
																			     
																		    </tr>
																		    <%}else if(compRule.getSelect_type()==2){
																		   
																		    String extra="";
																		    String title="";
																		    String content="";
																		    
																		         for(int i=0;i<list.size();i++){
                                                                                                 checkRule=(CompCheckRule)list.get(i);
                                                                                                 if(checkRule.getRelation()==-2){
                                                                                               isExist=checkRule.getIsViolation();
                                                                                                   content=checkRule.getContent();
                                                                                                   title="��ʼ��";
                                                                                                  }else if(checkRule.getRelation()==-3){
                                                                                                   content=checkRule.getContent();
                                                                                                   title="������";
                                                                                                  }else if(checkRule.getRelation()==-4){
                                                                                                  extra=checkRule.getContent();
                                                                                                   title="���ӿ�";
                                                                                                  if(checkRule.getIsContain()==-1){
                                                                                                  content="��";
                                                                                                  }else if(checkRule.getIsContain()==0){
                                                                                                  content="������"+"  "+extra;
                                                                                                  }else if(checkRule.getIsContain()==1){
                                                                                                   content="����"+"  "+extra;
                                                                                                  }
                                                                                                  }else{
                                                                                                  continue;
                                                                                                  }
                                                                                                  %>
                                                                            <tr>
																			     <td nowrap align="left" height="30" width="20%" valign=top> <%=title %>&nbsp;&nbsp;��</td>
																			     <td width="80%" height="30"><%=content %></td>
																		    </tr> 
                                                                                                  <% 
                                                                                                  }
                                                                                               
																		     %>
																		     <tr>
																			     <td nowrap align="left" height="30"  colspan=2> �߼��Զ����׼</td>
																			     
																		    </tr>
																		    <%} 
																		    if(isExist==0&&compRule.getSelect_type()==2)
																		    {
																		    %>
																		    <tr>
																			     <td nowrap align="center" height="30"  colspan=2>
																			      <font color=red>δ�ҵ��������õı�׼��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
																			     </td>
																			     <% 
																		    }else{
																		    %>
																		   <tr>
																			     <td nowrap align="left" height="30"  colspan=2>
																			         <div>
																			              <table bgcolor="#F8F8FF" >
																			                    <tr>
																			                        <td height="30" align=center>���</td>
																			                        <td height="30" align=center>��ϵ</td>
																			                        <td height="30" align=center>����</td>
																			                        <td height="30" align=center>ģʽ</td>
																			                        <td height="30" align=center>���</td>
																			                    </tr>
																			                    <%
																			                    if(list!=null&&list.size()>0){
																			                    String relation="";
																			                    String isContain="";
																			                    String isCompare="";
																			                    String isCompareImg="";
																			                    int temp=0;
                                                                                                for(int i=0;i<list.size();i++){
                                                                                                  checkRule=(CompCheckRule)list.get(i);
                                                                                                  int re=checkRule.getRelation();
                                                                                                   temp++;
                                                                                                 if(re==0){
                                                                                                   relation="or";
                                                                                                  }else if(re==1){
                                                                                                   relation="and";
                                                                                                  }else if(re==-2||re==-3||re==-4){
                                                                                                  temp=temp-1;
                                                                                                 continue;
                                                                                                  }else{
                                                                                                  relation="";
                                                                                                  }
                                                                                                  if(checkRule.getIsContain()==0){
                                                                                                  isContain="������";
                                                                                                  }else if(checkRule.getIsContain()==1){
                                                                                                   isContain="����";
                                                                                                  }
                                                                                                  if(checkRule.getIsViolation()==0){
                                                                                                  isCompare="��ƥ��";
                                                                                                  isCompareImg="<img src='/afunms/img/failed.gif'>";
                                                                                                  }else if(checkRule.getIsViolation()==1){
                                                                                                  isCompare="ƥ��";
                                                                                                  isCompareImg="<img src='/afunms/img/success.gif'>";
                                                                                                  }
                                                                                                 
                                                                                                 %>
                                                                                                 <tr>
                                                                                                     <td  height="28" align=center><%=temp%></td>
                                                                                                     <td height="28" align=center><%=relation %></td>
                                                                                                     <td height="28" align=center><%=isContain %></td>
                                                                                                     <td height="28" align=center><%=checkRule.getContent() %></td>
                                                                                                     <td height="28" align=center><%=isCompareImg%> <%=isCompare %></td>
                                                                                                 </tr>
                                                                                                 <%
                                                                                                  }
                                                                                                 } 
                                                                                                 %>
																			              </table>
																			         </div>
                                                                                 </td>
																			     
																		    </tr>
																		   <%
																		   }
																		   } 
																		   %>
																			
																	
																		   
																		        
																			
																			<tr>
																				<TD nowrap colspan="2" align=center colspan=2>
																				<br>
																					<input type="reset" style="width:50" value="�ر�" onclick="window.close();">
																				</TD>	
																			</tr>	
																		</TABLE>
										 							
										 							
				        										</td>
				        									</tr>
				        								</table>
				        							</td>
				        						</tr>
				        						
				        					</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		
	</form>
</BODY>


</HTML>