package com.ll.zzandi.service;

import com.ll.zzandi.domain.Board;
import com.ll.zzandi.domain.File;
import com.ll.zzandi.dto.board.BoardDetailDto;
import com.ll.zzandi.dto.board.BoardListDto;
import com.ll.zzandi.dto.board.BoardUpdateFormDto;
import com.ll.zzandi.enumtype.TableType;
import com.ll.zzandi.repository.BoardRepository;
import com.ll.zzandi.repository.FileRepository;
import com.ll.zzandi.util.aws.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;
    private final ImageUploadService imageUploadService;

    public Page<BoardListDto> findBoardListPaging(int page, Long studyId, String category) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Board> boardList = boardRepository.findBoardList(pageRequest, studyId, category);

        return boardList.map(board -> {
            List<File> fileList = fileRepository.findFileByBoardId(board.getId());
            return new BoardListDto(board.getId(), board.getUser().getUserId(), board.getCategory(),
                    board.getTitle(), board.getUser().getUserNickname(),
                    board.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")), board.getViews(),
                    board.getHeart(), page, board.getComments().size(), board.getUser().getUserprofileUrl(), fileList);
        });
    }

    public Page<BoardListDto> findBoardListPaging2(int page, Long studyId, String filter, String keyword) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));

        keyword = keyword.replaceAll(" ", "");
        Page<Board> boardList = switch (filter) {
            case "title" -> boardRepository.findBoardListFilterByTitle(pageRequest, studyId, keyword);
            case "content" -> boardRepository.findBoardListFilterByContent(pageRequest, studyId, keyword);
            case "comment" -> boardRepository.findBoardListFilterByComment(pageRequest, studyId, keyword);
            case "writer" -> boardRepository.findBoardListFilterByWriter(pageRequest, studyId, keyword);
            default -> boardRepository.findBoardListFilterByTitleAndContent(pageRequest, studyId, keyword);
        };

        return boardList.map(board -> {
            List<File> fileList = fileRepository.findFileByBoardId(board.getId());
            return new BoardListDto(board.getId(), board.getUser().getUserId(), board.getCategory(),
                    board.getTitle(), board.getUser().getUserNickname(),
                    board.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")), board.getViews(),
                    board.getHeart(), page, board.getComments().size(), board.getUser().getUserprofileUrl(), fileList);
        });
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
                .category(board.getCategory())
                .views(board.getViews())
                .heart(board.getHeart())
                .count(board.getComments().size())
                .page(page)
                .prev(prev)
                .next(next)
                .profile(board.getUser().getUserprofileUrl()).build();
    }

    @Transactional
    public void deleteFile(Long boardId) {
        List<File> files = fileRepository.findFileByBoardId(boardId);
        for (File file : files) {
            fileRepository.deleteById(file.getId());
            fileRepository.updateFileStatus(file.getId());
        }
    }

    public Board findByBoardId(Long boardId) {
        return boardRepository.findById(boardId).get();
    }

    @Transactional
    public Long createBoard(Board board, List<MultipartFile> files) throws IOException {
        Board saveBoard = boardRepository.save(board);
        for (MultipartFile file : files) {
            if(!file.isEmpty()) {
                String originalName = file.getOriginalFilename();

                int index = originalName.lastIndexOf(".");
                String fileName = originalName.substring(0, index);
                String ext = originalName.substring(index);

                String saveFileName = UUID.randomUUID().toString().replaceAll("-", "") + ext;
                String uploadUrl = imageUploadService.upload(saveFileName, file);

                File newFile = File.builder()
                        .fileName(saveFileName)
                        .originalName(fileName)
                        .fileExt(ext)
                        .fileSize(file.getSize())
                        .fileUrl(uploadUrl)
                        .tableId(saveBoard.getId())
                        .tableType(TableType.BOARD)
                        .build();

                fileRepository.save(newFile);
            }
        }
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