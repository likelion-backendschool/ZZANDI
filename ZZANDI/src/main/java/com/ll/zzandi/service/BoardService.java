package com.ll.zzandi.service;

import com.ll.zzandi.domain.Board;
import com.ll.zzandi.dto.BoardDetailDto;
import com.ll.zzandi.dto.BoardListDto;
import com.ll.zzandi.dto.BoardUpdateFormDto;
import com.ll.zzandi.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
                .map(board -> new BoardListDto(board.getId(), board.getCategory(), board.getTitle(), board.getUser().getUserNickname(),
                        board.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")), board.getViews(), board.getHeart()));
    }

    public BoardDetailDto boardDetail(Long id) {
        Board board = boardRepository.findById(id).orElseThrow();
        String createdDate = board.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        return new BoardDetailDto(board.getId(), board.getTitle(), createdDate, board.getUser().getUserNickname(), board.getContent(), board.getViews(), board.getHeart(), 0);
    }

    public Long save(Board board) {
        Board saveBoard = boardRepository.save(board);
        return saveBoard.getId();
    }

    @Transactional
    public void updateView(Long id) {
        boardRepository.updateView(id);
    }

    public BoardUpdateFormDto boardUpdateForm(Long id) {
        Board board = boardRepository.findById(id).orElse(null);
        return new BoardUpdateFormDto(board.getId(), board.getTitle(), board.getUser().getUserNickname(), board.getContent(), board.getUpdatedDate());
    }

    @Transactional
    public void boardUpdate(Long id, BoardUpdateFormDto updateData) {
        Board findBoard = boardRepository.findById(id).orElseGet(null);
        findBoard.setTitle(updateData.getTitle());
        findBoard.setContent(updateData.getContent());
        findBoard.setUpdatedDate(LocalDateTime.now());
    }

    @Transactional
    public void boardDelete(Long id) {
        boardRepository.deleteById(id);
    }

}
