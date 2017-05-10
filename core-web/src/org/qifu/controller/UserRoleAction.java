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
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.qifu.base.controller.BaseController;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.YesNo;
import org.qifu.po.TbAccount;
import org.qifu.po.TbRole;
import org.qifu.service.IAccountService;
import org.qifu.service.IRoleService;
import org.qifu.service.logic.IRoleLogicService;
import org.qifu.vo.AccountVO;
import org.qifu.vo.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Controller
public class UserRoleAction extends BaseController {
	
	private IAccountService<AccountVO, TbAccount, String> accountService;
	private IRoleService<RoleVO, TbRole, String> roleService;
	private IRoleLogicService roleLogicService;
	
	public IAccountService<AccountVO, TbAccount, String> getAccountService() {
		return accountService;
	}

	@Autowired
	@Resource(name="core.service.AccountService")
	@Required	
	public void setAccountService(IAccountService<AccountVO, TbAccount, String> accountService) {
		this.accountService = accountService;
	}

	public IRoleService<RoleVO, TbRole, String> getRoleService() {
		return roleService;
	}

	@Autowired
	@Resource(name="core.service.RoleService")
	@Required	
	public void setRoleService(IRoleService<RoleVO, TbRole, String> roleService) {
		this.roleService = roleService;
	}

	public IRoleLogicService getRoleLogicService() {
		return roleLogicService;
	}

	@Autowired
	@Resource(name="core.service.logic.RoleLogicService")
	@Required	
	public void setRoleLogicService(IRoleLogicService roleLogicService) {
		this.roleLogicService = roleLogicService;
	}

	private void init(String type, HttpServletRequest request, ModelAndView mv) throws ServiceException, ControllerException, Exception {
		mv.addObject("accountMap", this.accountService.findForAllMap(true));
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0002Q")
	@RequestMapping(value = "/core.userRoleManagement.do")	
	public ModelAndView queryPage(HttpServletRequest request) {
		String viewName = PAGE_SYS_ERROR;
		ModelAndView mv = this.getDefaultModelAndView("CORE_PROG002D0002Q");
		try {
			this.init("queryPage", request, mv);
			viewName = "user-role/user-role-management";
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0002Q")
	@RequestMapping(value = "/core.userRoleListByAccountOidJson.do", produces = "application/json")		
	public @ResponseBody DefaultControllerJsonResultObj< Map<String, List<RoleVO>> > queryRoleListByAccountOid(HttpServletRequest request, @RequestParam(name="accountOid") String accountOid) {
		DefaultControllerJsonResultObj< Map<String, List<RoleVO>> > result = this.getDefaultJsonResult("CORE_PROG002D0002Q");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}		
		try {
			Map<String, List<RoleVO>> searchDataMap = this.roleLogicService.findForAccountRoleEnableAndAll(accountOid);
			result.setValue(searchDataMap);
			result.setSuccess( YesNo.YES );
		} catch (AuthorityException | ServiceException | ControllerException e) {
			result.setMessage( e.getMessage().toString() );			
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage( e.getMessage().toString() );
		}		
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0002Q")
	@RequestMapping(value = "/core.userRoleUpdateJson.do", produces = "application/json")		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> updateMenu(HttpServletRequest request, @RequestParam(name="accountOid") String accountOid, @RequestParam(name="appendOid") String appendOid) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult("CORE_PROG002D0002Q");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			DefaultResult<Boolean> updateResult = this.roleLogicService.updateUserRole(accountOid, this.transformAppendKeyStringToList(appendOid));
			if (updateResult.getValue() != null && updateResult.getValue()) {
				result.setSuccess(YesNo.YES);
			}
			result.setMessage( updateResult.getSystemMessage().getValue() );
		} catch (AuthorityException | ServiceException | ControllerException e) {
			result.setMessage( e.getMessage().toString() );			
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage( e.getMessage().toString() );
		}
		return result;
	}	
	
}
