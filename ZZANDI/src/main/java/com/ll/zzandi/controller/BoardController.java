package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Board;
import com.ll.zzandi.domain.File;
import com.ll.zzandi.domain.Study;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.board.BoardCreateDto;
import com.ll.zzandi.dto.board.BoardFileDto;
import com.ll.zzandi.dto.board.BoardListDto;
import com.ll.zzandi.dto.board.BoardUpdateFormDto;
import com.ll.zzandi.repository.FileRepository;
import com.ll.zzandi.service.BoardService;
import com.ll.zzandi.service.CommentService;
import com.ll.zzandi.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

    @GetMapping("/file-list/{boardId}")
    @ResponseBody
    public List<File> createFileList(@PathVariable Long boardId) {
        return fileRepository.findFileByBoardId(boardId);
    }

    @PostMapping("/delete/file/{fileId}")
    @ResponseBody
    public void deleteAttachment(@PathVariable Long fileId) {
        boardService.deleteFileTest(fileId);
    }

    @GetMapping("/detail/{boardId}/{page}")
    public String findBoardDetail(@AuthenticationPrincipal User user, @PathVariable Long studyId, @PathVariable Long boardId, @PathVariable int page, Model model) {
        boardService.updateBoardView(boardId);
        String studyTitle = studyService.findByStudyId(studyId).get().getStudyTitle();
        List<File> fileList = fileRepository.findFileByBoardId(boardId);
        List<BoardFileDto> files = new ArrayList<>();

        for (File file : fileList) {
            files.add(BoardFileDto.builder()
                    .fileId(file.getId())
                    .originName(file.getOriginalName())
                    .ext(file.getFileExt())
                    .url(file.getFileUrl())
                    .build());
        }

        model.addAttribute("boardDetail", boardService.findBoardDetail(boardId, page));
        model.addAttribute("userUUID", user.getId());
        model.addAttribute("studyId", studyId);
        model.addAttribute("studyTitle", studyTitle);
        model.addAttribute("files", files);
        return "board/boardDetail";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long fileId) throws MalformedURLException, FileNotFoundException {
        // 다운로드 할 파일 가져오기
        File file = fileRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
        // 서버에 업로드된 파일 url (s3)
        UrlResource resource = new UrlResource(file.getFileUrl());
        // 실제 파일의 이름을 인코딩해서 가져온다. -> 한글인 경우 깨질수도 있기 때문이다.

        String encodedFileName = UriUtils.encode(file.getOriginalName() + file.getFileExt(), StandardCharsets.UTF_8);
        // 인코딩해서 가져온 String을 ResponseHeader에 넣어줘야 한다. 그래야 링크를 눌렀을 때 다운이 된다. -> 정해진 규칙이라고 함
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition).body(resource);
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
        boardService.deleteFile(boardId);
        boardService.deleteBoard(boardId);
        return "redirect:/%d/board/list".formatted(studyId);
    }
}