package com.ll.zzandi.service;

import com.ll.zzandi.domain.Board;
import com.ll.zzandi.dto.BoardDetailDto;
import com.ll.zzandi.dto.BoardListDto;
import com.ll.zzandi.dto.BoardUpdateFormDto;
import com.ll.zzandi.repository.BoardRepository;
import com.ll.zzandi.repository.CommentRepository;
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
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<BoardListDto> findBoardListPaging(int page, Long studyId) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Board> boardList = boardRepository.findBoardList(pageRequest, studyId);

        return boardList.map(board -> new BoardListDto(board.getId(), board.getUser().getUserId(), board.getCategory(), board.getTitle(), board.getUser().getUserNickname(),
                        board.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")), board.getViews(), board.getHeart(), page, board.getComments().size(), board.getUser().getUserprofileUrl()));
    }

    public BoardDetailDto detailBoard(Long boardId, int page) {
        Board board = boardRepository.findById(boardId).orElseThrow();
        String createdDate = board.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        return new BoardDetailDto(board.getId(), board.getUser().getId(), board.getUser().getUserId(), board.getTitle(),
                createdDate, board.getUser().getUserNickname(), board.getContent(),
                board.getViews(), board.getHeart(), 0, page, board.getUser().getUserprofileUrl());
    }

    public Board findByBoardId(Long boardId) {
        return boardRepository.findById(boardId).get();
    }

    @Transactional
    public Long createBoard(Board board) {
        Board saveBoard = boardRepository.save(board);
        return saveBoard.getId();
    }

    @Transactional
    public void updateBoardView(Long id) {
        boardRepository.updateView(id);
    }

    public BoardUpdateFormDto updateBoardForm(Long id, int page) {
        Board board = boardRepository.findById(id).orElse(null);
        return new BoardUpdateFormDto(board.getId(), board.getCategory(), board.getTitle(), board.getUser().getUserNickname(), board.getContent(), page, board.getUpdatedDate());
    }

    @Transactional
    public void updateBoard(Long id, BoardUpdateFormDto updateData) {
        Board findBoard = boardRepository.findById(id).orElseGet(null);
        findBoard.setCategory(updateData.getCategory());
        findBoard.setTitle(updateData.getTitle());
        findBoard.setContent(updateData.getContent());
        findBoard.setUpdatedDate(LocalDateTime.now());
    }

    @Transactional
    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public void deleteBoardByStudyId(Long studyId) {
        boardRepository.deleteBoardByStudyId(studyId);
    }

}