/* 
 * Copyright 2012-2017 qifu of copyright Chen Xin Nien
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
package org.qifu.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.qifu.base.controller.BaseController;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.QueryControllerJsonResultObj;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchValue;
import org.qifu.po.TbSys;
import org.qifu.po.TbSysCtxBean;
import org.qifu.service.ISysCtxBeanService;
import org.qifu.service.ISysService;
import org.qifu.service.logic.ISystemContextBeanLogicService;
import org.qifu.vo.SysCtxBeanVO;
import org.qifu.vo.SysVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Controller
public class SystemContextBeanAction extends BaseController {
	
	private ISysCtxBeanService<SysCtxBeanVO, TbSysCtxBean, String> sysCtxBeanService;
	private ISysService<SysVO, TbSys, String> sysService;
	private ISystemContextBeanLogicService systemContextBeanLogicService;

	public ISysCtxBeanService<SysCtxBeanVO, TbSysCtxBean, String> getSysCtxBeanService() {
		return sysCtxBeanService;
	}

	@Autowired
	@Resource(name="core.service.SysCtxBeanService")
	@Required	
	public void setSysCtxBeanService(ISysCtxBeanService<SysCtxBeanVO, TbSysCtxBean, String> sysCtxBeanService) {
		this.sysCtxBeanService = sysCtxBeanService;
	}

	public ISysService<SysVO, TbSys, String> getSysService() {
		return sysService;
	}

	@Autowired
	@Resource(name="core.service.SysService")
	@Required
	public void setSysService(ISysService<SysVO, TbSys, String> sysService) {
		this.sysService = sysService;
	}

	public ISystemContextBeanLogicService getSystemContextBeanLogicService() {
		return systemContextBeanLogicService;
	}

	@Autowired
	@Resource(name="core.service.logic.SystemContextBeanLogicService")
	@Required
	public void setSystemContextBeanLogicService(ISystemContextBeanLogicService systemContextBeanLogicService) {
		this.systemContextBeanLogicService = systemContextBeanLogicService;
	}
	
	private void init(String type, HttpServletRequest request, ModelAndView mv) throws ServiceException, ControllerException, Exception {
		mv.addObject("sysMap", this.sysService.findSysMap(this.getBasePath(request), true));
	}
	
	private void fetchData(SysCtxBeanVO sysCtxBean, ModelAndView mv) throws ServiceException, ControllerException, Exception {
		
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0006Q")
	@RequestMapping(value = "/core.sysCtxbeanManagement.do")	
	public ModelAndView queryPage(HttpServletRequest request) {
		String viewName = PAGE_SYS_ERROR;
		ModelAndView mv = this.getDefaultModelAndView("CORE_PROG001D0006Q");
		try {
			this.init("queryPage", request, mv);
			viewName = "sys-ctxbean/sys-ctxbean-management";
		} catch (AuthorityException e) {
			viewName = PAGE_SYS_NO_AUTH;
		} catch (ServiceException | ControllerException e) {
			viewName = PAGE_SYS_SEARCH_NO_DATA;
		} catch (Exception e) {
			e.printStackTrace();
			this.setPageMessage(request, e.getMessage().toString());
		}
		mv.setViewName(viewName);
		return mv;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0006Q")
	@RequestMapping(value = "/core.sysCtxbeanQueryGridJson.do", produces = "application/json")	
	public @ResponseBody QueryControllerJsonResultObj< List<SysCtxBeanVO> > queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj< List<SysCtxBeanVO> > result = this.getQueryJsonResult("CORE_PROG001D0006Q");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult< List<SysCtxBeanVO> > queryResult = this.sysCtxBeanService.findGridResult(searchValue, pageOf);
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			result.setMessage( e.getMessage().toString() );
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage( e.getMessage().toString() );
		}
		return result;
	}

}
