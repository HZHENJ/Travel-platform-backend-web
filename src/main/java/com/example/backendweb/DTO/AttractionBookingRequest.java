package com.example.backendweb.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private Integer userId;       // 用户ID
    private String uuid;          // 景点UUID
    private LocalDate visitDate;  // 参观日期
    private String ticketType;    // 门票类型
    private Integer numberOfTickets; // 预订门票数量
}
