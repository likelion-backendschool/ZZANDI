package com.ll.zzandi.service;

import com.ll.zzandi.domain.Board;
import com.ll.zzandi.dto.BoardDetailDto;
import com.ll.zzandi.dto.BoardListDto;
import com.ll.zzandi.dto.BoardUpdateFormDto;
import com.ll.zzandi.repository.BoardRepository;
import com.ll.zzandi.repository.CommentRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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

    public BoardDetailDto findBoardDetail(Long boardId, int page) {
        Board board = boardRepository.findById(boardId).orElseThrow();
        Optional<Board> prevBoard = boardRepository.findPrevBoard(boardId);
        Optional<Board> nextBoard = boardRepository.findNextBoard(boardId);

        Long prev = prevBoard.isPresent() ? prevBoard.get().getId() : null;
        Long next = nextBoard.isPresent() ? nextBoard.get().getId() : null;

        String createdDate = board.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        return BoardDetailDto.builder()
                .boardId(board.getId())
                .userUUID(board.getUser().getId())
                .userId(board.getUser().getUserId())
                .title(board.getTitle())
                .createdDate(createdDate)
                .writer(board.getUser().getUserNickname())
                .content(board.getContent())
                .views(board.getViews())
                .heart(board.getHeart())
                .count(0)
                .page(page)
                .prev(prev)
                .next(next)
                .profile(board.getUser().getUserprofileUrl()).build();
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