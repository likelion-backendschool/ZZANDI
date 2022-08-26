'use strict';

const boardId = document.querySelector('.board-id').value;
const commentCount = document.querySelector(".comment-count");
const commentList = document.querySelector(".comment-list");
const content = document.querySelector("#content");

window.onload = () => {
    findCommentList(boardId);
}

function findCommentList(boardId) {
    fetch(`/comment/list/${boardId}`)
        .then(response => response.json())
        .then(data => {
            const count = data.count;
            const comment = data.comment;

            commentCount.innerHTML = `<span>댓글 ${count}개</span>`;

            commentList.innerHTML = "";
            for (let i = 0; i < count; i++) {
                commentList.innerHTML += `<li style="border-bottom: 1px solid black;">
                                            <img src="${comment[i].profile}" alt="profile" width="50" height="50">
                                            <div>${comment[i].writer}</div>
                                            <div>${comment[i].content}</div>
                                          </li>`;
            }
        });
}

function create(){
    const value = content.value;
    const comment = {content: value}

    const url = `/comment/create/${boardId}`;
    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(comment)
    }).then(() => {
        content.value = "";
        findCommentList(boardId);
    });
}

// 삭제 버튼 함수
function deleteBoard() {
    if (confirm("정말로 삭제하시겠습니까?") === false) {
        return false;
    }
    document.forms['form'].submit();
}