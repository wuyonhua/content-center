package com.itmuch.contentcenter.feignclient;

import com.itmuch.contentcenter.domain.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-center")
public interface TestUserCenterFeignClient {

    /**
     * 使用了@SpringQueryMap 就不能使用Feign继承了
     *
     * @param userDTO
     * @return
     */
    @GetMapping("/q")
    UserDTO query(@SpringQueryMap UserDTO userDTO);
}