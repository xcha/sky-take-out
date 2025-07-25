package com.sky.controller.user;


import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("/status")
    public Result<Integer> getStatus() {
        log.info("查询店铺营业状态");
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);//获取redis中的营业状态
        log.info("当前店铺营业状态：{}", Objects.equals(status, 1) ? "营业中" : "打烊中");
        return Result.success(status);
    }
}
