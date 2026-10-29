// package com.school.project1.backend.entity;
//
// import org.springframework.data.annotation.Id;
// import org.springframework.data.relational.core.mapping.Column;
// import org.springframework.data.relational.core.mapping.Table;
//
// import java.time.LocalTime;
//
// @Table("stop")
// public record StopEntity(
//     @Id Long id,
//     @Column("stop_number") int stopNumber,
//     @Column("stop_name") String stopName,
//     @Column("arrival_time") LocalTime arrivalTime,
//     @Column("departure_time") LocalTime departureTime,
//     @Column("facility") String facility
// ) {
// }
