package com.duoer.takeout.dto;

import com.duoer.takeout.entity.OrderDetail;
import com.duoer.takeout.entity.Orders;
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
