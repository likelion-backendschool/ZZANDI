'use strict'

const body = document.querySelector(".list");

createListAjax(0);
function createListAjax(page) {
    fetch("/board/list-json?page=" + page)
        .then(response => response.json())
        .then(data => {
            const json = JSON.stringify(data);
            const items = JSON.parse(json);
            displayItems(items);
        });
}

// 만든 리스트 화면에 그리기
function displayItems(items) {
    let list = document.querySelector(".list");
    list.innerHTML = items.content.map(item => createHTMLString(item)).join('');
}

// 리스트 만들기
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