package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;


    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();//购物车数据
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);//浅拷贝
        Long userId = BaseContext.getCurrentId();//用户id
        shoppingCart.setUserId(userId);//设置用户id
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);//查询当前菜品或套餐是否在购物车中

        if (list != null && list.size() > 0) {//购物车中存在
            ShoppingCart cart = list.get(0);//购物车中已经存在
            cart.setNumber(cart.getNumber() + 1);//数量加一
            shoppingCartMapper.updateNumberById(cart);//更新
        }else {//购物车中不存在
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {//添加的是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }else {
               Long setmealId = shoppingCartDTO.getSetmealId();
               Setmeal setmeal = setmealMapper.getById(setmealId);

                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);//插入数据
        }
    }


    @Override
    public List<ShoppingCart> list() {
        Long userId = BaseContext.getCurrentId();//获取当前用户id
        ShoppingCart shoppingCart1 = new ShoppingCart();//创建查询条件对象
        shoppingCart1.setUserId(userId);//设置查询条件
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart1);
        return shoppingCartList;
    }

    @Override
    public void clean() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //设置查询条件，查询当前登录用户的购物车数据
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        if(list != null && list.size() > 0){
            shoppingCart = list.get(0);

            Integer number = shoppingCart.getNumber();
            if(number == 1){
                //当前商品在购物车中的份数为1，直接删除当前记录
                shoppingCartMapper.deleteById(shoppingCart.getId());
            }else {
                //当前商品在购物车中的份数不为1，修改份数即可
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updateNumberById(shoppingCart);
            }
        }
    }

    @Override
    public void deleteByUserId(Long userId) {
        shoppingCartMapper.deleteByUserId(userId);
        log.info("删除购物车数据成功");
    }


}
