'use strict';

const content = document.querySelector("#content");
const boardId = document.querySelector('.board-id').value;
const userUUID = document.querySelector(".user-uuid").value;
const writer = document.querySelector(".writer-box__writer").innerText;
const commentCount = document.querySelectorAll(".comment-count");
const commentList = document.querySelector(".comment-list");
const pagination = document.querySelector('.pagination');
let lastPage = 0;
let currPage = 0;

document.title = document.querySelector(".title-box__title").innerHTML;
window.onload = () => findCommentList(boardId, 0);

function findCommentList(boardId, page) {
    fetch(`/comment/list/${boardId}/${page}`)
        .then(response => response.json())
        .then(comments => {
            const count = comments.totalElements;
            const comment = comments.content;
            lastPage = (comments.totalPages - 1 > 0) ? comments.totalPages - 1 : 0;
            currPage = comments.pageable.pageNumber;

            for (let countBox of commentCount) {
                countBox.innerHTML = `<span>댓글 ${count}개</span>`;
            }

            if (comment.length !== 0) {
                pagination.innerHTML = commentPageList(comments);
            }

            commentList.innerHTML = "";
            for (let i = 0; i < count; i++) {
                let icon = '';
                if (comment[i].step > 0) {
                    icon = `<span class="comment-box__icon">
                                <i class="fa-solid fa-reply fa-rotate-180"></i>
                            </span>`;
                }

                const content = urlFilter(comment[i].content);
                const buttonList = commentButtonList(comment[i], i + 1);
                const ml = comment[i].step * 3; // 들여쓰기 깊이 계산
                commentList.innerHTML +=
                    `<li class="comment-box mb-2" data-num="${i + 1}" style="margin-left:${ml}%;">
                        ${icon}
                        <div class="d-flex justify-content-between my-2">
                            <div class="d-flex">
                                <img src="${comment[i].profile}" alt="profile">
                                <div class="comment-box-header align-self-center">
                                    <span class="comment-box-header__writer">${comment[i].writer}</span>
                                    <span class="comment-box-header__date">${comment[i].createdDate}</span>
                                </div>
                            </div>
                            <div class="d-flex">${buttonList}</div>
                        </div>
                        
                        <div class="mb-2 comment-box-content">
                            <span class="comment-box-content__parent-writer" style="display: ${comment[i].parentId === 0 ? 'none' : 'inline'};">${comment[i].parentWriter}</span>
                            <span class="comment-box-content__content" style="color: ${comment[i].writer === writer ? '#045CDF' : '#333333'};">${content}</span>
                        </div>
                    </li>
                    <!-- 대댓글 & 댓글 수정 입력창 -->
                    <div class="comment-form mb-3 hide" style="margin-left: ${ml}%;"></div>`;
            }
        });
}

function urlFilter(content) {
    const urlRegex = /(https?:\/\/[^\s]+)/g;
    return content.replace(urlRegex,(url) => `<a href="${url}" class="comment-box__url" target=_blank>${url}</a>`);
}

function commentPageList(comments) {
    let nowPage = comments.pageable.pageNumber;
    let totalPage = comments.totalPages;

    let startPage = Math.floor(nowPage / 10) * 10;
    let endPage = startPage + 10 - 1;
    if(endPage > totalPage) {
        endPage = totalPage;
    }
    let hasPrev = comments.first;
    let hasNext = comments.last;

    let pageHTML = '';
    const prevDisabled = hasPrev ? "disabled='disabled'" : '';
    pageHTML +=
        `<li class="page-item">
            <button onClick="findCommentList(${boardId}, 0);" ${prevDisabled}>
                <i class="fa-solid fa-angles-left"></i>
            </button>
        </li>
        <li class="page-item">
            <button onClick="findCommentList(${boardId}, ${nowPage - 1});" ${prevDisabled}>
                <i class="fa-solid fa-angle-left"></i>
            </button>
        </li>`;

    for (let i = startPage; i < endPage; i++) {
        const active = (i === nowPage) ? 'active' : '';
        pageHTML += `<li class="page-item"><button class="page-num ${active}" onclick="findCommentList(${boardId}, ${i})">${i + 1}</button></li>`;
    }

    const nextDisabled = hasNext ? "disabled='disabled'" : '';
    pageHTML +=
        `<li class="page-item">
            <button onClick="findCommentList(${boardId}, ${nowPage + 1});" ${nextDisabled}>
                <i class="fa-solid fa-angle-right"></i>
            </button>
        </li>
        <li class="page-item">
            <button onClick="findCommentList(${boardId}, ${totalPage - 1});" ${nextDisabled}>
                <i class="fa-solid fa-angles-right"></i>
            </button>
        </li>`;

    return pageHTML;
}

function commentButtonList(comment, num) {
    let html = "";
    if (parseInt(userUUID) === comment.userUUID && comment.status === 'EXIST') {
        html =
            `<div class="justify-content-between align-self-center mx-2">
                   <i class="fa-regular fa-thumbs-up mx-2 comment-box-thumbs"></i>
                   <i class="fa-regular fa-thumbs-down comment-box-thumbs"></i>
               </div>
               <div class="comment-box-button d-flex justify-content-start">
                   <button type="button" onclick="updateForm(${num}, '${comment.content}', ${comment.commentId}, ${comment.count})">수정</button>
                   <button type="button" onclick="deleteComment(${comment.commentId}, ${comment.count})">삭제</button>
                   <button type="button" onclick="createForm(${num}, ${comment.commentId})">댓글</button>
               </div>`;

    } else {
        html =
            `<div class="justify-content-between align-self-center mx-2">
                <i class="fa-regular fa-thumbs-up mx-2 comment-box-thumbs"></i>
                <i class="fa-regular fa-thumbs-down comment-box-thumbs"></i>
            </div>
            <button class="comment-box-button" type="button" onclick="createForm(${num}, ${comment.commentId})">댓글</button>`;
    }
    return html;
}

function createForm(num, commentId) {
    const form = document.querySelector(`.comment-list .comment-form:nth-of-type(${num})`);
    form.classList.toggle("hide");

    form.innerHTML = `<div class="reply-box">
                        <div class="reply-box__title">
                            <i class="fa-solid fa-reply fa-rotate-180"></i>
                            <span>댓글 쓰기</span>
                        </div>
                        <div class="box d-flex justify-content-between">
                            <textarea class="form-control reply-content" placeholder="따듯한 댓글 부탁드립니다."></textarea>
                            <button class="btn btn-secondary btn-sm reply-btn" onclick="createReply(${commentId})">등록</button>
                        </div>
                      </div>`;

    document.querySelector(".reply-content").focus();
}

function updateForm(num, content, commentId, count) {
    const form = document.querySelector(`.comment-list .comment-form:nth-of-type(${num})`);
    form.classList.toggle("hide");
    content = content.replace(/(<br>)/g, '\r\n');

    form.innerHTML = ` <div class="reply-box">
                        <div class="reply-box__title">
                            <i class="fa-solid fa-reply fa-rotate-180"></i>
                            <span>댓글 수정</span>
                        </div>
                        <div class="d-flex justify-content-between">
                            <textarea class="form-control update-content" placeholder="따듯한 댓글 부탁드립니다.">${content}</textarea>                            
                            <button class="btn btn-secondary btn-sm update-btn" onclick="update(${commentId}, ${count})">등록</button>
                        </div>
                       </div>`;

    document.querySelector(".update-content").focus();
}

function create() {
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
        findCommentList(boardId, lastPage);
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
        findCommentList(boardId, currPage);
    });
}

function update(commentId, count) {
    if(count > 0) {
        alert("대댓글이 있으면 수정하실 수 없습니다.");
        findCommentList(boardId, currPage);
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
        findCommentList(boardId, currPage);
    })
}

function deleteComment(commentId) {
    if (confirm("정말로 삭제하시겠습니까?")) {
        const url = `/comment/delete/${commentId}`;
        fetch(url, {
            method: "POST"
        }).then(() => {
            findCommentList(boardId, currPage);
        });
    }
}