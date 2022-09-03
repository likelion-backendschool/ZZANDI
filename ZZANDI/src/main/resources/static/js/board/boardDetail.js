'use strict';

const boardId = document.querySelector('.board-id').value;
const userUUID = document.querySelector(".user-uuid").value;
const commentCount = document.querySelectorAll(".comment-count");
const commentList = document.querySelector(".comment-list");
const content = document.querySelector("#content");

window.onload = () => findCommentList(boardId);

function findCommentList(boardId) {
    fetch(`/comment/list/${boardId}`)
        .then(response => response.json())
        .then(data => {
            const count = data.count;
            const comment = data.comment;

            for(let countBox of commentCount) {
                countBox.innerHTML = `<span style="font-size: 14px; font-weight: bold;">댓글 ${count}개</span>`;
            }

            commentList.innerHTML = "";
            for (let i = 0; i < count; i++) {
                let icon = '';
                if (comment[i].step !== 0) {
                    icon = `<i class="fa-solid fa-reply fa-rotate-180 position-absolute" style="top: 10px; left: -15px; color: #ccc; font-size: 11px;"></i>`;
                }

                const buttonList = commentButtonList(comment[i], i + 1);
                const ml = comment[i].step * 2; // 들여쓰기 깊이 계산
                commentList.innerHTML +=
                    `<li class="comment-box mb-2 border-bottom" data-num="${i + 1}" style="margin-left:${ml}%; position: relative;">
                        ${icon}
                        <div class="d-flex justify-content-between mb-2 mt-2">
                            <div class="d-flex">
                                <img src="${comment[i].profile}" alt="profile" class="rounded border bg-light" width="30" height="30" style="margin-right: 5px;">
                                <div class="align-self-center" style="font-size: 14px;">
                                    <span style="font-weight: 700;">${comment[i].writer}</span style="font-weight: 700;">
                                    <span style="margin-left: 10px; color: #888888; font-size: 11px;">${comment[i].createdDate}</span>
                                </div>
                            </div>
                            <div class="d-flex" style="font-size: 12px;">${buttonList}</div>
                        </div>
                        <div style="font-size: 12px; margin-bottom: 5px; overflow: auto;">${comment[i].content}</div>
                    </li>
                    <!-- 대댓글 & 댓글 수정 입력창 -->
                    <div class="comment-form mb-3 hide" style="margin-left: ${ml}%;"></div>`;
            }
        });
}

function commentButtonList(comment, num) {
    let html = "";
    if (parseInt(userUUID) === comment.userUUID && comment.status === 'EXIST') {
        html =
               `<div class="justify-content-between align-self-center mx-2">
                   <i class="fa-regular fa-thumbs-up mx-2"></i>
                   <i class="fa-regular fa-thumbs-down"></i>
               </div>
               <div class="d-flex justify-content-start">
                   <button type="button" onclick="updateForm(${num}, '${comment.content}', ${comment.commentId}, ${comment.count})" style="border: none; outline: none; background-color: transparent; color: #666666;">수정</button>
                   <button type="button" onclick="deleteComment(${comment.commentId}, ${comment.count})" style="border: none; outline: none; background-color: transparent; color: #666666;">삭제</button>
                   <button type="button" onclick="createForm(${num}, ${comment.commentId})" style="border: none; outline: none; background-color: transparent; color: #666666;">댓글</button>
               </div>`;

    } else {
        html =
            `<div class="justify-content-between align-self-center mx-2">
                <i class="fa-regular fa-thumbs-up mx-2"></i>
                <i class="fa-regular fa-thumbs-down"></i>
            </div>
            <button type="button" onclick="createForm(${num}, ${comment.commentId})" style="border: none; outline: none; background-color: transparent;">댓글</button>`;
    }
    return html;
}

function createForm(num, commentId) {
    const form = document.querySelector(`.comment-list .comment-form:nth-of-type(${num})`);
    form.classList.toggle("hide");

    form.innerHTML = `<div>
                        <i class="fa-solid fa-reply fa-rotate-180" style="color: #ccc; font-size: 11px;"></i>
                            <span style="font-size: 12px;">댓글 쓰기</span>
                          </div>
                          <div class="box d-flex justify-content-between">
                            <textarea class="form-control reply-content" placeholder="따듯한 댓글 부탁드립니다." style="height: 70px; font-size: 12px;"></textarea>
                            <button class="btn btn-secondary btn-sm" onclick="createReply(${commentId})" style="font-size: 12px; width: 100px; margin-left: 5px;">등록</button>
                          </div>
                        </div>`;

    document.querySelector(".reply-content").focus();
}

function updateForm(num, content, commentId, count) {
    console.log(content);
    const form = document.querySelector(`.comment-list .comment-form:nth-of-type(${num})`);
    form.classList.toggle("hide");
    content = content.replace(/(<br>)/g, '\r\n');

    form.innerHTML = ` 
                        <div>
                            <i class="fa-solid fa-reply fa-rotate-180" style="color: #ccc; font-size: 11px;"></i>
                            <span style="font-size: 12px;">댓글 수정</span>
                        </div>
                        <div class="d-flex justify-content-between">
                            <textarea class="form-control update-content" placeholder="따듯한 댓글 부탁드립니다." style="height: 70px; font-size: 12px;">${content}</textarea>                            
                            <button class="btn btn-secondary btn-sm" onclick="update(${commentId}, ${count})" style="font-size: 12px; width: 100px; margin-left: 5px;">등록</button>
                        </div>`;

    document.querySelector(".update-content").focus();
}

function create(){
    let value = content.value;
    value = value.replace(/(\n|\r\n)/g, '<br>');
    const comment = {
        content: value
    }

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

function createReply(commentId){
    const reply = document.querySelector(".reply-content").value.replace(/(\n|\r\n)/g, '<br>');
    const comment = {
        id : commentId,
        content: reply
    }

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

function update(commentId, count) {
    if(count > 0) {
        alert("대댓글이 있으면 수정하실 수 없습니다.");
        findCommentList(boardId);
        return false;
    }

    const updateParam = document.querySelector(".update-content").value.replace(/(\n|\r\n)/g, '<br>');
    const comment = {"content": updateParam};

    const url = `/comment/update/${commentId}`;
    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type":"application/json"
        },
        body: JSON.stringify(comment)
    }).then(() => {
        findCommentList(boardId);
    })
}

function deleteComment(commentId) {
    if (confirm("정말로 삭제하시겠습니까?")) {
        const url = `/comment/delete/${commentId}`;
        fetch(url, {
            method: "POST"
        }).then(() => {
            findCommentList(boardId);
        });
    }
}

function deleteBoard() {
    if (confirm("정말로 삭제하시겠습니까?") === false) {
        return false;
    }
    document.forms['form'].submit();
}