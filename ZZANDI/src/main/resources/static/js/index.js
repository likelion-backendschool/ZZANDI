'use strict';

const userNickname = document.querySelector(".userNickname");

//     const myStudyLists = document.querySelectorAll(".myStudyList");
//     for (let i = 0; i < myStudyLists.length; i++) {
//         myStudyLists.item(i).innerHTML = `

//         `;
//     }
// });

/*---------------------------------------------------------------------------
투두리스트 사이드바 JS 코드
 ---------------------------------------------------------------------------*/
const toDoList = document.querySelector(".toDoList");

function findAll(type) {
    let url = (type === 'TOTAL') ? "/todo/list-data" : "/todo/list-data?type="+type;
    fetch(url)
        .then(response => response.json())
        .then(data => {
            displayItems(data,type);
        });
}

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
                </li>`;
    }
    toDoList.innerHTML = html;
}
function changeType(id) {
    fetch("/todo/change?id=" +id)
        .then(response => response.json())
        .then(() => {
            findAll('TOTAL')
            displayItems();
        });
}

function findById(id) {
    fetch("/todo/todo-data?id="+id)
        .then(response => response.json())
        .then(data => {
            displayModifyForm(data);
        });
}

function disToggle() {
    if ($('#toDoListContent').css('display') == 'none') {
        findAll('TOTAL');
        $('#toDoListContent').show();
    } else {
        $('#toDoListContent').hide();
    }
}

