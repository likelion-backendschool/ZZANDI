'use strict'

let prev = document.querySelector(".prev-btn");
let next = document.querySelector(".next-btn");

// 이전, 다음 버튼 비활성화 함수
function disabled(isCheck, type) {
    if (type === 'prev' && isCheck === 'false') {
        prev.removeAttribute('href');
    } else if(type === 'next' && isCheck === 'false') {
        next.removeAttribute('href');
    }
}