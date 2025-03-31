package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransportRepository extends JpaRepository<Transport, String> {

    // Lấy tất cả transport chưa bị xóa (deleteAt = null)
    List<Transport> findAllByDeleteAtIsNull();

    // Lấy transport theo ID và chưa bị xóa
    Optional<Transport> findByIdAndDeleteAtIsNull(String id);

    // Kiểm tra tồn tại transport theo ID và chưa bị xóa
    boolean existsByIdAndDeleteAtIsNull(String id);

    // Lấy tất cả transport đang active và chưa bị xóa
    List<Transport> findAllByActiveIsTrueAndDeleteAtIsNull();

    // Lấy transport theo tên và chưa bị xóa (phục vụ check trùng tên)
    Optional<Transport> findByNameAndDeleteAtIsNull(String name);

    // Cập nhật trạng thái active
    @Query("UPDATE Transport t SET t.active = :active WHERE t.id = :id")
    void updateActiveStatus(String id, boolean active);

    // Soft delete bằng cách set deleteAt
    @Query("UPDATE Transport t SET t.deleteAt = :deleteAt WHERE t.id = :id")
    void softDelete(String id, LocalDateTime deleteAt);
}