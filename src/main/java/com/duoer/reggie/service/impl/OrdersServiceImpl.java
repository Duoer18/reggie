package com.duoer.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duoer.reggie.common.BaseContext;
import com.duoer.reggie.common.ServiceException;
import com.duoer.reggie.dao.OrdersMapper;
import com.duoer.reggie.dto.OrderDto;
import com.duoer.reggie.entity.*;
import com.duoer.reggie.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;

    private OrderDetail getDetailByCart(ShoppingCart cart, long oid) {
        OrderDetail orderDetail = new OrderDetail();
        BeanUtils.copyProperties(cart, orderDetail, "id", "userId", "createTime");
        orderDetail.setOrderId(oid);
        return orderDetail;
    }

    @Transactional
    @Override
    public boolean addOrder(Orders order) {
        Long uid = BaseContext.getEId();

        List<ShoppingCart> shoppingCarts = shoppingCartService.listCarts(uid);
        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new ServiceException("empty order");
        }

        User user = userService.getById(uid);
        if (user == null) {
            throw new ServiceException("invalid user");
        }

        AddressBook address = addressBookService.getById(order.getAddressBookId());
        if (address == null) {
            throw new ServiceException("invalid address");
        }

        long orderId = IdWorker.getId();
        order.setId(orderId);
        order.setNumber(String.valueOf(orderId));
        order.setUserId(uid);
        order.setAmount(shoppingCartService.getAmount(uid));
        order.setStatus(2);
        order.setUserName(user.getName());
        order.setConsignee(address.getConsignee());
        order.setPhone(address.getPhone());
        order.setAddress((address.getProvinceName() == null ? "" : address.getProvinceName())
                + (address.getCityName() == null ? "" : address.getCityName())
                + (address.getDistrictName() == null ? "" : address.getDistrictName())
                + (address.getDetail() == null ? "" : address.getDetail()));
        order.setOrderTime(LocalDateTime.now());
        order.setCheckoutTime(LocalDateTime.now());
        boolean isSaved = save(order);

        if (isSaved) {
            List<OrderDetail> orderDetails = shoppingCarts.stream()
                    .map(cart -> getDetailByCart(cart, orderId))
                    .collect(Collectors.toList());
            isSaved = orderDetailService.saveBatch(orderDetails);

            shoppingCartService.clean(uid);
            return isSaved;
        } else {
            return false;
        }
    }

    private OrderDto getDto(Orders order) {
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(order, orderDto);

        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, order.getId());

        orderDto.setOrderDetails(orderDetailService.list(queryWrapper));
        return orderDto;
    }

    @Override
    public Page<? extends Orders> getUserOrdersByPage(int page, int pageSize, boolean withDetails) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getEId())
                .orderByDesc(Orders::getOrderTime);
        return getOrdersByPage(page, pageSize, withDetails, queryWrapper);
    }

    @Override
    public Page<? extends Orders> getAllOrdersByPage(int page, int pageSize, String number,
                                                     String beginTime, String endTime,
                                                     boolean withDetails) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(number != null, Orders::getNumber, number)
                .ge(beginTime != null, Orders::getOrderTime, beginTime)
                .le(endTime != null, Orders::getOrderTime, endTime)
                .orderByDesc(Orders::getOrderTime);
        return getOrdersByPage(page, pageSize, withDetails, queryWrapper);
    }

    @Override
    public Page<? extends Orders> getOrdersByPage(int page, int pageSize, boolean withDetails,
                                                      LambdaQueryWrapper<Orders> queryWrapper) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        page(ordersPage, queryWrapper);

        if (withDetails) {
            List<OrderDto> orderDtoList = ordersPage.getRecords()
                    .stream()
                    .map(this::getDto)
                    .collect(Collectors.toList());
            Page<OrderDto> orderDtoPage = new Page<>(page, pageSize);
            BeanUtils.copyProperties(orderDtoPage, orderDtoPage, "records");
            orderDtoPage.setRecords(orderDtoList);
            return orderDtoPage;
        } else {
            return ordersPage;
        }
    }

    @Transactional
    @Override
    public boolean orderAgain(Orders order) {
        Orders postOrder = getById(order.getId());
        BeanUtils.copyProperties(postOrder, order, "id", "number", "status",
                "orderTime", "checkoutTime");

        long orderId = IdWorker.getId();
        order.setId(orderId);
        order.setNumber(String.valueOf(orderId));
        order.setStatus(2);
        order.setOrderTime(LocalDateTime.now());
        order.setCheckoutTime(LocalDateTime.now());

        boolean isSaved = save(order);
        if (isSaved) {
            LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderDetail::getOrderId, postOrder.getId());

            List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper);
            orderDetails.forEach(orderDetail -> {
                orderDetail.setId(null);
                orderDetail.setOrderId(orderId);
            });
            isSaved = orderDetailService.saveBatch(orderDetails);

            return isSaved;
        } else {
            return false;
        }
    }
}
