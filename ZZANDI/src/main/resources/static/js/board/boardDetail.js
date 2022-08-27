'use strict';

const boardId = document.querySelector('.board-id').value;
const userUUID = document.querySelector(".user-uuid").value;
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

            commentCount.innerHTML = `<div style="border: 1px solid black; border-radius: 5px; padding: 10px;">
                                        <span style="font-size: 14px; font-weight: bold;">댓글 ${count}개</span>
                                      </div>`

            commentList.innerHTML = "";
            for (let i = 0; i < count; i++) {
                let box = createBtnBox(comment[i], i + 1);
                commentList.innerHTML += `<li class="comment-box mb-2" data-num="${i + 1}" style="border-top: 1px solid #eceff1;">
                                            <div class="d-flex justify-content-between mb-2 mt-2">
                                                <div class="d-flex">
                                                    <img src="${comment[i].profile}" alt="profile" width="20" height="20" style="margin-right: 5px;">
                                                    <div class="align-items-center" style="font-size: 14px;">${comment[i].writer}</div>
                                                </div>
                                                <div class="d-flex" style="font-size: 12px;">
                                                    ${box}
                                                </div>
                                            </div>
                                            <div style="font-size: 12px; margin-bottom: 5px;">${comment[i].content}</div>
                                          </li>
                                          <!-- 대댓글 입력창 -->
                                          <div class="comment-form mb-3 hide">
                                              <div>
                                                <i class="bi bi-arrow-return-right"></i>
                                                <span style="font-size: 12px;">댓글 쓰기</span>
                                              </div>
                                              <div class="box d-flex justify-content-between">
                                                <textarea id="content" name="content" class="form-control" placeholder="따듯한 댓글 부탁드립니다." style="height: 100px;"></textarea>
                                                <div class="align-self-center" style="width: 70px; height: 30px; border-radius: 50%; margin-left: 10px;">
                                                    <button class="btn btn-primary" onclick="create()" style="font-size: 14px;">등록</button>
                                                </div>
                                              </div>
                                         </div>`;
            }
        });
}

/*
    수정 / 삭제 / 댓글 박스 생성 함수
    본인이 작성한 댓글인 경우만 수정 / 삭제 / 댓글 버튼 표시
    다른 사람이 작성한 댓글인 경우에는 댓글 버튼만 표시
 */
function createBtnBox(comment, num) {
    let html = "";
    if (parseInt(userUUID) === comment.userUUID) {
        html = `<button type="button" onclick="updateComment()" style="border: none; outline: none; background-color: transparent;">수정</button>
               <button type="button" onclick="deleteComment()" style="border: none; outline: none; background-color: transparent;">삭제</button>
               <button type="button" onclick="createCommentForm(${num})" style="border: none; outline: none; background-color: transparent;">댓글</button>`;
    } else {
        html = `<button type="button" onclick="createCommentForm(${num})" style="border: none; outline: none; background-color: transparent;">댓글</button>`;
    }
    return html;
}

// 대댓글 입력창 출력 함수
function createCommentForm(num) {
    let commentForm = document.querySelector(`.comment-list .comment-form:nth-of-type(${num})`);
    commentForm.classList.toggle('hide');
}

function updateComment() {

}

function deleteComment() {

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

// 게시글 삭제 버튼 함수
function deleteBoard() {
    if (confirm("정말로 삭제하시겠습니까?") === false) {
        return false;
    }
    document.forms['form'].submit();
}