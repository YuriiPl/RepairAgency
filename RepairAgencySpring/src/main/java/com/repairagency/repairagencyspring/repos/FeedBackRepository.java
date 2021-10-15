package com.repairagency.repairagencyspring.repos;

import com.repairagency.repairagencyspring.entity.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {
//update feed_back set message=? where repair_task_id=?
//    @Modifying
//    @Query("update FeedBack u set u.message = ?1 where u.id = ?2")
//    void updateMessageById(String message, Long feedBackId);
}
