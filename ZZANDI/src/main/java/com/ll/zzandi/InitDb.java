package com.ll.zzandi;


import com.ll.zzandi.domain.Board;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

//    @PostConstruct
//    public void init() {
//        initService.createData();
//    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void createData() {
            String userId="testId";
            String password="1234";
            String userEmail="test@test.com";
            String userNickname="아이유";

            UserDto.RegisterRequest userDto =new UserDto.RegisterRequest(userId,password,userEmail,userNickname);
            User user = User.of(userDto);
            em.persist(user);

            for (int i = 1; i <= 100; i++) {
                Board board = new Board(user, user.getUserNickname(), "제목" + i, "내용" + i, 0, 0);
                em.persist(board);
            }
        }
    }
}