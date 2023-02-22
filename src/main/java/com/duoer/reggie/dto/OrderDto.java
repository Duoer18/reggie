package com.duoer.reggie.dto;

import com.duoer.reggie.entity.OrderDetail;
import com.duoer.reggie.entity.Orders;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OrderDto extends Orders {
    private List<OrderDetail> orderDetails;
}
