package com.leyou.cart.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.auth.entity.UserInfo;
import com.leyou.cart.entity.Cart;
import com.leyou.cart.inteceptors.UserTokenInterceptor;
import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserTokenInterceptor tokenInterceptor;

    /**
     * 添加购物车
     *
     * @param cart
     */
    public void addCart(Cart cart) {
        //从token中获取个人信息
        UserInfo userInfo = tokenInterceptor.getUserInfo();
        //将Long类型的Id转化成String
        String key = String.valueOf(userInfo.getId());
        //双层map
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(key);

        //hKey其实就是sku的id
        String hKey = String.valueOf(cart.getSkuId());

        //判断用户是否把此sku加入过redis购物车，如果有，说明加入过
        if (ops.hasKey(hKey)) {
            String cartJson = ops.get(hKey);

            //数据库中存储的redis信息，:nativeRead：将json数据转换成任意对象

            Cart storeCart = JsonUtils.nativeRead(cartJson, new TypeReference<Cart>() {
            });

            //更改数量
            storeCart.setNum(storeCart.getNum() + cart.getNum());

            //TODO ,除了数量之外其他也应该更新

            //更新数量后要重新保存
            ops.put(hKey, JsonUtils.toString(storeCart));

        } else {//没有加入
            ops.put(hKey, JsonUtils.toString(cart));
        }

    }

    /**
     * 查询购物车
     *
     * @return
     */
    public List<Cart> queryCarts() {
        //获取Token中的信息
        UserInfo userInfo = tokenInterceptor.getUserInfo();
        //根据Id获取token
        String key = String.valueOf(userInfo.getId());
        //先判断，这个用户是否曾加入数据到购物车
        if (redisTemplate.hasKey(key)) {

            BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(key);

            //获取到当前用户的所有购物车json对象
            return ops.values()       //List<String>
                    .stream()        //stream流进行流式运算Stream<String>
                    .map(value -> {
                        return JsonUtils.nativeRead(value, new TypeReference<Cart>() {
                        });//Stream<Cart>
                    }).collect(Collectors.toList());//List<Cart>
        } else {
            log.error("购物车数据为空");
            throw new LyException(ExceptionEnum.CART_IS_NULL);
        }
    }

    /**
     * 修改购物车内的数据
     *
     * @param id
     * @param num
     */
    public void modifyCartNum(Long id, Integer num) {
        //获取token数据
        UserInfo userInfo = tokenInterceptor.getUserInfo();

        String key = String.valueOf(userInfo.getId());
        //直接根据用户的Id获取redis的操作对象
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(key);

        //hKey就是skuId
        String hKey = String.valueOf(id);

        //判断要修改的sku商品是否在数据库中
        if (ops.hasKey(hKey)) {//存在修改数量

            Cart cart = JsonUtils.nativeRead(ops.get(hKey), new TypeReference<Cart>() {
            });

            cart.setNum(num);

            ops.put(hKey, JsonUtils.toString(cart));
        } else { //商品已经不在，所以抛出异常
            log.error("商品已经不存在");
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }

    }

    /**
     * 合并购物车
     *
     * @param carts
     */
    public void mergeCart(List<Cart> carts) {
        carts.forEach(this::addCart);
    }
}
