/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.test.client;

import org.springframework.cloud.openfeign.FeignClient;

import com.jeesite.modules.cloud.feign.condition.ConditionalOnNotCurrentApplication;
import com.jeesite.modules.test.api.TestDataServiceApi;

/**
 * TestDataClient
 * @author ThinkGem
 * @version 2018-10-18
 */
@FeignClient(name="${service.search.name}", path="${service.search.path}")
@ConditionalOnNotCurrentApplication(name="${service.search.name}")
public interface TestDataServiceClient extends TestDataServiceApi {
	
}
