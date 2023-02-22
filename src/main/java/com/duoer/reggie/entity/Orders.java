package com.duoer.reggie.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    /** 订单号 */
    private String number;
    /** 订单状态 1待付款，2待派送，3已派送，4已完成，5已取消 */
    private Integer status;
    private Long userId;
    private Long addressBookId;
    private LocalDateTime orderTime;
    private LocalDateTime checkoutTime;
    /** 支付方式 1微信，2支付宝 */
    private Integer payMethod;
    private BigDecimal amount;
    private String remark;
    private String userName;
    private String phone;
    private String address;
    private String consignee;
}
