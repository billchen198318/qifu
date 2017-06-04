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
package org.qifu.service;

import java.util.List;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchValue;
import org.qifu.base.service.ISimpleService;
import org.qifu.po.TbSysLoginLog;

public interface ISysLoginLogService<T extends java.io.Serializable, PK extends java.io.Serializable> extends ISimpleService<T, PK> {
	
	public QueryResult<List<TbSysLoginLog>> findGridResult(SearchValue searchValue, PageOf pageOf) throws ServiceException, Exception;
	
	public int deleteAll() throws ServiceException, Exception;

}
