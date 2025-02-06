package com.example.backendweb.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName AttractionBookingRequest
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/6
 * @Version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttractionBookingRequest {
    private Integer userId;             // 用户ID
    private String uuid;                // 景点UUID
    private LocalDate visitDate;        // 参观日期
    private LocalDateTime visitTime;    // 参观时间
    private Integer numberOfTickets;    // 预订门票数量
    private Double price;      // 价格
}
