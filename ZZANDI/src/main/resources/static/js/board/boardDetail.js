'use strict';

const boardId = document.querySelector('.board-id').value;
const commentCount = document.querySelector(".comment-count");
const commentList = document.querySelector(".comment-list");

window.onload = () => {
    comment(boardId);
}

function comment(boardId) {
    fetch(`/comment/list/${boardId}`)
        .then(response => response.json())
        .then(data => {
            const count = data.count;
            const comment = data.comment;
            console.log(comment);
            console.log(comment[0].writer);

            commentCount.innerHTML = `<span>댓글 ${count}개</span>`;

            for (let i = 0; i < count; i++) {
                commentList.innerHTML += `<li style="border-bottom: 1px solid black;">
                                            <div>${comment[i].writer}</div>
                                            <div>${comment[i].content}</div>
                                          </li>`;
            }
        });
}

