'use strict'
/*
    -> 페이징 구현에 필요한 데이터
    현재 페이지 번호(nowPage) : Pageable.pageNumber (0부터 시작)
    페이지 시작 번호(startPage) : ((현재 페이지  - 1) / pageSize) * 10 + 1
    페이지 끝 번호(endPage) : (시작 번호 + pageSize) - 1;
    다음 페이지 번호(nextPage) : 현재 페이지 + 1;
    이전 페이지 번호(prevPage) : 현재 페이지 - 1;
    이전 페이지 존재 여부(hasPrev) : (pageable.first == true) ? "이전 페이지 존재 X" : "이전 페이지 존재"
    다음 페이지 존재 여부(hasNext) : (pageable.last == true) ? "다음 페이지 존재 X" : "다음 페이지 존재"
    전체 게시물 데이터 개수(totalElements)
    전체 페이지 개수(totalPages)
*/
const list = document.querySelector(".list");
const pagination = document.querySelector('.pagination');
const currPage = document.querySelector(".page").value; // 현재 페이지 정보
const studyId = document.querySelector(".study-id").value;

window.onload = () => {
    findByPage(currPage, studyId);
}

// 뒤로가기 / 앞으로가기 처리
window.addEventListener('popstate', (e) => {
    const data = history.state;
    console.log(data.page);
    fetch(`/${studyId}/board/list-data?page=${data.page}`)
        .then(response => response.json())
        .then(data => {
            if(data.content.length === 0) {
                list.innerHTML = '<td colspan="6">등록된 게시글이 없습니다.</td>';
                return false;
            } else {
                displayItems(data, studyId);
            }
        })
});

// 게시물 검색
function findByPage(page, studyId) {
    fetch(`/${studyId}/board/list-data?page=${page}`)
        .then(response => response.json())
        .then(data => {
            if(data.content.length === 0) {
                list.innerHTML = '<td colspan="6">등록된 게시글이 없습니다.</td>';
                return false;
            } else {
                history.pushState({page : page}, "", `/${studyId}/board/list?page=${page}`)
                displayItems(data, studyId);
            }
        });
}

/**
 * 만들어진 list 목록을 <tbody>에 그려주는 함수
 */
function displayItems(items) {
    list.innerHTML = items.content.map(item => createBoardList(item)).join('');
    pagination.innerHTML = createPageList(items);
}

// 게시물 리스트 생성
function createBoardList(item) {
    return `<tr>
                <td style="color: mediumpurple;">${item.category}</td>
                <td><a href="detail/${item.boardId}/${item.pageNum}">${item.title}</a><span style="font-weight: bold; font-size: 12px;">[${item.count}]</span></td>
                <td>${item.writer}</td>
                <td>${item.createdDate}</td>
                <td>${item.views}</td>
                <td>${item.heart}</td>
            </tr>`;
}

// 페이지 번호 리스트 생성
function createPageList(items) {
    let nowPage = items.pageable.pageNumber; // 시작 번호 0
    let pageSize = items.pageable.pageSize;
    let totalPage = items.totalPages; // 전체 페이지 개수

    let startPage = Math.floor((nowPage) / pageSize) * pageSize; // 페이지 시작 번호
    let endPage = startPage + pageSize; // 페이지 끝 번호
    if (endPage > totalPage) endPage = totalPage;
    let hasPrev = items.first; // 이전 페이지 존재 여부
    let hasNext = items.last; // 다음 페이지 존재 여부

    let pageHTML = '';
    // 처음 페이지, 이전 페이지
    // 첫 페이지(0)인 경우 이전 버튼 disabled 속성 추가
    const prevDisabled = hasPrev ? "disabled='disabled'" : '';
    pageHTML +=
        `<li class="page-item">
            <button class="page-btn" onClick="findByPage(0, ${studyId});" ${prevDisabled} aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </button>
        </li>
        <li class="page-item">
            <button class="page-btn" onClick="findByPage(${nowPage - 1}, ${studyId});" ${prevDisabled} aria-label="Previous">
                <span aria-hidden="true">&lsaquo;</span>
            </button>
        </li>`;

    // 페이지 번호
    for (let i = startPage; i < endPage; i++) {
        const active = (i === nowPage) ? 'active' : '';
        pageHTML += `<li class="page-item"><button class="page-btn page-num ${active}" onclick="findByPage(${i}, ${studyId})">${i + 1}</button></li>`;
    }

    // 다음 페이지, 마지막 페이지
    // 마지막 페이지인 경우 다음 버튼 disabled 속성 추가
    const nextDisabled = hasNext ? "disabled='disabled'" : '';
    pageHTML +=
        `<li class="page-item">
            <button class="page-btn" onClick="findByPage(${nowPage + 1}, ${studyId});" ${nextDisabled} aria-label="Next">
                <span aria-hidden="true">&rsaquo;</span>
            </button>
        </li>
        <li class="page-item">
            <button class="page-btn" onClick="findByPage(${totalPage - 1}, ${studyId});" ${nextDisabled}  aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </button>
        </li>`;

    return pageHTML;
}