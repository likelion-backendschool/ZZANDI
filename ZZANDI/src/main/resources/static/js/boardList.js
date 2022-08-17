'use strict'

const body = document.querySelector(".list");
const pageList = document.querySelectorAll(".page-curr");

createListAjax(0);
function createListAjax(page) {
    for(let i = 0; i < pageList.length; i++) {
        pageList[i].classList.remove("active");
    }

    const curr = pageList[page]; // 현재 페이지
    curr.classList.add("active");

    fetch("/board/list-json?page=" + page)
        .then(response => response.json())
        .then(data => {
            const json = JSON.stringify(data);
            const items = JSON.parse(json);
            displayItems(items);
        });
}

/**
 * 만들어진 list 목록을 <tbody>에 그려주는 함수
 */
function displayItems(items) {
    let list = document.querySelector(".list");
    list.innerHTML = items.content.map(item => createHTMLString(item)).join('');
}

// json 형태의 게시물 정보를 이용해서 리스트 row를 그려주는 함수
function createHTMLString(item) {
    return `<tr>
                <td>${item.boardId}</td>
                <td><a href="detail/${item.boardId}">${item.title}</a></td>
                <td>${item.writer}</td>
                <td>${item.createdDate}</td>
                <td>${item.views}</td>
                <td>${item.heart}</td>
            </tr>`;
}


let prev = document.querySelector(".prev-btn");
let next = document.querySelector(".next-btn");

// 이전, 다음 버튼 비활성화 함수
function disabled(isCheck, type) {
    if (type === 'prev' && isCheck === 'false') {
        prev.removeAttribute('href');
    } else if (type === 'next' && isCheck === 'false') {
        next.removeAttribute('href');
    }
}