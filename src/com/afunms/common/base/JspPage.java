/**
 * <p>Description:pagination,paginates records list</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.common.base;

import java.util.List;

public class JspPage
{
   private int perPage;     //ÿҳ�����¼��
   private int totalRecord; //�ܼ�¼��
   private int currentPage; //��ǰҳ��
   private int minNum;      //��ǰҳ�ĵ�һ����¼�ļ�¼��
   private int maxNum;      // ��ǰҳ�����һ����¼�ļ�¼��
   private int totalPage;   //��ҳ��
   private List list;  
   public JspPage()
   {
	   
   }
   public JspPage(int iPerPage,int iCurrentPage,int iTotalRecord)
   {
      init(iPerPage,iCurrentPage,iTotalRecord);
   }

   public JspPage(int iCurrentPage,int iTotalRecord)
   {
      init(15,iCurrentPage,iTotalRecord);
   }

    /**
     * ���캯��
     * @param iPerPage ÿҳ�����¼��
     * @param iCurrentPage ��ǰҳ��
     * @param iTotalRecord �ܼ�¼��
     */
   public void init(int iPerPage,int iCurrentPage,int iTotalRecord)
   {
      perPage = iPerPage;
      currentPage = iCurrentPage;
      totalRecord = iTotalRecord;

      if( currentPage <= 0 )
        currentPage = 1;

      if( totalRecord % perPage == 0 )
         totalPage = totalRecord / perPage;
      else
         totalPage = totalRecord / perPage + 1;

      minNum = (currentPage - 1) * perPage;

      if (totalRecord % perPage != 0 && currentPage == totalPage)
         maxNum = minNum + totalRecord % perPage;
      else
         maxNum = minNum + perPage;

      minNum++;
      if (totalPage == 0) totalPage = 1;
   }

   public int getPerPage()
   {
      return perPage;
   }

    public void setPerPage(int newPerPage)
    {
      perPage = newPerPage;
    }

    public void setPerPage(String newPerPage)
    {
    	
    	if(null!=newPerPage && !newPerPage.equals(""))
    	{
           perPage = Integer.parseInt(newPerPage);
    	}
    	else {
    		perPage=10;
		}
    }
    
    public int getRecordTotal()
    {
      return totalRecord;
    }

    public void setTotalRecord(int newTotalRecord)
    {
      totalRecord = newTotalRecord;
    }

    public int getCurrentPage()
    {
      return currentPage;
    }

    public void setCurrentPage(int newCurrentPage)
    {
      currentPage = newCurrentPage;
    }
    
    public void setCurrentPage(String newCurrentPage)
    {
    	if(null!=newCurrentPage && !newCurrentPage.equals(""))
    	{
    	  currentPage = Integer.parseInt(newCurrentPage);
    	}
    	else{
    		currentPage=1;
    	}
    }
    
    public int getMaxNum()
    {
      return maxNum;
    }

    public void setMaxNum(int newMaxNum)
    {
      maxNum = newMaxNum;
    }

    public int getMinNum()
    {
      return minNum;
    }

    public void setMinNum(int newMinNum)
    {
      minNum = newMinNum;
    }

    public int getPageTotal()
    {
      return totalPage;
    }

    public void setTotalPage(int newTotalPage)
    {
      totalPage = newTotalPage;
    }
    
    public int getStartRow()
    {
       return minNum;
    }
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}    
    
}
