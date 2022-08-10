package com.ll.zzandi.service;

import com.ll.zzandi.dto.BoardListDto;
import com.ll.zzandi.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // default: readOnly = false (읽기/쓰기)
public class BoardService {

    private final BoardRepository boardRepository;

    // 게시물 페이지 별로 조회
    public Page<BoardListDto> boardListPaging(Pageable pageable, int page) {
        // 페이지는 0부터 시작하고 size 개수만큼 잘라서 가져온다.
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        return boardRepository.findBoardBy(pageRequest)
                .map(board -> new BoardListDto(board.getId(), board.getTitle(), board.getWriter(),
                        board.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")), board.getViews(), board.getRecommend()));
    }

}
