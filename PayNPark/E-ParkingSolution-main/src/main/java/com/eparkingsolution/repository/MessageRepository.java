package com.eparkingsolution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eparkingsolution.model.Message;
import com.eparkingsolution.model.User;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverOrderBySentDateTimeDesc(User recipient);
//    List<Message> findByRecipientOrderByTimestampDesc(User recipient);
//    List<Message> findByRecipientOrderByTimestampDesc(User recipient);

}
