'use strict'

const list = document.querySelector(".list");
const menu = document.querySelector(".menu");
const addForm = document.querySelector(".addForm");

window.onload = () => {
    findAll('TOTAL');
    displayMenu();
    displayAddForm();
}

// ToDoList 검색
function findAll(type) {
    let url = (type === 'TOTAL') ? "/todo/list-json" : `/todo/list-json?type=${type}`;
    fetch(url)
        .then(response => response.json())
        .then(data => {
            displayItems(data,type);
        });
}

// ToDoList 삭제
function deleteToDo(id) {
    fetch("/todo/delete?id=" + id, {method: "DELETE"})
        .then(() => {
            findAll('TOTAL')
            displayMenu();
        });
}

// ToDoList Type 변경 (DOING <--> DONE)
function changeType(id) {
    fetch("/todo/change?id=" +id)
        .then(response => response.json())
        .then(() => {
            findAll('TOTAL')
            displayMenu();
        });
}


// ToDoList 추가
function submitAddForm(form) {
    form.todo.value = form.todo.value.trim();

    if (form.todo.value.length == 0) {
        alert("목표를 입력해주세요.");
        form.todo.focus();
        return;
    }

    let content = form.todo.value;

    fetch("/todo/add/?content="+ content)
        .then(response => response.json())
        .then(content => null)
        .then(() => {
            findAll('TOTAL')
            displayMenu();
            displayAddForm();
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

/**
 * All / Doing / Done 섹션을 그려주는 함수
 */
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

/**
 * ToDoList 추가 Form 을 그려주는 함수
 */
function displayAddForm() {
    let html = `
    <form class="d-flex justify-content-center align-items-center mb-4"
        onsubmit="submitAddForm(this); return false;" method="get">
        <div class="form-outline flex-fill">
            <input type="text" name="todo" class="form-control"/>
            <label class="form-label" > New task...</label>
        </div>
        <button type="submit" class="btn btn-info ms-2">Add</button>
    </form>`

    addForm.innerHTML = html;
}