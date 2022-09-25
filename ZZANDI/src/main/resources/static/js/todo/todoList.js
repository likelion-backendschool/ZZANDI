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
    let url = (type === 'TOTAL') ? "/todo/list-data" : `/todo/list-data?type=${type}`;
    fetch(url)
        .then(response => response.json())
        .then(data => {
            displayItems(data,type);
        });
}

function findById(id) {
    fetch("/todo/todo-data?id="+id)
        .then(response => response.json())
        .then(data => {
            displayModifyForm(data);
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

    fetch("/todo/create/?content="+ content)
        .then(response => response.json())
        .then(content => null)
        .then(() => {
            findAll('TOTAL')
            displayMenu();
            displayAddForm();
        });
}

// ToDoList 수정
function submitModifyForm(form, id) {
    form.todo.value = form.todo.value.trim();

    if (form.todo.value.length == 0) {
        alert("목표를 입력해주세요.");
        form.todo.focus();
        return;
    }

    let content = form.todo.value;

    let url = `/todo/update/?id=${id}&content=${content}`

    fetch(url)
        .then(response => response.json())
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
                    <i type="button" onclick="findById(${data[i].id})" class="fa-solid fa-pen me-3"></i>
                    <i type="button" onclick="deleteToDo(${data[i].id})" class="fa-solid fa-trash-can"></i>
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
            <label class="form-label" > TODO 추가!</label>
        </div>
        <button type="submit" class="btn btn-info ms-2" style="background-color: darkseagreen;">추가</button>
    </form>`

    addForm.innerHTML = html;
}

/**
 * ToDoList 수정 Form 을 그려주는 함수
 */
function displayModifyForm(data) {


    let html = `
    <form class="d-flex justify-content-between align-items-center mb-4"
        onsubmit="submitModifyForm(this, ${data.id}); return false;" method="get">
        <div class="form-outline flex-fill">
            <input type="text" name="todo" class="form-control"/>
            <label class="form-label" >${data.content}</label>
        </div>
        <div class="d-flex justify-content-around">
            <button type="submit" id="status" value="modify" class="btn btn-info mx-1" style="background-color: darkseagreen;">
                <span>수정</span>
            </button>
            <button onclick="displayAddForm()" id="status" value="undo" class="btn btn-info" style="background-color: darkseagreen;">취소</button>
        </div>
    </form>`
    addForm.innerHTML = html;
}