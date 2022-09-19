'use strict'

const list = document.querySelector(".list");
const pagination = document.querySelector('.pagination');
const currPage = document.querySelector(".page").value;
const studyId = document.querySelector(".study-id").value;

const colors = new Map();
colors.set('전체', '#42a5f5');
colors.set('공지', '#d50000');
colors.set('자유', '#ff6f00');
colors.set('정보', '#ffb2dd');
colors.set('질문', '#2e7d32');
colors.set('자랑', '#003c8f');

for(let category of document.querySelectorAll(".categories li > a")) {
    category.style.color = colors.get(category.innerHTML);
}

window.onload = () => findByPage(currPage, '', '', '', studyId);

function findByPage(page, category, filter, keyword, studyId) {
    let url = (keyword === '') ?
        `/${studyId}/board/list-data?page=${page}&category=${category}` :
        `/${studyId}/board/list-data2?page=${page}&filter=${filter}&keyword=${keyword}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            if(data.content.length === 0) {
                list.innerHTML = '<td colspan="6">등록된 게시글이 없습니다.</td>';
                return false;
            } else {
                history.pushState({page : page}, "", `/${studyId}/board/list?page=${page}`)
                displayItems(data, category, filter, keyword, studyId);
            }
        }
    );
}

function search() {
    const filter = document.querySelector("#filter").value;
    const keyword = document.querySelector("#keyword").value;

    if(keyword === '') {
        alert("검색어를 입력해주세요.");
        document.querySelector("#keyword").focus();
        return false;
    }

    findByPage(currPage, '', filter, keyword, studyId);
}

const searchBtn = document.querySelector(".search-btn");
searchBtn.addEventListener('click', () => {
    search();
});

const keywordInput = document.querySelector("#keyword");
keywordInput.addEventListener('keypress', (e) => {
    if (e.code === 'Enter') {
        search();
    }
})

const categories = document.querySelector(".categories");
const categoryArr = document.querySelectorAll(".li-category");
categories.addEventListener("click", (e) => {
    if (e.target.tagName !== 'A') {
        return false;
    }

    for(let ca of categoryArr) {
        if (ca.classList.contains("selected")) {
            ca.classList.remove("selected");
        }
    }

    let category = e.target.innerHTML === '전체' ? '' : e.target.innerHTML;
    e.target.classList.add("selected");
    findByPage(0, category, '', '', studyId);
});

function displayItems(items, category, filter, keyword) {
    list.innerHTML = items.content.map(item => createBoardList(item)).join('');
    pagination.innerHTML = createPageList(items, category, filter, keyword);
}

function createBoardList(item) {
    const title = (item.title.length > 50) ? `${item.title.substr(0, 50)}...` : `${item.title}`;
    const color = colors.get(item.category);

    return `<tr>
                <td class="board-table-category" style="color: ${color};"><a onclick="findByPage(0, '${item.category}', ${studyId})">${item.category}</a></td>
                <td class="board-table-title">
                    <a href="/${studyId}/board/detail/${item.boardId}/${item.pageNum}">${title}</a>
                    <span class="board-table-title__comment">${item.count}</span>
                </td>
                <td>
                    <div class="d-flex mx-2">
                        <img src="${item.profile}" alt="profile"> 
                        <span class="align-self-center">${item.writer}</span>
                    </div>
                </td>
                <td>${item.createdDate}</td>
                <td>${item.views}</td>
                <td>${item.heart}</td>
            </tr>`;
}

function createPageList(items, category, filter, keyword) {
    let nowPage = items.pageable.pageNumber;
    let pageSize = items.pageable.pageSize;
    let totalPage = items.totalPages;

    let startPage = Math.floor((nowPage) / pageSize) * pageSize;
    let endPage = startPage + pageSize;
    if (endPage > totalPage) endPage = totalPage;
    let hasPrev = items.first;
    let hasNext = items.last;

    let pageHTML = '';
    const prevDisabled = hasPrev ? "disabled='disabled'" : '';
    pageHTML +=
        `<li class="page-item">
            <button onClick="findByPage(0, '${category}', '${filter}', '${keyword}', ${studyId});" ${prevDisabled}>
                <i class="fa-solid fa-angles-left"></i>
            </button>
        </li>
        <li class="page-item">
            <button onClick="findByPage(${nowPage - 1}, '${category}', '${filter}', '${keyword}', ${studyId});" ${prevDisabled}>
                <i class="fa-solid fa-angle-left"></i>
            </button>
        </li>`;

    for (let i = startPage; i < endPage; i++) {
        const active = (i === nowPage) ? 'active' : '';
        pageHTML += `<li class="page-item"><button class="${active}" onclick="findByPage(${i}, '${category}', '${filter}', '${keyword}', ${studyId})">${i + 1}</button></li>`;
    }

    const nextDisabled = hasNext ? "disabled='disabled'" : '';
    pageHTML +=
        `<li class="page-item">
            <button onClick="findByPage(${nowPage + 1}, '${category}', '${filter}', '${keyword}', ${studyId});" ${nextDisabled}>
                <i class="fa-solid fa-angle-right"></i>
            </button>
        </li>
        <li class="page-item">
            <button onClick="findByPage(${totalPage - 1}, '${category}', '${filter}', '${keyword}', ${studyId});" ${nextDisabled}>
                <i class="fa-solid fa-angles-right"></i>
            </button>
        </li>`;

    return pageHTML;
}

window.addEventListener('popstate', (e) => {
    const data = history.state;
    fetch(`/${studyId}/board/list-data?page=${data.page}&category=`)
        .then(response => response.json())
        .then(data => {
            if(data.content.length === 0) {
                list.innerHTML = '<td colspan="6">등록된 게시글이 없습니다.</td>';
                return false;
            } else {
                displayItems(data, studyId);
            }
        })
    }
);