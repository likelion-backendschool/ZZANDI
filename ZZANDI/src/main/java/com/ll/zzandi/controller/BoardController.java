package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Board;
import com.ll.zzandi.domain.Study;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.BoardListDto;
import com.ll.zzandi.dto.BoardUpdateFormDto;
import com.ll.zzandi.dto.BoardWriteDto;
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
    public String boardListPaging(@PathVariable Long studyId, @RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("page", page);
        model.addAttribute("studyId", studyId);
        return "board/boardList";
    }

    @GetMapping("/list-data")
    @ResponseBody
    public Page<BoardListDto> boardListPagingToJson(@PathVariable Long studyId, @RequestParam(defaultValue = "0") int page) {
        return boardService.boardListPaging(page, studyId);
    }

    @GetMapping("/detail/{boardId}/{page}")
    public String boardDetail(@AuthenticationPrincipal User user, @PathVariable Long studyId, @PathVariable Long boardId, @PathVariable int page, Model model) {
        boardService.updateView(boardId);

        model.addAttribute("boardDetail", boardService.boardDetail(boardId, page));
        model.addAttribute("userUUID", user.getId());
        model.addAttribute("studyId", studyId);
        return "board/boardDetail";
    }

    @GetMapping("/write")
    public String boardWriteForm(@PathVariable Long studyId, BoardWriteDto boardWriteDto, Model model) {
        model.addAttribute("studyId", studyId);
        return "board/boardWriteForm";
    }

    @PostMapping("/write")
    public String boardWrite(@AuthenticationPrincipal User user, @PathVariable Long studyId, @Valid BoardWriteDto boardWriteDto, BindingResult result) {
        if (result.hasErrors()) {
            return "/board/boardWriteForm";
        }

        Study study = studyService.findById(studyId).get();
        Board board = new Board(user, boardWriteDto.getCategory(), boardWriteDto.getTitle(), boardWriteDto.getContent(), 0, 0, study);
        Long boardId = boardService.save(board);

        return "redirect:/%d/board/detail/%d/0".formatted(studyId, boardId);
    }

    @GetMapping("/update/{boardId}/{page}")
    public String boardUpdateForm(@PathVariable Long studyId, @PathVariable Long boardId, @PathVariable int page, Model model) {
        model.addAttribute("updateDto", boardService.boardUpdateForm(boardId, page));
        model.addAttribute("studyId", studyId);
        return "board/boardUpdateForm";
    }

    @PostMapping("/update/{boardId}/{page}")
    public String boardUpdate(@PathVariable Long studyId, @PathVariable Long boardId, @PathVariable int page, BoardUpdateFormDto updateFormDto) {
        boardService.boardUpdate(boardId, updateFormDto);
        return "redirect:/%d/board/detail/%d/%d".formatted(studyId, boardId, page);
    }

    @PostMapping("/delete/{boardId}")
    public String boardDelete(@PathVariable Long studyId, @PathVariable Long boardId) {
        commentService.deleteComment(boardId);
        boardService.boardDelete(boardId);
        return "redirect:/%d/board/list".formatted(studyId);
    }
}