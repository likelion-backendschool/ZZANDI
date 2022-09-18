package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Board;
import com.ll.zzandi.domain.Study;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.board.BoardCreateDto;
import com.ll.zzandi.dto.board.BoardListDto;
import com.ll.zzandi.dto.board.BoardUpdateFormDto;
import com.ll.zzandi.service.BoardService;
import com.ll.zzandi.service.CommentService;
import com.ll.zzandi.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/{studyId}/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final StudyService studyService;

    @GetMapping("/list")
    public String findBoardListPaging(@PathVariable Long studyId, @RequestParam(defaultValue = "0") int page, Model model) {
        String studyTitle = studyService.findByStudyId(studyId).get().getStudyTitle();

        model.addAttribute("page", page);
        model.addAttribute("studyId", studyId);
        model.addAttribute("studyTitle", studyTitle);
        return "board/boardList";
    }

    @GetMapping("/list-data")
    @ResponseBody
    public Page<BoardListDto> findBoardListPagingToJson(@PathVariable Long studyId, @RequestParam(defaultValue = "0") int page) {
        return boardService.findBoardListPaging(page, studyId);
    }

    @GetMapping("/detail/{boardId}/{page}")
    public String findBoardDetail(@AuthenticationPrincipal User user, @PathVariable Long studyId, @PathVariable Long boardId, @PathVariable int page, Model model) {
        boardService.updateBoardView(boardId);
        String studyTitle = studyService.findByStudyId(studyId).get().getStudyTitle();

        model.addAttribute("boardDetail", boardService.findBoardDetail(boardId, page));
        model.addAttribute("userUUID", user.getId());
        model.addAttribute("studyId", studyId);
        model.addAttribute("studyTitle", studyTitle);
        return "board/boardDetail";
    }

    @GetMapping("/create")
    public String createBoardForm(@PathVariable Long studyId, BoardCreateDto boardCreateDto, Model model) {
        model.addAttribute("studyId", studyId);
        return "board/boardWriteForm";
    }

    @PostMapping("/create")
    public String createBoard(@AuthenticationPrincipal User user, @PathVariable Long studyId, @Valid BoardCreateDto boardCreateDto, BindingResult result) {
        if (result.hasErrors()) {
            return "/board/boardWriteForm";
        }

        Study study = studyService.findByStudyId(studyId).get();
        Board board = new Board(user, boardCreateDto.getCategory(), boardCreateDto.getTitle(), boardCreateDto.getContent(), 0, 0, study);
        Long boardId = boardService.createBoard(board);

        return "redirect:/%d/board/detail/%d/0".formatted(studyId, boardId);
    }

    @GetMapping("/update/{boardId}/{page}")
    public String updateBoardForm(@PathVariable Long studyId, @PathVariable Long boardId, @PathVariable int page, Model model) {
        model.addAttribute("updateDto", boardService.updateBoardForm(boardId, page));
        model.addAttribute("studyId", studyId);
        return "board/boardUpdateForm";
    }

    @PostMapping("/update/{boardId}/{page}")
    public String updateBoard(@PathVariable Long studyId, @PathVariable Long boardId, @PathVariable int page, BoardUpdateFormDto updateFormDto) {
        boardService.updateBoard(boardId, updateFormDto);
        return "redirect:/%d/board/detail/%d/%d".formatted(studyId, boardId, page);
    }

    @PostMapping("/delete/{boardId}")
    public String deleteBoard(@PathVariable Long studyId, @PathVariable Long boardId) {
        commentService.deleteCommentByBoardId(boardId);
        boardService.deleteBoard(boardId);
        return "redirect:/%d/board/list".formatted(studyId);
    }
}