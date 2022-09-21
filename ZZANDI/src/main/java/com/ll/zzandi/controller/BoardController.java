package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Board;
import com.ll.zzandi.domain.File;
import com.ll.zzandi.domain.Study;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.board.BoardCreateDto;
import com.ll.zzandi.dto.board.BoardFileDto;
import com.ll.zzandi.dto.board.BoardListDto;
import com.ll.zzandi.dto.board.BoardUpdateFormDto;
import com.ll.zzandi.enumtype.TableType;
import com.ll.zzandi.repository.BoardRepository;
import com.ll.zzandi.repository.FileRepository;
import com.ll.zzandi.service.BoardService;
import com.ll.zzandi.service.CommentService;
import com.ll.zzandi.service.StudyService;
import com.ll.zzandi.util.aws.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/{studyId}/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final StudyService studyService;
    private final FileRepository fileRepository;

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
    public Page<BoardListDto> findBoardListPagingToJson(@PathVariable Long studyId, @RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String category) {
        return boardService.findBoardListPaging(page, studyId, category);
    }

    @GetMapping("/list-data2")
    @ResponseBody
    public Page<BoardListDto> findBoardListPagingToJson(@PathVariable Long studyId, @RequestParam(defaultValue = "0") int page, @RequestParam String filter, @RequestParam String keyword) {
        return boardService.findBoardListPaging2(page, studyId, filter, keyword);
    }

    @GetMapping("/detail/{boardId}/{page}")
    public String findBoardDetail(@AuthenticationPrincipal User user, @PathVariable Long studyId, @PathVariable Long boardId, @PathVariable int page, Model model) {
        boardService.updateBoardView(boardId);
        String studyTitle = studyService.findByStudyId(studyId).get().getStudyTitle();
        List<File> fileList = fileRepository.findFileByBoardId(boardId);
        List<BoardFileDto> files = new ArrayList<>();

        for (File file : fileList) {
            files.add(BoardFileDto.builder()
                    .originName(file.getOriginalName())
                    .ext(file.getFileExt())
                    .url(file.getFileUrl())
                    .build());
        }

        for (BoardFileDto file : files) {
            System.out.println("file.getOriginName() = " + file.getOriginName());
            System.out.println("file.getExt() = " + file.getExt());
            System.out.println("file.getUrl() = " + file.getUrl());
        }

        model.addAttribute("boardDetail", boardService.findBoardDetail(boardId, page));
        model.addAttribute("userUUID", user.getId());
        model.addAttribute("studyId", studyId);
        model.addAttribute("studyTitle", studyTitle);
        model.addAttribute("files", files);
        return "board/boardDetail";
    }

    @GetMapping("/create")
    public String createBoardForm(@PathVariable Long studyId, BoardCreateDto boardCreateDto, Model model) {
        model.addAttribute("studyId", studyId);
        return "board/boardWriteForm";
    }

    @PostMapping("/create")
    public String createBoard(@AuthenticationPrincipal User user, @PathVariable Long studyId, @RequestParam("file") List<MultipartFile> files, @Valid BoardCreateDto boardCreateDto, BindingResult result) throws IOException {

        if (result.hasErrors()) {
            return "/board/boardWriteForm";
        }

        Study study = studyService.findByStudyId(studyId).get();
        Board board = new Board(user, boardCreateDto.getCategory(), boardCreateDto.getTitle(), boardCreateDto.getContent(), 0, 0, study);
        Long boardId = boardService.createBoard(board, files);

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