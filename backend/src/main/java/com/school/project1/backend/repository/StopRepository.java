// package com.school.project1.backend.repository;
//
// import com.school.project1.backend.entity.StopEntity;
// import org.springframework.data.jdbc.repository.query.Query;
// import org.springframework.data.repository.ListCrudRepository;
// import org.springframework.stereotype.Repository;
//
// import java.util.List;
//
// @Repository
// public interface StopRepository extends ListCrudRepository<StopEntity, Long> {
//
//     @Query("""
//         SELECT id,
//                stop_number,
//                stop_name,
//                arrival_time,
//                departure_time,
//                facility
//         FROM stop
//         ORDER BY stop_number
//         """)
//     List<StopEntity> findAllOrderedByStopNumber();
// }
