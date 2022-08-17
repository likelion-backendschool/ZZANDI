package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Board;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.BoardDetailDto;
import com.ll.zzandi.dto.BoardListDto;
import com.ll.zzandi.dto.BoardUpdateFormDto;
import com.ll.zzandi.dto.BoardWriteDto;
import com.ll.zzandi.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list-ajax")
    public String ajaxTest() {
        return "board/boardListAjax";
    }

    @GetMapping("/list")
    public String boardListPaging(Model model, Pageable pageable, @RequestParam(defaultValue = "0") int page) {
        Page<BoardListDto> boardList = boardService.boardListPaging(pageable, page);
        model.addAttribute("boardList", boardList); // 게시물 데이터
        model.addAttribute("totalPage", boardList.getTotalPages()); // 총 페이지
        model.addAttribute("page", 10); // 10개씩 끊기
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber()); // 현재 페이지 기준 이전 페이지
        model.addAttribute("next", pageable.next().getPageNumber()); // 현재 페이지 기준 다음 페이지
        model.addAttribute("hasNext", boardList.hasNext());
        model.addAttribute("hasPrev", boardList.hasPrevious());
        return "board/boardList";
    }

    @GetMapping("/list-json")
    @ResponseBody
    public Page<BoardListDto> boardListPagingToJson(Pageable pageable, @RequestParam(defaultValue = "0") int page) {
        return boardService.boardListPaging(pageable, page);
    }

    @GetMapping("/detail/{id}")
    public String boardDetail(Model model, @PathVariable Long id) {
        boardService.updateView(id);
        BoardDetailDto boardDetail = boardService.boardDetail(id);
        model.addAttribute("boardDetail", boardDetail);
        return "board/boardDetail";
    }

    @GetMapping("/update/{id}")
    public String boardUpdateForm(@PathVariable Long id, Model model) {
        BoardUpdateFormDto updateFormDto = boardService.boardUpdateForm(id);
        model.addAttribute("updateDto", updateFormDto);
        return "board/boardUpdateForm";
    }

    @GetMapping("/write")
    public String boardWriteForm() {
        return "board/boardWriteForm";
    }

    @PostMapping("/write")
    public String boardWrite(BoardWriteDto boardWriteDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal(); // 현재 로그인 한 유저 정보

        Board board = new Board(user, boardWriteDto.getTitle(), boardWriteDto.getContent(), 0, 0);
        Long saveId = boardService.save(board);

        return "redirect:/board/detail/" + saveId;
    }

    @PostMapping("/update/{id}")
    public String boardUpdate(@PathVariable Long id, BoardUpdateFormDto updateFormDto) {
        boardService.boardUpdate(id, updateFormDto);
        return "redirect:/board/detail/" + id;
    }

    @PostMapping("/delete/{id}")
    public String boardDelete(@PathVariable Long id) {
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

}
