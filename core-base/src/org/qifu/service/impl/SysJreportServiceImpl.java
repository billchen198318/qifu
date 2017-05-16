/* 
 * Copyright 2012-2016 bambooCORE, greenstep of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package org.qifu.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.qifu.base.SysMessageUtil;
import org.qifu.base.SysMsgConstants;
import org.qifu.base.dao.IBaseDAO;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchValue;
import org.qifu.base.model.SystemMessage;
import org.qifu.base.service.BaseService;
import org.qifu.dao.ISysJreportDAO;
import org.qifu.po.TbSysJreport;
import org.qifu.service.ISysJreportService;
import org.qifu.vo.SysJreportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("core.service.SysJreportService")
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class SysJreportServiceImpl extends BaseService<SysJreportVO, TbSysJreport, String> implements ISysJreportService<SysJreportVO, TbSysJreport, String> {
	protected Logger logger=Logger.getLogger(SysJreportServiceImpl.class);
	private ISysJreportDAO<TbSysJreport, String> sysJreportDAO;
	
	public SysJreportServiceImpl() {
		super();
	}

	public ISysJreportDAO<TbSysJreport, String> getSysJreportDAO() {
		return sysJreportDAO;
	}

	@Autowired
	@Resource(name="core.dao.SysJreportDAO")
	@Required		
	public void setSysJreportDAO(ISysJreportDAO<TbSysJreport, String> sysJreportDAO) {
		this.sysJreportDAO = sysJreportDAO;
	}

	@Override
	protected IBaseDAO<TbSysJreport, String> getBaseDataAccessObject() {
		return sysJreportDAO;
	}

	@Override
	public String getMapperIdPo2Vo() {		
		return MAPPER_ID_PO2VO;
	}

	@Override
	public String getMapperIdVo2Po() {
		return MAPPER_ID_VO2PO;
	}
	
	private Map<String, Object> getQueryGridParameter(SearchValue searchValue) throws Exception {
		return super.getQueryParamHandler(searchValue).fullEquals4TextField("reportId").getValue();
	}	

	@Override
	public QueryResult<List<SysJreportVO>> findGridResult(SearchValue searchValue, PageOf pageOf) throws ServiceException, Exception {
		if (searchValue==null || pageOf==null) {
			throw new ServiceException(SysMessageUtil.get(SysMsgConstants.SEARCH_NO_DATA));
		}
		Map<String, Object> params=this.getQueryGridParameter(searchValue);	
		int limit=Integer.parseInt(pageOf.getShowRow());
		int offset=(Integer.parseInt(pageOf.getSelect())-1)*limit;		
		QueryResult<List<SysJreportVO>> result=this.sysJreportDAO.findPageQueryResultByQueryName(
				"findSysJreportPageGrid", params, offset, limit);
		pageOf.setCountSize(String.valueOf(result.getRowCount()));
		pageOf.toCalculateSize();
		return result;
	}

	@Override
	public DefaultResult<SysJreportVO> findForSimple(String oid) throws ServiceException, Exception {
		if (StringUtils.isBlank(oid)) {
			throw new ServiceException(SysMessageUtil.get(SysMsgConstants.PARAMS_BLANK));
		}
		SysJreportVO searchObj = this.sysJreportDAO.findForSimple(oid);
		DefaultResult<SysJreportVO> result = new DefaultResult<SysJreportVO>();
		if (null!=searchObj && !StringUtils.isBlank(searchObj.getOid())) {
			result.setValue(searchObj);
		} else {
			result.setSystemMessage( new SystemMessage(SysMessageUtil.get(SysMsgConstants.DATA_NO_EXIST)) );
		}
		return result;
	}

	@Override
	public DefaultResult<SysJreportVO> findForSimpleByReportId(String reportId) throws ServiceException, Exception {
		if (StringUtils.isBlank(reportId)) {
			throw new ServiceException(SysMessageUtil.get(SysMsgConstants.PARAMS_BLANK));
		}
		SysJreportVO searchObj = this.sysJreportDAO.findForSimpleByReportId(reportId);
		DefaultResult<SysJreportVO> result = new DefaultResult<SysJreportVO>();
		if (null!=searchObj && !StringUtils.isBlank(searchObj.getOid())) {
			result.setValue(searchObj);
		} else {
			result.setSystemMessage( new SystemMessage(SysMessageUtil.get(SysMsgConstants.DATA_NO_EXIST)) );
		}
		return result;
	}

}
