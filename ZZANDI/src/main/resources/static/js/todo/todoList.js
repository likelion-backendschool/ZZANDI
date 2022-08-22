'use strict'

const list = document.querySelector(".list");

window.onload = () => {
    findAll();
}

// 게시물 검색
function findAll(type) {
    let url;
    if (type === undefined) {
        url = "/todo/list-json";
    }
    else {
        url = "/todo/list-json?type=" + type;
    }

    console.log(url);

    fetch(url)
        .then(response => response.json())
        .then(data => {
            displayItems(data,type);
        });
}

/**
 * 만들어진 list 목록을 <div>에 그려주는 함수
 */
function displayItems(data, type) {
    let html = '';
    for(let i = 0; i < data.length; i++) {
        let li = '';
        if(type === 'DONE' || type === undefined && data[i].type === 'DONE') {
            li += `<input class="form-check-input me-2" type="checkbox" checked>${data[i].content}</input>`;
        } else {
            li += `<input class="form-check-input me-2" type="checkbox"/>${data[i].content}</input>`;
        }

        html += `<li class="list-group-item d-flex align-items-center border-0 mb-2 rounded"
                    style="background-color: #f4f6f7;">
                    <div class="flex-grow-1">
                        ${li}
                    </div>
                    <button type="button" class="btn-close" aria-label="Close"></button>
                </li>`;
    }
    list.innerHTML = html;
}