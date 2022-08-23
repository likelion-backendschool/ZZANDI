'use strict'

const list = document.querySelector(".list");
const menu = document.querySelector(".menu");

window.onload = () => {
    findAll('TOTAL');
    displayMenu();
}

// 게시물 검색
function findAll(type) {
    let url = (type === 'TOTAL') ? "/todo/list-json" : `/todo/list-json?type=${type}`;
    fetch(url)
        .then(response => response.json())
        .then(data => {
            displayItems(data,type);
        });
}

function deleteToDo(id) {
    fetch("/todo/delete?id=" + id, {method: "DELETE"})
        .then(() => {
            findAll('TOTAL')
            displayMenu();
        });
}

function changeType(id) {
    console.log(id);
    fetch("/todo/change?id=" +id)
        .then(response => response.json())
        .then(() => {
            findAll('TOTAL')
            displayMenu();
        });
}

/**
 * 만들어진 list 목록을 <div>에 그려주는 함수
 */
function displayItems(data, type) {
    let html = '';
    for(let i = 0; i < data.length; i++) {
        let li = '';
        if(type === 'DONE' || type === 'TOTAL' && data[i].type === 'DONE') {
            li += `<input class="form-check-input me-2" onclick="changeType(${data[i].id})" type="checkbox" checked>${data[i].content}</input>`;
        } else {
            li += `<input class="form-check-input me-2" onclick="changeType(${data[i].id})" type="checkbox">${data[i].content}</input>`;
        }

        html += `<li class="list-group-item d-flex align-items-center border-0 mb-2 rounded"
                    style="background-color: #f4f6f7;">
                    <div class="flex-grow-1">
                        ${li}
                    </div>
                    <button type="button" onclick="deleteToDo(${data[i].id})" class="btn-close" aria-label="Close"></button>
                </li>`;
    }
    list.innerHTML = html;
}

function displayMenu() {
    let html = '';
    let tab1 = "active";
    let tab2 = "";
    let tab3 = "";

    html += `<li class="nav-item" role="presentation">
                <button onclick="findAll('TOTAL')" class="nav-link ${tab1}" id="ex1-tab-1" data-mdb-toggle="tab" role="tab"
                                   aria-controls="ex1-tabs-1" aria-selected="true">ALL</button>
             </li>
            <li class="nav-item" role="presentation">
                <button onclick="findAll('DOING')" class="nav-link ${tab2}" id="ex1-tab-2" data-mdb-toggle="tab" role="tab"
                   aria-controls="ex1-tabs-2" aria-selected="fasle">DOING</button>
            </li>
            <li class="nav-item" role="presentation">
                <button onclick="findAll('DONE')" class="nav-link ${tab3}" id="ex1-tab-3" data-mdb-toggle="tab" role="tab"
                   aria-controls="ex1-tabs-3" aria-selected="false">DONE</button>
            </li>
    `
    menu.innerHTML = html;
}